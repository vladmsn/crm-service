/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.config.database;

import com.mmdevelopement.crm.domain.organization.OrganizationEntity;
import com.mmdevelopement.crm.domain.organization.OrganizationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class TenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final DataSource dataSource;
    private final OrganizationService organizationService;

    @Override
    public Connection getConnection(String tenantId) throws SQLException {

        if (tenantId == null || tenantId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Could not acquire connection for empty tenantId");
        }

        final Connection connection = dataSource.getConnection();

        String schema = organizationService.findOrganizationByTenantId(tenantId)
                .map(OrganizationEntity::getDbSchemaName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Organization with tenantId: " + tenantId + " not found"));

        connection.setSchema(schema);

        return connection;
    }

    @Override
    public void releaseConnection(String o, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public DataSource getAnyDataSource() {
        return dataSource;
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(@NonNull Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(@NonNull Class<T> aClass) {
        return null;
    }
}
