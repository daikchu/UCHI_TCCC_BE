package com.vn.osp.notarialservices.masterConvert.domain;

import com.vn.osp.notarialservices.common.domain.AbstractAuditEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "npo_contract_property")
public class ContractPropertyBO extends AbstractAuditEntity implements Serializable {
    @Id
    @Column(name = "contract_id", nullable = false)
    private Long contract_id;

    @Id
    @Column(name = "property_id", nullable = false)
    private Long property_id;

    public ContractPropertyBO(Long contract_id, Long property_id) {
        this.contract_id = contract_id;
        this.property_id = property_id;
    }

    public ContractPropertyBO() {
    }

    public Long getContract_id() {
        return contract_id;
    }

    public void setContract_id(Long contract_id) {
        this.contract_id = contract_id;
    }

    public Long getProperty_id() {
        return property_id;
    }

    public void setProperty_id(Long property_id) {
        this.property_id = property_id;
    }
}
