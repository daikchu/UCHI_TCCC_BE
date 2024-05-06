package com.vn.osp.notarialservices.authenticatedCopies.dto;

import com.vn.osp.notarialservices.common.dto.BaseEntityBeans;
import lombok.Data;

import java.sql.Timestamp;
/**
 * Created by Tuannq on 15/08/2020.
 */
@Data
public class AuthenticateDTO{
    private Long id;
    private String cert_number;
    private String cert_date;
    private String cert_request_user;
    private String cert_request_doc;
    private String cert_sign_user;
    private Integer cert_doc_number;
    private String cert_fee;
    private String note;

    private Long entry_user_id;
    private String entry_date_time;
    private Long update_user_id;
    private String update_date_time;

    public AuthenticateDTO(){

    }

    public AuthenticateDTO(Long id, String cert_number, String cert_date, String cert_request_user, String cert_request_doc,String cert_sign_user, Integer cert_doc_number,String cert_fee, String note, Long entry_user_id, String entry_date_time, Long update_user_id, String update_date_time){
        this.id = id;
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

    public Long getEntry_user_id() {
        return entry_user_id;
    }

    public void setEntry_user_id(Long entry_user_id) {
        this.entry_user_id = entry_user_id;
    }

    public String getEntry_date_time() {
        return entry_date_time;
    }

    public void setEntry_date_time(String entry_date_time) {
        this.entry_date_time = entry_date_time;
    }

    public Long getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(Long update_user_id) {
        this.update_user_id = update_user_id;
    }

    public String getUpdate_date_time() {
        return update_date_time;
    }

    public void setUpdate_date_time(String update_date_time) {
        this.update_date_time = update_date_time;
    }
}
