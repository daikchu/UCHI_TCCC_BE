package com.vn.osp.notarialservices.citizenVerificationOrder.repository;

import com.vn.osp.notarialservices.citizenVerificationOrder.domain.CitizenVerifyOrderBO;
import com.vn.osp.notarialservices.citizenVerificationOrder.dto.CitizenVerifyOrderDTO;
import com.vn.osp.notarialservices.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CitizenVerificationOrderRepositoryImpl implements CitizenVerificationOrderRepository {
    private static final Logger LOG = LoggerFactory.getLogger(CitizenVerificationOrderRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @Override
    public Optional<CitizenVerifyOrderBO> insert(CitizenVerifyOrderBO citizenVerifyOrderBO) {

        return Optional.empty();
    }

    @Override
    public Optional<CitizenVerifyOrderBO> update(CitizenVerifyOrderBO citizenVerifyOrderBO) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try{
            entityManagerCurrent.getTransaction().begin();
            entityManagerCurrent.merge(citizenVerifyOrderBO);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            return Optional.of(citizenVerifyOrderBO);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error(" Have error in update.ContractFeeRepositoryImpl method :"+e.getMessage());
            throw e;
        }finally {
            if(entityManagerCurrent!=null)
                entityManagerCurrent.close();
        }
    }

    @Override
    public Optional<Boolean> deleteById(Long order_id) {
        return Optional.empty();
    }

    @Override
    public Optional<CitizenVerifyOrderBO> get(String order_id) {
        try{
            CitizenVerifyOrderBO bo = entityManager.find(CitizenVerifyOrderBO.class,order_id);
            return Optional.of(bo);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<CitizenVerifyOrderDTO> getDetail(String order_id) {
        String queryStr = buildQueryColumns() + buildQueryFrom() + " where orders.order_id = '"+order_id+"'";
        LOG.info(queryStr);
        Query query = entityManager.createNativeQuery(queryStr);
        Object[] result = (Object[]) query.getSingleResult();
        CitizenVerifyOrderDTO dto = mapDataFromDbQueryResult(result);
        return Optional.of(dto);
    }

    @Override
    public Optional<List<CitizenVerifyOrderDTO>> filter(String order_id, String notary_office_code, String province_code, String transaction_status, String status, String update_by, String order_time_from, String order_time_to) {
        String filter = buildFilter(order_id, notary_office_code, province_code, transaction_status, status, update_by,order_time_from, order_time_to);
        String queryStr = buildQueryColumns() + buildQueryFrom() + " where 1=1 " + filter + " order by orders.order_time desc";
        Query query = entityManager.createNativeQuery(queryStr);
        List<Object[]> queryResultList = query.getResultList();
        List<CitizenVerifyOrderDTO> results = new ArrayList<>();
        for (Object[] result : queryResultList) {
            CitizenVerifyOrderDTO dto = mapDataFromDbQueryResult(result);
            results.add(dto);
        }

        return Optional.of(results);
    }

    @Override
    public Optional<List<CitizenVerifyOrderDTO>> page(int page, int numberPerPage, String order_id, String notary_office_code, String province_code, String transaction_status, String status, String update_by, String order_time_from, String order_time_to) {
        int offset = numberPerPage*(page-1);
        try{

            String filter = buildFilter(order_id, notary_office_code, province_code, transaction_status, status, update_by,order_time_from, order_time_to);

            String queryStr = buildQueryColumns() + buildQueryFrom() + " where 1=1 " + filter + " order by orders.order_time desc";
            LOG.info(queryStr);
            Query query = entityManager.createNativeQuery(queryStr);
            query.setFirstResult(offset);
            query.setMaxResults(numberPerPage);
            List<Object[]> queryResultList = query.getResultList();
            List<CitizenVerifyOrderDTO> results = new ArrayList<>();
            for (Object[] result : queryResultList) {
                CitizenVerifyOrderDTO dto = mapDataFromDbQueryResult(result);
                results.add(dto);
            }
            return Optional.of(results);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error(" Have error in page.CitizenVerificationOrderRepositoryImpl method :"+e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> count(String order_id, String notary_office_code, String province_code, String transaction_status, String status, String update_by, String order_time_from, String order_time_to) {
        String filter = buildFilter(order_id, notary_office_code, province_code, transaction_status, status, update_by,order_time_from, order_time_to);
        long count = 0;
        try{
            String query = "Select count(DISTINCT orders.order_id) " + buildQueryFrom() + " where 1=1 " + filter;
            LOG.info(query);
            BigInteger countBigInt = (BigInteger)entityManager.createNativeQuery(query).getSingleResult();
            count = countBigInt.longValue();
            return Optional.of(count);

        }catch (Exception e){
            LOG.error("Have error in count.CitizenVerificationOrderRepositoryImpl method :"+e.getMessage());
        }
        return Optional.empty();
    }

    private String buildQueryColumns() {
        return "select" +
                " distinct(orders.order_id)" +
                ", orders.order_time" +
                ", orders.verify_number" +
                ", orders.verify_fee" +
                ", orders.verify_fee_received" +
                ", orders.notary_office_code" +
                ", office.name as notary_office_name" +
                ", orders.province_code" +
                ", province.name as province_name" +
                ", orders.transaction_status" +
                ", transactionStatus.name as transaction_status_name" +
                ", orders.status" +
                ", statusAdd.name as status_name " +
                ", orders.notary_office_excutor" +
                ", orders.notary_office_excutor_fullname" +
                ", orders.note" +
                ", orders.attach_files" +
                ", orders.update_by" +
                ", orders.payment_content" +
                ", orders.update_by_name";
    }

    private String buildQueryFrom() {
        return " from npo_citizen_verification_orders orders " +
                " join npo_province province on province.code = orders.province_code " +
                " join npo_customer office on office.code = orders.notary_office_code " +
                " left join npo_status transactionStatus on transactionStatus.code = orders.transaction_status " +
                " left join npo_status statusAdd on statusAdd.code = orders.status ";
    }
    private String buildFilter(String order_id, String notary_office_code, String province_code, String transaction_status, String status, String update_by, String order_time_from, String order_time_to){
        StringBuilder query = new StringBuilder();
        if(StringUtils.isNotBlank(order_id)){
            query.append(" and orders.order_id = '").append(order_id).append("'");
        }
        if(StringUtils.isNotBlank(notary_office_code)){
            query.append(" and orders.notary_office_code = '").append(notary_office_code).append("'");
        }
        if(StringUtils.isNotBlank(province_code)){
            query.append(" and orders.province_code = '").append(province_code).append("'");
        }
        if(StringUtils.isNotBlank(transaction_status)){
            query.append(" and orders.transaction_status = '").append(transaction_status).append("'");
        }
        if(StringUtils.isNotBlank(status)){
            query.append(" and orders.status = '").append(status).append("'");
        }
        if(StringUtils.isNotBlank(update_by)){
            query.append(" and orders.update_by = '").append(update_by).append("'");
        }
        if(StringUtils.isNotBlank(order_time_from)){
            query.append(" and orders.order_time >= '").append(TimeUtil.toTimestamp(true, order_time_from)).append("'");
        }
        if(StringUtils.isNotBlank(order_time_to)){
            query.append(" and orders.order_time <= '").append(TimeUtil.toTimestamp(false, order_time_to)).append("'");
        }
        return query.toString();
    }

    private CitizenVerifyOrderDTO mapDataFromDbQueryResult(Object[] result){
        CitizenVerifyOrderDTO dto = new CitizenVerifyOrderDTO();
        dto.setOrder_id((String) result[0]);
        dto.setOrder_time((Timestamp) result[1]);
        dto.setVerify_number((int) result[2]);
        dto.setVerify_fee((String) result[3]);
        dto.setVerify_fee_received((String) result[4]);
        dto.setNotary_office_code((String) result[5]);
        dto.setNotary_office_name((String) result[6]);
        dto.setProvince_code((String) result[7]);
        dto.setProvince_name((String) result[8]);
        dto.setTransaction_status((String) result[9]);
        dto.setTransaction_status_name((String) result[10]);
        dto.setStatus((String) result[11]);
        dto.setStatus_name((String) result[12]);
        dto.setNotary_office_excutor((String) result[13]);
        dto.setNote((String) result[14]);
        dto.setAttach_files((String) result[15]);
        dto.setUpdate_by((String) result[16]);
        dto.setPayment_content((String) result[17]);
        dto.setUpdate_by_name((String) result[18]);
        dto.setNotary_office_excutor_fullname((String) result[19]);
        return dto;
    }
}
