package com.vn.osp.notarialservices.notaryBook.service;

import com.vn.osp.notarialservices.notaryBook.dto.NotaryBookDTO;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface NotaryBookService {
    String getStringFilter(String notaryBook, Integer type);
    Boolean delete(Long id);
    Boolean add(NotaryBookDTO item);
    Boolean update(NotaryBookDTO item);
    List<NotaryBookDTO> getAll();
    NotaryBookDTO get(Long id);
    List<NotaryBookDTO> findByFilter(String stringFilter);
    List<NotaryBookDTO> findByFilterSattusOpen(String stringFilter);
    Integer countByFilter(String stringFilter);
    List<NotaryBookDTO> listItemPage(String stringFilter, int offset, int limit);
    Integer countByNotaryNumber(String notary_book, Long id, Long type);
    Integer checkDeleteNotaryNumber(String notary_book, Long type);
    Integer checkDeleteNotaryNumberContract(String notary_book, Long type);
    Integer checkValidateStatusNotary(Long status, Long type);
}
