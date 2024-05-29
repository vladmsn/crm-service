package com.mmdevelopement.crm.config.database;

import com.mmdevelopement.crm.CrmApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@Import(GlobalDatabaseConfiguration.class)
public class GlobalDatabaseIntegrationTest extends CrmApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testGlobalDatabaseConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.isValid(1)).isTrue();
        }
    }

    @Test
    public void testFlywayMigrations() {
    }
}
