/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.config.database;

import com.mmdevelopement.crm.domain.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantIdResolver implements CurrentTenantIdentifierResolver<String> {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            tenantId = jwt.getClaimAsString("tenant_id");
        }

        return tenantId;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
