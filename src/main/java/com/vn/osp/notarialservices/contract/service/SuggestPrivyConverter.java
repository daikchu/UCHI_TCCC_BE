package com.vn.osp.notarialservices.contract.service;

import com.vn.osp.notarialservices.common.converter.Converter;
import com.vn.osp.notarialservices.contract.domain.SuggestPrivyBO;
import com.vn.osp.notarialservices.contract.dto.SuggestPrivy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 10/1/2018.
 */
@Service
public class SuggestPrivyConverter implements Converter<SuggestPrivyBO,SuggestPrivy> {
    @Override
    public SuggestPrivy convert(SuggestPrivyBO source){
        return new SuggestPrivy(
                source.getId(),
                source.getTemplate(),
                source.getName(),
                convertTimeStampToString(source.getBirthday()),
                source.getPassport(),
                convertTimeStampToString(source.getCertification_date()),
                source.getCertification_place(),
                source.getAddress(),
                source.getPosition(),
                source.getDescription(),
                source.getOrg_name(),
                source.getOrg_address(),
                source.getOrg_code(),
                convertTimeStampToString(source.getFirst_registed_date()),
                source.getNumber_change_registed(),
                convertTimeStampToString(source.getChange_registed_date()),
                source.getDepartment_issue(),
                source.getCustomer_management_unit(),
                source.getPhone(),
                source.getFax(),
                source.getRegistration_certificate(),
                source.getAuthorization_document()

        );
    }
    @Override
    public SuggestPrivyBO convert(SuggestPrivy source){
        return new SuggestPrivyBO(
                source.getid(),
                source.getTemplate(),
                source.getName(),
                convertStringToTimeStamp(source.getBirthday()),
                source.getPassport(),
                convertStringToTimeStamp(source.getCertification_date()),
                source.getCertification_place(),
                source.getAddress(),
                source.getPosition(),
                source.getDescription(),
                source.getOrg_name(),
                source.getOrg_address(),
                source.getOrg_code(),
                convertStringToTimeStamp(source.getFirst_registed_date()),
                source.getNumber_change_registed(),
                convertStringToTimeStamp(source.getChange_registed_date()),
                source.getDepartment_issue(),
                source.getCustomer_management_unit(),
                source.getPhone(),
                source.getFax(),
                source.getRegistration_certificate(),
                source.getAuthorization_document()
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
