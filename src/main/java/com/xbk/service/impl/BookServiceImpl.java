package com.xbk.service.impl;

import com.xbk.entity.Book;
import com.xbk.entity.BorrowRecord;
import com.xbk.mapper.BookMapper;
import com.xbk.mapper.BorrowRecordMapper;
import com.xbk.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 图书服务实现类
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Override
    public boolean addBook(Book book) {
        // 验证ISBN唯一性
        Book existing = bookMapper.findByIsbn(book.getIsbn());
        if (existing != null) {
            System.err.println("ISBN已存在: " + book.getIsbn());
            return false;
        }

        int result = bookMapper.insert(book);
        if (result > 0) {
            System.out.println("图书添加成功，ID: " + book.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateBook(Book book) {
        // 验证图书是否存在
        Book existing = bookMapper.selectById(book.getId());
        if (existing == null) {
            System.err.println("图书不存在，ID: " + book.getId());
            return false;
        }

        // 如果修改了ISBN，需要验证新ISBN的唯一性
        if (!existing.getIsbn().equals(book.getIsbn())) {
            Book duplicateIsbn = bookMapper.findByIsbn(book.getIsbn());
            if (duplicateIsbn != null) {
                System.err.println("ISBN已被其他图书使用: " + book.getIsbn());
                return false;
            }
        }

        int affected = bookMapper.updateById(book);
        if (affected > 0) {
            System.out.println("图书更新成功");
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBook(int id) {
        // 检查是否有未归还的借阅记录
        List<BorrowRecord> activeRecords = borrowRecordMapper.findByBookId(id);
        for (BorrowRecord record : activeRecords) {
            if (BorrowRecord.STATUS_BORROWED.equals(record.getStatus())) {
                System.err.println("该图书还有未归还的借阅记录，无法删除");
                return false;
            }
        }

        int affected = bookMapper.deleteById(id);
        if (affected > 0) {
            System.out.println("图书删除成功");
            return true;
        }
        return false;
    }

    @Override
    public Book getBookById(int id) {
        return bookMapper.selectById(id);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookMapper.findByIsbn(isbn);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookMapper.selectList(null);
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        return bookMapper.findByKeyword(keyword);
    }

    @Override
    public List<Book> getBooksByCategory(int categoryId) {
        return bookMapper.findByCategory(categoryId);
    }
}
