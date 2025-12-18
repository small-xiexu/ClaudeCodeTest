package com.xbk.dao;

import com.xbk.dao.impl.CategoryDAOImpl;
import com.xbk.entity.Category;
import com.xbk.util.DBUtil;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CategoryDAO 测试类
 */
class CategoryDAOTest {

    private static CategoryDAO categoryDAO;

    @BeforeAll
    static void setUpAll() {
        DBUtil.resetDatabase();
        DBUtil.initDatabase();
        categoryDAO = new CategoryDAOImpl();
    }

    @AfterAll
    static void tearDownAll() {
        DBUtil.resetDatabase();
    }

    @Test
    void testInsert() {
        Category category = new Category("测试分类", "这是一个测试分类");
        int id = categoryDAO.insert(category);

        assertTrue(id > 0, "插入应该返回生成的ID");

        Category found = categoryDAO.findById(id);
        assertNotNull(found);
        assertEquals("测试分类", found.getName());
        assertEquals("这是一个测试分类", found.getDescription());
    }

    @Test
    void testUpdate() {
        Category category = new Category("原始名称", "原始描述");
        int id = categoryDAO.insert(category);

        category.setId(id);
        category.setName("更新名称");
        category.setDescription("更新描述");

        int affected = categoryDAO.update(category);
        assertEquals(1, affected);

        Category found = categoryDAO.findById(id);
        assertEquals("更新名称", found.getName());
        assertEquals("更新描述", found.getDescription());
    }

    @Test
    void testDelete() {
        Category category = new Category("待删除分类", "测试删除");
        int id = categoryDAO.insert(category);

        int affected = categoryDAO.delete(id);
        assertEquals(1, affected);

        Category found = categoryDAO.findById(id);
        assertNull(found);
    }

    @Test
    void testFindById() {
        Category found = categoryDAO.findById(1);
        assertNotNull(found);
        assertEquals("文学", found.getName());
    }

    @Test
    void testFindAll() {
        List<Category> categories = categoryDAO.findAll();
        assertNotNull(categories);
        assertTrue(categories.size() >= 5, "至少应有5个初始分类");
    }

    @Test
    void testFindByName() {
        Category category = categoryDAO.findByName("科技");
        assertNotNull(category);
        assertEquals("科技", category.getName());
    }

    @Test
    void testFindByNameNotExists() {
        Category category = categoryDAO.findByName("不存在的分类");
        assertNull(category);
    }
}
