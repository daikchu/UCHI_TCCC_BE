package com.vn.osp.notarialservices.certificate.dto;


import lombok.Data;

@Data
public class CertDTO {
    private Long id;
    private String cert_number;
    private String cert_date;
    private String cert_request_user;
    private String cert_request_doc;
    private String cert_sign_user;
    private Integer cert_doc_number;
    private String cert_fee;
    private String note;
    private String attestation_template_code;
    private String kind_html;
    private String notary_book;

    private Long entry_user_id;
    private Long update_user_id;
    private String entry_date_time;
    private String update_date_time;

    private Integer type;

    public CertDTO() {
    }

    public CertDTO(Long id, String cert_number, String cert_date, String cert_request_user, String cert_request_doc,
                   String cert_sign_user, Integer cert_doc_number, String cert_fee, String note, String kind_html, String notary_book, String attestation_template_code,
                   Long entry_user_id, Long update_user_id, String entry_date_time, String update_date_time, Integer type) {
        this.id = id;
        this.cert_number = cert_number;
        this.cert_date = cert_date;
        this.cert_request_user = cert_request_user;
        this.cert_request_doc = cert_request_doc;
        this.cert_sign_user = cert_sign_user;
        this.cert_doc_number = cert_doc_number;
        this.cert_fee = cert_fee;
        this.note = note;
        this.attestation_template_code = attestation_template_code;
        this.kind_html = kind_html;
        this.notary_book = notary_book;
        this.entry_user_id = entry_user_id;
        this.update_user_id = update_user_id;
        this.entry_date_time = entry_date_time;
        this.update_date_time = update_date_time;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCert_number() {
        return cert_number;
    }

    public void setCert_number(String cert_number) {
        this.cert_number = cert_number;
    }

    public String getCert_date() {
        return cert_date;
    }

    public void setCert_date(String cert_date) {
        this.cert_date = cert_date;
    }

    public String getCert_request_user() {
        return cert_request_user;
    }

    public void setCert_request_user(String cert_request_user) {
        this.cert_request_user = cert_request_user;
    }

    public String getCert_request_doc() {
        return cert_request_doc;
    }

    public void setCert_request_doc(String cert_request_doc) {
        this.cert_request_doc = cert_request_doc;
    }

    public String getCert_sign_user() {
        return cert_sign_user;
    }

    public void setCert_sign_user(String cert_sign_user) {
        this.cert_sign_user = cert_sign_user;
    }

    public Integer getCert_doc_number() {
        return cert_doc_number;
    }

    public void setCert_doc_number(Integer cert_doc_number) {
        this.cert_doc_number = cert_doc_number;
    }

    public String getCert_fee() {
        return cert_fee;
    }

    public void setCert_fee(String cert_fee) {
        this.cert_fee = cert_fee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAttestation_template_code() {
        return attestation_template_code;
    }

    public void setAttestation_template_code(String attestation_template_code) {
        this.attestation_template_code = attestation_template_code;
    }

    public String getKind_html() {
        return kind_html;
    }

    public void setKind_html(String kind_html) {
        this.kind_html = kind_html;
    }

    public String getNotary_book() {
        return notary_book;
    }

    public void setNotary_book(String notary_book) {
        this.notary_book = notary_book;
    }

    public Long getEntry_user_id() {
        return entry_user_id;
    }

    public void setEntry_user_id(Long entry_user_id) {
        this.entry_user_id = entry_user_id;
    }

    public Long getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(Long update_user_id) {
        this.update_user_id = update_user_id;
    }

    public String getEntry_date_time() {
        return entry_date_time;
    }

    public void setEntry_date_time(String entry_date_time) {
        this.entry_date_time = entry_date_time;
    }

    public String getUpdate_date_time() {
        return update_date_time;
    }

    public void setUpdate_date_time(String update_date_time) {
        this.update_date_time = update_date_time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
