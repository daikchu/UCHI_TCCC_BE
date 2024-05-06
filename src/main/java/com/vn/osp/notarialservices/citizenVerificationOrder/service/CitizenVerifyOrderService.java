package com.vn.osp.notarialservices.citizenVerificationOrder.service;

import com.vn.osp.notarialservices.citizenVerificationOrder.dto.CitizenVerifyOrderDTO;
import com.vn.osp.notarialservices.common.util.PagingResult;

import java.util.List;
import java.util.Optional;

public interface CitizenVerifyOrderService {
    Optional<CitizenVerifyOrderDTO> insert(CitizenVerifyOrderDTO citizenVerifyOrderDTO);
    Optional<CitizenVerifyOrderDTO> update(CitizenVerifyOrderDTO citizenVerifyOrderDTO);
    Optional<Boolean> deleteById(Long order_id);
    Optional<CitizenVerifyOrderDTO> getDetail(String order_id);
    Optional<List<CitizenVerifyOrderDTO>> filter(String order_id, String notary_office_code,
                                                 String province_code, String transaction_status, String status,
                                                 String update_by, String order_time_from, String order_time_to);
    Optional<PagingResult> page(int page, int numberPerPage, String order_id, String transaction_status, String status, String update_by, String order_time_from, String order_time_to);
    Optional<Long> count(String order_id, String notary_office_code,
                         String province_code, String transaction_status, String status,
                         String update_by, String order_time_from, String order_time_to);
}

