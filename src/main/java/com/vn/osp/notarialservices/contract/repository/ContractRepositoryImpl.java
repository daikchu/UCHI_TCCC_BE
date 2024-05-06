package com.vn.osp.notarialservices.contract.repository;

import com.vn.osp.notarialservices.contract.domain.*;
import com.vn.osp.notarialservices.contract.dto.ReportByNotaryPerson;
import com.vn.osp.notarialservices.contract.dto.ReportByUser;
import com.vn.osp.notarialservices.contract.dto.*;

import com.vn.osp.notarialservices.masterConvert.domain.ContractPropertyBO;
import com.vn.osp.notarialservices.masterConvert.domain.PropertyBo;
import com.vn.osp.notarialservices.systemmanager.service.AccessHistoryService;
import com.vn.osp.notarialservices.transaction.domain.TransactionPropertyBo;
import com.vn.osp.notarialservices.utils.Constants;
import com.vn.osp.notarialservices.utils.SystemProperties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by TienManh on 5/11/2017.
 */
public class ContractRepositoryImpl implements ContractRepositoryCustom {
    private static final Logger LOG = Logger.getLogger(ContractRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public ContractRepositoryImpl(JpaContext context) {
        Assert.notNull(context, "JpaContext must not be null!");
    }

    @Override
    public Optional<List<ContractBO>> getAllContract() {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("stp_npo_contact_select_all");
            storedProcedure.execute();
            List<ContractBO> result = storedProcedure.getResultList();
            return Optional.of((List<ContractBO>) (result));
        } catch (Exception e) {
            LOG.error("Have an error in method getAllContract:" + e.getMessage());
            return Optional.of(new ArrayList<ContractBO>());
        }

    }

    @Override
    public Optional<List<ContractBO>> findLatestContract(Long countNumber) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_select_lastest_count");
            storedProcedure
                    .setParameter("countNumber", countNumber);
            storedProcedure.execute();
            List<ContractBO> contractBo = (List<ContractBO>) storedProcedure.getResultList();
            return Optional.of(contractBo);
        } catch (Exception e) {
            LOG.error("Have an error in method  findLatestContract:" + e.getMessage());
            return Optional.of(new ArrayList<ContractBO>());
        }
    }

    @Override
    public Optional<Integer> addContractWrapper(String xml_contract, String xml_property) {
        Integer result = Integer.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("stp_npo_contract_property_add");
            storedProcedure
                    .setParameter("xml_contract", xml_contract)
                    .setParameter("xml_property", xml_property);
            storedProcedure.execute();
            result = (Integer) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            LOG.error("Have an error in method addContractWrapper:" + e.getMessage());
            return Optional.of(result);
        }
    }

    @Override
    public Optional<Integer> addContract(ContractBO contract, TransactionPropertyBo trans, ContractHistoryInfoBO his) {
        Long result = Long.valueOf(0);
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            //update contract number plus 1 for tccc hoac phuong xa
            String key = "";
            Calendar ca = Calendar.getInstance();
            ca.setTimeInMillis(trans.getNotary_date().getTime());
            int year = Calendar.getInstance().get(Calendar.YEAR);
            if (SystemProperties.getProperty("org_type").equals("1")) {
                if (trans.getEntry_user_id() != null) key = year + "/" + trans.getEntry_user_id();
            } else {
                key = String.valueOf(year);
            }
            ContractNumberBO contractNumberBO = entityManagerCurrent.find(ContractNumberBO.class, key);
            if (contractNumberBO == null) {
                contractNumberBO = new ContractNumberBO();
                contractNumberBO.setKind_id(key);
                contractNumberBO.setContract_number(2);
                entityManagerCurrent.persist(contractNumberBO);
                entityManagerCurrent.flush();
            } else {
                contractNumberBO.setContract_number(contractNumberBO.getContract_number() + 1);
                entityManagerCurrent.merge(contractNumberBO);
            }

            contract.setId(null);
            entityManagerCurrent.persist(contract);
            entityManagerCurrent.flush();
            his.setContract_id(contract.getId().intValue());
            trans.setContract_id(contract.getId());
            entityManagerCurrent.persist(his);
            entityManagerCurrent.persist(trans);
            entityManagerCurrent.flush();
            result = trans.getTpid();
            /*if(SystemProperties.getProperty("status.synchronize").equals("1")){
                String getTrans = "select trans from TransactionPropertyBo trans where trans.synchronize_id=:synchronize_id and trans.contract_number=:contract_number";
                List<TransactionPropertyBo> transactionPropertyBoList = entityManagerCurrent.createQuery(getTrans).setParameter("synchronize_id",trans.getSynchronize_id()).setParameter("contract_number",trans.getContract_number()).getResultList();
                String synchronizeID = transactionPropertyBoList.get(0).getSynchronize_id();
                Long idtp = transactionPropertyBoList.get(0).getId();
                transactionPropertyBoList.get(0).setSynchronize_id(synchronizeID+idtp);
                entityManagerCurrent.merge(transactionPropertyBoList.get(0));
                entityManagerCurrent.flush();


            }*/

            entityManagerCurrent.getTransaction().commit();
        } catch (Exception e) {
            LOG.info("Have an error in method  ContractRespositoryImpl.addContract:" + e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
        } finally {
            entityManagerCurrent.close();
        }
        return Optional.ofNullable(result.intValue());
    }
    @Override
    public Long addContractFromBackUpTransaction(ContractBO contractBO, PropertyBo propertyBo){
        Long result = Long.valueOf(0);
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try{
            entityManagerCurrent.getTransaction().begin();
            entityManagerCurrent.persist(contractBO);
            result = contractBO.getId();

            if(propertyBo != null){
                entityManagerCurrent.persist(propertyBo);
                ContractPropertyBO contractPropertyBO = new ContractPropertyBO(contractBO.getId(), propertyBo.getId());
                entityManagerCurrent.persist(contractPropertyBO);
            }
            entityManagerCurrent.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            LOG.info("Have an error in method  ContractRespositoryImpl.addContractFromBackUpTransaction:" + e.getMessage());
            return Long.valueOf(0);
        }finally {
            entityManagerCurrent.close();
        }
        return result;
    }

    @Override
    public Optional<Integer> editContract(ContractBO contract, TransactionPropertyBo trans, ContractHistoryInfoBO his) {
        Long result = Long.valueOf(0);
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            ContractBO itemContract = entityManagerCurrent.find(ContractBO.class, contract.getId());
            TransactionPropertyBo itemTrans = entityManagerCurrent.find(TransactionPropertyBo.class, trans.getId());
            if (itemContract != null && itemTrans != null) {
                itemContract = genInfo(contract, itemContract);
                entityManagerCurrent.merge(itemContract);
                his.setContract_id(contract.getId().intValue());
                trans.setContract_id(contract.getId());
                entityManagerCurrent.persist(his);
                trans.setSyn_status(0);
                itemTrans = genInfoTrans(trans, itemTrans);
                entityManagerCurrent.merge(itemTrans);
                entityManagerCurrent.flush();
                result = trans.getTpid();
            }

            entityManagerCurrent.getTransaction().commit();
        } catch (Exception e) {
            LOG.info("Have an error in method  ContractRespositoryImpl.editContract:" + e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
        } finally {
            entityManagerCurrent.close();
        }
        return Optional.ofNullable(result.intValue());
    }

    @Override
    public Optional<Integer> cancelContract(long contract_cancel_id, ContractBO contract, TransactionPropertyBo trans, ContractHistoryInfoBO his) {
        Long result = Long.valueOf(0);
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            ContractBO itemContractCacel = entityManagerCurrent.find(ContractBO.class, contract_cancel_id);
            List<TransactionPropertyBo> list = entityManagerCurrent.createQuery("select tran from TransactionPropertyBo tran where tran.contract_id = :contract_id",
                    TransactionPropertyBo.class).setParameter("contract_id", contract_cancel_id).getResultList();

            //update contract number plus 1 for tccc hoac phuong xa
            String key = "";
            Calendar ca = Calendar.getInstance();
            ca.setTimeInMillis(trans.getNotary_date().getTime());
            int year = Calendar.getInstance().get(Calendar.YEAR);
            if (SystemProperties.getProperty("org_type").equals("1")) {
                if (trans.getEntry_user_id() != null) key = year + "/" + trans.getEntry_user_id();
            } else {
                key = String.valueOf(year);
            }
            ContractNumberBO contractNumberBO = entityManagerCurrent.find(ContractNumberBO.class, key);
            if (contractNumberBO == null) {
                contractNumberBO = new ContractNumberBO();
                contractNumberBO.setKind_id(key);
                contractNumberBO.setContract_number(2);
                entityManagerCurrent.persist(contractNumberBO);
                entityManagerCurrent.flush();
            } else {
                contractNumberBO.setContract_number(contractNumberBO.getContract_number() + 1);
                entityManagerCurrent.merge(contractNumberBO);
            }

            //update & add contract
            if (list != null && list.size() > 0 && itemContractCacel != null) {
                TransactionPropertyBo tran_cancel = list.get(0);

                entityManagerCurrent.persist(contract);
                entityManagerCurrent.flush();
                his.setContract_id(contract.getId().intValue());
                trans.setContract_id(contract.getId());
                entityManagerCurrent.persist(his);
                entityManagerCurrent.persist(trans);
                entityManagerCurrent.flush();

                itemContractCacel.setCancel_status(Long.valueOf(1));
                itemContractCacel.setCancel_description("Đã hủy bởi hợp đồng số " + contract.getContract_number());
                itemContractCacel.setCancel_relation_contract_id(contract.getId());
                itemContractCacel.setUpdate_user_id(contract.getUpdate_user_id());
                itemContractCacel.setUpdate_date_time(new Timestamp(new Date().getTime()));
                itemContractCacel.setUpdate_user_name(contract.getUpdate_user_name());
                entityManagerCurrent.merge(itemContractCacel);

                tran_cancel.setCancel_status(Long.valueOf(1));
                tran_cancel.setCancel_description("Đã hủy bởi hợp đồng số " + contract.getContract_number());
                tran_cancel.setUpdate_user_id(trans.getUpdate_user_id());
                tran_cancel.setUpdate_date_time(new Timestamp(new Date().getTime()));
                tran_cancel.setUpdate_user_name(trans.getUpdate_user_name());
                tran_cancel.setSyn_status(0);
                entityManagerCurrent.merge(tran_cancel);

                entityManagerCurrent.flush();
                result = trans.getTpid();
            }

            entityManagerCurrent.getTransaction().commit();
        } catch (Exception e) {
            LOG.info("Have an error in method  ContractRespositoryImpl.cancelContract::" + e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
        } finally {
            entityManagerCurrent.close();
        }
        return Optional.ofNullable(result.intValue());
    }


    @Override
    public Boolean deleteContract(String id, String xml_contract_history) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_delete");
            storedProcedure
                    .setParameter("id", id)
                    .setParameter("xml_contract_history", xml_contract_history);
            storedProcedure.execute();
            return true;
        } catch (Exception e) {
            LOG.error("Have an error in method  contractRepositoryImpl.deleteContract():" + e.getMessage());
            return false;
        }

    }


    @Override
    public Optional<List<ContractKindBO>> selectContractKindByFilter(String stringFilter) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("stp_npo_contract_kind_select");
            storedProcedure
                    .setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            List<ContractKindBO> contractKindBOS = (List<ContractKindBO>) storedProcedure.getResultList();
            return Optional.of(contractKindBOS);
        } catch (Exception e) {
            LOG.error("Have an error in method  selectContractKindByFilter:" + e.getMessage());
            return Optional.of(new ArrayList<ContractKindBO>());
        }
    }

    @Override
    public Optional<BigInteger> countByContractNumber(String contract_number) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_contract_number");
            storedProcedure
                    .setParameter("contract_number", contract_number);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            LOG.error("Have an error in method countByConractNumber:" + e.getMessage());
            return Optional.of(result);
        }
    }

    @Override
    public Optional<Long> countContractNumber(String contractNumber) {
        Long value = 0L;
        try {
            Query query = entityManager.createNativeQuery("select count(c.id) from npo_contract c where c.contract_number =:contractNumber");
            query.setParameter("contractNumber", contractNumber);
            int count = ((BigInteger) query.getSingleResult()).intValue();

            value = Long.valueOf(count);
//            value=entityManager.createQuery("select count(id) from Contract c where c.contract_number =:contractNumber",
//                    Long.class).setParameter("contractNumber", contractNumber).getSingleResult().longValue();
        } catch (Exception e) {
            LOG.error("Have an error in method countContractNumber:" + e.getMessage());
        }
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<List<ContractTemplateBO>> selectContractTeamplateByFilter(String stringFilter) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("stp_npo_contract_template_select");
            storedProcedure
                    .setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            List<ContractTemplateBO> contractTemplateBOS = (List<ContractTemplateBO>) storedProcedure.getResultList();
            return Optional.of(contractTemplateBOS);
        } catch (Exception e) {
            LOG.error("Have an error in method selectContractTeamplateByFilter:" + e.getMessage());
            return Optional.of(new ArrayList<ContractTemplateBO>());
        }
    }

    @Override
    public Optional<BigInteger> countHistoryContract(String stringFilter) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("stp_npo_contract_history_count_total")
                    .setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            LOG.error("Have an error in method countHistoryContract:" + e.getMessage());
            return Optional.of(result);
        }
    }

    @Override
    public List<ContractHistory> selectByFilter(String stringFilter) {
        try {
            ArrayList<ContractHistory> contractHistoryInfors = new ArrayList<ContractHistory>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("stp_npo_contract_history_select_by_filter");

            storedProcedure.setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ContractHistory contractHistoryInfor = new ContractHistory();
                contractHistoryInfor.setHid(record[0] == null ? null : ((Integer) record[0]).intValue());
                contractHistoryInfor.setContract_id(record[1] == null ? null : ((Integer) record[1]).intValue());
                contractHistoryInfor.setContract_number(record[2] == null ? null : (String) record[2]);
                contractHistoryInfor.setClient_info(record[3] == null ? null : (String) record[3]);
                contractHistoryInfor.setExecute_date_time(convertTimeStampToString(record[4] == null ? null : (Timestamp) record[4]));
                contractHistoryInfor.setExecute_person(record[5] == null ? null : (String) record[5]);
                contractHistoryInfor.setExecute_content(record[6] == null ? null : (String) record[6]);
                contractHistoryInfor.setContract_content(record[7] == null ? null : (String) record[7]);
                contractHistoryInfor.setFamily_name(record[8] == null ? null : (String) record[8]);
                contractHistoryInfor.setFirst_name(record[9] == null ? null : (String) record[9]);
                contractHistoryInfors.add(contractHistoryInfor);
            });
            return contractHistoryInfors;
        } catch (Exception e) {
            LOG.error("Have an error in method selectByFilter:" + e.getMessage());
            return new ArrayList<ContractHistory>();
        }
    }

    @Override
    public List<ReportByNotaryPerson> getReportByNotary(String stringFitler) {
        try {
            ArrayList<ReportByNotaryPerson> reportByNotaries = new ArrayList<ReportByNotaryPerson>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_report_by_notary");
            storedProcedure.setParameter("stringFilter", stringFitler);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ReportByNotaryPerson reportByNotaryPerson = new ReportByNotaryPerson();

                reportByNotaryPerson.setId(record[0] == null ? null : ((Integer) record[0]).intValue());
                reportByNotaryPerson.setContract_number(record[1] == null ? null : ((String) record[1]));
                reportByNotaryPerson.setContract_name(record[2] == null ? null : ((String) record[2]));
                reportByNotaryPerson.setRelate_object_A_display(record[3] == null ? null : ((String) record[3]));
                reportByNotaryPerson.setRelate_object_B_display(record[4] == null ? null : ((String) record[4]));
                reportByNotaryPerson.setRelate_object_C_display(record[5] == null ? null : ((String) record[5]));
                reportByNotaryPerson.setRelation_object_A(record[6] == null ? null : ((String) record[6]));
                reportByNotaryPerson.setRelation_object_B(record[7] == null ? null : ((String) record[7]));
                reportByNotaryPerson.setRelation_object_C(record[8] == null ? null : ((String) record[8]));
                String notaryDate = convertTimeStampToString(record[9] == null ? null : (Timestamp) record[9]);
                reportByNotaryPerson.setNotary_date(notaryDate == null ? "" : notaryDate.substring(0, 10).replaceAll("-", "/"));
                //reportByNotaryPerson.setNotary_date(record[9] == null ? null : ((Timestamp) record[9]));
                reportByNotaryPerson.setNotary_id(record[10] == null ? null : ((Integer) record[10]).intValue());
                reportByNotaryPerson.setFamily_name(record[11] == null ? null : ((String) record[11]));
                reportByNotaryPerson.setFirst_name(record[12] == null ? null : ((String) record[12]));
                reportByNotaryPerson.setSummary(record[13] == null ? null : ((String) record[13]));

                reportByNotaries.add(reportByNotaryPerson);
            });
            return reportByNotaries;
        } catch (Exception e) {
            LOG.error("Have an error in method getReportByNotary:" + e.getMessage());
            return new ArrayList<ReportByNotaryPerson>();
        }
    }
    @Override
    public List<ReportByNotaryPerson> getReportByDraft(String stringFitler) {
        try {
            ArrayList<ReportByNotaryPerson> reportByNotaries = new ArrayList<ReportByNotaryPerson>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_report_by_draft");
            storedProcedure.setParameter("stringFilter", stringFitler);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ReportByNotaryPerson reportByNotaryPerson = new ReportByNotaryPerson();

                reportByNotaryPerson.setId(record[0] == null ? null : ((Integer) record[0]).intValue());
                reportByNotaryPerson.setContract_number(record[1] == null ? null : ((String) record[1]));
                reportByNotaryPerson.setContract_name(record[2] == null ? null : ((String) record[2]));
                reportByNotaryPerson.setRelate_object_A_display(record[3] == null ? null : ((String) record[3]));
                reportByNotaryPerson.setRelate_object_B_display(record[4] == null ? null : ((String) record[4]));
                reportByNotaryPerson.setRelate_object_C_display(record[5] == null ? null : ((String) record[5]));
                reportByNotaryPerson.setRelation_object_A(record[6] == null ? null : ((String) record[6]));
                reportByNotaryPerson.setRelation_object_B(record[7] == null ? null : ((String) record[7]));
                reportByNotaryPerson.setRelation_object_C(record[8] == null ? null : ((String) record[8]));
                String notaryDate = convertTimeStampToString(record[9] == null ? null : (Timestamp) record[9]);
                reportByNotaryPerson.setNotary_date(notaryDate == null ? "" : notaryDate.substring(0, 10).replaceAll("-", "/"));
                //reportByNotaryPerson.setNotary_date(record[9] == null ? null : ((Timestamp) record[9]));
                reportByNotaryPerson.setNotary_id(record[10] == null ? null : ((Integer) record[10]).intValue());
                reportByNotaryPerson.setFamily_name(record[11] == null ? null : ((String) record[11]));
                reportByNotaryPerson.setFirst_name(record[12] == null ? null : ((String) record[12]));
                reportByNotaryPerson.setSummary(record[13] == null ? null : ((String) record[13]));

                reportByNotaries.add(reportByNotaryPerson);
            });
            return reportByNotaries;
        } catch (Exception e) {
            LOG.error("Have an error in method getReportByNotary:" + e.getMessage());
            return new ArrayList<ReportByNotaryPerson>();
        }
    }

    @Override
    public Optional<BigInteger> countTotalReportByNotary(String stringFilter) {
        BigInteger result = BigInteger.valueOf(0);
        stringFilter += " LIMIT 0, 20";
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_report_by_notary_count_total");
            storedProcedure.setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            LOG.error("Have an error in method countTotalReportByNotary:" + e.getMessage());
            return Optional.of(result);
        }
    }
    @Override
    public Optional<BigInteger> countTotalReportByDraft(String stringFilter) {
        BigInteger result = BigInteger.valueOf(0);
        stringFilter += " LIMIT 0, 20";
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_report_by_draft_count_total");
            storedProcedure.setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            LOG.error("Have an error in method countTotalReportByNotary:" + e.getMessage());
            return Optional.of(result);
        }
    }

    @Override
    public List<ReportByUser> getReportByUser(String stringFitler) {
        try {
            ArrayList<ReportByUser> reportByUsers = new ArrayList<ReportByUser>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_report_by_user");

            storedProcedure.setParameter("stringFilter", stringFitler);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ReportByUser reportByUser = new ReportByUser();

                reportByUser.setId(record[0] == null ? null : ((Integer) record[0]).intValue());
                reportByUser.setContract_number(record[1] == null ? null : ((String) record[1]));
                reportByUser.setContract_name(record[2] == null ? null : ((String) record[2]));
                reportByUser.setRelate_object_A_display(record[3] == null ? null : ((String) record[3]));
                reportByUser.setRelate_object_B_display(record[4] == null ? null : ((String) record[4]));
                reportByUser.setRelate_object_C_display(record[5] == null ? null : ((String) record[5]));
                reportByUser.setRelation_object_A(record[6] == null ? null : ((String) record[6]));
                reportByUser.setRelation_object_B(record[7] == null ? null : ((String) record[7]));
                reportByUser.setRelation_object_C(record[8] == null ? null : ((String) record[8]));
                reportByUser.setNotary_date(record[9] == null ? null : ((Timestamp) record[9]));
                reportByUser.setFamily_name(record[10] == null ? null : ((String) record[10]));
                reportByUser.setFirst_name(record[11] == null ? null : ((String) record[11]));
                reportByUser.setDrafter_id(record[12] == null ? null : ((Integer) record[12]).intValue());
                reportByUser.setDrafter_family_name(record[13] == null ? null : ((String) record[13]));
                reportByUser.setDrafter_first_name(record[14] == null ? null : ((String) record[14]));
                reportByUser.setSummary(record[15] == null ? null : ((String) record[15]));

                reportByUsers.add(reportByUser);
            });
            return reportByUsers;
        } catch (Exception e) {
            LOG.error("Have an error in method getReportByUser:" + e.getMessage());
            return new ArrayList<ReportByUser>();
        }
    }

    @Override
    public Optional<BigInteger> countTotalReportByUser(String stringFilter) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_report_by_user_count_total");
            storedProcedure.setParameter("stringFilter", stringFilter);
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            LOG.error("Have an error in method countTotalReportByUser:" + e.getMessage());
            return Optional.of(result);
        }
    }

    @Override
    public List<ReportByGroupTotal> selectReportByGroupTotal(ReportByGroupTotalList reportByGroupTotalList, String filter) {
        try {
            ArrayList<ReportByGroupTotal> reportByGroupTotals = new ArrayList<ReportByGroupTotal>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("stp_npo_select_report_by_group_total");
            storedProcedure.setParameter("stringFilter", filter);
            storedProcedure.setParameter("notaryDateFromFilter", reportByGroupTotalList.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByGroupTotalList.getNotaryDateToFilter());
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ReportByGroupTotal reportByGroupTotal = new ReportByGroupTotal();

                reportByGroupTotal.setKind_id(record[0] == null ? null : (String) record[0]);
                reportByGroupTotal.setKind_name(record[1] == null ? null : (String) record[1]);
                reportByGroupTotal.setTemplate_name(record[2] == null ? null : (String) record[2]);
                reportByGroupTotal.setTemplate_number(record[3] == null ? null : (BigInteger) record[3]);
                reportByGroupTotal.setCode_template(record[4] == null ? null : (Integer) record[4]);
                reportByGroupTotals.add(reportByGroupTotal);
            });
            return reportByGroupTotals;
        } catch (Exception e) {
            LOG.error("Have an error in method selectReportByGroupTotal:" + e.getMessage());
            return new ArrayList<ReportByGroupTotal>();
        }
    }

    @Override
    public BigInteger numberOfNotaryPerson(ReportByTT20List reportByTT20List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_notary");
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT20List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT20List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOgNotaryPerson:" + e.getMessage());
            return result;
        }

    }


    @Override
    public BigInteger numberOfContractLand(ReportByTT20List reportByTT20List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");

            storedProcedure.setParameter("kind_code", Constants.LCV_QSDD_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT20List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT20List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOgContractLand:" + e.getMessage());
            return result;
        }
    }

    @Override
    public BigInteger numberOfContractOther(ReportByTT20List reportByTT20List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_TAI_SAN_KHAC_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT20List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT20List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfContractOther:" + e.getMessage());
            return result;
        }
    }

    @Override
    public BigInteger numberOfContractDanSu(ReportByTT20List reportByTT20List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_THUC_HIEN_NGHIA_VU_DAN_SU_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT20List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT20List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfContractDanSu:" + e.getMessage());
            return result;
        }
    }

    @Override
    public BigInteger numberOfThuaKe(ReportByTT20List reportByTT20List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_GIAO_DICH_THUA_KE_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT20List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT20List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method  numberOFThuaKe:" + e.getMessage());
            return result;
        }
    }

    @Override
    public BigInteger numberOfOther(ReportByTT20List reportByTT20List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_LOAI_CONG_VIEC_KHAC_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT20List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT20List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfOther:" + e.getMessage());
            return result;
        }
    }

    @Override
    public BigDecimal tongPhiCongChung(ReportByTT20List reportByTT20List) {
        BigDecimal result = BigDecimal.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_cost_total_tt20");
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT20List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT20List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigDecimal) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method tongPhiCongChung:" + e.getMessage());
            return result;
        }
    }



    @Override
    public List<ContractError> getReportContractError(String stringFitler) {
        try {
            ArrayList<ContractError> reportContractErrors = new ArrayList<ContractError>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_error");

            storedProcedure.setParameter("stringFilter", stringFitler);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ContractError reportContractError = new ContractError();

                reportContractError.setId(record[0] == null ? null : ((Integer) record[0]).intValue());
                reportContractError.setContract_number(record[1] == null ? null : ((String) record[1]));
                reportContractError.setContract_name(record[2] == null ? null : ((String) record[2]));
                reportContractError.setRelate_object_A_display(record[3] == null ? null : ((String) record[3]));
                reportContractError.setRelate_object_B_display(record[4] == null ? null : ((String) record[4]));
                reportContractError.setRelate_object_C_display(record[5] == null ? null : ((String) record[5]));
                reportContractError.setRelation_object_A(record[6] == null ? null : ((String) record[6]));
                reportContractError.setRelation_object_B(record[7] == null ? null : ((String) record[7]));
                reportContractError.setRelation_object_C(record[8] == null ? null : ((String) record[8]));
                reportContractError.setNotary_date(record[9] == null ? null : ((Timestamp) record[9]));
                reportContractError.setNotary_id(record[10] == null ? null : ((Integer) record[10]).intValue());
                reportContractError.setFamily_name(record[11] == null ? null : ((String) record[11]));
                reportContractError.setFirst_name(record[12] == null ? null : ((String) record[12]));
                reportContractError.setError_description(record[13] == null ? null : ((String) record[13]));

                reportContractErrors.add(reportContractError);
            });
            return reportContractErrors;
        } catch (Exception e) {
            LOG.error("Have an error in method getReportContractError:" + e.getMessage());
            return new ArrayList<ContractError>();
        }
    }

    @Override
    public Optional<ContractBO> getContractById(String id) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_select_by_id")
                    .setParameter("id", id);
            storedProcedure.execute();
            return Optional.ofNullable((ContractBO) storedProcedure.getSingleResult());
        } catch (Exception e) {
            LOG.error("Have an error in method getContractById:" + e.getMessage());
            return Optional.of(new ContractBO());
        }
    }

    @Override
    public Optional<ContractTemplateBO> getContractTemplateById(String id) {

        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_select_contract_template_by_id")
                    .setParameter("id", id);
            storedProcedure.execute();
            return Optional.ofNullable((ContractTemplateBO) storedProcedure.getSingleResult());
        } catch (Exception e) {
            LOG.error("Have an error in method getContractTemplateById:" + e.getMessage());
            return Optional.of(new ContractTemplateBO());
        }
    }

    @Override
    public Optional<ContractTemplateBO> getContractTemplateByCodeTemp(String code_temp) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_select_contract_template_by_code_temp")
                    .setParameter("code_temp", code_temp);
            storedProcedure.execute();
            return Optional.ofNullable((ContractTemplateBO) storedProcedure.getSingleResult());
        } catch (Exception e) {
            LOG.error("Have an error in method getContractTemplateByCodeTemp:" + code_temp + " -- " + e.getMessage());
            return Optional.of(new ContractTemplateBO());
        }
    }

    @Override
    public List<ContractTemplate> getContractTemplateByParentCodeTemplate(String kind_id) {
        List<ContractTemplate> result = new ArrayList<>();
        try {
            Long kind_id_parent = Long.parseLong(kind_id);
            String query = "select bo from ContractTemplateBO bo where kind_id=:kind_id and active_flg=1";
            result = entityManager.createQuery(query).setParameter("kind_id",kind_id_parent).getResultList();

            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method getContractTemplateByParentCodeTemplate:" + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<ContractKindBO> getContractKindByContractTempId(String id) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_select_contract_kind_by_contract_template_id");
            storedProcedure
                    .setParameter("id", id);
            storedProcedure.execute();
            return Optional.ofNullable((ContractKindBO) storedProcedure.getSingleResult());
        } catch (Exception e) {
            LOG.error("Have an error in method getContractKindByContractTempID:" + e.getMessage());
            return Optional.ofNullable(new ContractKindBO());
        }

    }

    @Override
    public Optional<ContractKindBO> getContractKindByContractTempCode(int code_temp) {
        try {
            List<ContractTemplateBO> list = entityManager.createQuery("select c from ContractTemplateBO c where c.code_template =:code_temp",
                    ContractTemplateBO.class).setParameter("code_temp", Long.valueOf(code_temp)).getResultList();
            if (list != null && list.size() > 0) {
                ContractTemplateBO temp = list.get(0);
                String code = temp.getCode();
                List<ContractKindBO> lst = entityManager.createQuery("select c from ContractKindBO c where c.contract_kind_code =:code ",
                        ContractKindBO.class).setParameter("code", code).getResultList();
                if (lst != null && lst.size() > 0)
                    return Optional.ofNullable(lst.get(0));
            }

        } catch (Exception e) {
            LOG.error("Have an error in method  getContractKindByContractTempCode():" + e.getMessage());
        }
        return Optional.ofNullable(new ContractKindBO());
    }
    @Transactional
    @Override
    public Optional<BigInteger> checkUserExistContract(int idUser){

        BigInteger rt = BigInteger.valueOf(0);
        try{

            String sqlCheckUser = "select count(ct) from ContractBO ct where ct.drafter_id=:userId or ct.notary_id=:userId" ;
            int count = ((Number)entityManager.createQuery(sqlCheckUser).setParameter("userId",Long.valueOf(idUser)).getSingleResult()).intValue();

            return Optional.of(BigInteger.valueOf(count));
        }catch (Exception e){
            LOG.error("Have an error in method checkUserExistContract"+e.getMessage());
            return Optional.of(rt);
        }

    }

    @Override
    public Optional<List<ContractKindBO>> listContractKind() {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("stp_npo_contract_kind_getAll");
            storedProcedure.execute();
            return Optional.ofNullable((List<ContractKindBO>) storedProcedure.getResultList());
        } catch (Exception e) {
            LOG.error("Have an error in method listContractKind:" + e.getMessage());
            return Optional.ofNullable(new ArrayList<>());
        }
    }

    @Override
    public Optional<List<ContractTemplateBO>> listContractTemplateByContractKindId(String id) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_contract_template_select_by_contract_kind");
            storedProcedure.setParameter("id", id);
            storedProcedure.execute();
            return Optional.ofNullable((List<ContractTemplateBO>) storedProcedure.getResultList());
        } catch (Exception e) {
            LOG.error("Have an error in method listContractTemplateByContractKindId:" + e.getMessage());
            return Optional.ofNullable(new ArrayList<>());
        }

    }

    @Override
    public Optional<List<ContractTemplateBO>> listContractTemplateByContractKindCode(String code) {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_contract_template_select_by_code");
            storedProcedure.setParameter("codeKind", code);
            storedProcedure.execute();
            return Optional.ofNullable((List<ContractTemplateBO>) storedProcedure.getResultList());
        } catch (Exception e) {
            LOG.error("Have an error in method listContractTemplateByContractKindCode:" + e.getMessage());
            return Optional.ofNullable(new ArrayList<>());
        }
    }

//    @Override
//    public Optional<List<ContractTemplateBO>> listContractTemplateSameKind(String id) {
//        StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_contract_template_select_same_kind");
//        storedProcedure.setParameter("id_value",id);
//        storedProcedure.execute();
//        return Optional.ofNullable((List<ContractTemplateBO>) storedProcedure.getResultList());
//    }

    @Override
    public Optional<List<ContractTemplateBO>> listContractTemplateSameKind(int code) {
        try {
            List<ContractTemplateBO> list = entityManager.createQuery("select c from ContractTemplateBO c where c.code_template = :code",
                    ContractTemplateBO.class).setParameter("code", Long.valueOf(code)).getResultList();
            if (list != null && list.size() > 0) {
                ContractTemplateBO item = list.get(0);
                String codeKind = item.getCode();
                List<ContractTemplateBO> listnew = entityManager.createQuery("select c from ContractTemplateBO c where c.code = :code and active_flg=1 and (kind_id =0 or kind_id is null)",
                        ContractTemplateBO.class).setParameter("code", codeKind).getResultList();
                return Optional.ofNullable(listnew);
            }

        } catch (Exception e) {
            LOG.error("Have an error in method ContractRepositoryImpl.listContractTemplateSameKind():" + e.getMessage());

        }
        return Optional.ofNullable(new ArrayList<ContractTemplateBO>());
    }

    @Override
    public Optional<List<ContractTemplateBO>> listContractTemplate() {
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_contract_template_list");
            storedProcedure.execute();
            return Optional.ofNullable((List<ContractTemplateBO>) storedProcedure.getResultList());
        } catch (Exception e) {
            LOG.error("Have an error in method listContractTemplate:" + e.getMessage());
        }
        return Optional.ofNullable(new ArrayList<>());
    }

    @Override
    public List<ContractAdditional> getReportContractAdditional(String stringFitler) {
        try {
            ArrayList<ContractAdditional> reportContractAdditionals = new ArrayList<ContractAdditional>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_report_contract_additional");

            storedProcedure.setParameter("stringFilter", stringFitler);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ContractAdditional reportContractAdditional = new ContractAdditional();

                reportContractAdditional.setId(record[0] == null ? null : ((Integer) record[0]).intValue());
                reportContractAdditional.setContract_number(record[1] == null ? null : ((String) record[1]));
                reportContractAdditional.setContract_name(record[2] == null ? null : ((String) record[2]));
                reportContractAdditional.setRelate_object_A_display(record[3] == null ? null : ((String) record[3]));
                reportContractAdditional.setRelate_object_B_display(record[4] == null ? null : ((String) record[4]));
                reportContractAdditional.setRelate_object_C_display(record[5] == null ? null : ((String) record[5]));
                reportContractAdditional.setRelation_object_A(record[6] == null ? null : ((String) record[6]));
                reportContractAdditional.setRelation_object_B(record[7] == null ? null : ((String) record[7]));
                reportContractAdditional.setRelation_object_C(record[8] == null ? null : ((String) record[8]));
                reportContractAdditional.setNotary_date(record[9] == null ? null : ((Timestamp) record[9]));
                reportContractAdditional.setFamily_name(record[10] == null ? null : ((String) record[10]));
                reportContractAdditional.setFirst_name(record[11] == null ? null : ((String) record[11]));
                reportContractAdditional.setDrafter_id(record[12] == null ? null : ((Integer) record[12]).intValue());
                reportContractAdditional.setDrafter_family_name(record[13] == null ? null : ((String) record[13]));
                reportContractAdditional.setDrafter_first_name(record[14] == null ? null : ((String) record[14]));
                reportContractAdditional.setAddition_description(record[15] == null ? null : ((String) record[15]));

                reportContractAdditionals.add(reportContractAdditional);
            });
            return reportContractAdditionals;
        } catch (Exception e) {
            LOG.error("Have an error in method getReportContractAdditional:" + e.getMessage());
            return new ArrayList<ContractAdditional>();
        }
    }

    @Override
    public List<ContractCertify> getReportContractCertify(String stringFitler) {
        try {
            ArrayList<ContractCertify> reportContractCertifies = new ArrayList<ContractCertify>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_report_contract_certify");

            storedProcedure.setParameter("stringFilter", stringFitler);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ContractCertify reportContractCertifie = new ContractCertify();

                reportContractCertifie.setContract_number(record[0] == null ? null : ((String) record[0]));
                reportContractCertifie.setNotary_date(convertTimeStampToStringNoHour(record[1] == null ? null : ((Timestamp) record[1])));
                reportContractCertifie.setRelate_object_A_display(record[2] == null ? null : ((String) record[2]));
                reportContractCertifie.setRelate_object_B_display(record[3] == null ? null : ((String) record[3]));
                reportContractCertifie.setRelation_object_C(record[4] == null ? null : ((String) record[4]));
                reportContractCertifie.setRelation_object_A(record[5] == null ? null : ((String) record[5]));
                reportContractCertifie.setRelation_object_B(record[6] == null ? null : ((String) record[6]));
                reportContractCertifie.setRelation_object_C(record[7] == null ? null : ((String) record[7]));
                reportContractCertifie.setName(record[8] == null ? null : ((String) record[8]));
                reportContractCertifie.setNotary_family_name(record[9] == null ? null : ((String) record[9]));
                reportContractCertifie.setNotary_first_name(record[10] == null ? null : ((String) record[10]));
                reportContractCertifie.setCost_tt91(record[11] == null ? null : ((BigInteger) record[11]));
                reportContractCertifie.setNote(record[12] == null ? null : ((String) record[12]));
                reportContractCertifie.setCost_total(record[13] == null ? null : ((BigInteger) record[13]));

                reportContractCertifies.add(reportContractCertifie);
            });
            return reportContractCertifies;
        } catch (Exception e) {
            LOG.error("Have an error in method getReportContractCertify:" + e.getMessage());
            LOG.error("Query error is:" + stringFitler);
            return new ArrayList<ContractCertify>();
        }
    }

    @Override
    public List<ContractStastics> getContractStasticsDrafter(String notaryDateFromFilter, String notaryDateToFilter) {
        try {
            ArrayList<ContractStastics> contractStasticses = new ArrayList<ContractStastics>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_contract_stastics_drafter");

            storedProcedure.setParameter("notaryDateFromFilter", notaryDateFromFilter);
            storedProcedure.setParameter("notaryDateToFilter", notaryDateToFilter);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ContractStastics contractStastics = new ContractStastics();

                contractStastics.setUsid(record[0] == null ? null : ((Integer) record[0]).intValue());
                contractStastics.setFamily_name(record[1] == null ? null : ((String) record[1]));
                contractStastics.setFirst_name(record[2] == null ? null : ((String) record[2]));
                contractStastics.setNumber_of_contract(record[3] == null ? 0 : ((BigInteger) record[3]).intValue());
                contractStastics.setNumber_of_error_contract(record[4] == null ? 0 : ((BigInteger) record[4]).intValue());

                contractStasticses.add(contractStastics);
            });
            return contractStasticses;
        } catch (Exception e) {
            LOG.error("Have an error in method getContractStatsticsDrafter:" + e.getMessage());
            return new ArrayList<ContractStastics>();
        }
    }

    @Override
    public List<ContractStastics> getContractStasticsNotary(String notaryDateFromFilter, String notaryDateToFilter) {
        try {
            ArrayList<ContractStastics> contractStasticses = new ArrayList<ContractStastics>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_contract_statistic_notary");

            storedProcedure.setParameter("notaryDateFromFilter", notaryDateFromFilter);
            storedProcedure.setParameter("notaryDateToFilter", notaryDateToFilter);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ContractStastics contractStastics = new ContractStastics();

                contractStastics.setUsid(record[0] == null ? null : ((Integer) record[0]).intValue());
                contractStastics.setFamily_name(record[1] == null ? null : ((String) record[1]));
                contractStastics.setFirst_name(record[2] == null ? null : ((String) record[2]));
                contractStastics.setNumber_of_contract(record[3] == null ? 0 : ((BigInteger) record[3]).intValue());
                contractStastics.setNumber_of_error_contract(record[4] == null ? 0 : ((BigInteger) record[4]).intValue());

                contractStasticses.add(contractStastics);
            });
            return contractStasticses;
        } catch (Exception e) {
            LOG.error("Have an error in method getContactStaticsNotaty:" + e.getMessage());
            return new ArrayList<ContractStastics>();
        }
    }

    @Override
    public List<ContractStasticsOfWards> getContractStasticsNotaryOfWards(String district_code, String notaryDateFromFilter, String notaryDateToFilter) {
        try {
            ArrayList<ContractStasticsOfWards> contractStasticses = new ArrayList<ContractStasticsOfWards>();



            /*String nativeQuery = "SELECT nu.id AS usid,nu.family_name  ,nu.first_name ,number_of_contract,C.number_of_error_contract FROM npo_user nu " +
                    " LEFT JOIN (SELECT notary_id, COUNT(*) AS number_of_contract " +
                    " FROM npo_contract WHERE true AND  npo_contract.notary_date >= "+notaryDateFromFilter+" AND  npo_contract.notary_date <= " +notaryDateToFilter+
                    " GROUP BY notary_id) A ON nu.id = A.notary_id " +
                    " LEFT JOIN (SELECT nu2.id AS usid,nu2.family_name  ,nu2.first_name ,number_of_error_contract " +
                    " FROM npo_user nu2 " +
                    " LEFT JOIN (SELECT notary_id, COUNT(*) AS number_of_error_contract " +
                    " FROM npo_contract " +
                    " WHERE npo_contract.error_status = 1 AND  npo_contract.notary_date >= "+notaryDateFromFilter+" AND  npo_contract.notary_date <= "+notaryDateToFilter+" GROUP BY notary_id ) B ON nu2.id = B.notary_id where nu2.role = 02) C ON nu.id = C.usid WHERE nu.role = 02 and nu.district_code = "+district_code;*/
            /*  List<>*/

            /*            List<Object> list = entityManager.createNativeQuery(nativeQuery).getResultList();*/
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_contract_statistic_notary_of_districts_wards");
            storedProcedure.setParameter("district_code", district_code);
            storedProcedure.setParameter("notaryDateFromFilter", notaryDateFromFilter);
            storedProcedure.setParameter("notaryDateToFilter", notaryDateToFilter);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ContractStasticsOfWards contractStastics = new ContractStasticsOfWards();

                contractStastics.setUsid(record[0] == null ? null : ((Integer) record[0]).intValue());
                contractStastics.setFamily_name(record[1] == null ? null : ((String) record[1]));
                contractStastics.setFirst_name(record[2] == null ? null : ((String) record[2]));
                contractStastics.setNumber_of_contract(record[3] == null ? 0 : ((BigInteger) record[3]).intValue());
                contractStastics.setNumber_of_error_contract(record[4] == null ? 0 : ((BigInteger) record[4]).intValue());
                contractStastics.setDistrictCode(record[5] == null ? null : ((String) record[5]));
                contractStastics.setDistrictName(record[6] == null ? null : ((String) record[6]));

                contractStasticses.add(contractStastics);
            });
            return contractStasticses;
        } catch (Exception e) {
            LOG.error("Have an error in method getContactStaticsNotaty:" + e.getMessage());
            return new ArrayList<ContractStasticsOfWards>();
        }
    }

    @Override
    public List<ContractStasticsBank> getContractStasticsBank(String notaryDateFromFilter, String notaryDateToFilter) {
        try {
            ArrayList<ContractStasticsBank> contractStasticsBanks = new ArrayList<ContractStasticsBank>();
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_contact_stastics_bank");

            storedProcedure.setParameter("notaryDateFromFilter", notaryDateFromFilter);
            storedProcedure.setParameter("notaryDateToFilter", notaryDateToFilter);
            storedProcedure.execute();

            List<Object[]> results = storedProcedure.getResultList();

            results.stream().forEach((record) -> {
                ContractStasticsBank contractStasticsBank = new ContractStasticsBank();

                contractStasticsBank.setBank_id(record[0] == null ? null : ((Integer) record[0]).intValue());
                contractStasticsBank.setBank_name(record[1] == null ? null : ((String) record[1]));
                contractStasticsBank.setNumber_of_contract(record[2] == null ? 0 : ((BigInteger) record[2]).intValue());

                contractStasticsBanks.add(contractStasticsBank);
            });
            return contractStasticsBanks;
        } catch (Exception e) {
            LOG.error("Have an error in method getContractStasticsBank:" + e.getMessage());
            return new ArrayList<ContractStasticsBank>();
        }
    }

    public Timestamp convertStringToTimeStamp(String dateString) {
        try {
            if (dateString == "" || dateString == null) return null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
            Date parsedDate = null;
            parsedDate = dateFormat.parse(dateString);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        } catch (ParseException e) {
            LOG.error("Have an error in method convertStringToTimeStamp:" + e.getLocalizedMessage());
        }
        return null;
    }

    public String convertTimeStampToString(Timestamp date) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
        String result = dateFormat.format(date);
        return result;
    }

    public String longToDate(long longDate) {
        Date date = new Date(longDate);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
        String dateText = df2.format(date);
        return dateText;
    }

    @Override
    public ContractBO genInfo(ContractBO item, ContractBO itemResult) {
        itemResult.setContract_template_id(item.getContract_template_id());
        itemResult.setContract_number(item.getContract_number());
        itemResult.setContract_value(item.getContract_value());
        itemResult.setRelation_object_a(item.getRelation_object_a());
        itemResult.setRelation_object_b(item.getRelation_object_b());
        itemResult.setRelation_object_c(item.getRelation_object_c());
        itemResult.setNotary_id(item.getNotary_id());
        itemResult.setDrafter_id(item.getDrafter_id());
        itemResult.setReceived_date(item.getReceived_date());
        itemResult.setNotary_date(item.getNotary_date());
        itemResult.setUser_require_contract(item.getUser_require_contract());
        itemResult.setNumber_copy_of_contract(item.getNumber_copy_of_contract());
        itemResult.setNumber_of_sheet(item.getNumber_of_sheet());
        itemResult.setNumber_of_page(item.getNumber_of_page());
        itemResult.setCost_tt91(item.getCost_tt91());
        itemResult.setCost_draft(item.getCost_draft());
        itemResult.setCost_notary_outsite(item.getCost_notary_outsite());
        itemResult.setCost_other_determine(item.getCost_other_determine());
        itemResult.setCost_total(item.getCost_total());
        itemResult.setNotary_place_flag(item.getNotary_place_flag());
        itemResult.setNotary_place(item.getNotary_place());
        itemResult.setBank_id(item.getBank_id());
        itemResult.setBank_name(item.getBank_name());
        itemResult.setBank_service_fee(item.getBank_service_fee());
        itemResult.setCrediter_name(item.getCrediter_name());
        itemResult.setFile_name(item.getFile_name());
        itemResult.setFile_path(item.getFile_path());
        itemResult.setError_status(item.getError_status());
        itemResult.setError_description(item.getError_description());
        itemResult.setError_user_id(item.getError_user_id());
        itemResult.setAddition_status(item.getAddition_status());
        itemResult.setAddition_description(item.getAddition_description());
        itemResult.setCancel_status(item.getCancel_status());
        itemResult.setCancel_description(item.getCancel_description());
        itemResult.setCancel_relation_contract_id(item.getCancel_relation_contract_id());
        itemResult.setContract_period(item.getContract_period());
        itemResult.setMortage_cancel_flag(item.getMortage_cancel_flag());
        itemResult.setMortage_cancel_date(item.getMortage_cancel_date());
        itemResult.setMortage_cancel_note(item.getMortage_cancel_note());
        itemResult.setOriginal_store_place(item.getOriginal_store_place());
        itemResult.setNote(item.getNote());
        itemResult.setSummary(item.getSummary());
        itemResult.setUpdate_user_id(item.getUpdate_user_id());
        itemResult.setUpdate_user_name(item.getUpdate_user_name());
        itemResult.setUpdate_date_time(item.getUpdate_date_time());
        itemResult.setJsonstring(item.getJsonstring());
        itemResult.setKindhtml(item.getKindhtml());
        itemResult.setContent(item.getContent());
        itemResult.setTitle(item.getTitle());
        itemResult.setBank_code(item.getBank_code());
        itemResult.setJson_property(item.getJson_property());
        itemResult.setJson_person(item.getJson_person());

        return itemResult;
    }


    public TransactionPropertyBo genInfoTrans(TransactionPropertyBo item, TransactionPropertyBo itemResult) {
        itemResult.setCode_template(item.getCode_template());
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
    public BigInteger numberOfNotaryPerson03(ReportByTT03List reportByTT03List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT count(*) FROM npo_user nu WHERE nu.role = '02'");
            result = (BigInteger) q.getSingleResult();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigDecimal numThuLaoCongChungTT03(ReportByTT03List reportByTT03List) {
        BigDecimal result = BigDecimal.valueOf(0);
        Query q = entityManager.createNativeQuery("select SUM(cost_draft) AS A  FROM npo_contract nc where true  AND  nc.notary_date >= :fromdate and nc.notary_date <= :todate ");
        q.setParameter("fromdate", reportByTT03List.getNotaryDateFromFilter()).setParameter("todate", reportByTT03List.getNotaryDateToFilter());
        result = (BigDecimal) q.getSingleResult();
        return result;
    }

    /*@Override
    public BigInteger numberOfNotaryPersonHopDanh03(ReportByTT03List reportByTT03List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT count(*) FROM npo_user nu WHERE nu.role = '02' and nu.active_ccv = 1");
            result = (BigInteger) q.getSingleResult();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return result;
    }*/

    @Override
    public BigInteger numberOfContractLand03(ReportByTT03List reportByTT03List) {
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");

            storedProcedure.setParameter("kind_code", Constants.LCV_QSDD_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOgContractLand:" + e.getMessage());
            return result;
        }*/
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_QSDD_CODE)
                    .setParameter("notary_date_from", reportByTT03List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT03List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigInteger numberOfContractOther03(ReportByTT03List reportByTT03List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_TAI_SAN_KHAC_CODE)
                    .setParameter("notary_date_from", reportByTT03List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT03List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
        /*try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_TAI_SAN_KHAC_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfContractOther:" + e.getMessage());
            return result;
        }*/
    }

    @Override
    public BigInteger numberOfContractWorkOther03(ReportByTT03List reportByTT03List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_LOAI_CONG_VIEC_KHAC_CODE)
                    .setParameter("notary_date_from", reportByTT03List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT03List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_LOAI_CONG_VIEC_KHAC_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfContractOther:" + e.getMessage());
            return result;
        }*/
    }

    @Override
    public BigInteger numberOfContractDanSu03(ReportByTT03List reportByTT03List) {
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_THUC_HIEN_NGHIA_VU_DAN_SU_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfContractDanSu:" + e.getMessage());
            return result;
        }*/
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_THUC_HIEN_NGHIA_VU_DAN_SU_CODE)
                    .setParameter("notary_date_from", reportByTT03List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT03List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigInteger numberOfThuaKe03(ReportByTT03List reportByTT03List) {
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_GIAO_DICH_THUA_KE_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method  numberOFThuaKe:" + e.getMessage());
            return result;
        }*/
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_GIAO_DICH_THUA_KE_CODE)
                    .setParameter("notary_date_from", reportByTT03List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT03List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigInteger numberOfOther03(ReportByTT03List reportByTT03List) {
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_LOAI_CONG_VIEC_KHAC_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfOther:" + e.getMessage());
            return result;
        }*/
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_LOAI_CONG_VIEC_KHAC_CODE)
                    .setParameter("notary_date_from", reportByTT03List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT03List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigDecimal tongPhiCongChung03(ReportByTT03List reportByTT03List) {
        BigDecimal result = BigDecimal.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_cost_total_tt20");
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT03List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT03List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigDecimal) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method tongPhiCongChung:" + e.getMessage());
            return result;
        }
    }

    @Override
    public BigInteger numberOfTotalContract03(ReportByTT03List reportByTT03List) {

        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("notary_date_from", reportByTT03List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT03List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigInteger numberOfNotaryPerson04(ReportByTT04List reportByTT04List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT count(*) FROM npo_user nu WHERE nu.role = '02'");
            result = (BigInteger) q.getSingleResult();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigDecimal numThuLaoCongChung(ReportByTT04List reportByTT04List) {
        BigDecimal result = BigDecimal.valueOf(0);
        Query q = entityManager.createNativeQuery("select SUM(cost_draft) AS A  FROM npo_contract nc where true  AND  nc.notary_date >= :fromdate and nc.notary_date <= :todate ");
        q.setParameter("fromdate", reportByTT04List.getNotaryDateFromFilter()).setParameter("todate", reportByTT04List.getNotaryDateToFilter());
        result = (BigDecimal) q.getSingleResult();
        return result;
    }

    @Override
    public BigInteger numberOfNotaryPersonHopDanh04(ReportByTT04List reportByTT04List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT count(*) FROM npo_user nu WHERE nu.role = '02' and nu.active_ccv = 1");
            result = (BigInteger) q.getSingleResult();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigInteger numberOfContractLand04(ReportByTT04List reportByTT04List) {
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");

            storedProcedure.setParameter("kind_code", Constants.LCV_QSDD_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOgContractLand:" + e.getMessage());
            return result;
        }*/
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_QSDD_CODE)
                    .setParameter("notary_date_from", reportByTT04List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT04List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigInteger numberOfContractOther04(ReportByTT04List reportByTT04List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_TAI_SAN_KHAC_CODE)
                    .setParameter("notary_date_from", reportByTT04List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT04List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
        /*try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_TAI_SAN_KHAC_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfContractOther:" + e.getMessage());
            return result;
        }*/
    }

    @Override
    public BigInteger numberOfContractWorkOther04(ReportByTT04List reportByTT04List) {
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_LOAI_CONG_VIEC_KHAC_CODE)
                    .setParameter("notary_date_from", reportByTT04List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT04List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_LOAI_CONG_VIEC_KHAC_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfContractOther:" + e.getMessage());
            return result;
        }*/
    }

    @Override
    public BigInteger numberOfContractDanSu04(ReportByTT04List reportByTT04List) {
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_THUC_HIEN_NGHIA_VU_DAN_SU_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfContractDanSu:" + e.getMessage());
            return result;
        }*/
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_THUC_HIEN_NGHIA_VU_DAN_SU_CODE)
                    .setParameter("notary_date_from", reportByTT04List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT04List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigInteger numberOfThuaKe04(ReportByTT04List reportByTT04List) {
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_GIAO_DICH_THUA_KE_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method  numberOFThuaKe:" + e.getMessage());
            return result;
        }*/
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_GIAO_DICH_THUA_KE_CODE)
                    .setParameter("notary_date_from", reportByTT04List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT04List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigInteger numberOfOther04(ReportByTT04List reportByTT04List) {
        /*BigInteger result = BigInteger.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_by_kind_code");
            storedProcedure.setParameter("kind_code", Constants.LCV_LOAI_CONG_VIEC_KHAC_CODE);
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigInteger) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method numberOfOther:" + e.getMessage());
            return result;
        }*/
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true  AND nc.contract_kind = :kind_code AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("kind_code", Constants.LCV_LOAI_CONG_VIEC_KHAC_CODE)
                    .setParameter("notary_date_from", reportByTT04List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT04List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigDecimal tongPhiCongChung04(ReportByTT04List reportByTT04List) {
        BigDecimal result = BigDecimal.valueOf(0);
        try {
            StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("vpcc_npo_contract_count_cost_total_tt20");
            storedProcedure.setParameter("notaryDateFromFilter", reportByTT04List.getNotaryDateFromFilter());
            storedProcedure.setParameter("notaryDateToFilter", reportByTT04List.getNotaryDateToFilter());
            storedProcedure.execute();
            result = (BigDecimal) storedProcedure.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.error("Have an error in method tongPhiCongChung:" + e.getMessage());
            return result;
        }
    }

    @Override
    public BigInteger numberOfTotalContract04(ReportByTT04List reportByTT04List) {

        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT COUNT(*)  FROM  npo_transaction_property nc INNER JOIN  npo_contract_template nct  ON  nc.code_template = nct.code_template WHERE true AND  nc.notary_date >= :notary_date_from AND  nc.notary_date <= :notary_date_to");
            q.setParameter("notary_date_from", reportByTT04List.getNotaryDateFromFilter())
                    .setParameter("notary_date_to", reportByTT04List.getNotaryDateToFilter());
            result = (BigInteger) q.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    // bao cao thong ke tinh phi
    @Override
    public List<SalesByNotarys> getSalesByNotary(String fromDate,String toDate) {
        List<SalesByNotarys> list = new ArrayList<>();
        try{
            String query = "Select family_name,first_name,count(nc.id),SUM(cost_tt91),SUM(cost_draft),SUM(cost_notary_outsite),SUM(cost_other_determine),SUM(cost_total) FROM npo_contract nc INNER JOIN npo_user nu ON nc.notary_id = nu.id WHERE nu.role='02' and nc.notary_date >= :fromDate and nc.notary_date <= :toDate GROUP BY nu.id ";
            List<Object[]> result = entityManager.createNativeQuery(query).setParameter("fromDate",fromDate).setParameter("toDate",toDate).getResultList();
            result.stream().forEach((record)->{
                SalesByNotarys item = new SalesByNotarys();
                item.setFamily_name(record[0]==null? null : (String) record[0]);
                item.setFirst_name(record[1]==null? null: (String) record[1]);
                item.setNumberContract(record[2] == null? 0 : ((BigInteger) record[2]).longValue());
                item.setCost_tt91(record[3]== null? 0:((BigDecimal) record[3]).longValue());
                item.setCost_draft(record[4]== null? 0: ((BigDecimal) record[4]).longValue());
                item.setCost_notary_outside(record[5]==null?0:((BigDecimal) record[5]).longValue());
                item.setCost_other_determine(record[6]==null?0:((BigDecimal) record[6]).longValue());
                item.setCost_total(record[7]==null?0:((BigDecimal) record[7]).longValue());
                list.add(item);
            });
            return list;
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("Have an error in method getSalesByNotary:" + e.getMessage());
        }
        return new ArrayList<SalesByNotarys>();


    }
    @Override
    public List<SalesByDrafts> getSalesByDraft(String fromDate,String toDate) {
        List<SalesByDrafts> list = new ArrayList<>();
        try{
            String query = "Select family_name,first_name,count(nc.id),SUM(cost_tt91),SUM(cost_draft),SUM(cost_notary_outsite),SUM(cost_other_determine),SUM(cost_total) FROM npo_contract nc INNER JOIN npo_user nu ON nc.drafter_id = nu.id WHERE nu.role='03' and nc.notary_date >= :fromDate and nc.notary_date <= :toDate GROUP BY nu.id ";
            List<Object[]> result = entityManager.createNativeQuery(query).setParameter("fromDate",fromDate).setParameter("toDate",toDate).getResultList();
            result.stream().forEach((record)->{
                SalesByDrafts item = new SalesByDrafts();
                item.setFamily_name(record[0]==null? null : (String) record[0]);
                item.setFirst_name(record[1]==null? null: (String) record[1]);
                item.setNumberContract(record[2] == null? 0 : ((BigInteger) record[2]).longValue());
                item.setCost_tt91(record[3]== null? 0:((BigDecimal) record[3]).longValue());
                item.setCost_draft(record[4]== null? 0: ((BigDecimal) record[4]).longValue());
                item.setCost_notary_outside(record[5]==null?0:((BigDecimal) record[5]).longValue());
                item.setCost_other_determine(record[6]==null?0:((BigDecimal) record[6]).longValue());
                item.setCost_total(record[7]==null?0:((BigDecimal) record[7]).longValue());
                list.add(item);
            });
            return list;
        }catch (Exception e){
            LOG.error("Have an error in method getSalesByDraft:" + e.getMessage());
        }
        return new ArrayList<SalesByDrafts>();


    }
    @Override
    public List<SalesByKindContract> getSalesByKindContract(String fromDate,String toDate) {
        List<SalesByKindContract> list = new ArrayList<>();
        try{
            String query = "SELECT nck.`name`,count(nc.id),SUM(cost_tt91),SUM(cost_draft),SUM(cost_notary_outsite),SUM(cost_other_determine),SUM(cost_total) FROM npo_contract nc INNER JOIN npo_contract_template nct ON nc.contract_template_id = nct.code_template INNER JOIN npo_contract_kind nck on nct.`code` = nck.contract_kind_code WHERE nc.notary_date >= :fromDate and nc.notary_date <= :toDate GROUP BY nck.id";
            List<Object[]> result = entityManager.createNativeQuery(query).setParameter("fromDate",fromDate).setParameter("toDate",toDate).getResultList();
            result.stream().forEach((record)->{
                SalesByKindContract item = new SalesByKindContract();
                item.setKindName(record[0]==null? null : (String) record[0]);
                item.setNumberContract(record[1] == null? 0 : ((BigInteger) record[1]).longValue());
                item.setCost_tt91(record[2]== null? 0:((BigDecimal) record[2]).longValue());
                item.setCost_draft(record[3]== null? 0: ((BigDecimal) record[3]).longValue());
                item.setCost_notary_outside(record[4]==null?0:((BigDecimal) record[4]).longValue());
                item.setCost_other_determine(record[5]==null?0:((BigDecimal) record[5]).longValue());
                item.setCost_total(record[6]==null?0:((BigDecimal) record[6]).longValue());
                list.add(item);
            });
            return list;
        }catch (Exception e){
            LOG.error("Have an error in method getSalesByDraft:" + e.getMessage());
        }
        return new ArrayList<SalesByKindContract>();
    }

    @Override
    public PropertyBo getPropertyByContractId(Long contract_id) {
        ContractPropertyBO contractPropertyBO = findContractPropertyBO(contract_id);
        PropertyBo propertyBo = null;
        if(contractPropertyBO != null){
            propertyBo = findPropertyBo(contractPropertyBO.getProperty_id());
        }
        return propertyBo;
    }
    public PropertyBo findPropertyBo(Long id) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        PropertyBo bo = null;
        try {
            entityManagerCurrent.getTransaction().begin();
            bo = (PropertyBo) entityManagerCurrent.createQuery("select bo from PropertyBo bo where id=:id")
                    .setParameter("id", id).getSingleResult();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        }catch (Exception e){
            return null;
        }
        return bo;
    }
    public ContractPropertyBO findContractPropertyBO(Long contract_id) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        ContractPropertyBO bo = null;
        try {
            entityManagerCurrent.getTransaction().begin();
            bo = (ContractPropertyBO) entityManagerCurrent.createQuery("select bo from ContractPropertyBO bo where contract_id=:contract_id ")
                    .setParameter("contract_id", contract_id).getSingleResult();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        }catch (Exception e){
            System.out.println("ContractPropertyBO no result");
            return null;
        }
        return bo;
    }

    public BigInteger countAll() {
        BigInteger result = BigInteger.valueOf(0);
        try {
            Query q = entityManager.createNativeQuery("SELECT  COUNT(*)  FROM  npo_transaction_property nc where TRUE");
            result = (BigInteger) q.getSingleResult();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String convertTimeStampToStringNoHour(Timestamp date) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String result = dateFormat.format(date);
        return result;
    }
}
