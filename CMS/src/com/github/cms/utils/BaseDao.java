package com.github.cms.utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

public class BaseDao {

    /***
     * @Description: 对数据库进行更新操作
     * @param sql:   sql语句
     * @param args:  输入的?占位符
     * @return: int 返回更新的行数
     */

    protected int update(String sql, Object... args) throws SQLException {
        // 建PreparedStatement对象，对sql预编译
        Connection connection = JDBCTools.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);// 创建PreparedStatement对象，传入sql语句
        // 对占位符进行赋值
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
        }

        // 发送sql语句并返回结果
        int len = ps.executeUpdate();
        ps.close();
        // 开启事务（连接不自动提交，可能会回滚）就不回收连接
        if (connection.getAutoCommit()) {
            JDBCTools.free();// 回收连接
        }
        return len;
    }

    /**
     * @param clazz: 接受的T类型的Class对象，比如查询的是员工信息，clazz代表Employee.class
     * @param sql:   sql语句
     * @param args:  输入的?占位符
     * @Description: 对数据库进行查询操作，因为要返回的是存储查询对象的集合，所以使用了泛型
     * @return: java.util.ArrayList<T> 返回查询对象的集合
     */

    protected <T> ArrayList<T> query(Class<T> clazz, String sql, Object... args) throws Exception {
        // 获取PreparedStatement对象
        Connection connection = JDBCTools.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
        }

        // 发送sql语句
        ArrayList<T> list = new ArrayList<>();
        ResultSet res = ps.executeQuery();

        // 获取结果
        ResultSetMetaData metaData = res.getMetaData();// 存储的列数据
        int columnCount = metaData.getColumnCount();// 获取结果集列数

        // 遍历结果集ResultSet，把查询结果中的一条一条记录，变成一个一个T 对象，放到list中。
        while (res.next()) {// 行遍历，一行对应一个T对象（T对象的类的属性就是这一行的列字段）
            T t = clazz.newInstance();// 反射实例化对象，要求这个clazz类型必须有公共的无参构造

            // 把这条记录的每一个单元格的值取出来，设置到t对象对应的属性中。
            for (int i = 1; i <= columnCount; i++) {
                Object value = res.getObject(i);// 获取对应列的值
                String columnName = metaData.getColumnLabel(i);// 获取对应列的字段名或字段的别名
                Field field = clazz.getDeclaredField(columnName);// 获取T类中的属性
                field.setAccessible(true);// 这么做可以操作T类中private的属性
                field.set(t, value);
            }
            list.add(t);
        }

        res.close();
        ps.close();
        // 开启事务（连接不自动提交，可能会回滚）就不回收连接
        if (connection.getAutoCommit()) {
            JDBCTools.free();
        }
        return list;
    }

    // 保证query()方法，返回T对象（集合当作的第一行）
    protected <T> T queryBean(Class<T> clazz, String sql, Object... args) throws Exception {
        ArrayList<T> list = query(clazz, sql, args);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
}