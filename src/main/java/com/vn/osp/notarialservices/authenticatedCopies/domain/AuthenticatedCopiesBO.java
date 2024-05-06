package com.vn.osp.notarialservices.authenticatedCopies.domain;

import com.vn.osp.notarialservices.common.domain.AbstractAuditEntity;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "npo_authenticater_copies")
public class AuthenticatedCopiesBO extends AbstractAuditEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "cert_number")
    private String cert_number;
    @Column(name = "cert_date")
    private java.sql.Timestamp cert_date;
    @Column(name = "cert_request_user")
    private String cert_request_user;
    @Column(name = "cert_request_doc")
    private String cert_request_doc;
    @Column(name = "cert_sign_user")
    private String cert_sign_user;
    @Column(name = "cert_doc_number")
    private Integer cert_doc_number;
    @Column(name = "cert_fee")
    private String cert_fee;
    @Column(name = "note")
    private String note;

    @Column(name = "entry_user_id")
    private Long entry_user_id;
    @Column(name = "entry_date_time")
    private java.sql.Timestamp entry_date_time;
    @Column(name = "update_user_id")
    private Long update_user_id;
    @Column(name = "update_date_time")
    private java.sql.Timestamp update_date_time;

    public AuthenticatedCopiesBO(){

    }

    public AuthenticatedCopiesBO(String cert_number, Timestamp cert_date, String cert_request_user, String cert_request_doc,String cert_sign_user, Integer cert_doc_number,String cert_fee, String note, Long entry_user_id, Timestamp entry_date_time, Long update_user_id, Timestamp update_date_time){
        this.cert_number = cert_number;
        this.cert_date = cert_date;
        this.cert_request_user = cert_request_user;
        this.cert_request_doc = cert_request_doc;
        this.cert_sign_user = cert_sign_user;
        this.cert_doc_number = cert_doc_number;
        this.cert_fee = cert_fee;
        this.note = note;

        this.entry_user_id = entry_user_id;
        this.entry_date_time = entry_date_time;
        this.update_user_id = update_user_id;
        this.entry_date_time = update_date_time;
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

    public Timestamp getCert_date() {
        return cert_date;
    }

    public void setCert_date(Timestamp cert_date) {
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

    public Long getEntry_user_id() {
        return entry_user_id;
    }

    public void setEntry_user_id(Long entry_user_id) {
        this.entry_user_id = entry_user_id;
    }

    public Timestamp getEntry_date_time() {
        return entry_date_time;
    }

    public void setEntry_date_time(Timestamp entry_date_time) {
        this.entry_date_time = entry_date_time;
    }

    public Long getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(Long update_user_id) {
        this.update_user_id = update_user_id;
    }

    public Timestamp getUpdate_date_time() {
        return update_date_time;
    }

    public void setUpdate_date_time(Timestamp update_date_time) {
        this.update_date_time = update_date_time;
    }
}
