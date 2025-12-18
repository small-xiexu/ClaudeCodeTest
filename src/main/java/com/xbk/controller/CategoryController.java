package com.xbk.controller;

import com.xbk.dto.request.CategoryRequest;
import com.xbk.dto.response.CategoryResponse;
import com.xbk.entity.Category;
import com.xbk.exception.BusinessException;
import com.xbk.exception.ResourceNotFoundException;
import com.xbk.service.CategoryService;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类管理控制器
 */
@Tag(name = "分类管理", description = "图书分类相关接口")
@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "添加分类", description = "创建新的图书分类")
    @PostMapping
    public ApiResponse<CategoryResponse> addCategory(@Valid @RequestBody CategoryRequest request) {
        logger.info("添加分类: {}", request.getName());
        
        Category category = new Category(request.getName(), request.getDescription());
        boolean result = categoryService.addCategory(category);
        
        if (!result) {
            logger.error("添加分类失败: {}", request.getName());
            throw new BusinessException("添加分类失败");
        }
        
        logger.info("添加分类成功: {} (ID: {})", request.getName(), category.getId());
        return ApiResponse.success(CategoryResponse.fromEntity(category));
    }

    @Operation(summary = "更新分类", description = "更新已有分类信息")
    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @Parameter(description = "分类ID", required = true) @PathVariable @Min(value = 1, message = "分类ID必须大于0") Integer id,
            @Valid @RequestBody CategoryRequest request) {
        logger.info("更新分类: ID={}, 名称={}", id, request.getName());
        
        Category existingCategory = categoryService.getCategoryById(id);
        if (existingCategory == null) {
            logger.error("分类不存在: ID={}", id);
            throw new ResourceNotFoundException("分类不存在: ID=" + id);
        }
        
        Category category = new Category(request.getName(), request.getDescription());
        category.setId(id);
        
        boolean result = categoryService.updateCategory(category);
        if (!result) {
            logger.error("更新分类失败: ID={}", id);
            throw new BusinessException("更新分类失败");
        }
        
        Category updatedCategory = categoryService.getCategoryById(id);
        logger.info("更新分类成功: ID={}", id);
        return ApiResponse.success(CategoryResponse.fromEntity(updatedCategory));
    }

    @Operation(summary = "删除分类", description = "删除指定的分类")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(
            @Parameter(description = "分类ID", required = true) @PathVariable @Min(value = 1, message = "分类ID必须大于0") Integer id) {
        logger.info("删除分类: ID={}", id);
        
        Category existingCategory = categoryService.getCategoryById(id);
        if (existingCategory == null) {
            logger.error("分类不存在: ID={}", id);
            throw new ResourceNotFoundException("分类不存在: ID=" + id);
        }
        
        boolean result = categoryService.deleteCategory(id);
        if (!result) {
            logger.error("删除分类失败: ID={}", id);
            throw new BusinessException("删除分类失败");
        }
        
        logger.info("删除分类成功: ID={}", id);
        return ApiResponse.success();
    }

    @Operation(summary = "获取分类详情", description = "根据ID获取分类详细信息")
    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(
            @Parameter(description = "分类ID", required = true) @PathVariable @Min(value = 1, message = "分类ID必须大于0") Integer id) {
        logger.info("获取分类详情: ID={}", id);
        
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            logger.error("分类不存在: ID={}", id);
            throw new ResourceNotFoundException("分类不存在: ID=" + id);
        }
        
        logger.info("获取分类详情成功: ID={}", id);
        return ApiResponse.success(CategoryResponse.fromEntity(category));
    }

    @Operation(summary = "获取所有分类", description = "获取所有图书分类列表")
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        logger.info("获取所有分类");
        
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> responses = categories.stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
        
        logger.info("获取所有分类成功，共{}个", responses.size());
        return ApiResponse.success(responses);
    }
}
