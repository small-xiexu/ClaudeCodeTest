package com.xbk.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DBUtil 测试类
 */
class DBUtilTest {

    @BeforeAll
    static void setUp() {
        DBUtil.resetDatabase();
        DBUtil.initDatabase();
    }

    @AfterAll
    static void tearDown() {
        DBUtil.resetDatabase();
    }

    @Test
    void testGetConnection() {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            assertNotNull(conn, "连接不应为 null");
            assertFalse(conn.isClosed(), "连接应该是打开的");
        } catch (SQLException e) {
            fail("获取连接失败: " + e.getMessage());
        } finally {
            DBUtil.closeConnection(conn);
        }
    }

    @Test
    void testInitDatabase() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT COUNT(*) FROM category");
            assertTrue(rs.next());
            int count = rs.getInt(1);
            assertEquals(5, count, "分类表应该有 5 条初始数据");

        } catch (SQLException e) {
            fail("测试初始化数据库失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(stmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Test
    void testTableExists() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();

            String[] tables = {"category", "book", "reader", "borrow_record"};

            for (String table : tables) {
                rs = stmt.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + table.toUpperCase() + "'");
                assertTrue(rs.next());
                int count = rs.getInt(1);
                assertEquals(1, count, table + " 表应该存在");
                DBUtil.closeResultSet(rs);
            }

        } catch (SQLException e) {
            fail("测试表存在性失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(stmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Test
    void testCloseConnection() {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            assertNotNull(conn);
            assertFalse(conn.isClosed());

            DBUtil.closeConnection(conn);
            assertTrue(conn.isClosed(), "连接应该被关闭");

        } catch (SQLException e) {
            fail("测试关闭连接失败: " + e.getMessage());
        }
    }

    @Test
    void testCloseNullConnection() {
        assertDoesNotThrow(() -> DBUtil.closeConnection(null), "关闭 null 连接不应抛出异常");
    }
}
