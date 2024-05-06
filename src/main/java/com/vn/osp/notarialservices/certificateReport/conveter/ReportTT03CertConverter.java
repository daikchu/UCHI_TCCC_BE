package com.vn.osp.notarialservices.certificateReport.conveter;

import com.vn.osp.notarialservices.certificateReport.domain.ReportTT03CertBO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DaiCQ on 24/08/2020.
 */
@Service
public class ReportTT03CertConverter {
    public ReportTT03CertBO convert(ReportTT03CertDTO source) {
        ReportTT03CertBO signCertBO = new ReportTT03CertBO();
        signCertBO.setCert_copies_number(source.getCert_copies_number());
        signCertBO.setCert_signature_number(source.getCert_signature_number());
        signCertBO.setCert_contract_number(source.getCert_contract_number());
        return signCertBO;
    }

    public ReportTT03CertDTO convert(ReportTT03CertBO source) {
        return new ReportTT03CertDTO(
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
