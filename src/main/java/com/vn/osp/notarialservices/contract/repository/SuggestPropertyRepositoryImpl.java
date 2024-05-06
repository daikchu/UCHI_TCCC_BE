package com.vn.osp.notarialservices.contract.repository;

import com.vn.osp.notarialservices.contract.domain.SuggestPropertyBO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 15/1/2018.
 */
public class SuggestPropertyRepositoryImpl implements SuggestPropertyRepositoryCustom {
    private static final Logger LOG = Logger.getLogger(SuggestPropertyRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SuggestPropertyRepositoryImpl(JpaContext context) {
        Assert.notNull(context, "JpaContext must not be null!");
    }

    public void findByOverridingMethod() {
        LOG.info("A method overriding a finder was invoked!");
    }

    @Override
    public Optional<Boolean> addSuggestProperty(SuggestPropertyBO suggestPropertyBO){
        try{
            if(suggestPropertyBO.getType().equals("01")){
                String hqlFindByLandCer = "select sp from SuggestPropertyBO sp where sp.land_certificate= :land_certificate ";
                List<SuggestPropertyBO> itemDBLand = entityManager.createQuery("select sp from SuggestPropertyBO sp where sp.land_certificate= :land_certificate and sp.type= :type ").setParameter("land_certificate",suggestPropertyBO.getLand_certificate()).setParameter("type","01").getResultList();
                if(itemDBLand.size()>0){
                    SuggestPropertyBO itemDBLandUpdate = movePropertyLand(suggestPropertyBO,itemDBLand.get(0));
                    entityManager.merge(itemDBLandUpdate);
                    entityManager.flush();
                }else {
                    entityManager.persist(suggestPropertyBO);
                }
            }else if(suggestPropertyBO.getType().equals("02")){
                String hqlFindByPassport = "select sp from SuggestPropertyBO sp where sp.car_frame_number=:car_frame_number and sp.type=:type";
                List<SuggestPropertyBO> itemDBVehicle = entityManager.createQuery(hqlFindByPassport).setParameter("car_frame_number",suggestPropertyBO.getCar_frame_number()).setParameter("type","02").getResultList();
                if(itemDBVehicle.size()>0){
                    SuggestPropertyBO itemDBVehicleUpdate = movePropertyVehicle(suggestPropertyBO,itemDBVehicle.get(0));
                    entityManager.merge(itemDBVehicleUpdate);
                    entityManager.flush();
                }else {
                    entityManager.persist(suggestPropertyBO);
                }
            }


            /*entityManager.persist(suggestPropertyBO);
            entityManager.flush();*/



            return Optional.of(Boolean.valueOf(true));
        }catch (Exception e){
            e.printStackTrace();
            return Optional.of(Boolean.valueOf(false));
        }

    }
    @Override
    public Optional<List<SuggestPropertyBO>> searchSuggestProperty(String type,String searchKey){
        try{
            List<SuggestPropertyBO> itemDB = null;
            if(type.equals("01")){

                String hqlSearch = "select sp from SuggestPropertyBO sp where sp.type = :type and sp.land_certificate LIKE :keySearch";
                itemDB = entityManager.createQuery(hqlSearch).setParameter("keySearch","%"+searchKey+"%").setParameter("type","01").setFirstResult(0).setMaxResults(20).getResultList();

            }else if(type.equals("02")){

                String hqlSearch = "select sp from SuggestPropertyBO sp where sp.template = :type and sp.car_frame_number LIKE :keySearch";
                itemDB = entityManager.createQuery(hqlSearch).setParameter("keySearch","%"+searchKey+"%").setParameter("type","02").setFirstResult(0).setMaxResults(20).getResultList();
            }
            return Optional.of((List<SuggestPropertyBO>)(itemDB));

        }catch (Exception e){
            e.printStackTrace();
            return Optional.of(new ArrayList<SuggestPropertyBO>());
        }


    }
    public Timestamp convertStringToTimeStamp(String dateString){
        try {
            if(dateString == "" || dateString == null) return null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = null;
            parsedDate = dateFormat.parse(dateString);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return  timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String convertTimeStampToString(Timestamp date){
        if(date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String result  = dateFormat.format(date);
        return result;
    }
    public SuggestPropertyBO movePropertyLand(SuggestPropertyBO item,SuggestPropertyBO itemDB){
        itemDB.setType_view(item.getType_view());
        itemDB.setOwner_info(item.getOwner_info());
        itemDB.setOther_info(item.getOther_info());
        itemDB.setRe_strict(item.getRe_strict());
        itemDB.setLand_certificate(item.getLand_certificate());
        itemDB.setLand_issue_date(item.getLand_issue_date());
        itemDB.setLand_issue_place(item.getLand_issue_place());
        itemDB.setLand_map_number(item.getLand_map_number());
        itemDB.setLand_number(item.getLand_number());
        itemDB.setLand_address(item.getLand_address());
        itemDB.setLand_area(item.getLand_area());
        itemDB.setLand_area_text(item.getLand_area_text());
        itemDB.setLand_public_area(item.getLand_public_area());
        itemDB.setLand_private_area(item.getLand_private_area());
        itemDB.setLand_use_purpose(item.getLand_use_purpose());
        itemDB.setLand_use_period(item.getLand_use_period());
        itemDB.setLand_use_origin(item.getLand_use_origin());
        itemDB.setLand_associate_property(item.getLand_associate_property());
        itemDB.setLand_street(item.getLand_street());
        itemDB.setLand_district(item.getLand_district());
        itemDB.setLand_province(item.getLand_province());
        itemDB.setLand_full_of_area(item.getLand_full_of_area());
        return itemDB;
    }
    public SuggestPropertyBO movePropertyVehicle(SuggestPropertyBO item,SuggestPropertyBO itemDB){
        itemDB.setType_view(item.getType_view());
        itemDB.setOwner_info(item.getOwner_info());
        itemDB.setOther_info(item.getOther_info());
        itemDB.setRe_strict(item.getRe_strict());
        itemDB.setCar_license_number(item.getCar_license_number());
        itemDB.setCar_regist_number(item.getCar_regist_number());
        itemDB.setCar_issue_place(item.getCar_issue_place());
        itemDB.setCar_issue_date(item.getCar_issue_date());
        itemDB.setCar_frame_number(item.getCar_frame_number());
        itemDB.setCar_machine_number(item.getCar_machine_number());
        return itemDB;
    }



}
