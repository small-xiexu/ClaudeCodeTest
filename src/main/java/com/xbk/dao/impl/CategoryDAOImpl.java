package com.xbk.dao.impl;

import com.xbk.dao.CategoryDAO;
import com.xbk.entity.Category;
import com.xbk.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类 DAO 实现类
 */
public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public int insert(Category category) {
        String sql = "INSERT INTO category (name, description) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;

        } catch (SQLException e) {
            System.err.println("插入分类失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public int update(Category category) {
        String sql = "UPDATE category SET name = ?, description = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            pstmt.setInt(3, category.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("更新分类失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM category WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("删除分类失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public Category findById(int id) {
        String sql = "SELECT * FROM category WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCategory(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("查询分类失败: " + e.getMessage());
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT * FROM category ORDER BY id";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Category> categories = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                categories.add(extractCategory(rs));
            }

        } catch (SQLException e) {
            System.err.println("查询所有分类失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(stmt);
            DBUtil.closeConnection(conn);
        }

        return categories;
    }

    @Override
    public Category findByName(String name) {
        String sql = "SELECT * FROM category WHERE name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCategory(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("根据名称查询分类失败: " + e.getMessage());
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    private Category extractCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setCreatedAt(rs.getTimestamp("created_at"));
        return category;
    }
}
