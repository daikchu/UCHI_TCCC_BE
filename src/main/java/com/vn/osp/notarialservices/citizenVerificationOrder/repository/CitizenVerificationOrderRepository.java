package com.vn.osp.notarialservices.citizenVerificationOrder.repository;

import com.vn.osp.notarialservices.citizenVerificationOrder.domain.CitizenVerifyOrderBO;
import com.vn.osp.notarialservices.citizenVerificationOrder.dto.CitizenVerifyOrderDTO;

import java.util.List;
import java.util.Optional;

public interface CitizenVerificationOrderRepository {
    Optional<CitizenVerifyOrderBO> insert(CitizenVerifyOrderBO citizenVerifyOrderBO);
    Optional<CitizenVerifyOrderBO> update(CitizenVerifyOrderBO citizenVerifyOrderBO);
    Optional<Boolean> deleteById(Long order_id);
    Optional<CitizenVerifyOrderBO> get(String order_id);
    Optional<CitizenVerifyOrderDTO> getDetail(String order_id);
    Optional<List<CitizenVerifyOrderDTO>> filter(String order_id, String notary_office_code,
                                                 String province_code, String transaction_status, String status,
                                                 String update_by, String order_time_from, String order_time_to);
    Optional<List<CitizenVerifyOrderDTO>> page(int page, int numberPerPage, String order_id, String notary_office_code,
                                               String province_code, String transaction_status, String status,
                                               String update_by, String order_time_from, String order_time_to);
    Optional<Long> count(String order_id, String notary_office_code,
                         String province_code, String transaction_status, String status,
                         String update_by, String order_time_from, String order_time_to);

}
