package com.xbk.dao;

import com.xbk.entity.Book;
import java.util.List;

/**
 * 图书 DAO 接口
 */
public interface BookDAO {
    int insert(Book book);
    int update(Book book);
    int delete(int id);
    Book findById(int id);
    Book findByIsbn(String isbn);
    List<Book> findAll();
    List<Book> findByCategory(int categoryId);
    List<Book> findByKeyword(String keyword);
    int updateAvailableQuantity(int bookId, int change);
}
