package com.xbk.service.impl;

import com.xbk.dao.BookDAO;
import com.xbk.dao.BorrowRecordDAO;
import com.xbk.dao.impl.BookDAOImpl;
import com.xbk.dao.impl.BorrowRecordDAOImpl;
import com.xbk.entity.Book;
import com.xbk.entity.BorrowRecord;
import com.xbk.service.BookService;

import java.util.List;

/**
 * 图书服务实现类
 */
public class BookServiceImpl implements BookService {

    private BookDAO bookDAO = new BookDAOImpl();
    private BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAOImpl();

    @Override
    public boolean addBook(Book book) {
        // 验证ISBN唯一性
        Book existing = bookDAO.findByIsbn(book.getIsbn());
        if (existing != null) {
            System.err.println("ISBN已存在: " + book.getIsbn());
            return false;
        }

        int id = bookDAO.insert(book);
        if (id > 0) {
            System.out.println("图书添加成功，ID: " + id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateBook(Book book) {
        // 验证图书是否存在
        Book existing = bookDAO.findById(book.getId());
        if (existing == null) {
            System.err.println("图书不存在，ID: " + book.getId());
            return false;
        }

        // 如果修改了ISBN，需要验证新ISBN的唯一性
        if (!existing.getIsbn().equals(book.getIsbn())) {
            Book duplicateIsbn = bookDAO.findByIsbn(book.getIsbn());
            if (duplicateIsbn != null) {
                System.err.println("ISBN已被其他图书使用: " + book.getIsbn());
                return false;
            }
        }

        int affected = bookDAO.update(book);
        if (affected > 0) {
            System.out.println("图书更新成功");
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBook(int id) {
        // 检查是否有未归还的借阅记录
        List<BorrowRecord> activeRecords = borrowRecordDAO.findByBookId(id);
        for (BorrowRecord record : activeRecords) {
            if (BorrowRecord.STATUS_BORROWED.equals(record.getStatus())) {
                System.err.println("该图书还有未归还的借阅记录，无法删除");
                return false;
            }
        }

        int affected = bookDAO.delete(id);
        if (affected > 0) {
            System.out.println("图书删除成功");
            return true;
        }
        return false;
    }

    @Override
    public Book getBookById(int id) {
        return bookDAO.findById(id);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookDAO.findByIsbn(isbn);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        return bookDAO.findByKeyword(keyword);
    }

    @Override
    public List<Book> getBooksByCategory(int categoryId) {
        return bookDAO.findByCategory(categoryId);
    }
}
