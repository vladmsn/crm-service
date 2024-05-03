package com.mmdevelopement.crm.infrastructure.database;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/migration")
public class MigrationController {

    private final TenantMigrationService tenantMigrationService;

    @PostMapping("/{tenantId}/migrate")
    public ResponseEntity<Void> migrateTenant(@PathVariable String tenantId) {
        return tenantMigrationService.migrateTenant(tenantId) ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
}
