package com.joejensen.ngjaxrs.assembly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Adapter the Jackson provider to take an injected object mapper instance instead of creating one with default settings
 *
 * @author jjensen
 */
@Singleton
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class WebServerJsonJacksonProvider extends JacksonJsonProvider
{
    @Inject
    public WebServerJsonJacksonProvider(ObjectMapper objectMapper)
    {
        super(objectMapper);
    }
}

