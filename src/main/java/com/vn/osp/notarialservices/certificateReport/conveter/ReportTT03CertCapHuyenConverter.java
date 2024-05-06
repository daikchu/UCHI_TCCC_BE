package com.vn.osp.notarialservices.certificateReport.conveter;

import com.vn.osp.notarialservices.certificateReport.domain.ReportTT03CertCapHuyenBO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertCapHuyenDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DaiCQ on 24/08/2020.
 */
@Service
public class ReportTT03CertCapHuyenConverter {

    public ReportTT03CertCapHuyenBO convert(ReportTT03CertCapHuyenDTO source) {
        ReportTT03CertCapHuyenBO reportTT03CertCapHuyenBO = new ReportTT03CertCapHuyenBO();
        reportTT03CertCapHuyenBO.setUser_id(source.getUser_id());
        reportTT03CertCapHuyenBO.setUser_first_name(source.getUser_first_name());
        reportTT03CertCapHuyenBO.setUser_family_name(source.getUser_family_name());
        reportTT03CertCapHuyenBO.setUser_district_code(source.getUser_district_code());
        /*reportTT03CertCapHuyenBO.setUser_district_name(source.getUser_district_name());*/
        reportTT03CertCapHuyenBO.setUser_level_cert(source.getUser_level_cert());
        reportTT03CertCapHuyenBO.setCert_copies_number(source.getCert_copies_number());
        reportTT03CertCapHuyenBO.setCert_signature_number(source.getCert_signature_number());
        reportTT03CertCapHuyenBO.setCert_contract_number(source.getCert_contract_number());
        return reportTT03CertCapHuyenBO;
    }

    public ReportTT03CertCapHuyenDTO convert(ReportTT03CertCapHuyenBO source) {
        return new ReportTT03CertCapHuyenDTO(
                source.getUser_id(),
                source.getUser_first_name(),
                source.getUser_family_name(),
                source.getUser_district_code(),
                /*source.getUser_district_name(),*/
                source.getUser_level_cert(),
                source.getCert_copies_number(),
                source.getCert_signature_number(),
                source.getCert_contract_number()
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
