package com.market.store.config;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE)
@EnableJpaRepositories(
    basePackages = "com.market.store.domain.repository",
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager",
    includeFilters =
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = {".*Primary.*"}))
public class DataSourcePrimaryConfig {
  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.primary")
  public DataSource primaryDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(primaryDataSource())
        .packages("com.market.store.domain.entity")
        .persistenceUnit("primary")
        .build();
  }

  @Bean
  @Primary
  PlatformTransactionManager primaryTransactionManager(EntityManagerFactoryBuilder builder) {
    return new JpaTransactionManager(primaryEntityManagerFactory(builder).getObject());
  }
}
