package com.mmdevelopement.crm.config.security.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestContextHolder {
    private static final RequestContext INVALID_REQUEST_CONTEXT = RequestContext.builder().tenantId("").build();
    private static final ThreadLocal<RequestContext> threadLocalRequestContext = new InheritableThreadLocal<>();

    public static RequestContext getRequestContext() {
        RequestContext requestContext = threadLocalRequestContext.get();
        if (requestContext == null) {
            log.warn("Request context is not set");
            return INVALID_REQUEST_CONTEXT;
        }

        return requestContext;
    }

    public static String getCurrentTenantId() {
        return getRequestContext().tenantId();
    }

    public static String getCurrentUserGuid() {
        return getRequestContext().userGuid();
    }

    static void setRequestContext(RequestContext requestContext) {
        threadLocalRequestContext.set(requestContext);
    }

    static void clear() {
        threadLocalRequestContext.remove();
    }
}
