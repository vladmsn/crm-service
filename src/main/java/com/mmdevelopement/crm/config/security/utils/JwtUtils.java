package com.mmdevelopement.crm.config.security.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtUtils {

    public static final String BEARER_TYPE = "Bearer";

    public String extractJwtToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return null;
        }

        if (!authorizationHeader.startsWith(BEARER_TYPE)) {
            return null;
        }

        return authorizationHeader.substring(BEARER_TYPE.length() + 1);
    }
}
