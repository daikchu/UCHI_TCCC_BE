package com.vn.osp.notarialservices.contract.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.vn.osp.notarialservices.common.dto.BaseEntityBeans;
import lombok.Data;

/**
 * Created by Admin on 10/1/2018.
 */
@XStreamAlias("SuggestPrivy")
@Data
public class SuggestPrivy extends BaseEntityBeans {
    private int id;
    private String template;
    private String name;
    private String birthday;
    private String passport;
    private String certification_date;
    private String certification_place;
    private String address;
    private String position;
    private String description;
    private String org_name;
    private String org_address;
    private String org_code;
    private String first_registed_date;
    private String number_change_registed;
    private String change_registed_date;
    private String department_issue;
    private String customer_management_unit;
    private String phone;
    private String fax;
    private String registration_certificate;
    private String authorization_document;

    public SuggestPrivy() {
    }
    @JsonCreator
    public SuggestPrivy(
            @JsonProperty(value="id",required= true) final int id,
            @JsonProperty(value="template",required= true) final String template,
            @JsonProperty(value="name",required= true) final String name,
            @JsonProperty(value="birthday",required= true) final String birthday,
            @JsonProperty(value="passport",required= true) final String passport,
            @JsonProperty(value="certification_date",required= true) final String certification_date,
            @JsonProperty(value="certification_place",required= true) final String certification_place,
            @JsonProperty(value="address",required= true) final String address,
            @JsonProperty(value="position",required= true) final String position,
            @JsonProperty(value="description",required= true) final String description,
            @JsonProperty(value="org_name",required= true) final String org_name,
            @JsonProperty(value="org_address",required= true) final String org_address,
            @JsonProperty(value="org_code",required= true) final String org_code,
            @JsonProperty(value="first_registed_date",required= true) final String first_registed_date,
            @JsonProperty(value="number_change_registed",required= true) final String number_change_registed,
            @JsonProperty(value="change_registed_date",required= true) final String change_registed_date,
            @JsonProperty(value="department_issue",required= true) final String department_issue,
            @JsonProperty(value="customer_management_unit",required= true) final String customer_management_unit,
            @JsonProperty(value="phone",required= true) final String phone,
            @JsonProperty(value="fax",required= true) final String fax,
            @JsonProperty(value="registration_certificate",required= true) final String registration_certificate,
            @JsonProperty(value="authorization_document",required= true) final String authorization_document

    ) {
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

    public int getid() {
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getCertification_date() {
        return certification_date;
    }

    public void setCertification_date(String certification_date) {
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

    public String getFirst_registed_date() {
        return first_registed_date;
    }

    public void setFirst_registed_date(String first_registed_date) {
        this.first_registed_date = first_registed_date;
    }

    public String getNumber_change_registed() {
        return number_change_registed;
    }

    public void setNumber_change_registed(String number_change_registed) {
        this.number_change_registed = number_change_registed;
    }

    public String getChange_registed_date() {
        return change_registed_date;
    }

    public void setChange_registed_date(String change_registed_date) {
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