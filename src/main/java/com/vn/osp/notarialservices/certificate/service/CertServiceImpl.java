package com.vn.osp.notarialservices.certificate.service;

import com.vn.osp.notarialservices.certificate.domain.CertBO;
import com.vn.osp.notarialservices.certificate.domain.CertNumberBO;
import com.vn.osp.notarialservices.certificate.dto.CertDTO;
import com.vn.osp.notarialservices.certificate.dto.CertNumberDTO;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CertServiceImpl implements CertService {
    private Logger logger= LogManager.getLogger(CertServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    CertConverter certConverter;
    @Autowired
    CertNumberConverter certNumberConverter;

    @Override
    public String getStringFilter(String userId, String cert_number, String notary_book, String dateFrom, String dateTo, Integer type, String attestation_template_code) {
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
            if(!StringUtils.isBlank(attestation_template_code)){
                search += " and bo.attestation_template_code = '"+attestation_template_code+"'";
            }
            if(!StringUtils.isBlank(dateFrom)){
                dateFrom = format2.format(format1.parse(dateFrom))+" 00:00:00";
                search += " and bo.cert_date >= '"+dateFrom+"'";
            }
            if(!StringUtils.isBlank(dateTo)){
                dateTo = format2.format(format1.parse(dateTo)) +" 23:59:59";
                search += " and bo.cert_date <= '"+dateTo+"'";
            }
            if (!StringUtils.isBlank(notary_book)) search += " and bo.notary_book like '%"+notary_book+"%'";
            if(type != null) search += " and bo.type = "+type;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return search;
    }

    @Override
    public Boolean add(CertDTO item) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            CertBO bo = certConverter.convert(item);
            entityManagerCurrent.persist(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

            return Boolean.valueOf(true);
        }catch (Exception e){
            logger.error("Have an error in method ReportTT03CertServiceImpl.add(): "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return Boolean.valueOf(false);
    }

    @Override
    public Boolean update(CertDTO item) {
        CertDTO itemDB = get(item.getId());
        item.setEntry_date_time(itemDB.getEntry_date_time());
        item.setEntry_user_id(itemDB.getEntry_user_id());
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            CertBO bo = certConverter.convert(item);
            entityManagerCurrent.merge(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception e){
            logger.error("Have an error in method ReportTT03CertServiceImpl.update(): "+e.getMessage());
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
            String hql = "delete from CertBO bo Where bo.id = "+id;
            entityManagerCurrent.createQuery(hql).executeUpdate();
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception e){
            logger.error("Have an error in method ReportTT03CertServiceImpl.delete: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }

        return result;
    }

    @Override
    public List<CertDTO> getAll() {
        List<CertBO> listBO = new ArrayList<>();
        ArrayList<CertDTO> result = new ArrayList<CertDTO>();
        try{
            String hql = "select bo from CertBO bo";
            hql += " order by bo.cert_date desc";
            listBO = entityManager.createQuery(hql).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(certConverter::convert).orElse(new CertDTO()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method ReportTT03CertServiceImpl.getAll(): "+e.getMessage());
        }
        return result;
    }

    @Override
    public CertDTO get(long id) {
        try {

            CertDTO result = new CertDTO();
            CertBO itemDB=entityManager.find(CertBO.class,id);
            result = certConverter.convert(itemDB);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Have an error in method  ReportTT03CertServiceImpl.get()::"+e.getMessage());
            return new CertDTO();
        }
    }

    @Override
    public List<CertDTO> findByFilter(String stringFilter) {
        List<CertBO> listBO = new ArrayList<>();
        ArrayList<CertDTO> result = new ArrayList<CertDTO>();
        try{
            String hql = "select bo from CertBO bo where 1=1 "+stringFilter;
            hql += " order by bo.cert_date desc, length(bo.cert_number) desc, bo.cert_number desc ";
            listBO = entityManager.createQuery(hql).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(certConverter::convert).orElse(new CertDTO()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method ReportTT03CertServiceImpl.findByFilter: "+e.getMessage());
        }
        return result;
    }


    @Override
    public Integer countByFilter(String stringFilter) {
        Integer count = 0;
        try{
            String hql = "select count(1) from CertBO bo where 1=1 "+stringFilter;
            Long countLong = (Long) entityManager.createQuery(hql).getSingleResult();
            count = countLong.intValue();

        }catch (Exception e){
            logger.error("Have an error in method ReportTT03CertServiceImpl.countByFilter: "+e.getMessage());
        }
        return count;
    }

    @Override
    public List<CertDTO> listItemPage(String filter, int offset, int limit) {
        List<CertBO> listBO = new ArrayList<>();
        ArrayList<CertDTO> result = new ArrayList<>();
        try{
            String hql = "select bo from CertBO bo where 1=1 "+filter;
            hql += " order by bo.cert_date desc, length(bo.cert_number) desc, bo.cert_number desc ";
            listBO = entityManager.createQuery(hql).setFirstResult(offset).setMaxResults(limit).getResultList();
            if (listBO != null && listBO.size() > 0) {
                for (int i = 0; i < listBO.size(); i++) {
                    result.add(Optional.ofNullable(listBO.get(i)).map(certConverter::convert).orElse(new CertDTO()));
                }
            }

        }catch (Exception e){
            logger.error("Have an error in method all: "+e.getMessage());
        }
        return result;
    }


    @Override
    public List<CertNumberDTO> getCertNumber(Long userId, Long cert_type) {
        List<CertNumberBO> listBO = new ArrayList<>();
        ArrayList<CertNumberDTO> result = new ArrayList<CertNumberDTO>();
        try{
            if(userId >0){
                String hql = "SELECT * FROM npo_certificate_number WHERE user_id ="+userId+" AND cert_type="+cert_type+" AND kind_id=(SELECT MAX(kind_id) FROM npo_certificate_number WHERE user_id="+userId+") \n" +
                        "AND cert_number=(SELECT MAX(cert_number) FROM npo_certificate_number WHERE user_id="+userId+" AND cert_type="+cert_type+")";
                listBO = entityManager.createNativeQuery(hql,CertNumberBO.class).getResultList();
                if(listBO != null && listBO.size() > 0){
                    for (int i= 0; i < listBO.size(); i++){
                        result.add(Optional.ofNullable(listBO.get(i)).map(certNumberConverter::convert).orElse(new CertNumberDTO()));
                    }
                }
            }
        }catch (Exception ex){
            logger.error("Have an error in method CertSericeImpl.getCertNumber(): "+ex.getMessage());
        }
        return result;
    }

    @Override
    public Boolean addCertNumber(CertNumberDTO item) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            CertNumberBO bo = certNumberConverter.convert(item);
            entityManagerCurrent.persist(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

            return Boolean.valueOf(true);
        }catch (Exception e){
            logger.error("Have an error in method addCertNumber(): "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return Boolean.valueOf(false);
    }

    @Override
    public Boolean updateCertNumber(CertNumberDTO item) {
      //  CertNumberDTO itemDB = getByIdCertNumber(item.getId());

        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            CertNumberBO bo = certNumberConverter.convert(item);
            entityManagerCurrent.merge(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception e){
            logger.error("Have an error in method ReportTT03CertServiceImpl.update(): "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }
        return result;
    }

    @Override
    public List<CertNumberDTO> checkExistCertnumber(String kind_id, Long user_id, Integer cert_type){
        List<CertNumberBO> listBO = new ArrayList<>();
        List<CertNumberDTO> result = new ArrayList<CertNumberDTO>();
        try{
//            Query query = entityManager.createQuery("SELECT bo FROM CertNumberBO bo WHERE bo.kind_id = :kind_id AND bo.user_id = :user_id ",CertNumberBO.class);
//                query.setParameter("kind_id",kind_id);
//                query.setParameter("user_id",user_id);

//            result = (List) query.getSingleResult();
//            return result;
            String hql = "SELECT * FROM npo_certificate_number WHERE kind_id ="+ kind_id +" AND user_id ="+user_id+" AND cert_type="+cert_type;

            listBO = entityManager.createNativeQuery(hql,CertNumberBO.class).getResultList();
            if(listBO != null && listBO.size() > 0){
                for (int i= 0; i < listBO.size(); i++){
                    result.add(Optional.ofNullable(listBO.get(i)).map(certNumberConverter::convert).orElse(new CertNumberDTO()));
                }
            }
        }catch (Exception e){
            logger.error("Have an error in method checkUpdateCertnumber(): "+e.getMessage());
        }
        return result;
    }

}
