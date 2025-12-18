package com.xbk.dao.impl;

import com.xbk.dao.BookDAO;
import com.xbk.entity.Book;
import com.xbk.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {

    @Override
    public int insert(Book book) {
        String sql = "INSERT INTO book (isbn, title, author, publisher, publish_date, category_id, total_quantity, available_quantity, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setDate(5, book.getPublishDate());
            if (book.getCategoryId() != null) {
                pstmt.setInt(6, book.getCategoryId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            pstmt.setInt(7, book.getTotalQuantity());
            pstmt.setInt(8, book.getAvailableQuantity());
            pstmt.setString(9, book.getLocation());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;

        } catch (SQLException e) {
            System.err.println("插入图书失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public int update(Book book) {
        String sql = "UPDATE book SET isbn=?, title=?, author=?, publisher=?, publish_date=?, category_id=?, total_quantity=?, available_quantity=?, location=? WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setDate(5, book.getPublishDate());
            if (book.getCategoryId() != null) {
                pstmt.setInt(6, book.getCategoryId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            pstmt.setInt(7, book.getTotalQuantity());
            pstmt.setInt(8, book.getAvailableQuantity());
            pstmt.setString(9, book.getLocation());
            pstmt.setInt(10, book.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("更新图书失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM book WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("删除图书失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public Book findById(int id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractBook(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("查询图书失败: " + e.getMessage());
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public Book findByIsbn(String isbn) {
        String sql = "SELECT * FROM book WHERE isbn = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractBook(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("根据ISBN查询图书失败: " + e.getMessage());
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book ORDER BY id";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                books.add(extractBook(rs));
            }

        } catch (SQLException e) {
            System.err.println("查询所有图书失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(stmt);
            DBUtil.closeConnection(conn);
        }

        return books;
    }

    @Override
    public List<Book> findByCategory(int categoryId) {
        String sql = "SELECT * FROM book WHERE category_id = ? ORDER BY id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBook(rs));
            }

        } catch (SQLException e) {
            System.err.println("按分类查询图书失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }

        return books;
    }

    @Override
    public List<Book> findByKeyword(String keyword) {
        String sql = "SELECT * FROM book WHERE title LIKE ? OR author LIKE ? ORDER BY id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBook(rs));
            }

        } catch (SQLException e) {
            System.err.println("关键字搜索图书失败: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }

        return books;
    }

    @Override
    public int updateAvailableQuantity(int bookId, int change) {
        String sql = "UPDATE book SET available_quantity = available_quantity + ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, change);
            pstmt.setInt(2, bookId);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("更新图书可借数量失败: " + e.getMessage());
            return -1;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    private Book extractBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setPublishDate(rs.getDate("publish_date"));
        book.setCategoryId(rs.getInt("category_id"));
        book.setTotalQuantity(rs.getInt("total_quantity"));
        book.setAvailableQuantity(rs.getInt("available_quantity"));
        book.setLocation(rs.getString("location"));
        book.setCreatedAt(rs.getTimestamp("created_at"));
        book.setUpdatedAt(rs.getTimestamp("updated_at"));
        return book;
    }
}
