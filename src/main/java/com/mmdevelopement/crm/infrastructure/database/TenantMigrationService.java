/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.infrastructure.database;

import com.mmdevelopement.crm.config.database.TenantConnectionProvider;
import com.mmdevelopement.crm.domain.organization.OrganizationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@RequiredArgsConstructor
public class TenantMigrationService {

    private final OrganizationService organizationService;
    private final TenantConnectionProvider tenantConnectionProvider;

    @PostConstruct
    public void migrateAllTenants() {
        organizationService.getAllOrganizations()
                .forEach(organizationEntity -> migrateTenant(organizationEntity.getTenantId()));
    }

    protected boolean migrateTenant(String tenantId) {
        AtomicBoolean migrationStatus = new AtomicBoolean(false);
        organizationService.findOrganizationByTenantId(tenantId)
                .ifPresentOrElse(organizationEntity -> {
                    MigrateResult result = runFlywayMigration(organizationEntity.getDbSchemaName(), tenantConnectionProvider.getAnyDataSource());

                    if (!result.success) {
                        log.error("Migration failed for tenantId: {}", organizationEntity.getTenantId());
                    } else {
                        log.info("Migration successful for tenantId: {}. Executed {} migrations; latest version: {}",
                                organizationEntity.getTenantId(), result.migrationsExecuted, result.targetSchemaVersion);
                        migrationStatus.set(true);
                    }
                }, () -> {
                    throw new RuntimeException("Organization with tenantId: " + tenantId + " not found");
                });

        return migrationStatus.get();
    }

    public MigrateResult runFlywayMigration(String schema, DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .baselineOnMigrate(true)
                .locations("classpath:/db/migration/tenant")
                .load();

        return flyway.migrate();
    }

}
