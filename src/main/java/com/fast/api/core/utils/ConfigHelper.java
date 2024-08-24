/*
 * Author: Devendra Singh
 * Date: 24/08/24
 */

package com.fast.api.core.utils;

import java.io.*;
import java.util.Properties;

public class ConfigHelper {
    /**
     * responsible for fetching any value from property file depending upon the key
     * argument and from any file depending upon the file name
     *
     * @param propertyFileName
     * @param key
     * @return
     */
    public static String returnPropVal(final String propertyFileName, final String key) {
        final Properties properties = new Properties();
        String value = null;
        {
            try {
                properties.load(new FileInputStream(new File(
                        System.getProperty("user.dir") + "/src/main/resources/" + propertyFileName + ".properties")));
                // get property value based on key:
                value = properties.getProperty(key);

            } catch (final FileNotFoundException e) {
                e.printStackTrace();

            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static String GetBaseUrl(String property) {
        try (InputStream input = ConfigHelper.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return null;
            }
            prop.load(input);
            String url = prop.getProperty(property);
            return url;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}