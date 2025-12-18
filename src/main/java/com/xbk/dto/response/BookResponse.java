package com.xbk.dto.response;

import com.xbk.entity.Book;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 图书响应
 */
@Schema(description = "图书响应")
public class BookResponse {

    @Schema(description = "图书ID", example = "1")
    private Integer id;

    @Schema(description = "ISBN编号", example = "978-7-111-12345-6")
    private String isbn;

    @Schema(description = "书名", example = "Java编程思想")
    private String title;

    @Schema(description = "作者", example = "Bruce Eckel")
    private String author;

    @Schema(description = "出版社", example = "机械工业出版社")
    private String publisher;

    @Schema(description = "出版日期", example = "2007-06-01")
    private Date publishDate;

    @Schema(description = "分类ID", example = "1")
    private Integer categoryId;

    @Schema(description = "总藏书量", example = "10")
    private Integer totalQuantity;

    @Schema(description = "可借数量", example = "8")
    private Integer availableQuantity;

    @Schema(description = "位置", example = "A区-1层-001")
    private String location;

    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    private Timestamp createdAt;

    @Schema(description = "更新时间", example = "2024-01-01 12:00:00")
    private Timestamp updatedAt;

    public BookResponse() {
    }

    public static BookResponse fromEntity(Book book) {
        if (book == null) {
            return null;
        }
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setIsbn(book.getIsbn());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setPublisher(book.getPublisher());
        response.setPublishDate(book.getPublishDate());
        response.setCategoryId(book.getCategoryId());
        response.setTotalQuantity(book.getTotalQuantity());
        response.setAvailableQuantity(book.getAvailableQuantity());
        response.setLocation(book.getLocation());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
