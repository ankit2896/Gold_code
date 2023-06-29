package com.freecharge.financial.config;

import com.freecharge.financial.constants.DBConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DBConfig {

    @Autowired
    AppProperties dbProperties;

    private DataSource dataSource = null;
    private DataSource poolDataSource = null;

    @Bean(name = {"hikariDataSource"})
    @Primary
    DataSource hikariDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            dbProperties.generateDbCredentials();
            try {
                config.setDriverClassName(dbProperties.getProperty(DBConstants.DRIVER_CLASSNAME));
                config.setJdbcUrl(dbProperties.getProperty(DBConstants.JDBC_URL));
                log.info("jdbcurl: " + dbProperties.getProperty(DBConstants.JDBC_URL));
                config.setUsername(dbProperties.getProperty(DBConstants.USERNAME));
                log.info("username: " + dbProperties.getProperty(DBConstants.USERNAME));
                config.setPassword(dbProperties.getProperty(DBConstants.PASS_WORD));
                //log.info("password: " + dbProperties.getProperty(DBConstants.PASSWORD).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            poolDataSource = DataSourceBuilder.create()
                    .driverClassName(dbProperties.getProperty(DBConstants.DRIVER_CLASSNAME))
                    .url(dbProperties.getProperty(DBConstants.JDBC_URL))
                    .username(dbProperties.getProperty(DBConstants.USERNAME))
                    .password(dbProperties.getProperty(DBConstants.PASS_WORD))
                    .build();
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    @Scheduled(cron = "0 */55 * * * *")
    private void updateDataSource() {
        log.info("Updating the datasource");
        System.out.println("Updating the datasource");
        dbProperties.generateDbCredentials();
        try {
            HikariDataSource hikariDataSource = (HikariDataSource) hikariDataSource();
            HikariConfigMXBean hikariConfigMXBean = hikariDataSource.getHikariConfigMXBean();
            hikariConfigMXBean.setUsername(dbProperties.getProperty(DBConstants.USERNAME));
            hikariConfigMXBean.setPassword(dbProperties.getProperty(DBConstants.PASS_WORD));
           // ((DataSourceProxy) poolDataSource).setUsername(dbProperties.getProperty(DBConstants.USERNAME).toString());
            //((DataSourceProxy) poolDataSource).setPassword(dbProperties.getProperty(DBConstants.PASS_WORD).toString());
            HikariPoolMXBean hikariPoolMXBean = hikariDataSource.getHikariPoolMXBean();
            hikariPoolMXBean.softEvictConnections();
            log.info("Value" + dataSource.toString());
            log.info("Updated datasource value.....");
            System.out.println("Value" + dataSource.toString());
            System.out.println("Updated datasource value.....");
        } catch (Exception e) {
            log.error("Exception occurred trying updating the datasource with error: " + e);
        }
    }

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate(new JdbcTemplate(dataSource))
                        .usingDbTime() // Works on Postgres, MySQL, MariaDb, MS SQL, Oracle, DB2, HSQL and H2
                        .build()
        );
    }

}