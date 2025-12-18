package com.xbk.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;

/**
 * 图书创建请求
 */
@Schema(description = "图书创建请求")
public class BookCreateRequest {

    @Schema(description = "ISBN编号", example = "978-7-111-12345-6", required = true)
    @NotBlank(message = "ISBN不能为空")
    @Pattern(regexp = "^\\d{3}-\\d-\\d{3}-\\d{5}-\\d$", message = "ISBN格式不正确，正确格式：xxx-x-xxx-xxxxx-x")
    private String isbn;

    @Schema(description = "书名", example = "Java编程思想", required = true)
    @NotBlank(message = "书名不能为空")
    @Size(max = 100, message = "书名长度不能超过100个字符")
    private String title;

    @Schema(description = "作者", example = "Bruce Eckel", required = true)
    @NotBlank(message = "作者不能为空")
    @Size(max = 50, message = "作者名称长度不能超过50个字符")
    private String author;

    @Schema(description = "出版社", example = "机械工业出版社")
    @Size(max = 100, message = "出版社名称长度不能超过100个字符")
    private String publisher;

    @Schema(description = "出版日期", example = "2007-06-01")
    private Date publishDate;

    @Schema(description = "分类ID", example = "1", required = true)
    @NotNull(message = "分类ID不能为空")
    @Min(value = 1, message = "分类ID必须大于0")
    private Integer categoryId;

    @Schema(description = "总藏书量", example = "10", required = true)
    @NotNull(message = "总藏书量不能为空")
    @Min(value = 1, message = "总藏书量必须大于0")
    private Integer totalQuantity;

    @Schema(description = "位置", example = "A区-1层-001")
    @Size(max = 50, message = "位置描述长度不能超过50个字符")
    private String location;

    public BookCreateRequest() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
