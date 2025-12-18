package com.xbk.service.impl;

import com.xbk.dao.BorrowRecordDAO;
import com.xbk.dao.ReaderDAO;
import com.xbk.dao.impl.BorrowRecordDAOImpl;
import com.xbk.dao.impl.ReaderDAOImpl;
import com.xbk.entity.BorrowRecord;
import com.xbk.entity.Reader;
import com.xbk.service.ReaderService;

import java.util.List;

/**
 * 读者服务实现类
 */
public class ReaderServiceImpl implements ReaderService {

    private ReaderDAO readerDAO = new ReaderDAOImpl();
    private BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAOImpl();

    @Override
    public boolean registerReader(Reader reader) {
        // 验证借书证号唯一性
        Reader existing = readerDAO.findByCardNumber(reader.getCardNumber());
        if (existing != null) {
            System.err.println("借书证号已存在: " + reader.getCardNumber());
            return false;
        }

        int id = readerDAO.insert(reader);
        if (id > 0) {
            System.out.println("读者注册成功，ID: " + id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateReader(Reader reader) {
        // 验证读者是否存在
        Reader existing = readerDAO.findById(reader.getId());
        if (existing == null) {
            System.err.println("读者不存在，ID: " + reader.getId());
            return false;
        }

        // 如果修改了借书证号，需要验证新证号的唯一性
        if (!existing.getCardNumber().equals(reader.getCardNumber())) {
            Reader duplicateCard = readerDAO.findByCardNumber(reader.getCardNumber());
            if (duplicateCard != null) {
                System.err.println("借书证号已被其他读者使用: " + reader.getCardNumber());
                return false;
            }
        }

        int affected = readerDAO.update(reader);
        if (affected > 0) {
            System.out.println("读者信息更新成功");
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReader(int id) {
        // 检查是否有未归还的图书
        List<BorrowRecord> activeRecords = borrowRecordDAO.findActiveByReaderId(id);
        if (!activeRecords.isEmpty()) {
            System.err.println("该读者还有 " + activeRecords.size() + " 本图书未归还，无法删除");
            return false;
        }

        int affected = readerDAO.delete(id);
        if (affected > 0) {
            System.out.println("读者删除成功");
            return true;
        }
        return false;
    }

    @Override
    public Reader getReaderById(int id) {
        return readerDAO.findById(id);
    }

    @Override
    public Reader getReaderByCardNumber(String cardNumber) {
        return readerDAO.findByCardNumber(cardNumber);
    }

    @Override
    public List<Reader> getAllReaders() {
        return readerDAO.findAll();
    }
}
