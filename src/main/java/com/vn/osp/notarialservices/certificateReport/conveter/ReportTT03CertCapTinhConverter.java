package com.vn.osp.notarialservices.certificateReport.conveter;

import com.vn.osp.notarialservices.certificateReport.domain.ReportTT03CertCapHuyenBO;
import com.vn.osp.notarialservices.certificateReport.domain.ReportTT03CertCapTinhBO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertCapHuyenDTO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertCapTinhDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ReportTT03CertCapTinhConverter {
    public ReportTT03CertCapTinhBO convert(ReportTT03CertCapTinhDTO source) {
        ReportTT03CertCapTinhBO reportTT03CertCapTinhBO = new ReportTT03CertCapTinhBO();
        reportTT03CertCapTinhBO.setDistrict_code(source.getDistrict_code());
        reportTT03CertCapTinhBO.setDistrict_name(source.getDistrict_name());
        reportTT03CertCapTinhBO.setCert_copies_number(source.getCert_copies_number());
        reportTT03CertCapTinhBO.setCert_signature_number(source.getCert_signature_number());
        reportTT03CertCapTinhBO.setCert_contract_number(source.getCert_contract_number());
        return reportTT03CertCapTinhBO;
    }

    public ReportTT03CertCapTinhDTO convert(ReportTT03CertCapTinhBO source) {
        return new ReportTT03CertCapTinhDTO(
                source.getDistrict_code(),
                source.getDistrict_name(),
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
