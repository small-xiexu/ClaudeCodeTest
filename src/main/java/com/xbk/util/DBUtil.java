package com.xbk.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库工具类
 * 负责数据库连接管理和初始化
 */
public class DBUtil {

    private static final String JDBC_URL = "jdbc:h2:~/library_db;MODE=MySQL";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static boolean initialized = false;

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 Driver not found", e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    /**
     * 初始化数据库
     * 执行 schema.sql 脚本创建表结构
     */
    public static synchronized void initDatabase() {
        if (initialized) {
            return;
        }

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db/schema.sql");
            if (is == null) {
                throw new RuntimeException("schema.sql not found");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }
                sql.append(line).append(" ");
                if (line.endsWith(";")) {
                    String sqlStatement = sql.toString().trim();
                    sqlStatement = sqlStatement.substring(0, sqlStatement.length() - 1);
                    stmt.execute(sqlStatement);
                    sql = new StringBuilder();
                }
            }

            reader.close();
            initialized = true;
            System.out.println("数据库初始化成功");

        } catch (Exception e) {
            System.err.println("数据库初始化失败: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        } finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    /**
     * 关闭连接
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("关闭连接失败: " + e.getMessage());
            }
        }
    }

    /**
     * 关闭 Statement
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("关闭 Statement 失败: " + e.getMessage());
            }
        }
    }

    /**
     * 关闭 ResultSet
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("关闭 ResultSet 失败: " + e.getMessage());
            }
        }
    }

    /**
     * 重置数据库（用于测试）
     */
    public static synchronized void resetDatabase() {
        initialized = false;
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            stmt.execute("DROP TABLE IF EXISTS borrow_record");
            stmt.execute("DROP TABLE IF EXISTS book");
            stmt.execute("DROP TABLE IF EXISTS reader");
            stmt.execute("DROP TABLE IF EXISTS category");

            System.out.println("数据库已重置");

        } catch (SQLException e) {
            System.err.println("重置数据库失败: " + e.getMessage());
        } finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
    }
}
