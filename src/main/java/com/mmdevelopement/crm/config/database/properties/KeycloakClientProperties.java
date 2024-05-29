package com.mmdevelopement.crm.config.database.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak.client")
public class KeycloakClientProperties {
    private String serverUrl;
    private String realm;
    private String clientId;
    private String secret;
}
