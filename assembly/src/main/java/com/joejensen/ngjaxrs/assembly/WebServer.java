package com.joejensen.ngjaxrs.assembly;

import com.google.inject.ImplementedBy;

/**
 * Interface for the webserver that will host the jaxrs server
 */
@ImplementedBy(WebServerUndertow.class)
public interface WebServer
{
    /**
     * Starts the web server
     */
    void start();

    /**
     * Stops the web server
     */
    void stop();
}
