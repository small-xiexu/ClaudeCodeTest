package com.xbk.service;

import com.xbk.entity.Reader;
import java.util.List;

/**
 * 读者服务接口
 */
public interface ReaderService {
    boolean registerReader(Reader reader);
    boolean updateReader(Reader reader);
    boolean deleteReader(int id);
    Reader getReaderById(int id);
    Reader getReaderByCardNumber(String cardNumber);
    List<Reader> getAllReaders();
}
