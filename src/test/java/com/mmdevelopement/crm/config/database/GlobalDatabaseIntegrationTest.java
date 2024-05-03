package com.mmdevelopement.crm.config.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(GlobalDatabaseConfiguration.class)
public class GlobalDatabaseIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testGlobalDatabaseConnection() throws Exception {
        // Attempt to get a connection
        try (Connection connection = dataSource.getConnection()) {
            // Check that the connection is valid
            assertThat(connection).isNotNull();
            assertThat(connection.isValid(1)).isTrue();
        }
    }

    @Test
    public void testFlywayMigrations() {
    }
}
