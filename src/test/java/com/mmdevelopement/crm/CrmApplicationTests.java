package com.mmdevelopement.crm;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static com.mmdevelopement.crm.CrmApplicationTests.ContainerConfiguration.postgresqlContainer;

@SpringBootTest
public abstract class CrmApplicationTests {
	@TestConfiguration
	static class ContainerConfiguration {

		static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
				.withDatabaseName("test")
				.withUsername("test")
				.withPassword("test");

		static {
			postgresqlContainer.start();
		}

		@Bean
		@Primary
		public PostgreSQLContainer<?> providePostgresContainer() {
			return postgresqlContainer;
		}
	}

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresqlContainer::getUsername);
		registry.add("spring.datasource.password", postgresqlContainer::getPassword);
		registry.add("spring.datasource.crm.driver-class-name", () ->"org.testcontainers.jdbc.ContainerDatabaseDriver");
		registry.add("spring.jpa.hibernate.ddl-auto", () ->"create-drop");
	}



	}
