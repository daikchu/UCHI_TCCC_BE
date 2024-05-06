package com.vn.osp.notarialservices.contractKeyMap.service;

import com.vn.osp.notarialservices.contractKeyMap.domain.ContractKeyMapBO;
import com.vn.osp.notarialservices.utils.EditString;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ContractKeyMapServiceImpl implements ContractKeyMapService{
    private static final Logger LOG = Logger.getLogger(ContractKeyMapServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Boolean addContractKeyMap(ContractKeyMapBO contractKeyMapBO) {

        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{

            if(contractKeyMapBO.getEnd_word()==null){
                contractKeyMapBO.setEnd_word("");
            }
            contractKeyMapBO.setEntry_date_time(new Timestamp(System.currentTimeMillis()));
            contractKeyMapBO.setUpdate_date_time(new Timestamp(System.currentTimeMillis()));
            entityManagerCurrent.persist(contractKeyMapBO);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

            return true;
        }catch (Exception e){
            LOG.error("Have an error in method addContractKeyMap: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return false;
    }

    @Override
    public String getStringFilterFromSearch(String search) {
        StringBuilder stringFilter = new StringBuilder(" Where 1=1 ");
        try{
            JSONObject searchObject=new JSONObject(search);
            if(searchObject.has("basic") && !StringUtils.isBlank(searchObject.get("basic").toString())){
                String basic=searchObject.get("basic").toString();
                if(EditString.isNumber(basic)){
                    if(Integer.parseInt(basic)==0){//tim kiem co ban

                    }else if(Integer.parseInt(basic)==1){ //tim kiem nang cao

                        if (searchObject.has("type") && !StringUtils.isBlank(searchObject.get("type").toString()) && !searchObject.get("type").toString().equalsIgnoreCase("NULL")) {
                            stringFilter.append(" and type=" + searchObject.get("type") + " ");
                        }
                        if (searchObject.has("type_property") && !StringUtils.isBlank(searchObject.get("type_property").toString()) && !searchObject.get("type_property").toString().equalsIgnoreCase("NULL")) {
                            stringFilter.append(" and type_property=" + searchObject.get("type_property") + " ");
                        }
                        if (searchObject.has("begin_or_close") && !StringUtils.isBlank(searchObject.get("begin_or_close").toString()) && !searchObject.get("begin_or_close").toString().equalsIgnoreCase("NULL")) {
                            stringFilter.append(" and begin_or_close=" + searchObject.get("begin_or_close") + " ");
                        }
                        if(searchObject.has("map_var") && !StringUtils.isBlank(searchObject.get("map_var").toString()) && !searchObject.get("map_var").toString().equalsIgnoreCase("NULL")){
                            stringFilter.append(" and UPPER(map_var) like '%"+searchObject.get("map_var").toString().toUpperCase()+"%' ");
                        }
                        if(searchObject.has("first_word") && !StringUtils.isBlank(searchObject.get("first_word").toString()) && !searchObject.get("first_word").toString().equalsIgnoreCase("NULL")){
                            stringFilter.append(" and UPPER(first_word) like '%"+searchObject.get("first_word").toString().toUpperCase()+"%' ");
                        }
                        if(searchObject.has("end_word") && !StringUtils.isBlank(searchObject.get("end_word").toString()) && !searchObject.get("end_word").toString().equalsIgnoreCase("NULL")){
                            stringFilter.append(" and UPPER(end_word) like '%"+searchObject.get("end_word").toString().toUpperCase()+"%' ");
                        }
                    }
                }

            }

        }catch (Exception e){
            LOG.error("loi tai ContractKeyMapServiceImpl.getStringFilterFromSearch" + e.getMessage());
        }

        return stringFilter.toString();
    }

    @Override
    public List<ContractKeyMapBO> listContractKeyMap(String stringFilter, int offset, int number) {
        List<ContractKeyMapBO> result = new ArrayList<>();
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            String hql = "select bo from ContractKeyMapBO bo " + stringFilter;
            result = entityManagerCurrent.createQuery(hql).setFirstResult(offset).setMaxResults(number).getResultList();

            entityManagerCurrent.close();

        }catch (Exception e){
            LOG.error("Have an error in method listContractKeyMap: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return result;
    }

    @Override
    public List<ContractKeyMapBO> getContractKeyMapByFilter(String stringFilter) {
        List<ContractKeyMapBO> result = new ArrayList<>();
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            String hql = "select bo from ContractKeyMapBO bo " + stringFilter;
            result = entityManagerCurrent.createQuery(hql).getResultList();

            entityManagerCurrent.close();

        }catch (Exception e){
            LOG.error("Have an error in method getContractKeyMapByFilter: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return result;
    }

    @Override
    public Long countContractKeyMap(String stringFilter) {
        Long result = 0L;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            String hql = "select count(1) from ContractKeyMapBO bo " + stringFilter;
            result = (Long) entityManagerCurrent.createQuery(hql).getSingleResult();

            entityManagerCurrent.close();

        }catch (Exception e){
            LOG.error("Have an error in method countContractKeyMap: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
        }
        return result;
    }

    @Override
    public Boolean deleteContractKeyMaps(String listId) {
        String[] ids = listId.split(";");
        String ids_ = "";
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<ids.length;i++){
            if(i==0){
                stringBuilder.append(ids[i]);
            } else {
                stringBuilder.append("," + ids[i]);
            }
        }
        ids_ = stringBuilder.toString();
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            String hql = "delete from ContractKeyMapBO bo Where id in (" + ids_ + ")";
            entityManagerCurrent.createQuery(hql).executeUpdate();
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception e){
            LOG.error("Have an error in method deleteContractKeyMaps: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }
        return result;
    }

    @Override
    public Boolean editContractKeyMap(ContractKeyMapBO bo) {
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            if(bo.getEnd_word()==null){
                bo.setEnd_word("");
            }
            bo.setUpdate_date_time(new Timestamp(System.currentTimeMillis()));
            entityManagerCurrent.merge(bo);
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();

        }catch (Exception e){
            LOG.error("Have an error in method deleteContractKeyMaps: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }
        return result;
    }

    @Override
    public Boolean checkExistKey(ContractKeyMapBO bo) {
        Boolean result = true;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        entityManagerCurrent.getTransaction().begin();
        try{
            String hql = "select bo from ContractKeyMapBO bo where 1=1 ";
            if(bo.getKey_name()!=null){
                hql +=  " and CAST(key_name as binary) = CAST('"+bo.getKey_name()+"' as binary)";
            }
            if(bo.getType()!=null){
                hql +=  " and type =" + bo.getType();
            }
            if(bo.getMap_var()!=null){
                hql +=  " and CAST(map_var as binary) = CAST('"+bo.getMap_var()+"' as binary)";
            }

            if(bo.getFirst_word()!=null){
                hql +=  " and CAST(first_word as binary) = CAST('"+bo.getFirst_word()+"' as binary)";
            }
            if(bo.getEnd_word()!=null){
                hql +=  " and CAST(end_word as binary) = CAST('"+bo.getEnd_word()+"' as binary)";
            }
            if(bo.getType_property()!=null){
                hql +=  " and type_property = " + bo.getType_property();
            }
            if(bo.getBegin_or_close()!=null){
                hql +=  " and begin_or_close = " + bo.getBegin_or_close();
            }

            List<ContractKeyMapBO> list = entityManagerCurrent.createQuery(hql).getResultList();
            entityManagerCurrent.flush();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.close();
            if(list != null && !list.isEmpty()){
                result = false;
            }

        }catch (Exception e){
            LOG.error("Have an error in method checkExistKey: "+e.getMessage());
            entityManagerCurrent.getTransaction().rollback();
            entityManagerCurrent.close();
            result = false;
        }
        return result;
    }
}
