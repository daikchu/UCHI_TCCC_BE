package com.vn.osp.notarialservices.certificateReport.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class ReportTT03CertCapHuyenBO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Integer user_id;
    @Column(name = "user_first_name")
    private String user_first_name;
    @Column(name = "user_family_name")
    private String user_family_name;
    @Column(name = "user_district_code")
    private String user_district_code;
    /*@Column(name = "user_district_name")
    private String user_district_name;*/
    @Column(name = "user_level_cert")
    private String user_level_cert;
    @Column(name = "cert_copies_number")
    private Integer cert_copies_number;
    @Column(name = "cert_signature_number")
    private Integer cert_signature_number;
    @Column(name = "cert_contract_number")
    private Integer cert_contract_number;

    public ReportTT03CertCapHuyenBO() {
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_family_name() {
        return user_family_name;
    }

    public void setUser_family_name(String user_family_name) {
        this.user_family_name = user_family_name;
    }

    public String getUser_district_code() {
        return user_district_code;
    }

    public void setUser_district_code(String user_district_code) {
        this.user_district_code = user_district_code;
    }

    public String getUser_level_cert() {
        return user_level_cert;
    }

    public void setUser_level_cert(String user_level_cert) {
        this.user_level_cert = user_level_cert;
    }

    public Integer getCert_copies_number() {
        return cert_copies_number;
    }

    public void setCert_copies_number(Integer cert_copies_number) {
        this.cert_copies_number = cert_copies_number;
    }

    public Integer getCert_signature_number() {
        return cert_signature_number;
    }

    public void setCert_signature_number(Integer cert_signature_number) {
        this.cert_signature_number = cert_signature_number;
    }

    public Integer getCert_contract_number() {
        return cert_contract_number;
    }

    public void setCert_contract_number(Integer cert_contract_number) {
        this.cert_contract_number = cert_contract_number;
    }
}
