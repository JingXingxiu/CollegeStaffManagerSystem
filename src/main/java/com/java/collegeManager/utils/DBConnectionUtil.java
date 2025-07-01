package com.java.collegeManager.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionUtil {
    private static String url;
    private static String user;
    private static String password;

    static {
        // 加载驱动和数据库配置
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            loadProperties();
        } catch (ClassNotFoundException | IOException e) {
            throw new ExceptionInInitializerError("初始化数据库连接失败: " + e.getMessage());
        }
    }

    private static void loadProperties() throws IOException {
        try (InputStream input = DBConnectionUtil.class
                .getClassLoader().getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IOException("无法找到配置文件 config.properties");
            }

            Properties prop = new Properties();
            prop.load(input);

            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");

        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static String getDatabaseUrl() {
        return url;
    }

    public static String getDatabaseUser() {
        return user;
    }

    public static String getDatabasePassword() {
        return password;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("关闭数据库连接时出错: " + e.getMessage());
            }
        }
    }
}