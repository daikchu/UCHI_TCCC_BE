package com.vn.osp.notarialservices.certificateReport.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table
public class ReportTT03CertBO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "cert_copies_number")
    private Integer cert_copies_number;
    @Column(name = "cert_signature_number")
    private Integer cert_signature_number;
    @Column(name = "cert_contract_number")
    private Integer cert_contract_number;

    public ReportTT03CertBO() {
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
