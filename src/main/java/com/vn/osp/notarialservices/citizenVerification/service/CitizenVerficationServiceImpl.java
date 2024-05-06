package com.vn.osp.notarialservices.citizenVerification.service;

import com.vn.osp.notarialservices.common.util.PagingResult;
import com.vn.osp.notarialservices.systemmanager.service.AccessHistoryService;
import com.vn.osp.notarialservices.utils.OspQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class CitizenVerficationServiceImpl implements CitizenVerficationService {
    @Autowired
    private AccessHistoryService systemConfigService;

    @Override
    public Optional<PagingResult> page(int page, int numberPerPage, String verify_id, String notary_office_id, String verify_status, String verify_by,
                                       String citizen_verify_fullname, String citizen_verify_cccd,
                                       String verify_date_from, String verify_date_to, String order_id) throws UnsupportedEncodingException {
        if(StringUtils.isBlank(notary_office_id)) {
            notary_office_id = systemConfigService.getConfigValue("system_authentication_id").orElse(null);
        }
        PagingResult pagingResult = OspQueryFactory.getPageCitizenVerification(page, numberPerPage, verify_id, notary_office_id,
                verify_status, verify_by,citizen_verify_fullname, citizen_verify_cccd,
                verify_date_from, verify_date_to, order_id);

        return Optional.of(pagingResult);
    }
}
