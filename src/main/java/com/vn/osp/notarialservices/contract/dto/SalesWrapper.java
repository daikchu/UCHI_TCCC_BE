package com.vn.osp.notarialservices.contract.dto;

import java.util.List;

/**
 * Created by Admin on 2018-05-25.
 */
public class SalesWrapper {
    private List<SalesByDrafts> salesByDraftsList;
    private List<SalesByNotarys> salesByNotarysList;
    private List<SalesByKindContract> salesByKindContracts;

    public SalesWrapper() {
    }

    public SalesWrapper(List<SalesByDrafts> salesByDraftsList, List<SalesByNotarys> salesByNotarysList, List<SalesByKindContract> salesByKindContracts) {
        this.salesByDraftsList = salesByDraftsList;
        this.salesByNotarysList = salesByNotarysList;
        this.salesByKindContracts = salesByKindContracts;
    }

    public List<SalesByDrafts> getSalesByDraftsList() {
        return salesByDraftsList;
    }

    public void setSalesByDraftsList(List<SalesByDrafts> salesByDraftsList) {
        this.salesByDraftsList = salesByDraftsList;
    }

    public List<SalesByNotarys> getSalesByNotarysList() {
        return salesByNotarysList;
    }

    public void setSalesByNotarysList(List<SalesByNotarys> salesByNotarysList) {
        this.salesByNotarysList = salesByNotarysList;
    }

    public List<SalesByKindContract> getSalesByKindContracts() {
        return salesByKindContracts;
    }

    public void setSalesByKindContracts(List<SalesByKindContract> salesByKindContracts) {
        this.salesByKindContracts = salesByKindContracts;
    }
}
