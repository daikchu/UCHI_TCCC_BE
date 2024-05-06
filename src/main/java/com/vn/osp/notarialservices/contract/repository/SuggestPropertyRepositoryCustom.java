package com.vn.osp.notarialservices.contract.repository;

import com.vn.osp.notarialservices.contract.domain.SuggestPropertyBO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 15/1/2018.
 */
@Transactional(noRollbackFor = Exception.class)
public interface SuggestPropertyRepositoryCustom {
    Optional<Boolean> addSuggestProperty(SuggestPropertyBO suggestPropertyBO);
    Optional<List<SuggestPropertyBO>> searchSuggestProperty(String type, String searchKey);
}
