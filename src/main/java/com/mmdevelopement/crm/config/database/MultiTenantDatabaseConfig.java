/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.config.database;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "com.mmdevelopement.crm.domain.user",
                "com.mmdevelopement.crm.domain.financial",
                "com.mmdevelopement.crm.domain.partner"
        },
        entityManagerFactoryRef = "multiTenantEntityManagerFactory",
        transactionManagerRef = "multiTenantTransactionManager"
)
public class MultiTenantDatabaseConfig {
    private final TenantConnectionProvider tenantConnectionProvider;
    private final TenantIdResolver tenantIdResolver;

    public static final String[] TENANT_MODULE_PACKAGES = {
            "com.mmdevelopement.crm.domain.user",
            "com.mmdevelopement.crm.domain.financial",
            "com.mmdevelopement.crm.domain.partner",
    };

    @Bean(name = "multiTenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory() {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(TENANT_MODULE_PACKAGES);
        factory.setJpaPropertyMap(Map.of(
                "hibernate.multiTenancy", "DATABASE",
                "hibernate.tenant_identifier_resolver", tenantIdResolver,
                "hibernate.multi_tenant_connection_provider", tenantConnectionProvider
        ));
        return factory;
    }

    @Bean(name = "multiTenantTransactionManager")
    public PlatformTransactionManager tenantTransactionManager(@Qualifier("multiTenantEntityManagerFactory") EntityManagerFactory tenantEntityManagerFactory) {
        return new JpaTransactionManager(tenantEntityManagerFactory);
    }
}
