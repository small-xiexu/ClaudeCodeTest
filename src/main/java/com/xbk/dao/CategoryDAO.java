package com.xbk.dao;

import com.xbk.entity.Category;
import java.util.List;

/**
 * 分类 DAO 接口
 */
public interface CategoryDAO {

    /**
     * 插入分类
     */
    int insert(Category category);

    /**
     * 更新分类
     */
    int update(Category category);

    /**
     * 删除分类
     */
    int delete(int id);

    /**
     * 根据ID查询分类
     */
    Category findById(int id);

    /**
     * 查询所有分类
     */
    List<Category> findAll();

    /**
     * 根据名称查询分类
     */
    Category findByName(String name);
}
