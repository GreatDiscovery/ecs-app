package com.gavin.app.mysql;

import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.MysqlIO;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

/**
 * 尝试使用MysqlIO
 * @author gavin
 * @date 2022/12/14 下午11:28
 */
public class MysqlIOTest {

    private String host;
    private int port;
    private Properties props;
    private String socketFactoryClassName;
    private MySQLConnection conn;
    private int socketTimeout;
    private int useBufferRowSizeThreshold;
    private String url;
    private String user;
    private String password;

    @Before
    public void before() throws Exception {
        host = "127.0.0.1";
        port = 3306;
        props = new Properties();
        socketFactoryClassName = "com.mysql.jdbc.StandardSocketFactory";
        url = String.format("jdbc:mysql://%s:%s/test?serverTimezone=GMT%2B8", host, port);
        // fixme conn类型不对
//        conn = DriverManager.getConnection(url, user, password);
        socketTimeout = 30;
        useBufferRowSizeThreshold = 2048;
        user = "root";
        password = "123456a?";
    }

    @Test
    public void test1() throws Exception {
        MysqlIO mysqlIO = new MysqlIO(host, port, props, socketFactoryClassName, conn, socketTimeout, useBufferRowSizeThreshold);
        // fixme mysqlIO不能重写，并且外部无法调用，考虑是否需要重写一个类
    }
}
