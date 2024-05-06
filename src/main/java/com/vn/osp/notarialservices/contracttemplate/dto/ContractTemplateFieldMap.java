package com.vn.osp.notarialservices.contracttemplate.dto;

public class ContractTemplateFieldMap {
    private String key;
    private String name;

    public ContractTemplateFieldMap() {
    }

    public ContractTemplateFieldMap(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
