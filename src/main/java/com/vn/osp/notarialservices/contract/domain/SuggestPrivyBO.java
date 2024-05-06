package com.vn.osp.notarialservices.contract.domain;

import com.vn.osp.notarialservices.common.domain.AbstractAuditEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Admin on 10/1/2018.
 */
@Data
@Entity
@Table(name = "npo_suggest_privy")
public class SuggestPrivyBO extends AbstractAuditEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "template")
    private String template;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private java.sql.Timestamp birthday;

    @Column(name = "passport")
    private String passport;

    @Column(name = "certification_date")
    private java.sql.Timestamp certification_date;

    @Column(name = "certification_place")
    private String certification_place;

    @Column(name = "address")
    private String address;

    @Column(name = "position")
    private String position;

    @Column(name = "description")
    private String description;

    @Column(name = "org_name")
    private String org_name;

    @Column(name = "org_address")
    private String org_address;

    @Column(name = "org_code")
    private String org_code;

    @Column(name = "first_registed_date")
    private java.sql.Timestamp first_registed_date;

    @Column(name = "number_change_registed")
    private String number_change_registed;

    @Column(name = "change_registed_date")
    private java.sql.Timestamp change_registed_date;

    @Column(name = "department_issue")
    private String department_issue;

    @Column(name = "customer_management_unit")
    private String customer_management_unit;

    @Column(name = "phone")
    private String phone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "registration_certificate")
    private String registration_certificate;

    @Column(name = "authorization_document")
    private String authorization_document;

    public SuggestPrivyBO() {
    }

    public SuggestPrivyBO(int id, String template, String name, Timestamp birthday, String passport, Timestamp certification_date, String certification_place, String address, String position, String description, String org_name, String org_address, String org_code, Timestamp first_registed_date, String number_change_registed, Timestamp change_registed_date, String department_issue, String customer_management_unit, String phone, String fax, String registration_certificate, String authorization_document) {
        this.id = id;
        this.template = template;
        this.name = name;
        this.birthday = birthday;
        this.passport = passport;
        this.certification_date = certification_date;
        this.certification_place = certification_place;
        this.address = address;
        this.position = position;
        this.description = description;
        this.org_name = org_name;
        this.org_address = org_address;
        this.org_code = org_code;
        this.first_registed_date = first_registed_date;
        this.number_change_registed = number_change_registed;
        this.change_registed_date = change_registed_date;
        this.department_issue = department_issue;
        this.customer_management_unit = customer_management_unit;
        this.phone = phone;
        this.fax = fax;
        this.registration_certificate = registration_certificate;
        this.authorization_document = authorization_document;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Timestamp getCertification_date() {
        return certification_date;
    }

    public void setCertification_date(Timestamp certification_date) {
        this.certification_date = certification_date;
    }

    public String getCertification_place() {
        return certification_place;
    }

    public void setCertification_place(String certification_place) {
        this.certification_place = certification_place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_address() {
        return org_address;
    }

    public void setOrg_address(String org_address) {
        this.org_address = org_address;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public Timestamp getFirst_registed_date() {
        return first_registed_date;
    }

    public void setFirst_registed_date(Timestamp first_registed_date) {
        this.first_registed_date = first_registed_date;
    }

    public String getNumber_change_registed() {
        return number_change_registed;
    }

    public void setNumber_change_registed(String number_change_registed) {
        this.number_change_registed = number_change_registed;
    }

    public Timestamp getChange_registed_date() {
        return change_registed_date;
    }

    public void setChange_registed_date(Timestamp change_registed_date) {
        this.change_registed_date = change_registed_date;
    }

    public String getDepartment_issue() {
        return department_issue;
    }

    public void setDepartment_issue(String department_issue) {
        this.department_issue = department_issue;
    }

    public String getCustomer_management_unit() {
        return customer_management_unit;
    }

    public void setCustomer_management_unit(String customer_management_unit) {
        this.customer_management_unit = customer_management_unit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getRegistration_certificate() {
        return registration_certificate;
    }

    public void setRegistration_certificate(String registration_certificate) {
        this.registration_certificate = registration_certificate;
    }

    public String getAuthorization_document() {
        return authorization_document;
    }

    public void setAuthorization_document(String authorization_document) {
        this.authorization_document = authorization_document;
    }
}
