package com.vn.osp.notarialservices.citizenVerification.service;

import com.vn.osp.notarialservices.common.util.PagingResult;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

public interface CitizenVerficationService {
    Optional<PagingResult> page(int page,
                                int numberPerPage,
                                String verify_id,
                                String notary_office_id,
                                String verify_status,
                                String verify_by,
                                String citizen_verify_fullname,
                                String citizen_verify_cccd,
                                String verify_date_from,
                                String verify_date_to,
                                String order_id) throws UnsupportedEncodingException;
}
