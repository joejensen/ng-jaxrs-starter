package com.joejensen.ngjaxrs.assembly;

import com.google.common.base.Preconditions;
import com.google.inject.servlet.GuiceFilter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.PredicateParser;
import io.undertow.predicate.Predicates;
import io.undertow.predicate.PredicatesHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.builder.PredicatedHandlersParser;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.DeflateEncodingProvider;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * Instantiates an Undertow based server to deliver all
 */
@Log4j2
@Singleton
public class WebServerUndertow implements WebServer
{
    private final ServerProperties serverProperties;
    private final GuiceFilter guiceFilter;
    private final Set<ServletContextListener> listeners;
    private Undertow currentUndertow;

    @Inject
    public WebServerUndertow(ServerProperties serverProperties,
                             GuiceFilter guiceFilter,
                             Set<ServletContextListener> listeners
    )
    {
        this.serverProperties = serverProperties;
        this.guiceFilter = guiceFilter;
        this.listeners = listeners;
    }

    /**
     * Starts the undertow server
     */
    public synchronized void start()
    {
        Preconditions.checkState( currentUndertow == null);

        log.info("Starting Undertow Server.");
        Undertow newUndertow = buildUndertow();
        newUndertow.start();
        currentUndertow = newUndertow;
        log.info("Started Undertow Server.");
    }

    /**
     * Stops the undertow server.
     */
    public synchronized void stop()
    {
        Preconditions.checkState( currentUndertow != null);

        log.info("Stopping Undertow Server.");
        currentUndertow.stop();
        currentUndertow = null;
        log.info("Stopped Undertow Server.");
    }

    /**
     * Configure and build the actual undertow instance
     */
    private Undertow buildUndertow()
    {
        ContentEncodingRepository repo = new ContentEncodingRepository();
        Predicate zipPredicate = PredicateParser.parse("path-suffix('.css') or path-suffix('.js')", getClass().getClassLoader());
        repo.addEncodingHandler("gzip", new GzipEncodingProvider(),1000, zipPredicate);
        repo.addEncodingHandler("deflate", new DeflateEncodingProvider(),10, zipPredicate);

        DeploymentInfo servletBuilder = buildDeploymentInfo();

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();

        HttpHandler servletDeploymentHandler;
        try
        {
            servletDeploymentHandler = manager.start();
        }
        catch( ServletException e)
        {
            throw new IllegalStateException("Failed Starting the servlet deployment manager.", e);
        }

        // Do not allow caching the index html since it contains hashes of the more heavy weight files that allow long term caching of the js and other content
        ResourceHandler idxcprh = Handlers.resource( new ClassPathResourceManager(getClass().getClassLoader(), "public"));
        idxcprh.setCachable(Predicates.truePredicate());
        idxcprh.setCacheTime(0);

        // We do want to allow caching the js since it has a hash appended to it
        ResourceHandler jscprh = Handlers.resource( new ClassPathResourceManager(getClass().getClassLoader(), "public/js"));
        jscprh.setCachable(Predicates.truePredicate());
        jscprh.setCacheTime(31536000);

        // the path handle redirects paths to their correct handlers
        PathHandler pathHandler = Handlers
            .path(idxcprh)
            .addPrefixPath("/js", jscprh)
            .addPrefixPath("/api", servletDeploymentHandler);

        // The predicates rewrite all non-resource urls to the base url so HTML5 paths will work correctly
        PredicatesHandler predicateHandler = buildPredicatesHandler(pathHandler);

        EncodingHandler encodingHandler = new EncodingHandler(predicateHandler, repo);

        int port = serverProperties.getPort();
        return Undertow.builder()
            .addHttpListener(port, "0.0.0.0")
            .setHandler(encodingHandler)
            .build();
    }

    /**
     * Build the servlet system to be deployed to undertow
     */
    private DeploymentInfo buildDeploymentInfo()
    {
        ImmediateInstanceFactory<GuiceFilter> guiceFilterFactory = new ImmediateInstanceFactory<>(guiceFilter);

        DeploymentInfo deploymentInfo = Servlets.deployment().setDeploymentName("client.war")
                .setClassLoader(getClass().getClassLoader())
                .setContextPath("/api");

        // Guice Servlet doesn't supporrt context listeners so we use a multi-binder to define them on our own
        for( ServletContextListener listener : listeners)
        {
            ImmediateInstanceFactory<ServletContextListener> listenerFactory = new ImmediateInstanceFactory<>(listener);
            deploymentInfo = deploymentInfo.addListener(Servlets.listener(GuiceResteasyBootstrapServletContextListener.class, listenerFactory));
        }

        // The guice filter should catch everything
        deploymentInfo = deploymentInfo
            .addFilter(Servlets.filter("GuiceFilter", GuiceFilter.class, guiceFilterFactory).setAsyncSupported(true))
            .addFilterUrlMapping("GuiceFilter", "/*", DispatcherType.REQUEST);

        return deploymentInfo;
    }

    /**
     * Builds the routing table which rewrites requests to the appropriate path
     */
    private PredicatesHandler buildPredicatesHandler( HttpHandler next)
    {
        return Handlers.predicates(PredicatedHandlersParser.parse(
                "" +
                        "path-suffix('.js') -> done " + '\n' +
                        "path-suffix('.css') -> done " + '\n' +
                        "path-prefix('/api') -> done " + '\n' +
                        "path-prefix('/assets/') -> done " + '\n' +
                        "path-prefix('/') -> rewrite('/index.html')",
                getClass().getClassLoader()), next);
    }
}
