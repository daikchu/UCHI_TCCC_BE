package com.vn.osp.notarialservices.district.service;

import com.vn.osp.notarialservices.district.domain.DistrictBo;
import com.vn.osp.notarialservices.district.dto.District;
import com.vn.osp.notarialservices.user.domain.UserBO;
import com.vn.osp.notarialservices.utils.TimeUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DistrictServiceImpl implements DistrictService {
    private Logger logger= LogManager.getLogger(DistrictServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    DistrictConverter districtConverter;

    @Override
    public Optional<Boolean> add(District item) {

        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            DistrictBo bo = districtConverter.convert(item);
            entityManagerCurrent.persist(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

            return Optional.of(Boolean.valueOf(true));
        }catch (Exception e){
            logger.error("Have an error in method DistrictServiceImpl.add(): "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return Optional.of(Boolean.valueOf(false));
    }

    @Override
    public Optional<Boolean> update(District item)
    {
        District itemDB = get(item.getId()).orElse(null);

        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            DistrictBo bo = districtConverter.convert(item);
            entityManagerCurrent.merge(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

            if(result && itemDB != null){
                /*update lại mã quận huyện cho các user đã tồn tại quận/huyện này*/
                updateDistrictCodeForAllUsers(itemDB.getCode(), item.getCode());
            }

        }catch (Exception e){
            logger.error("Have an error in method DistrictServiceImpl.update(): "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }
        return Optional.of(result);
    }

    @Override
    public Optional<Boolean> delete(Long id) {
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            String hql = "delete from DistrictBo di Where di.id = "+id;
            entityManagerCurrent.createQuery(hql).executeUpdate();
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception e){
            logger.error("Have an error in method DistrictServiceImpl.delete: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }

        return Optional.of(result);

    }

    @Override
    public Optional<List<District>> getAll() {
        List<DistrictBo> listBO = new ArrayList<>();
        ArrayList<District> result = new ArrayList<District>();
        try{
            String hql = "select di from DistrictBo di";
            listBO = entityManager.createQuery(hql).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(districtConverter::convert).orElse(new District()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method DistrictServiceImpl.getAll(): "+e.getMessage());
        }
        return Optional.of(result);
    }

    @Override
    public Optional<District> get(Long id) {
        try {
            District result =  new District();
            DistrictBo itemDB=entityManager.find(DistrictBo.class,id);
            result = districtConverter.convert(itemDB);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Have an error in method  DistrictServiceImpl.get()::"+e.getMessage());
            return Optional.ofNullable(new District());
        }
    }

    @Override
    public Optional<List<District>> findByFilter(String stringFilter) {
        List<DistrictBo> listBO = new ArrayList<>();
        ArrayList<District> result = new ArrayList<District>();
        try{
            String hql = "select di from DistrictBo di where 1=1 "+stringFilter;
            listBO = entityManager.createQuery(hql).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(districtConverter::convert).orElse(new District()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method all: "+e.getMessage());
        }
        return Optional.of(result);
    }

    @Override
    public Optional<List<District>> findByCode(String code) {
        List<DistrictBo> listBO = new ArrayList<>();
        ArrayList<District> result = new ArrayList<District>();
        try{
            String hql = "select di from DistrictBo di where di.code = '"+code+"'";
            listBO = entityManager.createQuery(hql).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(districtConverter::convert).orElse(new District()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method all: "+e.getMessage());
        }
        return Optional.of(result);
    }

    @Override
    public Optional<Integer> countByFilter(String stringFilter) {
        Integer count = 0;
        try{
            String hql = "select count(1) from DistrictBo di where 1=1 "+stringFilter;
           Long countLong = (Long) entityManager.createQuery(hql).getSingleResult();
           count = countLong.intValue();

        }catch (Exception e){
            logger.error("Have an error in method all: "+e.getMessage());
        }
        return Optional.of(count);
    }

    @Override
    public Optional<List<District>> listItemPage(String filter, String offset, String limit) {
        List<DistrictBo> listBO = new ArrayList<>();
        ArrayList<District> result = new ArrayList<District>();
        try{
            String hql = "select di from DistrictBo di where 1=1 "+filter;
            listBO = entityManager.createQuery(hql).setFirstResult(Integer.valueOf(offset)).setMaxResults(Integer.valueOf(limit)).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(districtConverter::convert).orElse(new District()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method all: "+e.getMessage());
        }
        return Optional.of(result);
    }

    public Boolean updateDistrictCodeForAllUsers(String codeBefore, String codeUpdate){
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            if(!codeBefore.equals(codeUpdate)){
                String hql = "update UserBO bo set bo.district_code ='"+codeUpdate+"' where bo.district_code = '"+codeBefore+"'";
                entityManagerCurrent.createQuery(hql).executeUpdate();
                entityManagerCurrent.flush();
                entityManagerCurrent.getTransaction().commit();
                entityManagerCurrent.close();
            }

        }catch (Exception e){
            logger.error("Have an error in method DistrictServiceImpl.updateDistrictCodeForAllUsers: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }

        return result;
    }
}
