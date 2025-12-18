package com.xbk.dao.impl;

import com.xbk.dao.BorrowRecordDAO;
import com.xbk.entity.BorrowRecord;
import com.xbk.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDAOImpl implements BorrowRecordDAO {

    @Override
    public int insert(BorrowRecord record) {
        String sql = "INSERT INTO borrow_record (book_id, reader_id, due_date, status, fine) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, record.getBookId());
            pstmt.setInt(2, record.getReaderId());
            pstmt.setDate(3, record.getDueDate());
            pstmt.setString(4, record.getStatus());
            pstmt.setBigDecimal(5, record.getFine());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;

        } catch (SQLException e) {
            System.err.println("插入借阅记录失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public int update(BorrowRecord record) {
        String sql = "UPDATE borrow_record SET return_date=?, status=?, fine=?, remarks=? WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, record.getReturnDate());
            pstmt.setString(2, record.getStatus());
            pstmt.setBigDecimal(3, record.getFine());
            pstmt.setString(4, record.getRemarks());
            pstmt.setInt(5, record.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("更新借阅记录失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public BorrowRecord findById(int id) {
        String sql = "SELECT * FROM borrow_record WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractBorrowRecord(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("查询借阅记录失败: " + e.getMessage());
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public List<BorrowRecord> findByReaderId(int readerId) {
        String sql = "SELECT * FROM borrow_record WHERE reader_id = ? ORDER BY borrow_date DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }

        } catch (SQLException e) {
            System.err.println("查询读者借阅记录失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }

        return records;
    }

    @Override
    public List<BorrowRecord> findByBookId(int bookId) {
        String sql = "SELECT * FROM borrow_record WHERE book_id = ? ORDER BY borrow_date DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }

        } catch (SQLException e) {
            System.err.println("查询图书借阅记录失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }

        return records;
    }

    @Override
    public List<BorrowRecord> findActiveByReaderId(int readerId) {
        String sql = "SELECT * FROM borrow_record WHERE reader_id = ? AND status = 'BORROWED' ORDER BY borrow_date DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }

        } catch (SQLException e) {
            System.err.println("查询读者当前借阅记录失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }

        return records;
    }

    @Override
    public List<BorrowRecord> findOverdueRecords() {
        String sql = "SELECT * FROM borrow_record WHERE status = 'BORROWED' AND due_date < CURRENT_DATE ORDER BY due_date";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }

        } catch (SQLException e) {
            System.err.println("查询逾期记录失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(stmt);
            DBUtil.closeConnection(conn);
        }

        return records;
    }

    private BorrowRecord extractBorrowRecord(ResultSet rs) throws SQLException {
        BorrowRecord record = new BorrowRecord();
        record.setId(rs.getInt("id"));
        record.setBookId(rs.getInt("book_id"));
        record.setReaderId(rs.getInt("reader_id"));
        record.setBorrowDate(rs.getTimestamp("borrow_date"));
        record.setDueDate(rs.getDate("due_date"));
        record.setReturnDate(rs.getTimestamp("return_date"));
        record.setStatus(rs.getString("status"));
        record.setFine(rs.getBigDecimal("fine"));
        record.setRemarks(rs.getString("remarks"));
        return record;
    }
}
