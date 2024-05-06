package com.vn.osp.notarialservices.contract.repository;

import com.vn.osp.notarialservices.contract.domain.SynchronizeBO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by TienManh on 5/12/2017.
 */
@Transactional
public interface SynchronizeRepositoryCustom {
    Optional<List<SynchronizeBO>> getAllSynchronize();
    Optional<Boolean> addSynchronize(SynchronizeBO synchronizeBO);
    Optional<Boolean> removeSynchronize(String data_id);
}
