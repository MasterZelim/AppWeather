package com.example.appweather.util;

import org.hibernate.cfg.Configuration;

public class ConfigurationUtil {
    private static final Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

    public static String get(String key) {
        return configuration.getProperty(key);
    }
}
