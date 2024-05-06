package com.vn.osp.notarialservices.citizenVerificationOrder.service;

import com.vn.osp.notarialservices.citizenVerificationOrder.domain.CitizenVerifyOrderBO;
import com.vn.osp.notarialservices.citizenVerificationOrder.dto.CitizenVerifyOrderDTO;
import com.vn.osp.notarialservices.citizenVerificationOrder.repository.CitizenVerificationOrderRepository;
import com.vn.osp.notarialservices.common.util.PagingResult;
import com.vn.osp.notarialservices.systemmanager.service.AccessHistoryService;
import com.vn.osp.notarialservices.utils.OspQueryFactory;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CitizenVerifyOrderServiceImpl implements CitizenVerifyOrderService {

    @Autowired
    private CitizenVerificationOrderRepository citizenVerificationOrderRepository;
    @Autowired
    private AccessHistoryService systemConfigService;


    @Override
    public Optional<CitizenVerifyOrderDTO> insert(CitizenVerifyOrderDTO citizenVerifyOrderDTO) {

        return Optional.empty();
    }

    @Override
    public Optional<CitizenVerifyOrderDTO> update(CitizenVerifyOrderDTO citizenVerifyOrderDTO) {
        CitizenVerifyOrderBO citizenVerifyOrderBO = citizenVerifyOrderDTO.toEntity();
        citizenVerifyOrderBO = citizenVerificationOrderRepository.update(citizenVerifyOrderBO).orElse(citizenVerifyOrderBO);
        citizenVerifyOrderDTO = citizenVerifyOrderBO.toDTO(citizenVerifyOrderDTO);
        return Optional.of(citizenVerifyOrderDTO);
    }

    @Override
    public Optional<Boolean> deleteById(Long order_id) {
        return Optional.empty();
    }

    @Override
    public Optional<CitizenVerifyOrderDTO> getDetail(String order_id) {
        Optional<CitizenVerifyOrderDTO> optional = citizenVerificationOrderRepository.getDetail(order_id);
        if(!optional.isPresent()){
            throw new ObjectNotFoundException("order_id", "CitizenVerifyOrderDTO");
        }
        return optional;
    }

    @Override
    public Optional<List<CitizenVerifyOrderDTO>> filter(String order_id, String notary_office_code, String province_code, String transaction_status, String status, String update_by, String order_time_from, String order_time_to) {
        List<CitizenVerifyOrderDTO> citizenVerifyOrderDTOS = citizenVerificationOrderRepository.filter(order_id, notary_office_code, province_code, transaction_status, status, update_by,order_time_from, order_time_to).orElse(new ArrayList());
        return Optional.of(citizenVerifyOrderDTOS);
    }

    @Override
    public Optional<PagingResult> page(int page, int numberPerPage, String order_id, String transaction_status, String status, String update_by, String order_time_from, String order_time_to) {
        String notary_office_code = systemConfigService.getConfigValue("system_authentication_id").orElse(null);
        PagingResult pagingResult = OspQueryFactory.getPageCitizenVerifyOrder(notary_office_code, page, order_id, transaction_status, status, update_by,order_time_from, order_time_to);
        return Optional.of(pagingResult);
    }

    @Override
    public Optional<Long> count(String order_id, String notary_office_code, String province_code, String transaction_status, String status, String update_by, String order_time_from, String order_time_to) {
        Long count = citizenVerificationOrderRepository.count(order_id, notary_office_code, province_code, transaction_status, status, update_by,order_time_from, order_time_to).orElse(0L);
        return Optional.of(count);
    }
}
