package com.vn.osp.notarialservices.contract.service;

import com.vn.osp.notarialservices.common.converter.Converter;
import com.vn.osp.notarialservices.contract.domain.SuggestPropertyBO;
import com.vn.osp.notarialservices.contract.dto.SuggestProperty;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 15/1/2018.
 */
@Service
public class SuggestPropertyConverter implements Converter<SuggestPropertyBO,SuggestProperty> {
    @Override
    public SuggestProperty convert(SuggestPropertyBO source){
        return new SuggestProperty(
                source.getId(),
                source.getType(),
                source.getType_view(),
                source.getOwner_info(),
                source.getOther_info(),
                source.getRe_strict(),
                source.getLand_certificate(),
                source.getLand_certificate_number_input(),
                source.getLand_issue_place(),
                convertTimeStampToString(source.getLand_issue_date()),
                source.getLand_map_number(),
                source.getLand_number(),
                source.getLand_address(),
                source.getLand_area(),
                source.getLand_area_text(),
                source.getLand_public_area(),
                source.getLand_private_area(),
                source.getLand_use_purpose(),
                source.getLand_use_period(),
                source.getLand_use_origin(),
                source.getLand_associate_property(),
                source.getLand_street(),
                source.getLand_district(),
                source.getLand_province(),
                source.getLand_full_of_area(),
                source.getCar_license_number(),
                source.getCar_regist_number(),
                source.getCar_issue_place(),
                convertTimeStampToString(source.getLand_issue_date()),
                source.getCar_frame_number(),
                source.getCar_machine_number(),
                source.getApartment_address(),
                source.getApartment_number(),
                source.getApartment_floor(),
                source.getApartment_area_use(),
                source.getApartment_area_build(),
                source.getApartment_structure(),
                source.getApartment_total_floor()


        );
    }
    @Override
    public SuggestPropertyBO convert(SuggestProperty source){
        return new SuggestPropertyBO(
                source.getid(),
                source.getType(),
                source.getType_view(),
                source.getOwner_info(),
                source.getOther_info(),
                source.getRestrict(),
                source.getLand_certificate(),
                source.getLand_certificate_number_input(),
                source.getLand_issue_place(),
                convertStringToTimeStamp(source.getLand_issue_date()),
                source.getLand_map_number(),
                source.getLand_number(),
                source.getLand_address(),
                source.getLand_area(),
                source.getLand_area_text(),
                source.getLand_public_area(),
                source.getLand_private_area(),
                source.getLand_use_purpose(),
                source.getLand_use_period(),
                source.getLand_use_origin(),
                source.getLand_associate_property(),
                source.getLand_street(),
                source.getLand_district(),
                source.getLand_province(),
                source.getLand_full_of_area(),
                source.getCar_license_number(),
                source.getCar_regist_number(),
                source.getCar_issue_place(),
                convertStringToTimeStamp(source.getCar_issue_date()),
                source.getCar_frame_number(),
                source.getCar_machine_number(),
                source.getApartment_address(),
                source.getApartment_number(),
                source.getApartment_floor(),
                source.getApartment_area_use(),
                source.getApartment_area_build(),
                source.getApartment_structure(),
                source.getApartment_total_floor()

        );
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
}
