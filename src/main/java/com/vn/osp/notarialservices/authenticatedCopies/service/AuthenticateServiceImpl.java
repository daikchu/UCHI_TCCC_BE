package com.vn.osp.notarialservices.authenticatedCopies.service;

import com.vn.osp.notarialservices.authenticatedCopies.domain.AuthenticatedCopiesBO;
import com.vn.osp.notarialservices.authenticatedCopies.dto.AuthenticateDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AuthenticateServiceImpl implements AuthenticateService {
    private Logger logger = LogManager.getLogger(AuthenticateServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    AuthenticateConverter authenticateConverter;

    @Override
    public String getStringFilter(String userId, String cert_number, String dateFrom, String dateTo) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        String search = "";
        try {
            if(!StringUtils.isBlank(userId)){
                search += " and bo.entry_user_id = "+userId;
            }
            if(!StringUtils.isBlank(cert_number)){
                search += " and bo.cert_number like '%"+cert_number+"%'";
            }
            if (!StringUtils.isBlank(dateFrom)){
                dateFrom = format2.format(format1.parse(dateFrom))+" 00:00:00";
                search += " and bo.cert_date >= '"+dateFrom+"'";
            }
            if (!StringUtils.isBlank(dateTo)){
                dateTo = format2.format(format1.parse(dateTo)) +" 23:59:59";
                search += " and bo.cert_date <= '"+dateTo+"'";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return search;
    }

    @Override
    public String getStringCertNumber(String certNumber) {
        String search = "";
        try {
            if (!StringUtils.isBlank(certNumber)){
                search = " and bo.cert_number like '%"+certNumber+"%'";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return search;
    }

    @Override
    public Boolean delete(Long id) {
        Boolean result = true;
        EntityManager entityManagerCurrent =entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            String hql = "delete from AuthenticatedCopiesBO bo Where bo.id = "+id;
            entityManagerCurrent.createQuery(hql).executeUpdate();
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception ex){
            logger.error("Have an error in method AuthenticateServiceImpl.delete: "+ ex.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }

        return result;
    }

    @Override
    public Boolean add(AuthenticateDTO item) {
        EntityManager entityManagerCurrent =entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            AuthenticatedCopiesBO bo = authenticateConverter.convert(item);
            entityManagerCurrent.persist(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

            return Boolean.valueOf(true);
        }catch (Exception ex){
            logger.error("Have an error in method AuthenticateServiceImpl.add: "+ ex.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return Boolean.valueOf(false);
    }

    @Override
    public Boolean update(AuthenticateDTO item) {
        AuthenticateDTO itemDB = get(item.getId());
        item.setEntry_date_time(itemDB.getEntry_date_time());
        item.setEntry_user_id(itemDB.getEntry_user_id());
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            AuthenticatedCopiesBO bo = authenticateConverter.convert(item);
            entityManagerCurrent.merge(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();
        }catch (Exception ex){
            logger.error("Have an error in method AuthenticateServiceImpl.update(): "+ex.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }
        return result;
    }

    @Override
    public List<AuthenticateDTO> getAll() {
        List<AuthenticatedCopiesBO> listBO = new ArrayList<>();
        ArrayList<AuthenticateDTO> result = new ArrayList<AuthenticateDTO>();
        try{
            String hql = "select bo from AuthenticatedCopiesBO bo";
            listBO = entityManager.createQuery(hql).getResultList();
            if(listBO != null && listBO.size() > 0){
                for (int i= 0; i < listBO.size(); i++){
                    result.add(Optional.ofNullable(listBO.get(i)).map(authenticateConverter::convert).orElse(new AuthenticateDTO()));
                }
            }

        }catch (Exception ex){
            logger.error("Have an error in method AuthenticateServiceImpl.getAll(): "+ex.getMessage());
        }
        return result;
    }

    @Override
    public AuthenticateDTO get(Long id) {
        try {
            AuthenticateDTO result =  new AuthenticateDTO();
            AuthenticatedCopiesBO itemDB = entityManager.find(AuthenticatedCopiesBO.class,id);
            result = authenticateConverter.convert(itemDB);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Have an error in method  AuthenticateServiceImpl.get()::"+e.getMessage());
            return new AuthenticateDTO();
        }
    }

    @Override
    public List<AuthenticateDTO> findByFilter(String stringFilter) {
        List<AuthenticatedCopiesBO> listBO = new ArrayList<>();
        ArrayList<AuthenticateDTO> result = new ArrayList<AuthenticateDTO>();
        try{
            String hql = "select bo from AuthenticatedCopiesBO bo where 1=1 "+stringFilter;
            hql += " order by bo.cert_date desc";
            listBO = entityManager.createQuery(hql).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(authenticateConverter::convert).orElse(new AuthenticateDTO()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method AuthenticateServiceImpl.findByFilter: "+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer countByFilter(String stringFilter) {
        Integer count = 0;
        try{
            String hql = "select count(1) from AuthenticatedCopiesBO bo where 1=1 "+stringFilter;
            Long countLong = (Long) entityManager.createQuery(hql).getSingleResult();
            count = countLong.intValue();

        }catch (Exception e){
            logger.error("Have an error in method AuthenticateServiceImpl.countByFilter: "+e.getMessage());
        }
        return count;
    }

    @Override
    public List<AuthenticateDTO> listItemPage(String stringFilter, int offset, int limit) {
        List<AuthenticatedCopiesBO> listBO = new ArrayList<>();
        ArrayList<AuthenticateDTO> result = new ArrayList<>();
        try{
            String hql = "select bo from AuthenticatedCopiesBO bo where 1=1 "+stringFilter;
            hql += " order by bo.cert_date desc";
            listBO = entityManager.createQuery(hql).setFirstResult(offset).setMaxResults(limit).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(authenticateConverter::convert).orElse(new AuthenticateDTO()));
                }
            }

        }catch (Exception ex){
            logger.error("Have an error in method all: "+ex.getMessage());
        }
        return result;
    }


}
