package com.gavin.app.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author gavin
 * @date 2023/4/11 12:11 AM
 */
public class MyPoolBase {

    public final MyHikariConfig config;

    private DataSource dataSource;

    private Executor netTimeoutExecutor;

    private final AtomicReference<Throwable> lastConnectionFailure;

    private final boolean isUseJdbc4Validation;
    private final boolean isAutoCommit;
    private final boolean isIsolateInternalQueries;

    private int defaultTransactionIsolation;

    public MyPoolBase(final MyHikariConfig config) {
        this.config = config;
        this.lastConnectionFailure = new AtomicReference<>();
        this.isUseJdbc4Validation = config.getConnectionTestQuery() == null;
        this.isAutoCommit = config.isAutoCommit();
        this.isIsolateInternalQueries = config.isIsolateInternalQueries();
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
            if (connection == null) {
                throw new SQLTransientConnectionException("Datasource returned null unexpectedly");
            }
            setupConnection(connection);
            lastConnectionFailure.set(null);
            return connection;
        } catch (Exception e) {
            if (connection != null) {
                quietlyCloseConnection(connection, "create connection failed!");
            }
            throw e;
        }
    }

    private void initializeDataSource() {
        String jdbcUrl = config.getJdbcUrl();
        String username = config.getUsername();
        String password = config.getPassword();
        String driverClassName = config.getDriverClassName();
        Properties dataSourceProperties = config.getDataSourceProperties();
        dataSource = new MyDriverDataSource(jdbcUrl, driverClassName, dataSourceProperties, username, password);
        netTimeoutExecutor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("netTimeout").build());
    }

    private void setupConnection(final Connection connection) {
        checkDriverSupport(connection);
    }

    private void checkDriverSupport(final Connection connection) throws SQLException {
        try {
            if (isUseJdbc4Validation) {
                connection.isValid(1);
            } else {
                executeSql(connection, config.getConnectionTestQuery(), false);
            }
        } catch (Throwable e) {
            throw e;
        }

        try {
            defaultTransactionIsolation = connection.getTransactionIsolation();
        }
    }

    void quietlyCloseConnection(final Connection connection, final String closureReason) {
        if (connection != null) {
            try {
                try {
                    if (!connection.isClosed()) {
                        connection.setNetworkTimeout(netTimeoutExecutor, 15000);
                    }
                } finally {
                    connection.close();
                }
            } catch (Throwable ignore) {
            }
         }
    }

    private void executeSql(final Connection connection, final String sql, final boolean isCommit) throws SQLException {
        if (sql != null) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }

            if (isIsolateInternalQueries && !isAutoCommit) {
                if (isCommit) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            }
        }
    }
}
