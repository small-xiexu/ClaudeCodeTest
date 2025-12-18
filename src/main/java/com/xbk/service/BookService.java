package com.xbk.service;

import com.xbk.entity.Book;
import java.util.List;

/**
 * 图书服务接口
 */
public interface BookService {
    boolean addBook(Book book);
    boolean updateBook(Book book);
    boolean deleteBook(int id);
    Book getBookById(int id);
    Book getBookByIsbn(String isbn);
    List<Book> getAllBooks();
    List<Book> searchBooks(String keyword);
    List<Book> getBooksByCategory(int categoryId);
}
