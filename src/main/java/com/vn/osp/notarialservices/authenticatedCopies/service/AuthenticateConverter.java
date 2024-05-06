package com.vn.osp.notarialservices.authenticatedCopies.service;

import com.vn.osp.notarialservices.common.converter.Converter;
import com.vn.osp.notarialservices.authenticatedCopies.domain.AuthenticatedCopiesBO;
import com.vn.osp.notarialservices.authenticatedCopies.dto.AuthenticateDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AuthenticateConverter{

    public AuthenticateDTO convert(AuthenticatedCopiesBO source){
        return new AuthenticateDTO(
                source.getId(),
                source.getCert_number(),
                convertTimeStampToString(source.getCert_date()),
                source.getCert_request_user(),
                source.getCert_request_doc(),
                source.getCert_sign_user(),
                source.getCert_doc_number(),
                source.getCert_fee(),
                source.getNote(),
                source.getEntry_user_id(),
                convertTimeStampToString(source.getEntry_date_time()),
                source.getUpdate_user_id(),
                convertTimeStampToString(source.getUpdate_date_time())
        );
    }

    public AuthenticatedCopiesBO convert(AuthenticateDTO source){
        AuthenticatedCopiesBO acBo = new AuthenticatedCopiesBO();

        acBo.setId(source.getId());
        acBo.setCert_number(source.getCert_number());
        acBo.setCert_date(convertStringToTimeStamp(source.getCert_date()));
        acBo.setCert_request_user(source.getCert_request_user());
        acBo.setCert_request_doc(source.getCert_request_doc());
        acBo.setCert_sign_user(source.getCert_sign_user());
        acBo.setCert_doc_number(source.getCert_doc_number());
        acBo.setCert_fee(source.getCert_fee());
        acBo.setNote(source.getNote());

        acBo.setEntry_user_id(source.getEntry_user_id());
        acBo.setEntry_date_time(convertStringToTimeStamp(source.getEntry_date_time()));
        acBo.setUpdate_user_id(source.getUpdate_user_id());
        acBo.setUpdate_date_time(convertStringToTimeStamp(source.getUpdate_date_time()));

        return acBo;
    }

    public Timestamp convertStringToTimeStamp(String dateString){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = null;
            parsedDate = dateFormat.parse(dateString);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            return  timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String convertTimeStampToString(Timestamp date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(date==null){
            return null;
        }
        else {
            String result = dateFormat.format(date);
            return result;
        }
    }
}
