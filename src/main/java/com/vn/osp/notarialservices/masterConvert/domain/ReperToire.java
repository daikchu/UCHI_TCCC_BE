package com.vn.osp.notarialservices.masterConvert.domain;

import com.vn.osp.notarialservices.common.domain.AbstractAuditEntity;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;

public class ReperToire extends AbstractAuditEntity implements Serializable {
    private Long REP_REF_UNIQUE;
    private String ENREG_CODE;
    private String CODE_BREVET;
    private int PERS_CODE;
    private int PER_PERS_CODE;
    private int REP_AN_LEGAL;
    private String REP_LEGAL;
    private java.sql.Date REP_DATE;
    private String REP_LIBEL;
    private Long REP_MT_ENREG;
    private Date REP_DATE_ENREG;
    private int TIMBRE_ACTE;
    private Long MONTANT_TIMBRE_ACTE;
    private int NB_COP_AUTH;
    private int NB_PAGE_COP_AUTH;
    private Long MT_COP_AUTH;
    private int NB_COP_EXEC;
    private int NB_PAGE_COP_EXEC;
    private Long MT_COP_EXEC;
    private Blob REP_TEXTE;
    private String SDOS_ETUDE;
    private int REP_EDIT;
    private int REP_AN_LEGAL_DEFI;
    private int REP_LEGAL_DEFI;
    private int REP_MANU;
    private String REP_LIEU_SIGNATURE;
    private int REP_ENREG;
    private String REP_NUMENREG;
    private int REP_RECEUIL;
    private String REP_STAT;
    private int REP_COMPLET;
    private int REP_SUITE;
    private Blob REP_INTERVENANT;
    private String REP_ESTREMI;
    private int REP_VALEUR;
    private String REP_PS;
    private String SDOS_REF;
    private String SDOS_LIBEL;
    private String TEXTE;
    private Date TIME_UPDATE;

    public Long getREP_REF_UNIQUE() {
        return REP_REF_UNIQUE;
    }

    public void setREP_REF_UNIQUE(Long REP_REF_UNIQUE) {
        this.REP_REF_UNIQUE = REP_REF_UNIQUE;
    }

    public String getENREG_CODE() {
        return ENREG_CODE;
    }

    public void setENREG_CODE(String ENREG_CODE) {
        this.ENREG_CODE = ENREG_CODE;
    }

    public String getCODE_BREVET() {
        return CODE_BREVET;
    }

    public void setCODE_BREVET(String CODE_BREVET) {
        this.CODE_BREVET = CODE_BREVET;
    }

    public int getPERS_CODE() {
        return PERS_CODE;
    }

    public void setPERS_CODE(int PERS_CODE) {
        this.PERS_CODE = PERS_CODE;
    }

    public int getPER_PERS_CODE() {
        return PER_PERS_CODE;
    }

    public void setPER_PERS_CODE(int PER_PERS_CODE) {
        this.PER_PERS_CODE = PER_PERS_CODE;
    }

    public int getREP_AN_LEGAL() {
        return REP_AN_LEGAL;
    }

    public void setREP_AN_LEGAL(int REP_AN_LEGAL) {
        this.REP_AN_LEGAL = REP_AN_LEGAL;
    }

    public String getREP_LEGAL() {
        return REP_LEGAL;
    }

    public void setREP_LEGAL(String REP_LEGAL) {
        this.REP_LEGAL = REP_LEGAL;
    }

    public Date getREP_DATE() {
        return REP_DATE;
    }

    public void setREP_DATE(Date REP_DATE) {
        this.REP_DATE = REP_DATE;
    }

    public String getREP_LIBEL() {
        return REP_LIBEL;
    }

    public void setREP_LIBEL(String REP_LIBEL) {
        this.REP_LIBEL = REP_LIBEL;
    }

    public Long getREP_MT_ENREG() {
        return REP_MT_ENREG;
    }

    public void setREP_MT_ENREG(Long REP_MT_ENREG) {
        this.REP_MT_ENREG = REP_MT_ENREG;
    }

    public Date getREP_DATE_ENREG() {
        return REP_DATE_ENREG;
    }

    public void setREP_DATE_ENREG(Date REP_DATE_ENREG) {
        this.REP_DATE_ENREG = REP_DATE_ENREG;
    }

    public int getTIMBRE_ACTE() {
        return TIMBRE_ACTE;
    }

    public void setTIMBRE_ACTE(int TIMBRE_ACTE) {
        this.TIMBRE_ACTE = TIMBRE_ACTE;
    }

    public Long getMONTANT_TIMBRE_ACTE() {
        return MONTANT_TIMBRE_ACTE;
    }

    public void setMONTANT_TIMBRE_ACTE(Long MONTANT_TIMBRE_ACTE) {
        this.MONTANT_TIMBRE_ACTE = MONTANT_TIMBRE_ACTE;
    }

    public int getNB_COP_AUTH() {
        return NB_COP_AUTH;
    }

    public void setNB_COP_AUTH(int NB_COP_AUTH) {
        this.NB_COP_AUTH = NB_COP_AUTH;
    }

    public int getNB_PAGE_COP_AUTH() {
        return NB_PAGE_COP_AUTH;
    }

    public void setNB_PAGE_COP_AUTH(int NB_PAGE_COP_AUTH) {
        this.NB_PAGE_COP_AUTH = NB_PAGE_COP_AUTH;
    }

    public Long getMT_COP_AUTH() {
        return MT_COP_AUTH;
    }

    public void setMT_COP_AUTH(Long MT_COP_AUTH) {
        this.MT_COP_AUTH = MT_COP_AUTH;
    }

    public int getNB_COP_EXEC() {
        return NB_COP_EXEC;
    }

    public void setNB_COP_EXEC(int NB_COP_EXEC) {
        this.NB_COP_EXEC = NB_COP_EXEC;
    }

    public int getNB_PAGE_COP_EXEC() {
        return NB_PAGE_COP_EXEC;
    }

    public void setNB_PAGE_COP_EXEC(int NB_PAGE_COP_EXEC) {
        this.NB_PAGE_COP_EXEC = NB_PAGE_COP_EXEC;
    }

    public Long getMT_COP_EXEC() {
        return MT_COP_EXEC;
    }

    public void setMT_COP_EXEC(Long MT_COP_EXEC) {
        this.MT_COP_EXEC = MT_COP_EXEC;
    }

    public Blob getREP_TEXTE() {
        return REP_TEXTE;
    }

    public void setREP_TEXTE(Blob REP_TEXTE) {
        this.REP_TEXTE = REP_TEXTE;
    }

    public String getSDOS_ETUDE() {
        return SDOS_ETUDE;
    }

    public void setSDOS_ETUDE(String SDOS_ETUDE) {
        this.SDOS_ETUDE = SDOS_ETUDE;
    }

    public int getREP_EDIT() {
        return REP_EDIT;
    }

    public void setREP_EDIT(int REP_EDIT) {
        this.REP_EDIT = REP_EDIT;
    }

    public int getREP_AN_LEGAL_DEFI() {
        return REP_AN_LEGAL_DEFI;
    }

    public void setREP_AN_LEGAL_DEFI(int REP_AN_LEGAL_DEFI) {
        this.REP_AN_LEGAL_DEFI = REP_AN_LEGAL_DEFI;
    }

    public int getREP_LEGAL_DEFI() {
        return REP_LEGAL_DEFI;
    }

    public void setREP_LEGAL_DEFI(int REP_LEGAL_DEFI) {
        this.REP_LEGAL_DEFI = REP_LEGAL_DEFI;
    }

    public int getREP_MANU() {
        return REP_MANU;
    }

    public void setREP_MANU(int REP_MANU) {
        this.REP_MANU = REP_MANU;
    }

    public String getREP_LIEU_SIGNATURE() {
        return REP_LIEU_SIGNATURE;
    }

    public void setREP_LIEU_SIGNATURE(String REP_LIEU_SIGNATURE) {
        this.REP_LIEU_SIGNATURE = REP_LIEU_SIGNATURE;
    }

    public int getREP_ENREG() {
        return REP_ENREG;
    }

    public void setREP_ENREG(int REP_ENREG) {
        this.REP_ENREG = REP_ENREG;
    }

    public String getREP_NUMENREG() {
        return REP_NUMENREG;
    }

    public void setREP_NUMENREG(String REP_NUMENREG) {
        this.REP_NUMENREG = REP_NUMENREG;
    }

    public int getREP_RECEUIL() {
        return REP_RECEUIL;
    }

    public void setREP_RECEUIL(int REP_RECEUIL) {
        this.REP_RECEUIL = REP_RECEUIL;
    }

    public String getREP_STAT() {
        return REP_STAT;
    }

    public void setREP_STAT(String REP_STAT) {
        this.REP_STAT = REP_STAT;
    }

    public int getREP_COMPLET() {
        return REP_COMPLET;
    }

    public void setREP_COMPLET(int REP_COMPLET) {
        this.REP_COMPLET = REP_COMPLET;
    }

    public int getREP_SUITE() {
        return REP_SUITE;
    }

    public void setREP_SUITE(int REP_SUITE) {
        this.REP_SUITE = REP_SUITE;
    }

    public Blob getREP_INTERVENANT() {
        return REP_INTERVENANT;
    }

    public void setREP_INTERVENANT(Blob REP_INTERVENANT) {
        this.REP_INTERVENANT = REP_INTERVENANT;
    }

    public String getREP_ESTREMI() {
        return REP_ESTREMI;
    }

    public void setREP_ESTREMI(String REP_ESTREMI) {
        this.REP_ESTREMI = REP_ESTREMI;
    }

    public int getREP_VALEUR() {
        return REP_VALEUR;
    }

    public void setREP_VALEUR(int REP_VALEUR) {
        this.REP_VALEUR = REP_VALEUR;
    }

    public String getREP_PS() {
        return REP_PS;
    }

    public void setREP_PS(String REP_PS) {
        this.REP_PS = REP_PS;
    }

    public String getSDOS_REF() {
        return SDOS_REF;
    }

    public void setSDOS_REF(String SDOS_REF) {
        this.SDOS_REF = SDOS_REF;
    }

    public String getSDOS_LIBEL() {
        return SDOS_LIBEL;
    }

    public void setSDOS_LIBEL(String SDOS_LIBEL) {
        this.SDOS_LIBEL = SDOS_LIBEL;
    }

    public String getTEXTE() {
        return TEXTE;
    }

    public void setTEXTE(String TEXTE) {
        this.TEXTE = TEXTE;
    }

    public Date getTIME_UPDATE() {
        return TIME_UPDATE;
    }

    public void setTIME_UPDATE(Date TIME_UPDATE) {
        this.TIME_UPDATE = TIME_UPDATE;
    }
}
