package com.vn.osp.notarialservices.province.dto;

/**
 * Created by ProBook on 5/25/2017.
 */
public class AddProvince {

    private String name;
    private Long entry_user_id;
    private String entry_user_name;


    private String code;

    public AddProvince() {
    }

    public AddProvince(String name, Long entry_user_id, String entry_user_name , String code) {
        this.name = name;
        this.entry_user_id = entry_user_id;
        this.entry_user_name = entry_user_name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
