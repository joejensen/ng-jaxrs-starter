package com.joejensen.ngjaxrs.server;

import com.google.inject.AbstractModule;
import com.joejensen.ngjaxrs.server.api.UserResource;

public final class ServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(UserResource.class).asEagerSingleton();
    }
}
