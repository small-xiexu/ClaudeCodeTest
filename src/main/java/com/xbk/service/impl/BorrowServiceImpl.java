package com.xbk.service.impl;

import com.xbk.dao.BookDAO;
import com.xbk.dao.BorrowRecordDAO;
import com.xbk.dao.ReaderDAO;
import com.xbk.dao.impl.BookDAOImpl;
import com.xbk.dao.impl.BorrowRecordDAOImpl;
import com.xbk.dao.impl.ReaderDAOImpl;
import com.xbk.entity.Book;
import com.xbk.entity.BorrowRecord;
import com.xbk.entity.Reader;
import com.xbk.service.BorrowService;
import com.xbk.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 借阅服务实现类
 * 包含借还书的核心业务逻辑和事务管理
 */
public class BorrowServiceImpl implements BorrowService {

    private BookDAO bookDAO = new BookDAOImpl();
    private ReaderDAO readerDAO = new ReaderDAOImpl();
    private BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAOImpl();

    @Override
    public boolean borrowBook(int bookId, int readerId, int borrowDays) {
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            // 1. 检查图书是否存在且可借
            Book book = bookDAO.findById(bookId);
            if (book == null) {
                System.err.println("图书不存在");
                return false;
            }
            if (book.getAvailableQuantity() <= 0) {
                System.err.println("图书库存不足，无法借阅");
                return false;
            }

            // 2. 检查读者是否存在
            Reader reader = readerDAO.findById(readerId);
            if (reader == null) {
                System.err.println("读者不存在");
                return false;
            }

            // 3. 检查读者借书数量是否达到上限
            List<BorrowRecord> activeRecords = borrowRecordDAO.findActiveByReaderId(readerId);
            if (activeRecords.size() >= reader.getMaxBorrowCount()) {
                System.err.println("已达到最大借书数量限制（" + reader.getMaxBorrowCount() + "本）");
                return false;
            }

            // 4. 创建借阅记录
            Date dueDate = Date.valueOf(LocalDate.now().plusDays(borrowDays));
            BorrowRecord record = new BorrowRecord(bookId, readerId, dueDate);
            int recordId = borrowRecordDAO.insert(record);

            if (recordId <= 0) {
                conn.rollback();
                System.err.println("创建借阅记录失败");
                return false;
            }

            // 5. 更新图书可借数量（-1）
            int affected = bookDAO.updateAvailableQuantity(bookId, -1);
            if (affected <= 0) {
                conn.rollback();
                System.err.println("更新图书数量失败");
                return false;
            }

            conn.commit(); // 提交事务
            System.out.println("借书成功！借阅记录ID: " + recordId + ", 应还日期: " + dueDate);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                } catch (SQLException ex) {
                    System.err.println("事务回滚失败: " + ex.getMessage());
                }
            }
            System.err.println("借书失败: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // 恢复自动提交
                } catch (SQLException e) {
                    System.err.println("恢复自动提交失败: " + e.getMessage());
                }
            }
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public boolean returnBook(int recordId) {
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            // 1. 查询借阅记录
            BorrowRecord record = borrowRecordDAO.findById(recordId);
            if (record == null) {
                System.err.println("借阅记录不存在");
                return false;
            }

            // 2. 检查是否已归还
            if (BorrowRecord.STATUS_RETURNED.equals(record.getStatus())) {
                System.err.println("该图书已归还，无需重复操作");
                return false;
            }

            // 3. 计算罚金（如果逾期）
            double fine = calculateFine(record);

            // 4. 更新借阅记录
            record.setReturnDate(new Timestamp(System.currentTimeMillis()));
            record.setStatus(BorrowRecord.STATUS_RETURNED);
            record.setFine(BigDecimal.valueOf(fine));
            if (fine > 0) {
                record.setRemarks("逾期归还，罚金: " + fine + "元");
            } else {
                record.setRemarks("按时归还");
            }

            int affected = borrowRecordDAO.update(record);
            if (affected <= 0) {
                conn.rollback();
                System.err.println("更新借阅记录失败");
                return false;
            }

            // 5. 更新图书可借数量（+1）
            affected = bookDAO.updateAvailableQuantity(record.getBookId(), 1);
            if (affected <= 0) {
                conn.rollback();
                System.err.println("更新图书数量失败");
                return false;
            }

            conn.commit(); // 提交事务

            if (fine > 0) {
                System.out.println("还书成功！逾期 " + (int)fine + " 天，需支付罚金: " + fine + "元");
            } else {
                System.out.println("还书成功！按时归还，无罚金");
            }
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                } catch (SQLException ex) {
                    System.err.println("事务回滚失败: " + ex.getMessage());
                }
            }
            System.err.println("还书失败: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // 恢复自动提交
                } catch (SQLException e) {
                    System.err.println("恢复自动提交失败: " + e.getMessage());
                }
            }
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public List<BorrowRecord> getReaderBorrowHistory(int readerId) {
        return borrowRecordDAO.findByReaderId(readerId);
    }

    @Override
    public List<BorrowRecord> getActiveBorrows(int readerId) {
        return borrowRecordDAO.findActiveByReaderId(readerId);
    }

    @Override
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecordDAO.findOverdueRecords();
    }

    @Override
    public double calculateFine(BorrowRecord record) {
        if (record.getDueDate() == null) {
            return 0;
        }

        LocalDate dueDate = record.getDueDate().toLocalDate();
        LocalDate returnDate;

        if (record.getReturnDate() != null) {
            // Java 8兼容的Timestamp转LocalDate方式
            returnDate = record.getReturnDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
        } else {
            returnDate = LocalDate.now();
        }

        // 计算逾期天数
        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);

        if (overdueDays > 0) {
            return overdueDays * 1.0; // 每天1元
        }

        return 0;
    }
}
