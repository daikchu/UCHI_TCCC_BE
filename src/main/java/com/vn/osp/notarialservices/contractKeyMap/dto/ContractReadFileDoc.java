package com.vn.osp.notarialservices.contractKeyMap.dto;

import com.vn.osp.notarialservices.contract.dto.Contract;
import com.vn.osp.notarialservices.contract.dto.TemporaryContract;

public class ContractReadFileDoc {
    private Contract contract;

    private TemporaryContract temporaryContract;

    private String[] fields;

    private Integer flg_nhanDienKeyStartDuongSu;

    private Integer flg_nhanDienKeyEndDuongSu;

    private Integer flg_nhanDienKeyStartTaiSan;

    private Integer flg_nhanDienKeyEndTaiSan;

    public ContractReadFileDoc() {
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public TemporaryContract getTemporaryContract() {
        return temporaryContract;
    }

    public void setTemporaryContract(TemporaryContract temporaryContract) {
        this.temporaryContract = temporaryContract;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public Integer getFlg_nhanDienKeyStartDuongSu() {
        return flg_nhanDienKeyStartDuongSu;
    }

    public void setFlg_nhanDienKeyStartDuongSu(Integer flg_nhanDienKeyStartDuongSu) {
        this.flg_nhanDienKeyStartDuongSu = flg_nhanDienKeyStartDuongSu;
    }

    public Integer getFlg_nhanDienKeyEndDuongSu() {
        return flg_nhanDienKeyEndDuongSu;
    }

    public void setFlg_nhanDienKeyEndDuongSu(Integer flg_nhanDienKeyEndDuongSu) {
        this.flg_nhanDienKeyEndDuongSu = flg_nhanDienKeyEndDuongSu;
    }

    public Integer getFlg_nhanDienKeyStartTaiSan() {
        return flg_nhanDienKeyStartTaiSan;
    }

    public void setFlg_nhanDienKeyStartTaiSan(Integer flg_nhanDienKeyStartTaiSan) {
        this.flg_nhanDienKeyStartTaiSan = flg_nhanDienKeyStartTaiSan;
    }

    public Integer getFlg_nhanDienKeyEndTaiSan() {
        return flg_nhanDienKeyEndTaiSan;
    }

    public void setFlg_nhanDienKeyEndTaiSan(Integer flg_nhanDienKeyEndTaiSan) {
        this.flg_nhanDienKeyEndTaiSan = flg_nhanDienKeyEndTaiSan;
    }
}
