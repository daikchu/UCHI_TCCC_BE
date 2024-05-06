package com.vn.osp.notarialservices.notaryBook.dto;

import lombok.Data;

/**
 * Created by Tuannq on 24/11/2020.
 */
@Data
public class NotaryBookDTO {
    private Long id;
    private String notary_book;
    private Long status;
    private Long type;
    private String create_date;
    private String lock_day;
    private String note;

    private Long entry_user_id;
    private String entry_date_time;
    private Long update_user_id;
    private String update_date_time;

    public NotaryBookDTO(){

    }
    public NotaryBookDTO(Long id, String notary_book, Long status, Long type, String create_date, String lock_day, String note, Long entry_user_id, String entry_date_time, Long update_user_id, String update_date_time){
        this.id = id;
        this.notary_book = notary_book;
        this.status = status;
        this.type = type;
        this.create_date = create_date;
        this.lock_day = lock_day;
        this.note = note;

        this.entry_user_id = entry_user_id;
        this.entry_date_time = entry_date_time;
        this.update_user_id = update_user_id;
        this.entry_date_time = update_date_time;
    }
}
