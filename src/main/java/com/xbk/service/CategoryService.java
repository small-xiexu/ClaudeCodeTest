package com.xbk.service;

import com.xbk.entity.Category;
import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {
    boolean addCategory(Category category);
    boolean updateCategory(Category category);
    boolean deleteCategory(int id);
    Category getCategoryById(int id);
    List<Category> getAllCategories();
}
