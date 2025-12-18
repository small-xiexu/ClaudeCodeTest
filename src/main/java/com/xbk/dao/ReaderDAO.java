package com.xbk.dao;

import com.xbk.entity.Reader;
import java.util.List;

public interface ReaderDAO {
    int insert(Reader reader);
    int update(Reader reader);
    int delete(int id);
    Reader findById(int id);
    Reader findByCardNumber(String cardNumber);
    List<Reader> findAll();
}
