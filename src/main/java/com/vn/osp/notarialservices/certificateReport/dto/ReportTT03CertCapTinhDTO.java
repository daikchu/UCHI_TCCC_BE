package com.vn.osp.notarialservices.certificateReport.dto;

import lombok.Data;

@Data
public class ReportTT03CertCapTinhDTO {
    private String district_code;
    private String district_name;
    private Integer cert_copies_number;
    private Integer cert_signature_number;
    private Integer cert_contract_number;

    public ReportTT03CertCapTinhDTO(String district_code, String district_name, Integer cert_copies_number, Integer cert_signature_number, Integer cert_contract_number) {
        this.district_code = district_code;
        this.district_name = district_name;
        this.cert_copies_number = cert_copies_number;
        this.cert_signature_number = cert_signature_number;
        this.cert_contract_number = cert_contract_number;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
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
