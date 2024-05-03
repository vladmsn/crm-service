/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.config.database;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.flywaydb.core.Flyway;
import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "globalEntityManagerFactory",
        transactionManagerRef = "globalTransactionManager"
)
public class GlobalDatabaseConfiguration {

    public static final String[] ORGANIZATION_MODULE_PACKAGES = {
            "com.mmdevelopement.crm.domain.organization"
    };

    @Bean(name = "globalDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.global")
    public DataSource globalDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public Flyway flyway(@Qualifier("globalDataSource") DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration/global-crm")
                .load();

        flyway.migrate();
        return flyway;
    }


    @Bean(name = "globalEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean globalEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(globalDataSource());
        em.setPackagesToScan(ORGANIZATION_MODULE_PACKAGES);
        em.setPersistenceUnitName("globalPersistenceUnit");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalJpaProperties());
        return em;
    }

    @Bean(name = "globalTransactionManager")
    @Primary
    public PlatformTransactionManager globalTransactionManager(@Qualifier("globalEntityManagerFactory") EntityManagerFactory globalEntityManagerFactory) {
        return new JpaTransactionManager(globalEntityManagerFactory);
    }

    private Properties additionalJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return properties;
    }
}