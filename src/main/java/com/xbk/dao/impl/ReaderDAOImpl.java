package com.xbk.dao.impl;

import com.xbk.dao.ReaderDAO;
import com.xbk.entity.Reader;
import com.xbk.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAOImpl implements ReaderDAO {

    @Override
    public int insert(Reader reader) {
        String sql = "INSERT INTO reader (card_number, name, gender, phone, email, address, max_borrow_count) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, reader.getCardNumber());
            pstmt.setString(2, reader.getName());
            pstmt.setString(3, reader.getGender());
            pstmt.setString(4, reader.getPhone());
            pstmt.setString(5, reader.getEmail());
            pstmt.setString(6, reader.getAddress());
            pstmt.setInt(7, reader.getMaxBorrowCount());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;

        } catch (SQLException e) {
            System.err.println("插入读者失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public int update(Reader reader) {
        String sql = "UPDATE reader SET card_number=?, name=?, gender=?, phone=?, email=?, address=?, max_borrow_count=? WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reader.getCardNumber());
            pstmt.setString(2, reader.getName());
            pstmt.setString(3, reader.getGender());
            pstmt.setString(4, reader.getPhone());
            pstmt.setString(5, reader.getEmail());
            pstmt.setString(6, reader.getAddress());
            pstmt.setInt(7, reader.getMaxBorrowCount());
            pstmt.setInt(8, reader.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("更新读者失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM reader WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("删除读者失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public Reader findById(int id) {
        String sql = "SELECT * FROM reader WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractReader(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("查询读者失败: " + e.getMessage());
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public Reader findByCardNumber(String cardNumber) {
        String sql = "SELECT * FROM reader WHERE card_number = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractReader(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("根据卡号查询读者失败: " + e.getMessage());
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public List<Reader> findAll() {
        String sql = "SELECT * FROM reader ORDER BY id";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Reader> readers = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                readers.add(extractReader(rs));
            }

        } catch (SQLException e) {
            System.err.println("查询所有读者失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(stmt);
            DBUtil.closeConnection(conn);
        }

        return readers;
    }

    private Reader extractReader(ResultSet rs) throws SQLException {
        Reader reader = new Reader();
        reader.setId(rs.getInt("id"));
        reader.setCardNumber(rs.getString("card_number"));
        reader.setName(rs.getString("name"));
        reader.setGender(rs.getString("gender"));
        reader.setPhone(rs.getString("phone"));
        reader.setEmail(rs.getString("email"));
        reader.setAddress(rs.getString("address"));
        reader.setMaxBorrowCount(rs.getInt("max_borrow_count"));
        reader.setCreatedAt(rs.getTimestamp("created_at"));
        reader.setUpdatedAt(rs.getTimestamp("updated_at"));
        return reader;
    }
}
