package com.vn.osp.notarialservices.authenticatedCopies.service;

import com.vn.osp.notarialservices.authenticatedCopies.dto.AuthenticateDTO;

import java.util.List;
public interface AuthenticateService {
    String getStringFilter(String userId, String cert_number, String dateFrom, String dateTo);
    String getStringCertNumber(String certNumber);
    Boolean delete(Long id);
    Boolean add(AuthenticateDTO item);
    Boolean update(AuthenticateDTO item);
    List<AuthenticateDTO> getAll();
    AuthenticateDTO get(Long id);
    List<AuthenticateDTO> findByFilter(String stringFilter);
    Integer countByFilter(String stringFilter);
    List<AuthenticateDTO> listItemPage(String stringFilter, int offset, int limit);

}
