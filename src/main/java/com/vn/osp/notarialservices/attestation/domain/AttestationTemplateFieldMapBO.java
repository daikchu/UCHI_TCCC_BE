package com.vn.osp.notarialservices.attestation.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "npo_attestation_template_field_map")
public class AttestationTemplateFieldMapBO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id",nullable = false)
    private Long id;
    @Column(name = "key",nullable = false)
    private String key;
    @Column(name = "name",nullable = false)
    private String name;
}
