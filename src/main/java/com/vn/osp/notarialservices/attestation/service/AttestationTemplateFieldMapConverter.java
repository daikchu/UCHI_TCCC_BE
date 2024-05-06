package com.vn.osp.notarialservices.attestation.service;

import com.vn.osp.notarialservices.attestation.domain.AttestationTemplateFieldMapBO;
import com.vn.osp.notarialservices.attestation.dto.AttestationTemplateFieldMapDTO;
import org.springframework.stereotype.Service;

@Service
public class AttestationTemplateFieldMapConverter {
    public AttestationTemplateFieldMapBO convert(AttestationTemplateFieldMapDTO item){
        AttestationTemplateFieldMapBO bo = new AttestationTemplateFieldMapBO();
        bo.setId(item.getId());
        bo.setKey(item.getKey());

        bo.setName(item.getName());
        return bo;
    }

    public AttestationTemplateFieldMapDTO convert(AttestationTemplateFieldMapBO item){
        AttestationTemplateFieldMapDTO result = new AttestationTemplateFieldMapDTO();
        result.setId(item.getId());
        result.setKey(item.getKey());
        result.setName(item.getName());
        return result;
    }
}
