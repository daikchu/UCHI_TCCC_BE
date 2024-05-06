package com.vn.osp.notarialservices.contract.dto;

import lombok.Data;

@Data
public class PropertyRealEstateType {
    private int id;
    private String name;
    private String description;
    private Integer code;
    private Integer parent_code;

}
