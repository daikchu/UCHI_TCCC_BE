package com.vn.osp.notarialservices.contract.service;

import com.vn.osp.notarialservices.contract.domain.PropertyRealEstateTypeBO;
import com.vn.osp.notarialservices.contract.dto.PropertyRealEstateType;
import org.springframework.stereotype.Service;

@Service
public class PropertyRealEstateConveter {
    public PropertyRealEstateTypeBO convert(PropertyRealEstateType item){
        PropertyRealEstateTypeBO bo = new PropertyRealEstateTypeBO();
        bo.setId(item.getId());
        bo.setName(item.getName());
        bo.setCode(item.getCode());
        bo.setDescription(item.getDescription());
        bo.setParent_code(item.getParent_code());
        return bo;
    }

    public PropertyRealEstateType convert(PropertyRealEstateTypeBO item){
        PropertyRealEstateType result = new PropertyRealEstateType();
        result.setId(item.getId());
        result.setName(item.getName());
        result.setCode(item.getCode());
        result.setDescription(item.getDescription());
        result.setParent_code(item.getParent_code());
        return result;
    }
}
