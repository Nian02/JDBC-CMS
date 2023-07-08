package com.atguigu.cms.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 使得SQL语句能够通过连接池对数据库进行连接
 */
public class JDBCTools {
    private static DataSource ds;// 创建连接池
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();// 线程本地变量存储connection（线程独有的连接）
    static{//静态代码块，读取文件里的配置
        try {
            Properties pro = new Properties();// 读取外部配置文件 Properties
            pro.load(ClassLoader.getSystemResourceAsStream("druid.properties"));
            ds = DruidDataSourceFactory.createDataSource(pro);// 使用连接池的工具类的工程模式，创建连接池
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: SQL语句获取连接
     * @param :
     * @return: java.sql.Connection
     */

    public static Connection getConnection() throws SQLException {
         Connection connection = tl.get();// 从线程本地变量中去取
         if(connection  == null){// 当前线程还没有拿过连接
             connection = ds.getConnection();// 线程池创建连接
             tl.set(connection);
         }
         return connection;
    }

    /**
     * @Description: SQL语句释放连接
     * @param :
     * @return: void
     */

    public static void free() throws SQLException {
        Connection connection = tl.get();
        if(connection != null){
            tl.remove();
            connection.setAutoCommit(true);// 避免还给数据库连接池的连接不是自动提交模式（建议）
            connection.close();// 释放连接（确保是以自动提交模式还给数据库连接池）
        }
    }
}