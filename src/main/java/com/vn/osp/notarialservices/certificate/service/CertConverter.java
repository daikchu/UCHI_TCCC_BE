package com.vn.osp.notarialservices.certificate.service;

import com.vn.osp.notarialservices.certificate.domain.CertBO;
import com.vn.osp.notarialservices.certificate.dto.CertDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DaiCQ on 13/08/2020.
 */
@Service
public class CertConverter {
    public CertBO convert(CertDTO source) {
        CertBO certBO = new CertBO();
        certBO.setId(source.getId());
        certBO.setCert_number(source.getCert_number());
        certBO.setCert_date(convertStringToTimeStamp(source.getCert_date()));
        certBO.setCert_request_user(source.getCert_request_user());
        certBO.setCert_request_doc(source.getCert_request_doc());
        certBO.setCert_sign_user(source.getCert_sign_user());
        certBO.setCert_doc_number(source.getCert_doc_number());
        certBO.setCert_fee(source.getCert_fee());
        certBO.setNote(source.getNote());
        certBO.setAttestation_template_code(source.getAttestation_template_code());
        certBO.setKind_html(source.getKind_html());
        certBO.setNotary_book(source.getNotary_book());

        certBO.setEntry_user_id(source.getEntry_user_id());
        certBO.setUpdate_user_id(source.getUpdate_user_id());
        certBO.setEntry_date_time(convertStringToTimeStamp(source.getEntry_date_time()));
        certBO.setUpdate_date_time(convertStringToTimeStamp(source.getUpdate_date_time()));
        certBO.setType(source.getType());
        return certBO;
    }

    public CertDTO convert(CertBO source) {
        return new CertDTO(
                source.getId(),
                source.getCert_number(),
                convertTimeStampToString(source.getCert_date()),
                source.getCert_request_user(),
                source.getCert_request_doc(),
                source.getCert_sign_user(),
                source.getCert_doc_number(),
                source.getCert_fee(),
                source.getNote(),
                source.getKind_html(),
                source.getNotary_book(),

                source.getAttestation_template_code(),
                source.getEntry_user_id(),
                source.getUpdate_user_id(),
                convertTimeStampToString(source.getEntry_date_time()),
                convertTimeStampToString(source.getUpdate_date_time()),
                source.getType()
        );
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
