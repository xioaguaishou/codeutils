package org.lam.code.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DBUtil {
    private static String driver; // 数据库驱动
    private static String url; // 数据库连接
    private static String user; // 数据库用户名
    private static String password; // 数据库密码

    public static String getDriver() {
        return driver;
    }
    public static String getUrl() {
        return url;
    }
    public static String getUser() {
        return user;
    }
    public static String getPassword() {
        return password;
    }

    private static Connection conn = null;
    private static ResultSet rs = null;
    private static PreparedStatement ps = null;

    private static String propPath = "src/main/resources/db.properties";
    static {
        Properties prop = new Properties();
        FileInputStream is;
        try {
            is = new FileInputStream(propPath);
            prop.load(is);

            driver = prop.getProperty("jdbc.driver");
            url = prop.getProperty("jdbc.url");
            user = prop.getProperty("jdbc.user");
            password = prop.getProperty("jdbc.password");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {

        return conn;
    }

    public static void close(Connection conn, ResultSet rs) {
        // TODO Auto-generated method stub
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public static List<String> getTables() {
        // TODO Auto-generated method stub
        List<String> tables = new ArrayList<String>();
        String sql = "show tables;";
        try {
            ps = getConnection().prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    tables.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tables;
    }

    /**
     * -获取数据库所有表的结构信息
     * @param databaseName
     * @param tables
     * @return
     */
    public static List<Map<String, Map<String, String>>> getTableinformationList(String databaseName, List<String> tables) {
        List<Map<String, Map<String, String>>> tableList = new ArrayList<Map<String, Map<String, String>>>();

        if (tables != null) {
            for (int i = 0; i < tables.size(); i++) {
                String tableName = tables.get(i);
                tableList.add(getTableinformationMap(databaseName, tableName));
            }
        }
        return tableList;
    }

    /**
     * 获取单个表结构信息
     * @param databaseName
     * @param tableName
     * @return
     */
    public static Map<String, Map<String, String>> getTableinformationMap(String databaseName, String tableName) {
        Map<String, Map<String, String>> tableInfomationMap = new HashMap<String, Map<String, String>>();
        String sql;

        try {
            sql = "select * from information_schema.columns where table_schema ='" + databaseName + "'  and table_name = '" + tableName + "' ;"; // 查询表字段
            ps = getConnection().prepareStatement(sql);

            rs = ps.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();

            System.out.println(metaData.getColumnCount());
            while (rs.next()) {

                for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                    System.out.print(tableName + ":");
                    Map<String, String> hashtableInfomationColumnMap = new HashMap<String, String>();
                    for (int j = 1; j < metaData.getColumnCount(); j++) {
                        System.out.print(metaData.getColumnName(j) + ":" + rs.getString(metaData.getColumnName(j)) + "; ");
                        hashtableInfomationColumnMap.put(metaData.getColumnName(j), rs.getString(metaData.getColumnName(j)));
                        tableInfomationMap.put(tableName, hashtableInfomationColumnMap);
                    }
                }
                System.out.println();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tableInfomationMap;
    }

    public static Map<String,String> getCreateTableSQLMap(String tableName) {
        String sql = "SHOW CREATE TABLE "+ tableName +";";
        Map<String,String> createTableSQL = new HashMap<String, String>();
        try {
            ps = getConnection().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                createTableSQL.put(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return createTableSQL;
    }

}
