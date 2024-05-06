package com.vn.osp.notarialservices.contract.repository;

import com.vn.osp.notarialservices.contract.domain.SynchronizeBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by TienManh on 5/12/2017.
 */
public class SynchronizeRepositoryImpl implements SynchronizeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SynchronizeRepositoryImpl(JpaContext context) {
        Assert.notNull(context, "JpaContext must not be null!");
    }

    @Override
    public Optional<List<SynchronizeBO>> getAllSynchronize(){
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_synchronize_getAll");
            storedProcedure.execute();
            List<SynchronizeBO> result = storedProcedure.getResultList();
            return Optional.of((List<SynchronizeBO>)(result));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.of(new ArrayList<SynchronizeBO>());
        }

    }
    @Override
    public Optional<Boolean> addSynchronize(SynchronizeBO synchronizeBO){
        try {
            entityManager.persist(synchronizeBO);
            return Optional.of(Boolean.valueOf(true));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.of(Boolean.valueOf(false));
        }

    }
    @Override
    public Optional<Boolean> removeSynchronize(String data_id){
        try {
            String findSynchronizeDataId = "select syn from SynchronizeBO syn where syn.data_id=:data_id";
            List<SynchronizeBO> list = entityManager.createQuery(findSynchronizeDataId).setParameter("data_id",data_id).getResultList();

            entityManager.remove(list.get(0));
            return Optional.of(Boolean.valueOf(true));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.of(Boolean.valueOf(false));
        }

    }

}
