package com.vn.osp.notarialservices.district.dto;

public class District{
    private Long id;
    private String name;
    private String code;
    private String province_code;

    private Long entry_user_id;
    private String entry_date_time;
    private Long update_user_id;
    private String update_date_time;

    public District() {
    }

    public District(Long id, String name, String code, String province_code, Long entry_user_id, String entry_date_time, Long update_user_id, String update_date_time) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.province_code = province_code;
        this.entry_user_id = entry_user_id;
        this.entry_date_time = entry_date_time;
        this.update_user_id = update_user_id;
        this.update_date_time = update_date_time;
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
