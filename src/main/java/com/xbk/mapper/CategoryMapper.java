package com.xbk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xbk.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 分类 Mapper 接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 根据分类名称查找分类
     */
    Category findByName(@Param("name") String name);
}
