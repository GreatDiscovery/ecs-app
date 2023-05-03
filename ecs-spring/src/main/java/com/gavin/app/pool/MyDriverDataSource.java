package com.gavin.app.pool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author gavin
 * @date 2023/5/3 11:08 PM
 */
public class MyDriverDataSource implements DataSource {

    private final String jdbcUrl;
    private final Properties driverProperties;
    private Driver driver;

    public MyDriverDataSource(String jdbcUrl, String driverClassName, Properties properties, String userName, String password) {
        this.jdbcUrl = jdbcUrl;
        this.driverProperties = new Properties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            driverProperties.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        driverProperties.put("user", userName);
        driverProperties.put("password", password);
        if (driverClassName != null) {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver d = drivers.nextElement();
                if (d.getClass().getName().equals(driverClassName)) {
                    driver = d;
                    break;
                }
            }

            if (driver == null) {
                Class<?> driverClass = null;
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    driverClass = contextClassLoader.loadClass(driverClassName);
                    if (driverClass == null) {
                        driverClass = this.getClass().getClassLoader().loadClass(driverClassName);
                    }
                    driver = ((Driver) driverClass.newInstance());
                } catch (Exception ignore) {
                }

            }
        }

    }

    @Override
    public Connection getConnection() throws SQLException {
        return driver.connect(jdbcUrl, driverProperties);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Properties clone = (Properties) driverProperties.clone();
        clone.put("user", username);
        clone.put("password", password);
        return driver.connect(jdbcUrl, clone);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
