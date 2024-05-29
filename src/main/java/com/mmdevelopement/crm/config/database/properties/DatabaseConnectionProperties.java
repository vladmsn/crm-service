package com.mmdevelopement.crm.config.database.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.datasource.crm")
public class DatabaseConnectionProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
