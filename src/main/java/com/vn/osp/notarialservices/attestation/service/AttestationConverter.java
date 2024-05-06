package com.vn.osp.notarialservices.attestation.service;

import com.vn.osp.notarialservices.attestation.domain.AttestationBO;
import com.vn.osp.notarialservices.attestation.dto.AttestationDTO;
import org.springframework.stereotype.Service;

@Service
public class AttestationConverter {
    public AttestationDTO convert(AttestationBO item){
        AttestationDTO result = new AttestationDTO();
        result.setId(item.getId());
        result.setParent_code(item.getParent_code());
        result.setCode(item.getCode());
        result.setName(item.getName());
        result.setKind_html(item.getKind_html());
        result.setActive_flg(item.getActive_flg());
        result.setDescription(item.getDescription());
        result.setType(item.getType());
        result.setType_org(item.getType_org());
        result.setEntry_date_time(item.getEntry_date_time());
        result.setEntry_user_id(item.getEntry_user_id());
        result.setEntry_user_name(item.getEntry_user_name());
        result.setUpdate_date_time(item.getUpdate_date_time());
        result.setUpdate_user_id(item.getUpdate_user_id());
        result.setUpdate_user_name(item.getUpdate_user_name());
        return result;
    }

    public AttestationBO convert(AttestationDTO item){
        AttestationBO result = new AttestationBO();
        result.setId(item.getId());
        result.setParent_code(item.getParent_code());
        result.setCode(item.getCode());
        result.setName(item.getName());
        result.setKind_html(item.getKind_html());
        result.setActive_flg(item.getActive_flg());
        result.setDescription(item.getDescription());
        result.setType(item.getType());
        result.setType_org(item.getType_org());
        result.setEntry_date_time(item.getEntry_date_time());
        result.setEntry_user_id(item.getEntry_user_id());
        result.setEntry_user_name(item.getEntry_user_name());
        result.setUpdate_date_time(item.getUpdate_date_time());
        result.setUpdate_user_id(item.getUpdate_user_id());
        result.setUpdate_user_name(item.getUpdate_user_name());
        return result;
    }
    
}
