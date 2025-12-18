package com.xbk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xbk.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 图书 Mapper 接口
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

    /**
     * 根据 ISBN 查找图书
     */
    Book findByIsbn(@Param("isbn") String isbn);

    /**
     * 根据分类 ID 查找图书
     */
    List<Book> findByCategory(@Param("categoryId") Integer categoryId);

    /**
     * 根据关键词搜索图书（书名或作者）
     */
    List<Book> findByKeyword(@Param("keyword") String keyword);

    /**
     * 更新图书可用数量
     */
    int updateAvailableQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);
}
