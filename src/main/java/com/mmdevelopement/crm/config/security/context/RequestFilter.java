package com.mmdevelopement.crm.config.security.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof JwtAuthenticationToken) {
                Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                String tenantId = jwt.getClaimAsString("tenant_id");
                String userGuid = jwt.getSubject();

                log.debug("Setting request context for tenantId: {} and userGuid: {}", tenantId, userGuid);

                RequestContext requestContext = RequestContext.builder()
                        .tenantId(tenantId)
                        .userGuid(userGuid)
                        .build();
                RequestContextHolder.setRequestContext(requestContext);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        } finally {
            RequestContextHolder.clear();
        }

    }
}
