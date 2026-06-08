package com.engine.starter.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Registers com.engine.starter.persistence as an auto-configuration package so
 * that Spring Boot's JpaBaseConfiguration / LocalContainerEntityManagerFactory
 * picks up the starter's @Entity classes and @Repository interfaces
 * automatically — without requiring the consuming application to add
 * @EntityScan or @EnableJpaRepositories pointing at the starter's packages.
 *
 * This is the canonical Spring Boot starter pattern used by spring-data-jpa,
 * spring-security, etc. to expose their own managed types to the JPA context.
 */
public class EngineJpaPackageRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String PERSISTENCE_PACKAGE = "com.engine.starter.persistence";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry) {
        AutoConfigurationPackages.register(registry, PERSISTENCE_PACKAGE);
    }
}
