package com.xbk.controller;

import com.xbk.dto.request.BookCreateRequest;
import com.xbk.dto.request.BookUpdateRequest;
import com.xbk.dto.response.BookResponse;
import com.xbk.entity.Book;
import com.xbk.exception.BusinessException;
import com.xbk.exception.ResourceNotFoundException;
import com.xbk.service.BookService;
import com.xbk.vo.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图书管理控制器
 */
@Tag(name = "图书管理", description = "图书相关接口")
@RestController
@RequestMapping("/books")
@Validated
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @Operation(summary = "添加图书", description = "创建新的图书记录")
    @PostMapping
    public ApiResponse<BookResponse> addBook(@Valid @RequestBody BookCreateRequest request) {
        logger.info("添加图书: {}", request.getTitle());
        
        Book book = new Book();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublishDate(request.getPublishDate());
        book.setCategoryId(request.getCategoryId());
        book.setTotalQuantity(request.getTotalQuantity());
        book.setAvailableQuantity(request.getTotalQuantity());
        book.setLocation(request.getLocation());
        
        boolean result = bookService.addBook(book);
        if (!result) {
            logger.error("添加图书失败: {}", request.getTitle());
            throw new BusinessException("添加图书失败");
        }
        
        logger.info("添加图书成功: {} (ID: {})", request.getTitle(), book.getId());
        return ApiResponse.success(BookResponse.fromEntity(book));
    }

    @Operation(summary = "更新图书", description = "更新已有图书信息")
    @PutMapping("/{id}")
    public ApiResponse<BookResponse> updateBook(
            @Parameter(description = "图书ID", required = true) @PathVariable @Min(value = 1, message = "图书ID必须大于0") Integer id,
            @Valid @RequestBody BookUpdateRequest request) {
        logger.info("更新图书: ID={}", id);
        
        Book existingBook = bookService.getBookById(id);
        if (existingBook == null) {
            logger.error("图书不存在: ID={}", id);
            throw new ResourceNotFoundException("图书不存在: ID=" + id);
        }
        
        Book book = new Book();
        book.setId(id);
        if (request.getIsbn() != null) {
            book.setIsbn(request.getIsbn());
        }
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getPublisher() != null) {
            book.setPublisher(request.getPublisher());
        }
        if (request.getPublishDate() != null) {
            book.setPublishDate(request.getPublishDate());
        }
        if (request.getCategoryId() != null) {
            book.setCategoryId(request.getCategoryId());
        }
        if (request.getTotalQuantity() != null) {
            book.setTotalQuantity(request.getTotalQuantity());
        }
        if (request.getLocation() != null) {
            book.setLocation(request.getLocation());
        }
        
        boolean result = bookService.updateBook(book);
        if (!result) {
            logger.error("更新图书失败: ID={}", id);
            throw new BusinessException("更新图书失败");
        }
        
        Book updatedBook = bookService.getBookById(id);
        logger.info("更新图书成功: ID={}", id);
        return ApiResponse.success(BookResponse.fromEntity(updatedBook));
    }

    @Operation(summary = "删除图书", description = "删除指定的图书")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBook(
            @Parameter(description = "图书ID", required = true) @PathVariable @Min(value = 1, message = "图书ID必须大于0") Integer id) {
        logger.info("删除图书: ID={}", id);
        
        Book existingBook = bookService.getBookById(id);
        if (existingBook == null) {
            logger.error("图书不存在: ID={}", id);
            throw new ResourceNotFoundException("图书不存在: ID=" + id);
        }
        
        boolean result = bookService.deleteBook(id);
        if (!result) {
            logger.error("删除图书失败: ID={}", id);
            throw new BusinessException("删除图书失败");
        }
        
        logger.info("删除图书成功: ID={}", id);
        return ApiResponse.success();
    }

    @Operation(summary = "获取图书详情", description = "根据ID获取图书详细信息")
    @GetMapping("/{id}")
    public ApiResponse<BookResponse> getBookById(
            @Parameter(description = "图书ID", required = true) @PathVariable @Min(value = 1, message = "图书ID必须大于0") Integer id) {
        logger.info("获取图书详情: ID={}", id);
        
        Book book = bookService.getBookById(id);
        if (book == null) {
            logger.error("图书不存在: ID={}", id);
            throw new ResourceNotFoundException("图书不存在: ID=" + id);
        }
        
        logger.info("获取图书详情成功: ID={}", id);
        return ApiResponse.success(BookResponse.fromEntity(book));
    }

    @Operation(summary = "根据ISBN查询图书", description = "通过ISBN编号查询图书")
    @GetMapping("/isbn/{isbn}")
    public ApiResponse<BookResponse> getBookByIsbn(
            @Parameter(description = "ISBN编号", required = true) @PathVariable @NotBlank(message = "ISBN不能为空") String isbn) {
        logger.info("根据ISBN查询图书: {}", isbn);
        
        Book book = bookService.getBookByIsbn(isbn);
        if (book == null) {
            logger.error("图书不存在: ISBN={}", isbn);
            throw new ResourceNotFoundException("图书不存在: ISBN=" + isbn);
        }
        
        logger.info("根据ISBN查询图书成功: {}", isbn);
        return ApiResponse.success(BookResponse.fromEntity(book));
    }

    @Operation(summary = "获取所有图书", description = "获取所有图书列表")
    @GetMapping
    public ApiResponse<List<BookResponse>> getAllBooks() {
        logger.info("获取所有图书");
        
        List<Book> books = bookService.getAllBooks();
        List<BookResponse> responses = books.stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
        
        logger.info("获取所有图书成功，共{}本", responses.size());
        return ApiResponse.success(responses);
    }

    @Operation(summary = "搜索图书", description = "根据关键词搜索图书（书名、作者、ISBN）")
    @GetMapping("/search")
    public ApiResponse<List<BookResponse>> searchBooks(
            @Parameter(description = "搜索关键词", required = true) @RequestParam @NotBlank(message = "搜索关键词不能为空") String keyword) {
        logger.info("搜索图书: 关键词={}", keyword);
        
        List<Book> books = bookService.searchBooks(keyword);
        List<BookResponse> responses = books.stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
        
        logger.info("搜索图书成功，共找到{}本", responses.size());
        return ApiResponse.success(responses);
    }

    @Operation(summary = "按分类查询图书", description = "获取指定分类下的所有图书")
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<BookResponse>> getBooksByCategory(
            @Parameter(description = "分类ID", required = true) @PathVariable @Min(value = 1, message = "分类ID必须大于0") Integer categoryId) {
        logger.info("按分类查询图书: 分类ID={}", categoryId);
        
        List<Book> books = bookService.getBooksByCategory(categoryId);
        List<BookResponse> responses = books.stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
        
        logger.info("按分类查询图书成功，共{}本", responses.size());
        return ApiResponse.success(responses);
    }
}
