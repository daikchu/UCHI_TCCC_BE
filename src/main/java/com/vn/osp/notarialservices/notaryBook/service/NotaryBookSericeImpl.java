package com.vn.osp.notarialservices.notaryBook.service;

import com.vn.osp.notarialservices.notaryBook.domain.NotaryBookDO;
import com.vn.osp.notarialservices.notaryBook.dto.NotaryBookDTO;
import com.vn.osp.notarialservices.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class NotaryBookSericeImpl implements NotaryBookService {
    private static final Logger logger = LogManager.getLogger(NotaryBookSericeImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    NotaryBookConverter notaryBookConverter;

    @Override
    public String getStringFilter(String notaryBook, Integer type) {
        String search = "";
        try {
            if (!StringUtils.isBlank(notaryBook)){
                search += " and bo.notary_book like '%"+notaryBook+"%'";
            }
            if (!StringUtils.isBlank(type.toString())){
                search += " and bo.type = "+type;
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
            String hql = "delete from NotaryBookDO bo Where bo.id = "+id;
            entityManagerCurrent.createQuery(hql).executeUpdate();
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception ex){
            logger.error("Have an error in method NotaryBookSericeImpl.delete: "+ ex.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }

        return result;
    }

    @Override
    public Boolean add(NotaryBookDTO item) {
        EntityManager entityManagerCurrent =entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            NotaryBookDO bo = notaryBookConverter.convert(item);
            entityManagerCurrent.persist(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

            return Boolean.valueOf(true);
        }catch (Exception ex){
            logger.error("Have an error in method NotaryBookSericeImpl.add: "+ ex.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return Boolean.valueOf(false);
    }

    @Override
    public Boolean update(NotaryBookDTO item) {
        NotaryBookDTO itemDB = get(item.getId());
        item.setNotary_book(itemDB.getNotary_book());
        item.setType(itemDB.getType());
        item.setEntry_date_time(itemDB.getEntry_date_time());
        item.setEntry_user_id(itemDB.getEntry_user_id());
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            NotaryBookDO bo = notaryBookConverter.convert(item);
            entityManagerCurrent.merge(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();
        }catch (Exception ex){
            logger.error("Have an error in method NotaryBookSericeImpl.update(): "+ex.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }
        return result;
    }

    @Override
    public List<NotaryBookDTO> getAll() {
        List<NotaryBookDO> listBO = new ArrayList<>();
        ArrayList<NotaryBookDTO> result = new ArrayList<NotaryBookDTO>();
        try{
            String hql = "select bo from NotaryBookDO bo where 1=1";
            hql += " order by bo.create_date desc, length(bo.notary_book) desc, bo.notary_book desc";
            listBO = entityManager.createQuery(hql).getResultList();
            if(listBO != null && listBO.size() > 0){
                for (int i= 0; i < listBO.size(); i++){
                    result.add(Optional.ofNullable(listBO.get(i)).map(notaryBookConverter::convert).orElse(new NotaryBookDTO()));
                }
            }

        }catch (Exception ex){
            logger.error("Have an error in method NotaryBookSericeImpl.getAll(): "+ex.getMessage());
        }
        return result;
    }

    @Override
    public NotaryBookDTO get(Long id) {
        try {
            NotaryBookDTO result =  new NotaryBookDTO();
            NotaryBookDO itemDB = entityManager.find(NotaryBookDO.class,id);
            result = notaryBookConverter.convert(itemDB);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Have an error in method  NotaryBookSericeImpl.get()::"+e.getMessage());
            return new NotaryBookDTO();
        }
    }

    @Override
    public List<NotaryBookDTO> findByFilter(String stringFilter) {
        List<NotaryBookDO> listBO = new ArrayList<>();
        ArrayList<NotaryBookDTO> result = new ArrayList<NotaryBookDTO>();
        try{
            String hql = "select bo from NotaryBookDO bo where 1=1 "+stringFilter ;
            hql += " order by bo.create_date desc, length(bo.notary_book) desc, bo.notary_book desc";
            listBO = entityManager.createQuery(hql).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(notaryBookConverter::convert).orElse(new NotaryBookDTO()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method NotaryBookSericeImpl.findByFilter: "+e.getMessage());
        }
        return result;
    }

    @Override
    public List<NotaryBookDTO> findByFilterSattusOpen(String stringFilter) {
        List<NotaryBookDO> listBO = new ArrayList<>();
        ArrayList<NotaryBookDTO> result = new ArrayList<NotaryBookDTO>();
        try{
            String hql = "select bo from NotaryBookDO bo where 1=1 "+stringFilter ;
            hql += " and bo.status=0 order by bo.create_date desc, length(bo.notary_book) desc, bo.notary_book desc";
            listBO = entityManager.createQuery(hql).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(notaryBookConverter::convert).orElse(new NotaryBookDTO()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method NotaryBookSericeImpl.findByFilterType: "+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer countByFilter(String stringFilter) {
        Integer count = 0;
        try{
            String hql = "select count(1) from NotaryBookDO bo where 1=1 "+stringFilter;
            Long countLong = (Long) entityManager.createQuery(hql).getSingleResult();
            count = countLong.intValue();

        }catch (Exception e){
            logger.error("Have an error in method NotaryBookSericeImpl.countByFilter: "+e.getMessage());
        }
        return count;
    }

    @Override
    public List<NotaryBookDTO> listItemPage(String stringFilter, int offset, int limit) {
        List<NotaryBookDO> listBO = new ArrayList<>();
        ArrayList<NotaryBookDTO> result = new ArrayList<>();
        try{
            String hql = "select bo from NotaryBookDO bo where 1=1 "+stringFilter;
            hql += " order by bo.create_date desc, length(bo.notary_book) desc, bo.notary_book desc";
            listBO = entityManager.createQuery(hql).setFirstResult(offset).setMaxResults(limit).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(notaryBookConverter::convert).orElse(new NotaryBookDTO()));
                }
            }

        }catch (Exception ex){
            logger.error("Have an error in method all: "+ex.getMessage());
        }
        return result;
    }

    @Override
    public Integer countByNotaryNumber(String notary_book, Long id, Long type){
        Integer result = 0;
        try {
            if(id == null || id.equals("")){
                String hql = "select count(1) from NotaryBookDO bo where bo.notary_book='"+notary_book+"' and bo.type='"+type+"' ";
                Long count = (Long) entityManager.createQuery(hql).getSingleResult();
                result = (Integer) count.intValue();
            }else {
                String hql = "select count(1) from NotaryBookDO bo where bo.notary_book='"+notary_book+"' and bo.type='"+type+"' and bo.id<>'"+id+"' ";
                Long count = (Long) entityManager.createQuery(hql).getSingleResult();
                result = (Integer) count.intValue();
            }

            return result;
        } catch (Exception e) {
            logger.error("Have an error in method countByNotaryNumber:" + e.getMessage());
            return result;
        }
    }

    @Override
    public Integer checkDeleteNotaryNumber(String notary_book, Long type){
        Integer result = 0;
        try{
            if (!StringUtils.isBlank(notary_book) && !StringUtils.isBlank(type.toString())){
                //String hql = "SELECT COUNT(*) From npo_notary_book INNER JOIN npo_certificate ON npo_notary_book.notary_book=npo_certificate.notary_book AND npo_notary_book.type=npo_certificate.type where npo_notary_book.notary_book='"+notary_book+"' and npo_notary_book.type="+type;
                //Long count = (Long) entityManager.createNativeQuery(hql,NotaryBookDO.class).getResultList();

                Query query = entityManager.createNativeQuery("SELECT COUNT(nb.notary_book) From npo_notary_book nb INNER JOIN npo_certificate cf ON nb.notary_book=cf.notary_book AND nb.type=cf.type where nb.notary_book=:notary_book and nb.type=:type");
                query.setParameter("notary_book",notary_book).setParameter("type",type);
                result = ((BigInteger) query.getSingleResult()).intValue();
                //result = (Integer) count.intValue();
            }
            return result;
        }catch (Exception e){
            logger.error("Have an error in method checkDeleteNotaryNumber:" + e.getMessage());
            return result;
        }
    }

    @Override
    public Integer checkDeleteNotaryNumberContract(String notary_book, Long type){
        Integer result = 0;
        try{
            if (!StringUtils.isBlank(notary_book)){
                Query query = entityManager.createNativeQuery("SELECT COUNT(nb.notary_book) From npo_notary_book nb INNER JOIN npo_contract cf ON nb.notary_book=cf.notary_book where nb.notary_book=:notary_book and nb.type=:type");
                query.setParameter("notary_book",notary_book).setParameter("type",type);
                result = ((BigInteger) query.getSingleResult()).intValue();
                //result = (Integer) count.intValue();
            }
            return result;
        }catch (Exception e){
            logger.error("Have an error in method checkDeleteNotaryNumber:" + e.getMessage());
            return result;
        }
    }

    @Override
    public Integer checkValidateStatusNotary( Long status, Long type){
        Integer result = 0;
        try{
            if (status != null && type != null){
                Query query = entityManager.createNativeQuery("SELECT COUNT(nb.notary_book) From npo_notary_book nb where nb.status=:status and nb.type=:type");
                query.setParameter("status",status).setParameter("type",type);
                result = ((BigInteger) query.getSingleResult()).intValue();
            }
            return result;
        }catch (Exception e){
            logger.error("Have an error in method checkValidateStatusNotary:" + e.getMessage());
            return result;
        }
    }
}
