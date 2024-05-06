package com.vn.osp.notarialservices.certificate.service;

import com.vn.osp.notarialservices.certificate.domain.CertNumberBO;
import com.vn.osp.notarialservices.certificate.dto.CertNumberDTO;
import org.springframework.stereotype.Service;

@Service
public class CertNumberConverter {
    public CertNumberBO convert(CertNumberDTO source){
        CertNumberBO result = new CertNumberBO();
        result.setId(source.getId());
        result.setKind_id(source.getKind_id());
        result.setCert_number(source.getCert_number());
        result.setCert_type(source.getCert_type());
        result.setUser_id(source.getUser_id());
        result.setVillage_code(source.getVillage_code());
        result.setDistrict_code(source.getDistrict_code());
        return result;
    }

    public CertNumberDTO convert(CertNumberBO source){
        CertNumberDTO result = new CertNumberDTO();
        result.setId(source.getId());
        result.setKind_id(source.getKind_id());
        result.setCert_number(source.getCert_number());
        result.setCert_type(source.getCert_type());
        result.setUser_id(source.getUser_id());
        result.setVillage_code(source.getVillage_code());
        result.setDistrict_code(source.getDistrict_code());
        return result;
    }
}
