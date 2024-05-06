package com.vn.osp.notarialservices.contract.repository;

import com.vn.osp.notarialservices.contract.domain.SuggestPrivyBO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.util.Assert;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 10/1/2018.
 */
public class SuggestPrivyRepositoryImpl implements SuggestPrivyRepositoryCustom {
    private static final Logger LOG = Logger.getLogger(SuggestPrivyRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SuggestPrivyRepositoryImpl(JpaContext context) {
        Assert.notNull(context, "JpaContext must not be null!");
    }

    public void findByOverridingMethod() {
        LOG.info("A method overriding a finder was invoked!");
    }

    @Override
    public Optional<Boolean> addSuggestPrivy(SuggestPrivyBO suggestPrivyBO){
        try{
            if(suggestPrivyBO.getTemplate().equals("1")){
                String hqlFindByPassport = "select sp from SuggestPrivyBO sp where sp.passport= :passport ";
                List<SuggestPrivyBO> itemDBPerson = entityManager.createQuery("select sp from SuggestPrivyBO sp where sp.passport= :passport ").setParameter("passport",suggestPrivyBO.getPassport()).getResultList();
                if(itemDBPerson.size()>0){
                    SuggestPrivyBO itemDBPersonUpdate = moveProperty(suggestPrivyBO,itemDBPerson.get(0));
                    entityManager.merge(itemDBPersonUpdate);
                    entityManager.flush();
                }else {
                    entityManager.persist(suggestPrivyBO);
                }
            }else if(suggestPrivyBO.getTemplate().equals("2")){
                String hqlFindByPassport = "select sp from SuggestPrivyBO sp where sp.org_code=:org_code";
                List<SuggestPrivyBO> itemDBComp = entityManager.createQuery(hqlFindByPassport).setParameter("org_code",suggestPrivyBO.getOrg_code()).getResultList();
                if(itemDBComp.size()>0){
                    SuggestPrivyBO itemDBCompUpdate = moveProperty(suggestPrivyBO,itemDBComp.get(0));
                    entityManager.merge(itemDBCompUpdate);
                    entityManager.flush();
                }else {
                    entityManager.persist(suggestPrivyBO);
                }
            }


            return Optional.of(Boolean.valueOf(true));
        }catch (Exception e){
            e.printStackTrace();
            return Optional.of(Boolean.valueOf(false));
        }


    }
    @Override
    public Optional<List<SuggestPrivyBO>> searchSuggestPrivy(String template,String searchKey){
        try{
            List<SuggestPrivyBO> itemDB = null;
            if(template.equals("1")){

                String hqlSearchPrivy = "select sp from SuggestPrivyBO sp where sp.template = :template and sp.passport LIKE :keySearch";
                itemDB = entityManager.createQuery(hqlSearchPrivy).setParameter("keySearch","%"+searchKey+"%").setParameter("template",template).setFirstResult(0).setMaxResults(20).getResultList();

            }else if(template.equals("2")){

                String hqlSearchPrivy = "select sp from SuggestPrivyBO sp where sp.template = :template and sp.org_code LIKE :keySearch";
                itemDB = entityManager.createQuery(hqlSearchPrivy).setParameter("keySearch","%"+searchKey+"%").setParameter("template",template).setFirstResult(0).setMaxResults(20).getResultList();
            }
        return Optional.of((List<SuggestPrivyBO>)(itemDB));

        }catch (Exception e){
            e.printStackTrace();
            return Optional.of(new ArrayList<SuggestPrivyBO>());
        }


    }
    public SuggestPrivyBO moveProperty(SuggestPrivyBO item,SuggestPrivyBO itemDB){
        itemDB.setTemplate(item.getTemplate());
        itemDB.setName(item.getName());
        itemDB.setBirthday(item.getBirthday());
        itemDB.setPassport(item.getPassport());
        itemDB.setCertification_date(item.getCertification_date());
        itemDB.setCertification_place(item.getCertification_place());
        itemDB.setAddress(item.getAddress());
        itemDB.setPosition(item.getPosition());
        itemDB.setDescription(item.getDescription());
        itemDB.setOrg_name(item.getOrg_name());
        itemDB.setOrg_address(item.getOrg_address());
        itemDB.setOrg_code(item.getOrg_code());
        itemDB.setFirst_registed_date(item.getFirst_registed_date());
        itemDB.setNumber_change_registed(item.getNumber_change_registed());
        itemDB.setChange_registed_date(item.getChange_registed_date());
        itemDB.setDepartment_issue(item.getDepartment_issue());
        return itemDB;
    }





}
