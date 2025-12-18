package com.xbk.service;

import com.xbk.entity.BorrowRecord;
import java.util.List;

/**
 * 借阅服务接口
 */
public interface BorrowService {

    /**
     * 借书
     * @param bookId 图书ID
     * @param readerId 读者ID
     * @param borrowDays 借阅天数
     * @return 是否成功
     */
    boolean borrowBook(int bookId, int readerId, int borrowDays);

    /**
     * 还书
     * @param recordId 借阅记录ID
     * @return 是否成功
     */
    boolean returnBook(int recordId);

    /**
     * 获取读者的借阅历史
     */
    List<BorrowRecord> getReaderBorrowHistory(int readerId);

    /**
     * 获取读者当前借阅的图书
     */
    List<BorrowRecord> getActiveBorrows(int readerId);

    /**
     * 获取所有逾期记录
     */
    List<BorrowRecord> getOverdueRecords();

    /**
     * 计算罚金（每天1元）
     */
    double calculateFine(BorrowRecord record);
}
