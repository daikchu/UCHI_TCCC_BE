package com.vn.osp.notarialservices.certificate.dto;

import lombok.Data;

@Data
public class CertNumberDTO {
    private Long id;

    private String kind_id;
    private Long cert_number;
    private Integer cert_type;
    private Long user_id;
    private String village_code;
    private String district_code;
}
