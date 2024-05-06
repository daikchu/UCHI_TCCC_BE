package com.vn.osp.notarialservices.contract.repository;

import com.vn.osp.notarialservices.contract.domain.SuggestPrivyBO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 10/1/2018.
 */
@Transactional(noRollbackFor = Exception.class)
public interface SuggestPrivyRepositoryCustom {

    Optional<Boolean> addSuggestPrivy(SuggestPrivyBO suggestPrivyBO);
    Optional<List<SuggestPrivyBO>> searchSuggestPrivy(String template,String searchKey);
}
