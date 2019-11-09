package com.joejensen.ngjaxrs.assembly;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads a properties file from the current classpath and provides access to it's values
 */
@Singleton
public class ServerPropertiesClasspath implements ServerProperties
{
    private final Properties properties;

    public ServerPropertiesClasspath() throws IOException
    {
        try(InputStream propertiesStream = getClass().getResourceAsStream("config.properties"))
        {
            this.properties = new Properties();
            this.properties.load(propertiesStream);
        }
    }

    @Override
    public int getInt( String key, int def)
    {
        String value = properties.getProperty(key);
        return value == null ? def : Integer.parseInt(value);
    }

    @Override
    public String getString(String key, String def)
    {
        return properties.getProperty(key, def);
    }
}
