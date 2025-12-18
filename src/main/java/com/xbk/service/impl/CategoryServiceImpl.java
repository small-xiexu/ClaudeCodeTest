package com.xbk.service.impl;

import com.xbk.entity.Book;
import com.xbk.entity.Category;
import com.xbk.mapper.BookMapper;
import com.xbk.mapper.CategoryMapper;
import com.xbk.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类服务实现类
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BookMapper bookMapper;

    @Override
    public boolean addCategory(Category category) {
        // 验证分类名称唯一性
        Category existing = categoryMapper.findByName(category.getName());
        if (existing != null) {
            System.err.println("分类名称已存在: " + category.getName());
            return false;
        }

        int result = categoryMapper.insert(category);
        if (result > 0) {
            System.out.println("分类添加成功，ID: " + category.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCategory(Category category) {
        // 验证分类是否存在
        Category existing = categoryMapper.selectById(category.getId());
        if (existing == null) {
            System.err.println("分类不存在，ID: " + category.getId());
            return false;
        }

        // 如果修改了名称，需要验证新名称的唯一性
        if (!existing.getName().equals(category.getName())) {
            Category duplicateName = categoryMapper.findByName(category.getName());
            if (duplicateName != null) {
                System.err.println("分类名称已被使用: " + category.getName());
                return false;
            }
        }

        int affected = categoryMapper.updateById(category);
        if (affected > 0) {
            System.out.println("分类更新成功");
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCategory(int id) {
        // 检查是否有图书使用该分类
        List<Book> books = bookMapper.findByCategory(id);
        if (!books.isEmpty()) {
            System.err.println("该分类下还有 " + books.size() + " 本图书，无法删除");
            return false;
        }

        int affected = categoryMapper.deleteById(id);
        if (affected > 0) {
            System.out.println("分类删除成功");
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectList(null);
    }
}
