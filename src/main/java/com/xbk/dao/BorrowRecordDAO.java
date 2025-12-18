package com.xbk.dao;

import com.xbk.entity.BorrowRecord;
import java.util.List;

public interface BorrowRecordDAO {
    int insert(BorrowRecord record);
    int update(BorrowRecord record);
    BorrowRecord findById(int id);
    List<BorrowRecord> findByReaderId(int readerId);
    List<BorrowRecord> findByBookId(int bookId);
    List<BorrowRecord> findActiveByReaderId(int readerId);
    List<BorrowRecord> findOverdueRecords();
}
