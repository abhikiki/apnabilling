package com.abhishek.retail.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {
    @Bean(name = "retailbillingDs")
    @Primary
    public DataSource retailbillingDs(@Value("${spring.datasource.jdbcUrl}") final String url,
                                  @Value("${spring.datasource.username}") final String username,
                                  @Value("${spring.datasource.password}") final String password,
                                  @Value("${spring.datasource.driverClassName}") final String driverClassName) {
        return DataSourceBuilder
                .create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "retailBillingJdbcTemplate")
    public JdbcTemplate retailBillingJdbcTemplate(@Qualifier("retailbillingDs") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "namedRetailBillingJdbcTemplate")
    public NamedParameterJdbcTemplate namedRetailBillingJdbcTemplate(@Qualifier("retailbillingDs") DataSource ds) {
        return new NamedParameterJdbcTemplate(ds);
    }

    @Bean(name = "registeredCustomerDs")
    public DataSource registeredCustomerDs(@Value("${spring.second-db.jdbcUrl}") final String url,
                                  @Value("${spring.second-db.username}") final String username,
                                  @Value("${spring.second-db.password}") final String password,
                                  @Value("${spring.second-db.driverClassName}") final String driverClassName) {
        return DataSourceBuilder
                .create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "registeredBillingJdbcTemplate")
    public JdbcTemplate registeredBillingJdbcTemplate(@Qualifier("registeredCustomerDs") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "namedRegisteredBillingJdbcTemplate")
    public NamedParameterJdbcTemplate namedRegisteredBillingJdbcTemplate(@Qualifier("registeredCustomerDs") DataSource ds) {
        return new NamedParameterJdbcTemplate(ds);
    }
}
