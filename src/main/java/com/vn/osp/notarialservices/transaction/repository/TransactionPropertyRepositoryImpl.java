package com.vn.osp.notarialservices.transaction.repository;

import com.vn.osp.notarialservices.transaction.domain.TransactionPropertyBo;
import com.vn.osp.notarialservices.transaction.dto.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by minh on 11/24/2016.
 */

public class TransactionPropertyRepositoryImpl implements TransactionPropertyRepositoryCustom {
    private static final Logger LOG = Logger.getLogger(TransactionPropertyRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired

    public TransactionPropertyRepositoryImpl(JpaContext context) {
        Assert.notNull(context, "JpaContext must not be null!");
    }

    public void findByOverridingMethod() {
        LOG.info("A method overriding a finder was invoked!");
    }

    @Override
    public void delete(Integer id) {
        LOG.info("A method overriding a finder was invoked!");
    }

    @Override
    public Optional<Boolean> addTransactionProperty(String data) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_transaction_property_add");
            storedProcedure
                    .setParameter("xml_transaction_property", data);
            storedProcedure.execute();
            return Optional.of(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Have an error in method addTransactionProperty:"+e.getMessage());
            return Optional.of(false);
        }
    }

    @Override
    @Transactional
    public Optional<Boolean> addTransactionPropertyNative(TransactionProperty transactionProperty) {
        try {
            /*Boolean aBoolean = entityManager.createQuery("INSERT INTO TransactionPropertyBo(synchronize_id,type,property_info,land_street,land_district,land_province," +
                    "land_full_of_area,transaction_content,notary_date,notary_office_name,contract_id,contract_number,contract_name,contract_kind,contract_value,relation_object,notary_person,notary_place," +
                    "notary_fee,remuneration_cost,notary_cost_total,note,contract_period,mortage_cancel_flag,mortage_cancel_date,mortage_cancel_note,cancel_status,cancel_description,bank_name,bank_code) ")
                    VALUES();*/
            /*entityManager.persist(list);*/
            String query = "INSERT INTO npo_transaction_property(synchronize_id,type,property_info,land_street,land_district,land_province,land_full_of_area,transaction_content,notary_date," +
                    "notary_office_name,contract_id,contract_number,contract_name,contract_kind,contract_value,relation_object,notary_person,notary_place,notary_fee,remuneration_cost,notary_cost_total," +
                    "note,contract_period,mortage_cancel_flag,mortage_cancel_date,mortage_cancel_note,cancel_status,cancel_description,bank_name,syn_status,bank_code,code_template,json_person,json_property)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Query query1 = entityManager.createNativeQuery(query);
            query1.setParameter(1, transactionProperty.getSynchronize_id());
            query1.setParameter(2, transactionProperty.getType());
            query1.setParameter(3, transactionProperty.getProperty_info());
            query1.setParameter(4, transactionProperty.getLand_street());
            query1.setParameter(5, transactionProperty.getLand_district());
            query1.setParameter(6, transactionProperty.getLand_province());
            query1.setParameter(7, transactionProperty.getLand_full_of_area());
            query1.setParameter(8, transactionProperty.getTransaction_content());
            query1.setParameter(9, convertStringToTimeStampFromSTP(transactionProperty.getNotary_date()));
            query1.setParameter(10, transactionProperty.getNotary_office_name());
            query1.setParameter(11, transactionProperty.getContract_id());
            query1.setParameter(12, transactionProperty.getContract_number());
            query1.setParameter(13, transactionProperty.getContract_name());
            query1.setParameter(14, transactionProperty.getContract_kind());
            query1.setParameter(15, transactionProperty.getContract_value());
            query1.setParameter(16, transactionProperty.getRelation_object());
            query1.setParameter(17, transactionProperty.getNotary_person());
            query1.setParameter(18, transactionProperty.getNotary_place());
            query1.setParameter(19, transactionProperty.getNotary_fee());
            query1.setParameter(20, transactionProperty.getRemuneration_cost());
            query1.setParameter(21, transactionProperty.getNotary_cost_total());
            query1.setParameter(22, transactionProperty.getNote());
            query1.setParameter(23, transactionProperty.getContract_period());
            query1.setParameter(24, transactionProperty.getMortage_cancel_flag());
            query1.setParameter(25, transactionProperty.getMortage_cancel_date());
            query1.setParameter(26, transactionProperty.getMortage_cancel_note());
            query1.setParameter(27, transactionProperty.getCancel_status());
            query1.setParameter(28, transactionProperty.getCancel_description());
            query1.setParameter(29, transactionProperty.getBank_name());
            query1.setParameter(30, 1);
            query1.setParameter(31, transactionProperty.getBank_code());
            query1.setParameter(32,transactionProperty.getCode_template());
            query1.setParameter(33,transactionProperty.getJson_person());
            query1.setParameter(34,transactionProperty.getJson_property());


            query1.executeUpdate();


            return Optional.of(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Have an error in method addTransactionProperty:"+e.getMessage());
            return Optional.of(false);
        }
    }

    @Override
    public Optional<Boolean> synchronizeOk(String notaryOfficeCode, Integer contract_kind, String contractNumber) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_transaction_synchronize_ok");
            storedProcedure
                    .setParameter("notaryOfficeCode", notaryOfficeCode);
            storedProcedure.setParameter("contract_kind", contract_kind);
            storedProcedure.setParameter("contractNumber", contractNumber);
            storedProcedure.execute();
            return Optional.of(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Have an error in method  synchronizeOk:"+e.getMessage());
            return Optional.of(false);
        }
    }

    @Override
    public Optional<List<TransactionPropertyBo>> findTransactionByFilter(String stringFilter) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("findTransactionByFilter");
            storedProcedure
                    .setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            List<TransactionPropertyBo> transactionPropertyBo = (List<TransactionPropertyBo>) storedProcedure.getResultList();
            return Optional.of(transactionPropertyBo);
        } catch (Exception e) {
            LOG.error("Have an error in method  findTransactionByFilter:"+e.getMessage());
            return Optional.of(new ArrayList<TransactionPropertyBo>());
        }
    }

    @Override
    public Optional<BigInteger> countTotalByFilter(String stringFilter) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("stp_npo_transaction_property_count_total_by_filter");
            storedProcedure.setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e){
            LOG.error("Have an error in method countTotalByFilter:"+e.getMessage());
            return Optional.of(result);
        }
    }
    @Override
    public Optional<List<TransactionPropertyBo>> findTransactionByBank(String bankname, String notaryDateFromFilter , String notaryDateToFilter, Integer numOffset, Integer numLimit) {
        try {

            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("findTransactionByBank");
            storedProcedure
                    .setParameter("bankname", bankname)
                    .setParameter("notaryDateFromFilter", notaryDateFromFilter)
                    .setParameter("notaryDateToFilter", notaryDateToFilter)
                    .setParameter("numOffset", numOffset)
                    .setParameter("numLimit", numLimit)
            ;
            storedProcedure.execute();
            List<TransactionPropertyBo> transactionPropertyBo = (List<TransactionPropertyBo>) storedProcedure.getResultList();
            return Optional.of(transactionPropertyBo);
        } catch (Exception e) {
            LOG.error("Have an error in method findTransactionByBank:"+e.getMessage());
            return Optional.of(new ArrayList<TransactionPropertyBo>());
        }
    }
    @Override
    public Optional<BigInteger> countTotalContractByBank(String bankName, String notaryDateFromFilter, String notaryDateToFilter) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("countTotalContractByBank");
            storedProcedure
                    .setParameter("bankname", bankName)
                    .setParameter("notaryDateFromFilter", notaryDateFromFilter)
                    .setParameter("notaryDateToFilter", notaryDateToFilter);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            LOG.error("Have an error in method countTotalContractByBank:"+e.getMessage());
            return Optional.of(result);
        }
    }
    @Override
    public List<NotaryName> selectNotary(String notaryOffice) {
        try {
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<NotaryName> result = new ArrayList<NotaryName>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("selectNotary");
            storedProcedure.setParameter("notaryOffice", notaryOffice);
            storedProcedure.execute();
            list = (ArrayList<String>)storedProcedure.getResultList();

            for(int i=0;i<list.size();i++)
            {
                result.add(new NotaryName(list.get(i)));
            }
            return result;
        } catch (Exception e){
           LOG.error("Have an error in method selectNotaty:"+e.getMessage());
            return new ArrayList<NotaryName>();
        }
    }
    @Override
    public List<TransactionNotaryOfficeName> selectVPCC() {
        try {
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<TransactionNotaryOfficeName> result = new ArrayList<TransactionNotaryOfficeName>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("selectVPCC");

            storedProcedure.execute();
            list = (ArrayList<String>)storedProcedure.getResultList();
            for(int i=0;i<list.size();i++)
            {
                result.add(new TransactionNotaryOfficeName(list.get(i),Long.valueOf(0)));
            }

            return result;
        } catch (Exception e){
           LOG.error("Have an error in method selectVPCC:"+e.getMessage());
            return new ArrayList<TransactionNotaryOfficeName>();
        }
    }
    @Override
    public Optional<List<TransactionPropertyBo>> selectReportByNotary(String notaryOfficeName,String notaryName, String notaryDateFromFilter , String notaryDateToFilter, Integer numOffset, Integer numLimit ) {
        try {

            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("selectReportByNotary");
            storedProcedure
                    .setParameter("notaryOfficeName", notaryOfficeName)
                    .setParameter("notaryName", notaryName)
                    .setParameter("notaryDateFromFilter", notaryDateFromFilter)
                    .setParameter("notaryDateToFilter", notaryDateToFilter)
                    .setParameter("numOffset", numOffset)
                    .setParameter("numLimit", numLimit)
            ;
            storedProcedure.execute();
            List<TransactionPropertyBo> transactionPropertyBo = (List<TransactionPropertyBo>) storedProcedure.getResultList();
            return Optional.of(transactionPropertyBo);
        } catch (Exception e) {
            LOG.error("Have an error in method selectReportByNotary:"+e.getMessage());
            return Optional.of(new ArrayList<TransactionPropertyBo>());
        }
    }
    @Override
    public Optional<BigInteger> countTotalReportByNotary(String notaryOfficeName,String notaryName , String notaryDateFromFilter, String notaryDateToFilter) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("countTotalReportByNotary");
            storedProcedure
                    .setParameter("notaryOfficeName", notaryOfficeName)
                    .setParameter("notaryName", notaryName)
                    .setParameter("notaryDateFromFilter", notaryDateFromFilter)
                    .setParameter("notaryDateToFilter", notaryDateToFilter);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            LOG.error("Have an error in method countTotalReportByNotary:"+e.getMessage());
            return Optional.of(result);
        }
    }
    /*Lay ten chuyen vien soan thao*/
    @Override
    public List<EntryUserName> selectEntryUserName(String notaryOffice) {
        try {
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<EntryUserName> result = new ArrayList<EntryUserName>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("selectEntryUserName");
            storedProcedure.setParameter("notaryOffice", notaryOffice);
            storedProcedure.execute();
            list = (ArrayList<String>)storedProcedure.getResultList();

            for(int i=0;i<list.size();i++)
            {
                result.add(new EntryUserName(list.get(i)));
            }
            return result;
        } catch (Exception e){
            LOG.error("Have an error in method selectEntryUserName:"+e.getMessage());
            return new ArrayList<EntryUserName>();
        }
    }
    @Override
    public Optional<List<TransactionPropertyBo>> selectReportByUser(String notaryOfficeName,String entryUserName, String notaryDateFromFilter , String notaryDateToFilter, Integer numOffset, Integer numLimit ) {
        try {

            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("selectReportByUser");
            storedProcedure
                    .setParameter("notaryOfficeName", notaryOfficeName)
                    .setParameter("entryUserName", entryUserName)
                    .setParameter("notaryDateFromFilter", notaryDateFromFilter)
                    .setParameter("notaryDateToFilter", notaryDateToFilter)
                    .setParameter("numOffset", numOffset)
                    .setParameter("numLimit", numLimit)
            ;
            storedProcedure.execute();
            List<TransactionPropertyBo> transactionPropertyBo = (List<TransactionPropertyBo>) storedProcedure.getResultList();
            return Optional.of(transactionPropertyBo);
        } catch (Exception e) {
            LOG.error("Have an error in method selectREportByUser:"+e.getMessage());
            return Optional.of(new ArrayList<TransactionPropertyBo>());
        }
    }

    @Override
    public Optional<BigInteger> countTotalReportByUser(String notaryOfficeName,String entryUserName , String notaryDateFromFilter, String notaryDateToFilter) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("countTotalReportByUser");
            storedProcedure
                    .setParameter("notaryOfficeName", notaryOfficeName)
                    .setParameter("entryUserName", entryUserName)
                    .setParameter("notaryDateFromFilter", notaryDateFromFilter)
                    .setParameter("notaryDateToFilter", notaryDateToFilter);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            LOG.error("Have an error in method countTotalreportByUser:"+e.getMessage());
            return Optional.of(result);
        }
    }
    @Override
    public Optional<List<TransactionPropertyBo>> selectContractCertify(String contractKind, String notaryDateFromFilter, String notaryDateToFilter ) {
        try {

            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("selectContractCertify");
            storedProcedure
                    .setParameter("contractKind", contractKind)
                    .setParameter("notaryDateFromFilter", notaryDateFromFilter)
                    .setParameter("notaryDateToFilter", notaryDateToFilter)
            ;
            storedProcedure.execute();
            List<TransactionPropertyBo> transactionPropertyBo = (List<TransactionPropertyBo>) storedProcedure.getResultList();
            return Optional.of(transactionPropertyBo);
        } catch (Exception e) {
            LOG.error("Have an error in method selecdtContracdtCertify:"+e.getMessage());
            return Optional.of(new ArrayList<TransactionPropertyBo>());
        }
    }

    @Override
    public Optional<List<TransactionPropertyBo>> getByContractId(String id) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("getByContractId");
            storedProcedure
                    .setParameter("id", id);
            storedProcedure.execute();
            List<TransactionPropertyBo> transactionPropertyBo = (List<TransactionPropertyBo>) storedProcedure.getResultList();
            return Optional.of(transactionPropertyBo);
        } catch (Exception e) {
            LOG.error("Have an error in method  getByContractId:"+e.getMessage());
            return Optional.of(new ArrayList<TransactionPropertyBo>());
        }
    }
    @Override
    public Optional<Boolean> updateSynchStatus(Long tpid,Long syn_status){
        try {
            StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("vpcc_npo_transaction_property_update_syn_status");
            storedProcedureQuery.setParameter("tpid", tpid);
            storedProcedureQuery.setParameter("syn_status", syn_status);

            storedProcedureQuery.execute();
            return Optional.of(true);
        } catch (Exception e) {
            LOG.error("Have an error in method updateSynchStatus:"+e.getMessage());
        }
        return Optional.of(false);
    }

    @Override
    public TransactionPropertyBo genInfo(TransactionPropertyBo item, TransactionPropertyBo itemResult) {
        itemResult.setSynchronize_id(item.getSynchronize_id());
        itemResult.setType(item.getType());
        itemResult.setProperty_info(item.getProperty_info());
        itemResult.setTransaction_content(item.getTransaction_content());
        itemResult.setNotary_date(item.getNotary_date());
        itemResult.setNotary_office_name(item.getNotary_office_name());
        itemResult.setContract_id(item.getContract_id());
        itemResult.setContract_number(item.getContract_number());
        itemResult.setContract_name(item.getContract_name());
        itemResult.setContract_kind(item.getContract_kind());
        itemResult.setContract_value(item.getContract_value());
        itemResult.setRelation_object(item.getRelation_object());
        itemResult.setNotary_person(item.getNotary_person());
        itemResult.setNotary_place(item.getNotary_place());
        itemResult.setNotary_fee(item.getNotary_fee());
        itemResult.setNote(item.getNote());
        itemResult.setContract_period(item.getContract_period());
        itemResult.setMortage_cancel_flag(item.getMortage_cancel_flag());
        itemResult.setMortage_cancel_date(item.getMortage_cancel_date());
        itemResult.setMortage_cancel_note(item.getMortage_cancel_note());
        itemResult.setCancel_status(item.getCancel_status());
        itemResult.setCancel_description(item.getCancel_description());
        itemResult.setUpdate_user_id(item.getUpdate_user_id());
        itemResult.setUpdate_user_name(item.getUpdate_user_name());
        itemResult.setUpdate_date_time(item.getUpdate_date_time());
        itemResult.setBank_code(item.getBank_code());
        itemResult.setBank_name(item.getBank_name());
        itemResult.setSyn_status(item.getSyn_status());
        itemResult.setJson_property(item.getJson_property());
        itemResult.setJson_person(item.getJson_person());

        return itemResult;
    }
    @Override
    public List<ReportByBank> reportByBank(String stringFilter) {
        try {
            ArrayList<ReportByBank> reportByBanks = new ArrayList<ReportByBank>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_report_by_bank");

            storedProcedure.setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ReportByBank reportByBank = new ReportByBank();

                reportByBank.setTpid(record[0] == null ? null : ((Integer) record[0]).intValue());
                reportByBank.setSynchronize_id(record[1] == null ? null : ((String) record[1]));
                reportByBank.setType(record[2] == null ? null : (String) record[2]);
                reportByBank.setProperty_info(record[3] == null ? null : (String) record[3]);
                reportByBank.setLand_street(record[4] == null ? null :(String) record[4]);
                reportByBank.setLand_district(record[5] == null ? null : (String) record[5]);
                reportByBank.setLand_province(record[6] == null ? null : (String) record[6]);
                reportByBank.setLand_full_of_area(record[7] == null ? null : (String) record[7]);
                reportByBank.setTransaction_content(record[8] == null ? null : (String) record[8]);
                reportByBank.setNotary_date(convertTimeStampToString(record[9] == null ? null : (Timestamp) record[9]));
                reportByBank.setNotary_office_name(record[10] == null ? null : (String) record[10]);
                reportByBank.setContract_id(record[11] == null ? null : ((Integer) record[11]).intValue());
                reportByBank.setContract_number(record[12] == null ? null : (String) record[12]);
                reportByBank.setContract_name(record[13] == null ? null : (String) record[13]);
                reportByBank.setContract_kind(record[14] == null ? null : (String) record[14]);
                reportByBank.setCode_template(record[15] == null ? null : ((Integer) record[15]).intValue());
                reportByBank.setContract_value(record[16] == null ? null : (String) record[16]);
                reportByBank.setRelation_object(record[17] == null ? null : (String) record[17]);
                reportByBank.setNotary_person(record[18] == null ? null : (String) record[18]);
                reportByBank.setNotary_place(record[19] == null ? null : (String) record[19]);
                reportByBank.setNotary_fee(record[20] == null ? null : (BigInteger) record[20]);
                reportByBank.setRemuneration_cost(record[21] == null ? null : (BigInteger) record[21]);
                reportByBank.setNotary_cost_total(record[22] == null ? null : (BigInteger) record[22]);

                reportByBank.setNote(record[23] == null ? null : (String) record[23]);
                reportByBank.setContract_period(record[24] == null ? null : (String) record[24]);
                reportByBank.setMortage_cancel_flag(record[25] == null ? null : ((Boolean) record[25]).booleanValue());
                reportByBank.setMortage_cancel_date(record[26] == null ? null : (String) record[26]);
                reportByBank.setMortage_cancel_note(record[27] == null ? null : (String) record[27]);
                reportByBank.setCancel_status(record[28] == null ? null : ((Boolean) record[28]).booleanValue());
                reportByBank.setCancel_description(record[29] == null ? null : (String) record[29]);
                reportByBank.setEntry_user_id(record[30] == null ? null : ((Integer) record[30]).intValue());
                reportByBank.setEntry_user_name(record[31] == null ? null : (String) record[31]);
                reportByBank.setEntry_date_time(convertTimeStampToString(record[32] == null ? null : (Timestamp) record[32]));
                reportByBank.setUpdate_user_id(record[33] == null ? null : ((Integer) record[33]).intValue());
                reportByBank.setUpdate_user_name(record[34] == null ? null : (String) record[34]);
                reportByBank.setUpdate_date_time(convertTimeStampToString(record[35] == null ? null : (Timestamp) record[35]));
                reportByBank.setBank_id(record[36] == null ? null : ((Integer) record[36]).intValue());
                reportByBank.setBank_name(record[37] == null ? null : (String) record[37]);
                reportByBank.setSyn_status(record[38] == null ? null : ((Integer) record[38]).intValue());
                reportByBank.setBank_code(record[39] == null ? null : (String) record[39]);
                reportByBank.setJson_person(record[40] == null ? null : (String) record[40]);
                reportByBank.setJson_property(record[41] == null ? null : (String) record[41]);
                reportByBank.setName(record[42] == null ? null : (String) record[42]);
                reportByBank.setCode(record[43] == null ? null : (String) record[43]);

                reportByBanks.add(reportByBank);
            });
            return reportByBanks;
        } catch (Exception e) {
            LOG.error("Lỗi tại hàm TransactionPropertyRepositoryImpl.reportByBank(), query = "+stringFilter);
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new ArrayList<ReportByBank>();
        }
    }


    public Timestamp convertStringToTimeStampSpecial(String dateString){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = null;
            parsedDate = dateFormat.parse(dateString);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return  timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Timestamp convertStringToTimeStampFromSTP(String dateString){
        try {
            /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
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
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String result = dateFormat.format(date);
            return result;
        }catch (Exception e){
            return "";
        }
    }
    @Override
    public Optional<BigInteger> countTotalReportBank(String stringFilter) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_report_bank_count_total");
            storedProcedure.setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e){
            LOG.error("Have an error in method countTotalReportBank:"+e.getMessage());
            return Optional.of(result);
        }
    }
}