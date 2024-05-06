package com.vn.osp.notarialservices.masterConvert.domain;

import com.vn.osp.notarialservices.common.domain.AbstractAuditEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "master_contract")
public class MasterContractBO extends AbstractAuditEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "contract_number")
    private String contract_number;

    @Column(name = "contract_number_new")
    private String contract_number_new;

    @Column(name = "notary_date")
    private java.sql.Timestamp notary_date;

    @Column(name = "relation_object")
    private String relation_object;

    @Column(name = "contracttype_name")
    private String contracttype_name;

    @Column(name = "property_info")
    private String property_info;

    @Column(name = "notary_person")
    private String notary_person;

    @Column(name = "synchronize_id")
    private String synchronize_id;

    @Column(name = "notary_office_name")
    private String notary_office_name;

    @Column(name = "contract_kind")
    private String contract_kind;

    @Column(name = "contract_name")
    private String contract_name;

    @Column(name = "entry_user_id")
    private Long entry_user_id;

    @Column(name = "entry_user_name")
    private String entry_user_name;

    @Column(name = "entry_date_time")
    private java.sql.Timestamp entry_date_time;

    @Column(name = "update_user_id")
    private Long update_user_id;

    @Column(name = "update_user_name")
    private String update_user_name;

    @Column(name = "update_date_time")
    private java.sql.Timestamp update_date_time;

    @Column(name = "content")
    private String content;

    @Column(name = "transaction_content")
    private String transaction_content;

    @Column(name = "type_delete")
    private int type_delete;

    @Column(name = "type_duplicate_content")
    private int type_duplicate_content;

    @Column(name = "type_master_to_transaction")
    private int type_master_to_transaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContract_number() {
        return contract_number;
    }

    public void setContract_number(String contract_number) {
        this.contract_number = contract_number;
    }

    public String getContract_number_new() {
        return contract_number_new;
    }

    public void setContract_number_new(String contract_number_new) {
        this.contract_number_new = contract_number_new;
    }

    public Timestamp getNotary_date() {
        return notary_date;
    }

    public void setNotary_date(Timestamp notary_date) {
        this.notary_date = notary_date;
    }

    public String getRelation_object() {
        return relation_object;
    }

    public void setRelation_object(String relation_object) {
        this.relation_object = relation_object;
    }

    public String getContracttype_name() {
        return contracttype_name;
    }

    public void setContracttype_name(String contracttype_name) {
        this.contracttype_name = contracttype_name;
    }

    public String getProperty_info() {
        return property_info;
    }

    public void setProperty_info(String property_info) {
        this.property_info = property_info;
    }

    public String getNotary_person() {
        return notary_person;
    }

    public void setNotary_person(String notary_person) {
        this.notary_person = notary_person;
    }

    public String getSynchronize_id() {
        return synchronize_id;
    }

    public void setSynchronize_id(String synchronize_id) {
        this.synchronize_id = synchronize_id;
    }

    public String getNotary_office_name() {
        return notary_office_name;
    }

    public void setNotary_office_name(String notary_office_name) {
        this.notary_office_name = notary_office_name;
    }

    public String getContract_kind() {
        return contract_kind;
    }

    public void setContract_kind(String contract_kind) {
        this.contract_kind = contract_kind;
    }

    public String getContract_name() {
        return contract_name;
    }

    public void setContract_name(String contract_name) {
        this.contract_name = contract_name;
    }

    public Long getEntry_user_id() {
        return entry_user_id;
    }

    public void setEntry_user_id(Long entry_user_id) {
        this.entry_user_id = entry_user_id;
    }

    public String getEntry_user_name() {
        return entry_user_name;
    }

    public void setEntry_user_name(String entry_user_name) {
        this.entry_user_name = entry_user_name;
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

    public String getUpdate_user_name() {
        return update_user_name;
    }

    public void setUpdate_user_name(String update_user_name) {
        this.update_user_name = update_user_name;
    }

    public Timestamp getUpdate_date_time() {
        return update_date_time;
    }

    public void setUpdate_date_time(Timestamp update_date_time) {
        this.update_date_time = update_date_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType_duplicate_content() {
        return type_duplicate_content;
    }

    public void setType_duplicate_content(int type_duplicate_content) {
        this.type_duplicate_content = type_duplicate_content;
    }

    public int getType_master_to_transaction() {
        return type_master_to_transaction;
    }

    public void setType_master_to_transaction(int type_master_to_transaction) {
        this.type_master_to_transaction = type_master_to_transaction;
    }

    public int getType_delete() {
        return type_delete;
    }

    public void setType_delete(int type_delete) {
        this.type_delete = type_delete;
    }

    public String getTransaction_content() {
        return transaction_content;
    }

    public void setTransaction_content(String transaction_content) {
        this.transaction_content = transaction_content;
    }
}
