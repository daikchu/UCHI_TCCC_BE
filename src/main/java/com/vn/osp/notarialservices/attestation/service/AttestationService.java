package com.vn.osp.notarialservices.attestation.service;

import com.vn.osp.notarialservices.attestation.dto.AttestationDTO;
import com.vn.osp.notarialservices.attestation.dto.AttestationTemplateFieldMapDTO;

import java.util.List;

public interface AttestationService {
    Long count(String filter);
    List<AttestationDTO> searchByParentCode(String parent_code);
    List<AttestationDTO> search(String filter);
    Boolean add(AttestationDTO item);
    Boolean update(AttestationDTO item);
    Boolean delete(Long id);
    AttestationDTO get(Long id);
    List<AttestationTemplateFieldMapDTO> getAllAttestationTemplateFieldMap(String filter);
}
