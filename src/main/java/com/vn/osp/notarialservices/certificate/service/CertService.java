package com.vn.osp.notarialservices.certificate.service;

import com.vn.osp.notarialservices.certificate.dto.CertDTO;
import com.vn.osp.notarialservices.certificate.dto.CertNumberDTO;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface CertService {
    String getStringFilter(String userId, String cert_number, String notary_book, String dateFrom, String dateTo, Integer type, String attestation_template_code);
    Boolean add(CertDTO item);
    Boolean update(CertDTO item);
    Boolean delete(Long id);
    List<CertDTO> getAll();
    CertDTO get(long id);
    List<CertDTO> findByFilter(String stringFilter);
    Integer countByFilter(String stringFilter);
    List<CertDTO> listItemPage(String filter, int offset,int limit);

    List<CertNumberDTO> getCertNumber(Long userId, Long cert_type);
    Boolean addCertNumber(CertNumberDTO item);
    Boolean updateCertNumber(CertNumberDTO item);
    List<CertNumberDTO> checkExistCertnumber(String kind_id, Long user_id, Integer cert_type);


}
