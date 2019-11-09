package com.joejensen.ngjaxrs.assembly;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import lombok.extern.log4j.Log4j2;

/**
 * Instantiates the application and starts up the web server
 * @author jjensen
 */
@Log4j2
public class Main
{
    public static void main( String[] args)
    {
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new AssemblyModule());
        WebServer webServer = injector.getInstance(WebServer.class);
        webServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(webServer::stop));
    }
}
