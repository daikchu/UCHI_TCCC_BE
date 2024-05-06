package com.vn.osp.notarialservices.contracttemplate.service;

import com.vn.osp.notarialservices.contracttemplate.domain.ContractTemplateFieldMapBO;
import com.vn.osp.notarialservices.contracttemplate.dto.ContractTemplateFieldMap;
import org.springframework.stereotype.Service;

@Service
public class ContractTemplateFieldMapConveter {
    public ContractTemplateFieldMapBO convert(ContractTemplateFieldMap source) {
        return new ContractTemplateFieldMapBO(
                source.getKey(),
                source.getName()
        );
    }

    public ContractTemplateFieldMap convert(ContractTemplateFieldMapBO source) {
        return new ContractTemplateFieldMap(
                source.getKey(),
                source.getName()
        );
    }
}
