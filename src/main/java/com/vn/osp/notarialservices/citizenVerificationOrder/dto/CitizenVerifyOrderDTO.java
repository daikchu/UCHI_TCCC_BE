package com.vn.osp.notarialservices.citizenVerificationOrder.dto;

import com.vn.osp.notarialservices.citizenVerificationOrder.domain.CitizenVerifyOrderBO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

//@Data
//@Entity
//@Table(name = "npo_citizen_verification_orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CitizenVerifyOrderDTO {
    private String order_id;

    private Timestamp order_time;

    private int verify_number;

    private String verify_fee;

    private String verify_fee_received;

    private String notary_office_code;

    private String notary_office_name;

    private String province_code;

    private String province_name;

    private String transaction_status;

    private String transaction_status_name;

    private String status;

    private String status_name;

    private String notary_office_excutor;

    private String notary_office_excutor_fullname;

    private String note;

    private String attach_files;

    private String update_by;

    private String payment_content;

    private String update_by_name;

    private String order_time_formatted;

    private List transaction_hists;

    public CitizenVerifyOrderBO toEntity(){
        CitizenVerifyOrderBO entity = new CitizenVerifyOrderBO();

        entity.setOrder_id(this.order_id);
        entity.setOrder_time(this.order_time);
        entity.setVerify_number(this.verify_number);
        entity.setVerify_fee(this.verify_fee);
        entity.setVerify_fee_received(this.verify_fee_received);
        entity.setNotary_office_code(this.notary_office_code);
        entity.setProvince_code(this.province_code);
        entity.setTransaction_status(this.transaction_status);
        entity.setStatus(this.status);
        entity.setNotary_office_excutor(this.notary_office_excutor);
        entity.setNotary_office_excutor_fullname(this.notary_office_excutor_fullname);
        entity.setNote(this.note);
        entity.setAttach_files(this.attach_files);
        entity.setUpdate_by(this.update_by);
        entity.setPayment_content(this.payment_content);
        entity.setUpdate_by_name(this.update_by_name);
        return entity;
    }
}
