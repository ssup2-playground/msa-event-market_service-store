package com.market.store.config;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.market.store.domain.repository",
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    transactionManagerRef = "secondaryTransactionManager",
    includeFilters =
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = {".*Secondary.*"}))
public class DataSourceSecondaryConfig {
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.secondary")
  public DataSource secondaryDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(secondaryDataSource())
        .packages("com.market.store.domain.entity")
        .persistenceUnit("secondary")
        .build();
  }

  @Bean
  PlatformTransactionManager secondaryTransactionManager(EntityManagerFactoryBuilder builder) {
    return new JpaTransactionManager(secondaryEntityManagerFactory(builder).getObject());
  }
}
