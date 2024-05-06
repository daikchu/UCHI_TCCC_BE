package com.vn.osp.notarialservices.contract.service;

import com.vn.osp.notarialservices.contract.dto.SuggestProperty;

import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 15/1/2018.
 */
public interface SuggestPropertyService {
    Optional<Boolean> addSuggestProperty(SuggestProperty suggestProperty);
    Optional<List<SuggestProperty>> searchSuggestProperty(String type, String searchKey);
}
