package com.idmworks.bitbucket.listener.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties
public class ApplicationConfig {

    private Map<String, Map<String, String>> triggers = new HashMap<>();

    public Map<String, Map<String, String>> getTriggers() {
        return triggers;
    }

    public void setTriggers(final Map<String, Map<String, String>> triggers) {
        this.triggers = triggers;
    }
}
