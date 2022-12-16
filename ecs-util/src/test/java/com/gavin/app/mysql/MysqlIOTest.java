package com.gavin.app.mysql;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ConnectionImpl;
import com.mysql.jdbc.MySQLConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private String dbName;

    @Before
    public void before() throws Exception {
        host = "127.0.0.1";
        port = 3306;
        props = new Properties();
        socketFactoryClassName = "com.mysql.jdbc.StandardSocketFactory";
        url = String.format("jdbc:mysql://%s:%s/test", host, port);
        // fixme conn类型不对
//        conn = DriverManager.getConnection(url, user, password);
        socketTimeout = 30;
        useBufferRowSizeThreshold = 2048;
        user = "root";
        password = "123456a?";
        dbName = "test";
        url = "jdbc:mysql://localhost:3306/test?&useSSL=false";

        props.put("PORT", port);
        props.put("user", user);
        props.put("HOST", host);
        props.put("DBNAME", dbName);
        props.put("password", password);
        props.put("useSSL", "false");
        props.put("NUM_HOSTS", "1");
        props.put("HOST.1", host);
        props.put("PORT.1", port);

    }

    @Test
    public void test1() throws Exception {
        //  mysqlIO无法使用，只能使用connection
//        MysqlIO mysqlIO = new MysqlIO(host, port, props, socketFactoryClassName, conn, socketTimeout, useBufferRowSizeThreshold);
        Connection connection = new ConnectionImpl(host, port, props, dbName, url);
        connection.ping();
        PreparedStatement preparedStatement = connection.clientPrepareStatement("select * from user where name = 'gavin';");
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            // 通过字段检索
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");

            // 输出数据
            System.out.print("ID: " + id);
            System.out.print(", 名称: " + name);
            System.out.print(", 年龄: " + age);
            System.out.print("\n");
        }
        connection.close();
    }
}
