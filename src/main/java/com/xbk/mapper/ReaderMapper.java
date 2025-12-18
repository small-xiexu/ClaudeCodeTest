package com.xbk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xbk.entity.Reader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 读者 Mapper 接口
 */
@Mapper
public interface ReaderMapper extends BaseMapper<Reader> {

    /**
     * 根据借书证号查找读者
     */
    Reader findByCardNumber(@Param("cardNumber") String cardNumber);
}
