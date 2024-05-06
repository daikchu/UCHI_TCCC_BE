package com.vn.osp.notarialservices.attestation.service;

import com.vn.osp.notarialservices.attestation.domain.AttestationBO;
import com.vn.osp.notarialservices.attestation.domain.AttestationTemplateFieldMapBO;
import com.vn.osp.notarialservices.attestation.dto.AttestationDTO;
import com.vn.osp.notarialservices.attestation.dto.AttestationTemplateFieldMapDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class AttestationServiceImpl implements AttestationService{
    private Logger logger= LogManager.getLogger(AttestationServiceImpl.class);

    @Autowired AttestationTemplateFieldMapConverter attestationTemplateFieldMapConverter;
    @Autowired AttestationConverter attestationConverter;
    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Long count(String filter) {
        long count = 0;
        try{
            String query ="";
            query = "Select count(bo.id) from AttestationBO bo where 1=1 "+filter;
            count = (Long)entityManager.createQuery(query).getSingleResult();
            return count;

        }catch (Exception e){
            logger.error("Have error in AttestationServiceImpl.count method :"+e.getMessage());
        }
        return Long.valueOf(0);
    }

    @Override
    public List<AttestationDTO> searchByParentCode(String parent_code) {
        List<AttestationBO> listBO = new ArrayList<>();
        ArrayList<AttestationDTO> result = new ArrayList<AttestationDTO>();
        try{
            String hql = "select bo from AttestationBO bo";
            if(!StringUtils.isBlank(parent_code)) listBO = entityManager.createQuery("select bo from AttestationBO bo where 1=1 and bo.parent_code = :parent_code").setParameter("parent_code", parent_code).getResultList();
            else listBO = entityManager.createQuery("select bo from AttestationBO bo where 1=1 and bo.parent_code = :parent_code").setParameter("parent_code", parent_code).getResultList();
            if(listBO != null && listBO.size() > 0){
                for (int i= 0; i < listBO.size(); i++){
                    result.add(attestationConverter.convert(listBO.get(i)));
                }
            }

        }catch (Exception ex){
            logger.error("Have an error in method AttestationServiceImpl.search(): "+ex.getMessage());
        }
        return result;
    }

    @Override
    public List<AttestationDTO> search(String filter) {
        List<AttestationBO> listBO = new ArrayList<>();
        ArrayList<AttestationDTO> result = new ArrayList<AttestationDTO>();
        try{
            String hql = "select bo from AttestationBO bo where 1=1 "+filter;
            listBO = entityManager.createQuery(hql).getResultList();
            if(listBO != null && listBO.size() > 0){
                for (int i= 0; i < listBO.size(); i++){
                    result.add(attestationConverter.convert(listBO.get(i)));
                }
            }

        }catch (Exception ex){
            logger.error("Have an error in method AttestationServiceImpl.search(): "+ex.getMessage());
        }
        return result;
    }

    @Override
    public Boolean add(AttestationDTO item) {

        if(StringUtils.isBlank(item.getCode())) item.setCode(UUID.randomUUID().toString());
        item.setEntry_date_time(new Date());
        item.setUpdate_date_time(new Date());

        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            AttestationBO bo = new AttestationBO();
            bo = attestationConverter.convert(item);
            entityManagerCurrent.persist(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

            return Boolean.valueOf(true);
        }catch (Exception e){
            logger.error("Have an error in method AttestationServiceImpl.add(): "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return Boolean.valueOf(false);
    }

    @Override
    public Boolean update(AttestationDTO item) {

        AttestationDTO itemDB = get(item.getId());
        item.setUpdate_date_time(itemDB.getEntry_date_time());
        item.setEntry_user_id(itemDB.getEntry_user_id());
        item.setType(itemDB.getType());
        item.setType_org(itemDB.getType_org());
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            AttestationBO bo = attestationConverter.convert(item);
            entityManagerCurrent.merge(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception e){
            logger.error("Have an error in method AttestationServiceImpl.update(): "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }
        return result;
    }

    @Override
    public Boolean delete(Long id) {
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            String hql = "delete from AttestationBO bo Where bo.id = "+id;
            entityManagerCurrent.createQuery(hql).executeUpdate();
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception e){
            logger.error("Have an error in method AttestationServiceImpl.delete: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }

        return result;
    }

    @Override
    public AttestationDTO get(Long id) {
        try {
            AttestationDTO result = new AttestationDTO();
            AttestationBO itemDB=entityManager.find(AttestationBO.class,id);
            result = attestationConverter.convert(itemDB);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Have an error in method  AttestationServiceImpl.get()::"+e.getMessage());
            return new AttestationDTO();
        }
    }

    @Override
    public List<AttestationTemplateFieldMapDTO> getAllAttestationTemplateFieldMap(String filter) {
        List<AttestationTemplateFieldMapDTO> result = new ArrayList<>();
        List<AttestationTemplateFieldMapBO> listBO = new ArrayList<>();
        try{
            String hql = "select bo from AttestationTemplateFieldMapBO bo where 1=1 "+filter;
            listBO = entityManager.createQuery(hql).getResultList();
            if(listBO != null && listBO.size()>0){
                for(int i=0;i<listBO.size();i++){
                    result.add(attestationTemplateFieldMapConverter.convert(listBO.get(i)));
                }
            }
        }
        catch (Exception e){
            logger.error("Have an error in method AttestationServiceImpl.getAllAttestationTemplateFieldMap: "+e.getMessage());
        }
        return result;
    }
}
