package com.xbk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xbk.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 借阅记录 Mapper 接口
 */
@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {

    /**
     * 根据读者 ID 查找所有借阅记录
     */
    List<BorrowRecord> findByReaderId(@Param("readerId") Integer readerId);

    /**
     * 根据图书 ID 查找所有借阅记录
     */
    List<BorrowRecord> findByBookId(@Param("bookId") Integer bookId);

    /**
     * 查找读者当前未归还的借阅记录
     */
    List<BorrowRecord> findActiveByReaderId(@Param("readerId") Integer readerId);

    /**
     * 查找所有逾期未归还的借阅记录
     */
    List<BorrowRecord> findOverdueRecords();
}
