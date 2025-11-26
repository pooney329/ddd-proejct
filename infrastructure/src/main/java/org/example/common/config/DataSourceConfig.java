package org.example.common.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.example.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@EnableTransactionManagement
@Configuration
public class DataSourceConfig {
    public static final String MASTER_DATASOURCE = "masterDataSource";
    public static final String SLAVE_DATASOURCE = "slaveDataSource";
    public static final String ROUTING_DATASOURCE = "routingDataSource";
    public static final String BASIC_DATASOURCE = "basicDataSource";

    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public HikariConfig masterConfig() {

        return new HikariConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public HikariConfig slaveConfig() {
        return new HikariConfig();
    }

    @Bean(MASTER_DATASOURCE)
    public DataSource masterDataSource() {
        return new HikariDataSource(masterConfig());
    }

    @Bean(SLAVE_DATASOURCE)
    public DataSource slaveDataSource() {
        return new HikariDataSource(slaveConfig());
    }

    @Bean(ROUTING_DATASOURCE)
    @DependsOn({MASTER_DATASOURCE, SLAVE_DATASOURCE})
    public DataSource routingDataSource(
            @Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
            @Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource) {

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.MASTER, masterDataSource);
        dataSourceMap.put(DataSourceType.SLAVE, slaveDataSource);

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Primary
    @Bean(BASIC_DATASOURCE)
    @DependsOn(ROUTING_DATASOURCE)
    public LazyConnectionDataSourceProxy dataSource(
            @Qualifier(ROUTING_DATASOURCE) DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    public static class RoutingDataSource extends AbstractRoutingDataSource {

        @Override
        protected Object determineCurrentLookupKey() {

            boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("##### transaction isReadOnly : {} / transaction name : {} #####", isReadOnly, TransactionSynchronizationManager.getCurrentTransactionName());
            return isReadOnly ? DataSourceType.SLAVE : DataSourceType.MASTER;
        }
    }
}
