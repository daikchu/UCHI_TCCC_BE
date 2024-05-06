package com.vn.osp.notarialservices.notaryBook.domain;

import com.vn.osp.notarialservices.common.domain.AbstractAuditEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "npo_notary_book")
public class NotaryBookDO extends AbstractAuditEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "notary_book")
    private String notary_book;
    @Column(name = "status")
    private Long status;
    @Column(name = "type")
    private Long type;
    @Column(name = "create_date")
    private java.sql.Timestamp create_date;
    @Column(name = "lock_day")
    private java.sql.Timestamp lock_day;
    @Column(name = "note")
    private String note;

    @Column(name = "entry_user_id")
    private Long entry_user_id;
    @Column(name = "entry_date_time")
    private java.sql.Timestamp entry_date_time;
    @Column(name = "update_user_id")
    private Long update_user_id;
    @Column(name = "update_date_time")
    private java.sql.Timestamp update_date_time;

    public NotaryBookDO(){

    }

    public NotaryBookDO(String notary_book, Long status ,Long type, Timestamp create_date, Timestamp lock_day, String note, Long entry_user_id, Timestamp entry_date_time, Long update_user_id, Timestamp update_date_time){
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
