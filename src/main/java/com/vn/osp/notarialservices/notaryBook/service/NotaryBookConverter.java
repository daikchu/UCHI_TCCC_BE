package com.vn.osp.notarialservices.notaryBook.service;

import com.vn.osp.notarialservices.notaryBook.domain.NotaryBookDO;
import com.vn.osp.notarialservices.notaryBook.dto.NotaryBookDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class NotaryBookConverter {
    public NotaryBookDTO convert (NotaryBookDO source){
        return new NotaryBookDTO(
                source.getId(),
                source.getNotary_book(),
                source.getStatus(),
                source.getType(),
                convertTimeStampToString(source.getCreate_date()),
                convertTimeStampToString(source.getLock_day()),
                source.getNote(),
                source.getEntry_user_id(),
                convertTimeStampToString(source.getEntry_date_time()),
                source.getUpdate_user_id(),
                convertTimeStampToString(source.getUpdate_date_time())
        );
    }

    public NotaryBookDO convert(NotaryBookDTO source) {
        NotaryBookDO nbBo = new NotaryBookDO();

        nbBo.setId(source.getId());
        nbBo.setNotary_book(source.getNotary_book());
        nbBo.setStatus(source.getStatus());
        nbBo.setType(source.getType());
        nbBo.setCreate_date(convertStringToTimeStamp(source.getCreate_date()));
        if(source.getLock_day()!= null){
            nbBo.setLock_day(convertStringToTimeStamp(source.getLock_day()));
        }
        nbBo.setNote(source.getNote());

        nbBo.setEntry_user_id(source.getEntry_user_id());
        nbBo.setEntry_date_time(convertStringToTimeStamp(source.getEntry_date_time()));
        nbBo.setUpdate_user_id(source.getUpdate_user_id());
        nbBo.setUpdate_date_time(convertStringToTimeStamp(source.getUpdate_date_time()));

        return nbBo;
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
