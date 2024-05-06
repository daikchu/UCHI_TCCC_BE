package com.vn.osp.notarialservices.contract.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "npo_property_real_estate_type")
public class PropertyRealEstateTypeBO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "code", nullable = false)
    private Integer code;
    @Column(name = "parent_code", nullable = false)
    private Integer parent_code;
}
