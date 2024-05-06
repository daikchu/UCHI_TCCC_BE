package com.vn.osp.notarialservices.contracttemplate.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * Created by DaiCQ on 05/09/2019.
 */
@Data
@Entity
@Table(name = "npo_contract_template_field_map")
public class ContractTemplateFieldMapBO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "key")
    private String key;
    @Column(name = "name")
    private String name;

    public ContractTemplateFieldMapBO() {
    }

    public ContractTemplateFieldMapBO(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
