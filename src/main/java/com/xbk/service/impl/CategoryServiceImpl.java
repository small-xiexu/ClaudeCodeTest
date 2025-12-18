package com.xbk.service.impl;

import com.xbk.dao.BookDAO;
import com.xbk.dao.CategoryDAO;
import com.xbk.dao.impl.BookDAOImpl;
import com.xbk.dao.impl.CategoryDAOImpl;
import com.xbk.entity.Book;
import com.xbk.entity.Category;
import com.xbk.service.CategoryService;

import java.util.List;

/**
 * 分类服务实现类
 */
public class CategoryServiceImpl implements CategoryService {

    private CategoryDAO categoryDAO = new CategoryDAOImpl();
    private BookDAO bookDAO = new BookDAOImpl();

    @Override
    public boolean addCategory(Category category) {
        // 验证分类名称唯一性
        Category existing = categoryDAO.findByName(category.getName());
        if (existing != null) {
            System.err.println("分类名称已存在: " + category.getName());
            return false;
        }

        int id = categoryDAO.insert(category);
        if (id > 0) {
            System.out.println("分类添加成功，ID: " + id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCategory(Category category) {
        // 验证分类是否存在
        Category existing = categoryDAO.findById(category.getId());
        if (existing == null) {
            System.err.println("分类不存在，ID: " + category.getId());
            return false;
        }

        // 如果修改了名称，需要验证新名称的唯一性
        if (!existing.getName().equals(category.getName())) {
            Category duplicateName = categoryDAO.findByName(category.getName());
            if (duplicateName != null) {
                System.err.println("分类名称已被使用: " + category.getName());
                return false;
            }
        }

        int affected = categoryDAO.update(category);
        if (affected > 0) {
            System.out.println("分类更新成功");
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCategory(int id) {
        // 检查是否有图书使用该分类
        List<Book> books = bookDAO.findByCategory(id);
        if (!books.isEmpty()) {
            System.err.println("该分类下还有 " + books.size() + " 本图书，无法删除");
            return false;
        }

        int affected = categoryDAO.delete(id);
        if (affected > 0) {
            System.out.println("分类删除成功");
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryDAO.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }
}
