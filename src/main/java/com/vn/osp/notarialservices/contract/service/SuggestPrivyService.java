package com.vn.osp.notarialservices.contract.service;

import com.vn.osp.notarialservices.contract.dto.SuggestPrivy;

import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 10/1/2018.
 */
public interface SuggestPrivyService {
    Optional<Boolean> addSuggestPrivy(SuggestPrivy suggestPrivy);
    Optional<List<SuggestPrivy>> searchSuggestPrivy(String template,String searchKey);
}
