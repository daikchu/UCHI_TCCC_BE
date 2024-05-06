package com.vn.osp.notarialservices.certificate.domain;

import com.vn.osp.notarialservices.common.domain.AbstractAuditEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "npo_certificate_number")
public class CertNumberBO extends AbstractAuditEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id",nullable = false)
    private Long id;
    @Column(name = "kind_id")
    private String kind_id;
    @Column(name = "cert_number")
    private Long cert_number;
    @Column(name = "cert_type")
    private Integer cert_type;
    @Column(name = "user_id")
    private Long user_id;
    @Column(name = "village_code")
    private String village_code;
    @Column(name = "district_code")
    private String district_code;

    public CertNumberBO (Long id, String kind_id, Long cert_number, Integer cert_type, Long user_id, String village_code, String district_code){
        this.id = id;
        this.kind_id = kind_id;
        this.cert_number = cert_number;
        this.cert_type = cert_type;
        this.user_id = user_id;
        this.village_code = village_code;
        this.district_code = district_code;
    }

    public CertNumberBO(){

    }

    public String getKind_d() {
        return kind_id;
    }

    public void setKind_d(String kind_id) {
        this.kind_id = kind_id;
    }

    public Long getCert_number() {
        return cert_number;
    }

    public void setCert_number(Long cert_number) {
        this.cert_number = cert_number;
    }

    public Integer getCert_type() {
        return cert_type;
    }

    public void setCert_type(Integer cert_type) {
        this.cert_type = cert_type;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getVillage_code() {
        return village_code;
    }

    public void setVillage_code(String village_code) {
        this.village_code = village_code;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }
}
