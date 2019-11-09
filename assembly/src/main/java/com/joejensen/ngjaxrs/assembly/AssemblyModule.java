package com.joejensen.ngjaxrs.assembly;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import com.joejensen.ngjaxrs.server.ServerModule;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import javax.servlet.ServletContextListener;

/**
 * Root module used to configure all dependencies to serve up the server contents
 */
public class AssemblyModule extends ServletModule
{
    @Override
    protected void configureServlets()
    {
        Multibinder<ServletContextListener> multibinder = Multibinder.newSetBinder(binder(), ServletContextListener.class);
        multibinder.addBinding().to(GuiceResteasyBootstrapServletContextListener.class);

        bind(HttpServlet30Dispatcher.class).in(Singleton.class);
        bind(GuiceFilter.class).in(Singleton.class);
        bind(WebServerJsonJacksonProvider.class).in(Singleton.class);
        serve("/*").with(HttpServlet30Dispatcher.class);

        // Pull in the actual server content implementation
        install( new ServerModule());
    }

    @Provides
    @Singleton
    ObjectMapper objectMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper;
    }
}
