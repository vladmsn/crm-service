package com.mmdevelopement.crm.config.security.context;

import lombok.Builder;

@Builder
public record RequestContext(
        String tenantId,
        String userGuid) {
}
