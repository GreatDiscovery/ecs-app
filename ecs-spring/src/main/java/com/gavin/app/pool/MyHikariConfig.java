package com.gavin.app.pool;

import lombok.Data;

/**
 * @author gavin
 * @date 2023/4/11 12:27 AM
 */
@Data
public class MyHikariConfig {
    private volatile long maxLifetime;
    private volatile String username;
    private volatile String password;
    private volatile int maxPoolSize;

    private String jdbcUrl;
    private String driverClassName;

}
