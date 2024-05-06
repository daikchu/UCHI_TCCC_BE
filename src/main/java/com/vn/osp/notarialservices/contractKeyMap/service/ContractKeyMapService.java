package com.vn.osp.notarialservices.contractKeyMap.service;

import com.vn.osp.notarialservices.contractKeyMap.domain.ContractKeyMapBO;

import java.util.List;

public interface ContractKeyMapService {
    Boolean addContractKeyMap(ContractKeyMapBO contractKeyMapBO);
    String getStringFilterFromSearch(String search);
    List<ContractKeyMapBO> listContractKeyMap(String stringFilter, int offset, int number);
    List<ContractKeyMapBO> getContractKeyMapByFilter(String stringFilter);
    Long countContractKeyMap(String stringFilter);
    Boolean deleteContractKeyMaps(String listId);
    Boolean editContractKeyMap(ContractKeyMapBO bo);
    Boolean checkExistKey(ContractKeyMapBO bo);
}
