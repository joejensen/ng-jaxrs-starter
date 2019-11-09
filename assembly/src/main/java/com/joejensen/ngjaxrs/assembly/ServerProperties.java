package com.joejensen.ngjaxrs.assembly;

import com.google.inject.ImplementedBy;

/**
 * Provides the configuration of the server
 */
@ImplementedBy(ServerPropertiesClasspath.class)
public interface ServerProperties
{
    /**
     * Gets an integer property with a default
     * @param key The name of the property to retrieve
     * @param def The default value of the property
     * @return The int value of the property
     */
    int getInt( String key, int def);

    /**
     * Gets a string property, returning a default if undefined
     * @param key The name of the property to retrieve
     * @param def The default value of the property
     * @return The retrieved property value
     */
    String getString( String key, String def);

    /**
     * Gets the port on which the server should run
     */
    default int getPort()
    {
        return getInt("port", 80);
    }

    /**
     * Gets the host on which the server is running
     */
    default String getHost()
    {
        return getString( "host", "localhost:80");
    }

    /**
     * Gets the version of the current application
     */
    default String getVersion()
    {
        return getString( "version", "Unknown Version");
    }
}
