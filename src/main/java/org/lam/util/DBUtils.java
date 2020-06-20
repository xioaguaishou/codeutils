package org.lam.util;

import java.sql.*;

public class DBUtils {

    private static String driverClass = "com.mysql.jdbc.Driver"; // 驱动
    private static String url = "jdbc:mysql://localhost:3306/day17"; // 数据库url
    private static String user = "root"; // 登录名
    private static String password = "root"; // 登录密码

    private static Connection connection = null;

    static {
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    /**
     * 关闭数据库Connection
     * @param connection
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭ResultSet
     * @param rs
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭Statement
     * @param stat
     */
    public static void close(Statement stat) {
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 关闭PreparedStatement
     * @param prestat
     */
    public static void close(PreparedStatement prestat) {
        if (prestat != null) {
            try {
                prestat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭connection, PreparedStatement, ResultSet
     * @param connection
     * @param prestat
     * @param rs
     */
    public static void close(Connection connection, PreparedStatement prestat, ResultSet rs) {
        DBUtils.close(connection);
        DBUtils.close(prestat);
        DBUtils.close(rs);
    }

    /**
     * 关闭connection, Statement, ResultSet
     * @param connection
     * @param stat
     * @param rs
     */
    public static void close(Connection connection, Statement stat, ResultSet rs) {
        DBUtils.close(connection);
        DBUtils.close(stat);
        DBUtils.close(rs);
    }
}
