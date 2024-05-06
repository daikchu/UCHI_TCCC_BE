package com.vn.osp.notarialservices.certificateReport.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Data
@Table
public class ReportLuongGDBDSBO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "district_code")
    private String district_code;
    @Column(name = "district_name")
    private String district_name;
    @Column(name = "count_DNDO_phatTrienTheoDuAn")
    private BigInteger count_DNDO_phatTrienTheoDuAn;
    @Column(name = "count_DNDO_trongKhuDanCu")
    private BigInteger count_DNDO_trongKhuDanCu;
    @Column(name = "count_NORL_phatTrienTheoDuAn")
    private BigInteger count_NORL_phatTrienTheoDuAn;
    @Column(name = "count_NORL_trongKhuDanCu")
    private BigInteger count_NORL_trongKhuDanCu;
    @Column(name = "count_CHCC_dienTichDuoi70m2")
    private BigInteger count_CHCC_dienTichDuoi70m2;
    @Column(name = "count_CHCC_dienTichTu70Den120m2")
    private BigInteger count_CHCC_dienTichTu70Den120m2;
    @Column(name = "count_CHCC_dienTichTren120m2")
    private BigInteger count_CHCC_dienTichTren120m2;
    @Column(name = "count_vanPhongChoThue")
    private BigInteger count_vanPhongChoThue;
    @Column(name = "count_matBangThuongMaiDichVu")
        private BigInteger count_matBangThuongMaiDichVu;

    /*private BigInteger total_count_DNDO_phatTrienTheoDuAn;
    private BigInteger total_count_DNDO_trongKhuDanCu;
    private BigInteger total_count_NORL_phatTrienTheoDuAn;
    private BigInteger total_count_NORL_trongKhuDanCu;
    private BigInteger total_count_CHCC_dienTichDuoi70m2;
    private BigInteger total_count_CHCC_dienTichTu70Den120m2;
    private BigInteger total_count_CHCC_dienTichTren120m2;
    private BigInteger total_count_vanPhongChoThue;
    private BigInteger total_count_matBangThuongMaiDichVu;*/
}
