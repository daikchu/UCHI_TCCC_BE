package com.vn.osp.notarialservices.certificateReport.dto;

import lombok.Data;

@Data
public class ReportTT03CertCapHuyenDTO {
    private Integer user_id;
    private String user_first_name;
    private String user_family_name;
    private String user_district_code;
    /*private String user_district_name;*/
    private String user_level_cert;
    private Integer cert_copies_number;
    private Integer cert_signature_number;
    private Integer cert_contract_number;

    public ReportTT03CertCapHuyenDTO() {
    }

    public ReportTT03CertCapHuyenDTO(Integer user_id, String user_first_name, String user_family_name, String user_district_code, /*String user_district_name,*/ String user_level_cert, Integer cert_copies_number, Integer cert_signature_number, Integer cert_contract_number) {
        this.user_id = user_id;
        this.user_first_name = user_first_name;
        this.user_family_name = user_family_name;
        this.user_district_code = user_district_code;
        /*this.user_district_name = user_district_name;*/
        this.user_level_cert = user_level_cert;
        this.cert_copies_number = cert_copies_number;
        this.cert_signature_number = cert_signature_number;
        this.cert_contract_number = cert_contract_number;
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
