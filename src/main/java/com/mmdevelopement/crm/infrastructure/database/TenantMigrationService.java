/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.infrastructure.database;

import com.mmdevelopement.crm.config.database.TenantConnectionProvider;
import com.mmdevelopement.crm.domain.organization.service.OrganizationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Objects;
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
                .forEach(organizationDto -> migrateTenant(organizationDto.tenantId()));
    }

    public boolean migrateTenant(String tenantId) {
        AtomicBoolean migrationStatus = new AtomicBoolean(false);
        organizationService.findOrganizationByTenantId(tenantId)
                .ifPresentOrElse(organizationDto -> {

                    if (Objects.equals(organizationDto.status(), "FIRST_SYNC")) {
                        var result = createDatabaseSchema(organizationDto.dbSchemaName(), tenantConnectionProvider.getAnyDataSource());

                        if (!result) {
                            log.error("Failed to create database schema for tenantId: {}", organizationDto.tenantId());
                            return;
                        }

                        organizationService.updateOrganizationStatus(organizationDto.tenantId(), "ACTIVE");
                    }

                    MigrateResult result = runFlywayMigration(organizationDto.dbSchemaName(), tenantConnectionProvider.getAnyDataSource());

                    if (!result.success) {
                        log.error("Migration failed for tenantId: {}", organizationDto.tenantId());
                    } else {
                        log.info("Migration successful for tenantId: {}. Executed {} migrations; latest version: {}",
                                organizationDto.tenantId(), result.migrationsExecuted, result.targetSchemaVersion);
                        migrationStatus.set(true);
                    }
                }, () -> {
                    throw new RuntimeException("Organization with tenantId: " + tenantId + " not found");
                });

        return migrationStatus.get();
    }

    private boolean createDatabaseSchema(String db_name, DataSource anyDataSource) {
        try (Connection connection = anyDataSource.getConnection()) {
            connection.createStatement().execute("CREATE DATABASE " + db_name);
            return true;
        } catch (Exception e) {
            log.error("Failed to create database schema: {}", db_name);
            return false;
        }
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
