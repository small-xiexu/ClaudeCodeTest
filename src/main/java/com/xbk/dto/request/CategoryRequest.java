package com.xbk.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 分类请求
 */
@Schema(description = "分类请求")
public class CategoryRequest {

    @Schema(description = "分类名称", example = "计算机科学", required = true)
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50个字符")
    private String name;

    @Schema(description = "分类描述", example = "计算机、软件工程、算法等相关书籍")
    @Size(max = 200, message = "分类描述长度不能超过200个字符")
    private String description;

    public CategoryRequest() {
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
}
