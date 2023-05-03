package com.gavin.app.pool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author gavin
 * @date 2023/4/11 12:11 AM
 */
public class MyPoolBase {

    public final MyHikariConfig config;

    private DataSource dataSource;

    public MyPoolBase(final MyHikariConfig config) {
        this.config = config;
        initializeDataSource();
    }

    MyPoolEntry newPoolEntry() throws Exception {
        return new MyPoolEntry(newConnection(), this);
    }

    private Connection newConnection() throws Exception {
        final long start = System.currentTimeMillis();
        Connection connection = null;
        try {
            String userName = config.getUsername();
            String password = config.getPassword();
            connection = dataSource.getConnection(userName, password);

            return connection;
        } catch (Exception e) {

        }

    }

    private void initializeDataSource() {
        String jdbcUrl = config.getJdbcUrl();
        String username = config.getUsername();
        String password = config.getPassword();
        String driverClassName = config.getDriverClassName();
        Properties dataSourceProperties = config.getDataSourceProperties();
        dataSource = new MyDriverDataSource(jdbcUrl, driverClassName, dataSourceProperties, username, password);
    }
}
