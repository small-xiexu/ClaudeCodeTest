package com.xbk.dto.response;

import com.xbk.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

/**
 * 分类响应
 */
@Schema(description = "分类响应")
public class CategoryResponse {

    @Schema(description = "分类ID", example = "1")
    private Integer id;

    @Schema(description = "分类名称", example = "计算机科学")
    private String name;

    @Schema(description = "分类描述", example = "计算机、软件工程、算法等相关书籍")
    private String description;

    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    private Timestamp createdAt;

    public CategoryResponse() {
    }

    public static CategoryResponse fromEntity(Category category) {
        if (category == null) {
            return null;
        }
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setCreatedAt(category.getCreatedAt());
        return response;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
