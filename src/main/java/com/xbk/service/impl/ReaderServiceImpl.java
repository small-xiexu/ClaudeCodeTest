package com.xbk.service.impl;

import com.xbk.entity.BorrowRecord;
import com.xbk.entity.Reader;
import com.xbk.mapper.BorrowRecordMapper;
import com.xbk.mapper.ReaderMapper;
import com.xbk.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 读者服务实现类
 */
@Service
@Transactional
public class ReaderServiceImpl implements ReaderService {

    @Autowired
    private ReaderMapper readerMapper;

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Override
    public boolean registerReader(Reader reader) {
        // 验证借书证号唯一性
        Reader existing = readerMapper.findByCardNumber(reader.getCardNumber());
        if (existing != null) {
            System.err.println("借书证号已存在: " + reader.getCardNumber());
            return false;
        }

        int result = readerMapper.insert(reader);
        if (result > 0) {
            System.out.println("读者注册成功，ID: " + reader.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateReader(Reader reader) {
        // 验证读者是否存在
        Reader existing = readerMapper.selectById(reader.getId());
        if (existing == null) {
            System.err.println("读者不存在，ID: " + reader.getId());
            return false;
        }

        // 如果修改了借书证号，需要验证新证号的唯一性
        if (!existing.getCardNumber().equals(reader.getCardNumber())) {
            Reader duplicateCard = readerMapper.findByCardNumber(reader.getCardNumber());
            if (duplicateCard != null) {
                System.err.println("借书证号已被其他读者使用: " + reader.getCardNumber());
                return false;
            }
        }

        int affected = readerMapper.updateById(reader);
        if (affected > 0) {
            System.out.println("读者信息更新成功");
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReader(int id) {
        // 检查是否有未归还的图书
        List<BorrowRecord> activeRecords = borrowRecordMapper.findActiveByReaderId(id);
        if (!activeRecords.isEmpty()) {
            System.err.println("该读者还有 " + activeRecords.size() + " 本图书未归还，无法删除");
            return false;
        }

        int affected = readerMapper.deleteById(id);
        if (affected > 0) {
            System.out.println("读者删除成功");
            return true;
        }
        return false;
    }

    @Override
    public Reader getReaderById(int id) {
        return readerMapper.selectById(id);
    }

    @Override
    public Reader getReaderByCardNumber(String cardNumber) {
        return readerMapper.findByCardNumber(cardNumber);
    }

    @Override
    public List<Reader> getAllReaders() {
        return readerMapper.selectList(null);
    }
}
