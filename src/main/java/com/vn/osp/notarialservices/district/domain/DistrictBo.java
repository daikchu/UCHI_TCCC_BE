package com.vn.osp.notarialservices.district.domain;

import com.vn.osp.notarialservices.common.domain.AbstractAuditEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Daicq on 08/08/2019.
 */
@Data
@Entity
@Table(name = "npo_district")
public class DistrictBo extends AbstractAuditEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "province_code")
    private String province_code;

    @Column(name = "entry_user_id")
    private Long entry_user_id;
    @Column(name = "entry_date_time")
    private String entry_date_time;
    @Column(name = "update_user_id")
    private Long update_user_id;
    @Column(name = "update_date_time")
    private String update_date_time;

    public DistrictBo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
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
