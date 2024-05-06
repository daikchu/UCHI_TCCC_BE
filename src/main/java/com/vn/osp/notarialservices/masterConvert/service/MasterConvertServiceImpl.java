package com.vn.osp.notarialservices.masterConvert.service;

import com.vn.osp.notarialservices.contract.domain.ContractBO;
import com.vn.osp.notarialservices.masterConvert.connectDBMaster.ConnectUtils;
import com.vn.osp.notarialservices.masterConvert.domain.ContractPropertyBO;
import com.vn.osp.notarialservices.masterConvert.domain.MasterContractBO;
import com.vn.osp.notarialservices.masterConvert.domain.PropertyBo;
import com.vn.osp.notarialservices.masterConvert.domain.ReperToire;
import com.vn.osp.notarialservices.masterConvert.dto.ConfigDBMaster;
import com.vn.osp.notarialservices.masterConvert.dto.MasterContract;
import com.vn.osp.notarialservices.systemmanager.domain.SystemConfigBO;
import com.vn.osp.notarialservices.transaction.domain.TransactionPropertyBo;
import com.vn.osp.notarialservices.utils.Constants;
import com.vn.osp.notarialservices.utils.EditString;
import com.vn.osp.notarialservices.utils.SystemProperties;
import com.vn.osp.notarialservices.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Created by minh on 11/18/2016.
 */
@Component
public class MasterConvertServiceImpl implements MasterConvertService {
    private static final Logger LOGGER = Logger.getLogger(MasterConvertServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    ConnectUtils connectUtils = new ConnectUtils();

    @Override
    public List<ReperToire> selectReperToire(String stringFilter) {
        List<ReperToire> reperToires = new ArrayList<>();
        Statement stmt = null;
        ResultSet result = null;
        Connection conn = connectUtils.connect(getConfigDBMaster());
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery("select * from REPERTOIRE a " + stringFilter);

            while (result.next()) {
                ReperToire add = new ReperToire();

                add.setREP_REF_UNIQUE(result.getLong("REP_REF_UNIQUE"));
                add.setENREG_CODE(result.getString("ENREG_CODE"));
                add.setCODE_BREVET(result.getString("CODE_BREVET"));
                add.setPERS_CODE(result.getInt("PERS_CODE"));
                add.setPER_PERS_CODE(result.getInt("PER_PERS_CODE"));
                add.setREP_AN_LEGAL(result.getInt("REP_AN_LEGAL"));
                add.setREP_LEGAL(result.getString("REP_LEGAL"));
                add.setREP_DATE(result.getDate("REP_DATE"));
                add.setREP_LIBEL(result.getString("REP_LIBEL"));
                add.setREP_MT_ENREG(result.getLong("REP_MT_ENREG"));
                add.setREP_DATE_ENREG(result.getDate("REP_DATE_ENREG"));
                add.setTIMBRE_ACTE(result.getInt("TIMBRE_ACTE"));
                add.setMONTANT_TIMBRE_ACTE(result.getLong("MONTANT_TIMBRE_ACTE"));
                add.setNB_COP_AUTH(result.getInt("NB_COP_AUTH"));
                add.setNB_PAGE_COP_AUTH(result.getInt("NB_PAGE_COP_AUTH"));
                add.setMT_COP_AUTH(result.getLong("MT_COP_AUTH"));
                add.setNB_COP_EXEC(result.getInt("NB_COP_EXEC"));
                add.setNB_PAGE_COP_EXEC(result.getInt("NB_PAGE_COP_EXEC"));
                add.setMT_COP_EXEC(result.getLong("MT_COP_EXEC"));
                add.setREP_TEXTE(result.getBlob("REP_TEXTE"));
                add.setSDOS_ETUDE(result.getString("SDOS_ETUDE"));
                add.setREP_EDIT(result.getInt("REP_EDIT"));
                add.setREP_AN_LEGAL_DEFI(result.getInt("REP_AN_LEGAL_DEFI"));
                add.setREP_LEGAL_DEFI(result.getInt("REP_LEGAL_DEFI"));
                add.setREP_MANU(result.getInt("REP_MANU"));
                add.setREP_LIEU_SIGNATURE(result.getString("REP_LIEU_SIGNATURE"));
                add.setREP_ENREG(result.getInt("REP_ENREG"));
                add.setREP_NUMENREG(result.getString("REP_NUMENREG"));
                add.setREP_RECEUIL(result.getInt("REP_RECEUIL"));
                add.setREP_STAT(result.getString("REP_STAT"));
                add.setREP_COMPLET(result.getInt("REP_COMPLET"));
                add.setREP_SUITE(result.getInt("REP_SUITE"));
                add.setREP_INTERVENANT(result.getBlob("REP_INTERVENANT"));
                add.setREP_ESTREMI(result.getString("REP_ESTREMI"));
                add.setREP_VALEUR(result.getInt("REP_VALEUR"));
                add.setREP_PS(result.getString("REP_PS"));
                add.setSDOS_REF(result.getString("SDOS_REF"));
                add.setSDOS_LIBEL(result.getString("SDOS_LIBEL"));
                add.setTEXTE(result.getString("TEXTE"));
                add.setTIME_UPDATE(result.getDate("TIME_UPDATE"));

                reperToires.add(add);
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("loi tai selectReperToire "+e.getMessage());
        }
        return reperToires;
    }

    @Override
    public List<MasterContract> selectContractMasterConvert(String stringFilter, int offset, int number) {
        List<MasterContractBO> masterContractBO = new ArrayList<>();
        List<MasterContract> masterContract = new ArrayList<>();

        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            try {
                masterContractBO = entityManagerCurrent.createQuery("select bo from MasterContractBO bo " + stringFilter)
                        .setFirstResult(offset).setMaxResults(number).getResultList();
            }catch (Exception e){
                System.out.println("selectContractMasterConvert " + e.getMessage());
            }
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

            //bo to form
            for (int i=0;i<masterContractBO.size();i++){
                MasterContractBO bo = masterContractBO.get(i);
                MasterContract form = genInfoMasterContract(bo);
                masterContract.add(form);
            }
        } catch (Exception e) {
            System.out.println("loi tai selectContractMasterConvert "+e.getMessage());
        }
        return masterContract;
    }

    @Override
    public Long countContractMasterConvert(String stringFilter) {
        Long result__ = 0L;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            try {
                result__ = (Long) entityManagerCurrent.createQuery("select count(1) from MasterContractBO bo " + stringFilter).getSingleResult();
            }catch (Exception e){
                e.getMessage();
            }
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        } catch (Exception e) {
            System.out.println("loi tai countContractMasterConvert "+e.getMessage());
        }
        return result__;
    }

    @Override
    public String getStringFilterFromSearch(String search, String type) {
        StringBuilder stringFilter = new StringBuilder(" Where 1=1 ");
        try{
            JSONObject searchObject=new JSONObject(search);
            if(searchObject.has("basic") && !StringUtils.isBlank(searchObject.get("basic").toString())){
                String basic=searchObject.get("basic").toString();
                if(EditString.isNumber(basic)){
                    if(Integer.parseInt(basic)==0){//tim kiem co ban

                    }else if(Integer.parseInt(basic)==1){ //tim kiem nang cao

                        if (searchObject.has("contract_kind") && !StringUtils.isBlank(searchObject.get("contract_kind").toString())) {
                            stringFilter.append(" and contract_kind_code=" + searchObject.get("contract_kind") + " ");
                        }
                        if (searchObject.has("contract_template") && !StringUtils.isBlank(searchObject.get("contract_template").toString())) {
                            stringFilter.append(" and contract_template_id=" + searchObject.get("contract_template") + " ");
                        }
                        if(searchObject.has("contract_number") && !StringUtils.isBlank(searchObject.get("contract_number").toString())){
                            stringFilter.append(" and (contract_number like '%"+searchObject.get("contract_number")+"%' or contract_number_new like '%"+searchObject.get("contract_number")+"%' )");
                        }
                        if(searchObject.has("relation_object") && !StringUtils.isBlank(searchObject.get("relation_object").toString())){
                            stringFilter.append(" and relation_object like '%"+searchObject.get("relation_object")+"%' ");
                        }
                        if(searchObject.has("property_info") && !StringUtils.isBlank(searchObject.get("property_info").toString())){
                            stringFilter.append(" and property_info like '%"+searchObject.get("property_info")+"%' ");
                        }
                        if(searchObject.has("time") && !StringUtils.isBlank(searchObject.get("time").toString())){
                            java.util.Date now=new java.util.Date();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String result=df.format(now);
                            java.util.Date dateStart;
                            java.util.Date dateEnd;
                            switch (searchObject.get("time").toString()){
                                case "1":
                                    dateStart= TimeUtil.convertStringToDateWithPlusSecond(result,0);
                                    dateEnd=TimeUtil.convertStringToDateWithPlusSecond(result,86399);
                                    stringFilter.append(" and notary_date between '"+dateFormat.format(dateStart)+"' and '"+dateFormat.format(dateEnd)+"' ");
                                    break;
                                case "2":
                                    dateStart=TimeUtil.convertStringToDateWithPlusSecond(TimeUtil.getDateByFirstDayOfWeek(now),0);
                                    dateEnd=TimeUtil.convertStringToDateWithPlusSecond(TimeUtil.getDateByLastDayOfWeek(now),86399);
                                    stringFilter.append(" and notary_date between '"+dateFormat.format(dateStart)+"' and '"+dateFormat.format(dateEnd)+"' ");
                                    break;
                                case "3":
                                    dateStart=TimeUtil.convertStringToDateWithPlusSecond(TimeUtil.getDateByFirstDayOfMonth(now),0);
                                    dateEnd=TimeUtil.convertStringToDateWithPlusSecond(TimeUtil.getDateByLastDayOfMonth(now),86399);
                                    stringFilter.append(" and notary_date between '"+dateFormat.format(dateStart)+"' and '"+dateFormat.format(dateEnd)+"' ");
                                    break;
                                case "4":
                                    dateStart=TimeUtil.convertStringToDateWithPlusSecond(TimeUtil.getDateByFirstDayOfYear(now),0);
                                    dateEnd=TimeUtil.convertStringToDateWithPlusSecond(TimeUtil.getDateByLastDayOfYear(now),86399);
                                    stringFilter.append(" and notary_date between '"+dateFormat.format(dateStart)+"' and '"+dateFormat.format(dateEnd)+"' ");
                                    break;
                                case "5":
                                    if (searchObject.has("fromTime") && !StringUtils.isBlank(searchObject.get("fromTime").toString())) {
                                        dateStart = TimeUtil.convertStringToDateWithPlusSecond(searchObject.get("fromTime").toString(), 0);
                                        if (dateStart != null) {
                                            if (searchObject.has("toTime") &&!StringUtils.isBlank(searchObject.get("toTime").toString())) {
                                                dateEnd = TimeUtil.convertStringToDateWithPlusSecond(searchObject.get("toTime").toString(), 86399);
                                            } else {
                                                dateEnd = new Date();
                                            }
                                            if (dateEnd != null) {
                                                stringFilter.append(" and notary_date between '" + dateFormat.format(dateStart) + "' and '" + dateFormat.format(dateEnd) + "' ");
                                            }
                                        }
                                    }else  if (searchObject.has("toTime") && !StringUtils.isBlank(searchObject.get("toTime").toString())) {
                                        dateEnd = TimeUtil.convertStringToDateWithPlusSecond(searchObject.get("toTime").toString(), 86399);
                                        if(dateEnd!=null){
                                            stringFilter.append(" and notary_date < '"  + dateFormat.format(dateEnd) + "' ");
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

            }

            if(!StringUtils.isBlank(type)){
                if(EditString.isNumber(type)){
                    switch (Integer.parseInt(type)){
                        case 0:
                            stringFilter.append(" and type_master_to_transaction = "+ Constants.INVALID_MASTER_TO_TRANSACTION +" and type_delete = " + Constants.TYPE_NO_DELETE + " ");    //chưa chuyen doi
                            break;
                        case 1:
                            stringFilter.append(" and type_master_to_transaction = "+ Constants.VALID_MASTER_TO_TRANSACTION + " and type_delete =  " + Constants.TYPE_NO_DELETE + " ");    //da chuyen doi
                            break;
                        case 2:
                            stringFilter.append(" and type_duplicate_content = " + Constants.VALID_DUPLICATE + " and type_delete = " + Constants.TYPE_NO_DELETE + " ");        //trung noi dung
                            break;
                        case 3:
                            stringFilter.append(" and type_delete = " + Constants.TYPE_NO_DELETE + " ");                                                                        //search
                            break;
                        default:
                            break;
                    }
                }
            }

        }catch (Exception e){
            System.out.println("loi tai MasterConvertServiceImpl.getStringFilterFromSearch");
        }

        if(!StringUtils.isBlank(type)){
            if(EditString.isNumber(type)) {
                if(Integer.parseInt(type) == 2) {
                    stringFilter.append(" ORDER BY YEAR(notary_date) desc, content desc");
                }else{
                    stringFilter.append(" ORDER BY notary_date desc, LENGTH(contract_number) desc, contract_number DESC ");
                }
            }
        }

        return stringFilter.toString();
    }

    @Override
    public List<MasterContractBO> selectContractMasterConvert(String stringFilter) {
        List<MasterContractBO> masterContractBO = new ArrayList<>();

        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            try {
                masterContractBO = entityManagerCurrent.createQuery("select bo from MasterContractBO bo " + stringFilter).getResultList();
            }catch (Exception e){
                System.out.println("selectContractMasterConvert ------------------------------------------------------------------ " + e.getMessage());
            }
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        } catch (Exception e) {
            System.out.println("loi tai selectContractMasterConvert --------------------------------------------------------------- "+e.getMessage());
        }
        return masterContractBO;
    }

    @Override
    public Boolean saveOrUpdateMasterContractBO(List<MasterContractBO> masterContractBO) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();

            for(int i=0;i<masterContractBO.size();i++) {
                MasterContractBO bo = masterContractBO.get(i);
                //MasterContractBO check = findMasterContractBO(update.getId());
                if (bo.getContract_number() != null && bo.getContent() != null && !"".equals(bo.getContent().trim())) {
                    if(bo.getContent() != null) {
                        try {
                            bo.setContract_number(bo.getContract_number().trim());
                            bo.setNotary_office_name(getSystemConfigBO("notary_office_name").getConfig_value());
                            entityManagerCurrent.persist(bo);

                        } catch (Exception e) {
                            System.out.println(" loi saveOrUpdateMasterContractBO " + e.getMessage());
                        }
                    }
                }
                commitBat(entityManagerCurrent, i, "loi commit saveOrUpdateMasterContractBO");
            }

        }catch (Exception e){
            System.out.println("loi tai saveOrUpdateMasterContractBO vong thu: " + e.getMessage());
        }finally {
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        }
        return true;
    }

    @Override
    public Boolean saveOrUpdateContractNumberNew(List<MasterContractBO> masterContractBO) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();

            for(int i=0;i<masterContractBO.size();i++) {
                MasterContractBO update = masterContractBO.get(i);
                if (update.getContract_number() != null) {
                    try {
                        update.setContract_number(update.getContract_number().trim());
                        Timestamp stamp = update.getNotary_date();
                        Date date = dateformat.parse(convertTimeStampToString(stamp));
                        int year = Integer.parseInt(df.format(date));
                        update.setContract_number_new(year + "/" + update.getContract_number());
                        entityManagerCurrent.merge(update);

                    } catch (Exception e) {
                        System.out.println(" loi saveOrUpdateContractNumberNew " + e.getMessage());
                    }
                }
                commitBat(entityManagerCurrent, i, "loi commit saveOrUpdateContractNumberNew");
            }

        }catch (Exception e){
            System.out.println(" loi saveOrUpdateContractNumberNew " + e.getMessage());
        }finally {
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        }
        return true;
    }

    @Override
    public Boolean updateContractNumberNew(int fromYear, int toYear) {
        int toYear_ = toYear + 1;
        List<MasterContractBO> masterContractBO = new ArrayList<>();
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            entityManagerCurrent.getTransaction().begin();
            String hql = "SELECT bo FROM MasterContractBO bo where notary_date>='" + fromYear + "-01-01 00:00:00' and notary_date<'" + toYear_ + "-01-01 00:00:00' and contract_number in (SELECT contract_number FROM MasterContractBO GROUP BY contract_number HAVING COUNT(contract_number) > 1) ORDER BY LENGTH(contract_number) asc, contract_number asc, notary_date asc";
            masterContractBO = entityManagerCurrent.createQuery(hql).getResultList();

            if(masterContractBO != null && masterContractBO.size() > 0) {
                int j = 0;      //trùng lặp lần thứ j
                int yearCheck = 0;   // năm trùng lặp
                String contract_number_check = "";
                for (int i = 0; i < masterContractBO.size(); i++) {
                    MasterContractBO update = masterContractBO.get(i);
                    //set contract_number_new
                    String contract_number = update.getContract_number();
                    Timestamp stamp = update.getNotary_date();
                    Date date = dateformat.parse(convertTimeStampToString(stamp));
                    int year = Integer.parseInt(df.format(date));
                    try {
                        if (contract_number_check.equals(contract_number) && year == yearCheck) {
                            if (j == 0) {
                                j = 1;
                                update.setContract_number_new(year + "/" + contract_number + "_" + j);
                            } else {
                                update.setContract_number_new(year + "/" + contract_number + "_" + j);
                            }
                            contract_number_check = contract_number;
                            yearCheck = year;
                            j++;
                        } else {
                            j = 0;
                            update.setContract_number_new(year + "/" + contract_number);
                            contract_number_check = contract_number;
                            yearCheck = year;
                        }
                    } catch (Exception e) {
                        System.out.println(" lay so hop dong " + e.getMessage());
                    }
                    entityManagerCurrent.merge(update);
                    commitBat(entityManagerCurrent, i, "loi commit updateContractNumberNew");
                }
            }

        }catch (Exception e){
            System.out.println("loi tai updateContractNumberNew: " + e.getMessage());
        }finally {
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        }
        return true;
    }

    @Override
    public void deleteWhereContractNumberMasterNew(String fromYear, String toYear) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        int toYear_ = Integer.parseInt(toYear) + 1;
        try {
            String hql = "DELETE from MasterContractBO where notary_date>='" + fromYear + "-01-01 00:00:00' and notary_date<'" + toYear_ + "-01-01 00:00:00'";
            entityManagerCurrent.getTransaction().begin();
            entityManagerCurrent.createQuery(hql).executeUpdate();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        }catch (Exception e){
            System.out.println("loi tai deleteWhereContractNumberMasterNew "+e.getMessage());
        }
    }

    @Override
    public List<MasterContract> selectContractMasterConvertById(Long id) {
        List<MasterContract> result = null;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        MasterContractBO bo = null;
        try {
            entityManagerCurrent.getTransaction().begin();
            bo = (MasterContractBO) entityManagerCurrent.createQuery("select bo from MasterContractBO bo where id=:id").setParameter("id",id).getSingleResult();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

            result = new ArrayList<>();
            if(bo != null){
                MasterContract form = genInfoMasterContract(bo);
                result.add(form);
            }

        }catch (Exception e){
            System.out.println("selectContractMasterConvertById no result");
            return null;
        }

        return result;
    }

    @Override
    public Boolean editContractMasterConvert(List<MasterContractBO> masterContractBO) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();

            for(int i=0;i<masterContractBO.size();i++) {
                MasterContractBO bo = masterContractBO.get(i);
                //MasterContractBO check = findMasterContractBO(update.getId());
                if (bo.getContract_number() != null) {
                    if(bo.getContent() != null) {
                        try {
                            bo.setContract_number(bo.getContract_number().trim());
                            entityManagerCurrent.merge(bo);

                        } catch (Exception e) {
                            System.out.println(" loi saveOrUpdateMasterContractBO " + e.getMessage());
                        }
                    }
                }
                commitBat(entityManagerCurrent, i, "loi commit saveOrUpdateMasterContractBO");
            }

        }catch (Exception e){
            System.out.println("loi tai saveOrUpdateMasterContractBO vong thu: " + e.getMessage());
        }finally {
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        }
        return true;
    }

    public MasterContractBO findMasterContractBO(Long id) {

        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        MasterContractBO bo = null;
        try {
            entityManagerCurrent.getTransaction().begin();
            bo = (MasterContractBO) entityManagerCurrent.createQuery("select bo from MasterContractBO bo where id=:id").setParameter("id",id).getSingleResult();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        }catch (Exception e){
            System.out.println("findMasterContractBO no result");
            return null;
        }

        return bo;
    }

    @Override
    public ConfigDBMaster getConfigDBMaster() {
        ConfigDBMaster configDBMaster = new ConfigDBMaster();
        try {
            configDBMaster.setMaster_dbName(getSystemConfigBO("master_dbName").getConfig_value());
            configDBMaster.setMaster_serverip(getSystemConfigBO("master_serverip").getConfig_value());
            configDBMaster.setMaster_serverport(getSystemConfigBO("master_serverport").getConfig_value());
            configDBMaster.setMaster_driver(getSystemConfigBO("master_driver").getConfig_value());
            configDBMaster.setMaster_databaseUserName(getSystemConfigBO("master_databaseUserName").getConfig_value());
            configDBMaster.setMaster_databasePassword(getSystemConfigBO("master_databasePassword").getConfig_value());
        }catch (Exception e){
            System.out.println("loi tai getConfigDBMaster");
            return new ConfigDBMaster();
        }
        return configDBMaster;
    }

    @Override
    public Boolean updateConfigDBMaster(ConfigDBMaster configDBMasterUpdate) {
        if(configDBMasterUpdate == null){
            return false;
        }
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            saveOrUpdateSystemConfigBO("master_dbName",configDBMasterUpdate.getMaster_dbName());
            saveOrUpdateSystemConfigBO("master_serverip",configDBMasterUpdate.getMaster_serverip());
            saveOrUpdateSystemConfigBO("master_serverport",configDBMasterUpdate.getMaster_serverport());
            saveOrUpdateSystemConfigBO("master_driver",configDBMasterUpdate.getMaster_driver());
            saveOrUpdateSystemConfigBO("master_databaseUserName",configDBMasterUpdate.getMaster_databaseUserName());
            saveOrUpdateSystemConfigBO("master_databasePassword", Base64.getEncoder().encodeToString(configDBMasterUpdate.getMaster_databasePassword().getBytes()));
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        }catch (Exception e){
            System.out.println("loi tai updateConfigDBMaster "+e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Boolean connectTestConfigDBMaster(ConfigDBMaster configDBMaster) {
        boolean check = true;
        ConnectUtils cn = new ConnectUtils();
        Connection conn = cn.connect(getConfigDBMaster());
        if(conn == null) {
            check = false;
        }else {
            try {
                cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return check;
    }

    @Override
    public SystemConfigBO getSystemConfigBO(String config_key) {
        SystemConfigBO systemConfigBO = new SystemConfigBO();
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            systemConfigBO = (SystemConfigBO) entityManagerCurrent.createQuery("select bo from SystemConfigBO bo where config_key=:config_key").setParameter("config_key",config_key).getSingleResult();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        }catch (Exception e){
            System.out.println("loi tai getSystemConfigBO "+e.getMessage());
            return null;
        }
        return systemConfigBO;
    }

    public void saveOrUpdateSystemConfigBO(String config_key, String config_value) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            SystemConfigBO systemConfigBO  = getSystemConfigBO(config_key);
            if(systemConfigBO == null) {
                systemConfigBO = new SystemConfigBO();
                systemConfigBO.setConfig_key(config_key);
                systemConfigBO.setConfig_value(config_value);
                entityManagerCurrent.persist(systemConfigBO);
            }else {
                systemConfigBO.setConfig_value(config_value);
                entityManagerCurrent.merge(systemConfigBO);
            }
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        }catch (Exception e){
            System.out.println("loi tai updateSystemConfigBO "+e.getMessage());
        }
    }

    @Override
    public Boolean deleteContractMasterConvertById(String listIdSelect) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            try {
                String[] id = listIdSelect.split(";");
                for(int i=0;i<id.length;i++) {
                    Long id_ = Long.parseLong(id[i]);
                    //String hql = "DELETE from MasterContractBO bo WHERE bo.id =" + id_;
                    //entityManagerCurrent.createQuery(hql).executeUpdate();
                    MasterContractBO bo = findMasterContractBO(id_);
                    bo.setType_delete(Constants.TYPE_DELETE);
                    entityManagerCurrent.merge(bo);
                    commitBat(entityManagerCurrent, i, "loi commit deleteContractMasterConvertById");
                }
            } catch (Exception e) {
                System.out.println("deleteContractMasterConvertById " + e.getMessage());
            }
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        }catch (Exception e){
            System.out.println("loi tai deleteContractMasterConvertById" + e.getMessage());
        }
        return true;
    }

    @Override
    public Long countMaxContractMasterConvert(String stringFilter){
        Long result__ = 0L;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            try {
                result__ = (Long) entityManagerCurrent.createQuery("select max(id) from MasterContractBO bo " + stringFilter).getSingleResult();
            }catch (Exception e){
                e.getMessage();
            }
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        } catch (Exception e) {
            System.out.println("loi tai countContractMasterConvert "+e.getMessage());
        }
        return result__;
    }

    @Override
    public Long countMinContractMasterConvert(String stringFilter){
        Long result__ = 0L;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            try {
                result__ = (Long) entityManagerCurrent.createQuery("select min(id) from MasterContractBO bo " + stringFilter).getSingleResult();
            }catch (Exception e){
                e.getMessage();
            }
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        } catch (Exception e) {
            System.out.println("loi tai countContractMasterConvert "+e.getMessage());
        }
        return result__;
    }

    @Override
    public Long countTotal(int page, int fromYear, int toYear){
        int ROW_PER_PAGE_INDEX = Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","row_per_page_index_master_dong_bo"));
        int toYear_ = toYear + 1;
        int offset = ROW_PER_PAGE_INDEX * (page - 1) ;
        int toOffset = offset + ROW_PER_PAGE_INDEX;

        Long result__ = 0L;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            try {
                result__ = (Long) entityManagerCurrent.createQuery("select count(1) from MasterContractBO bo WHERE id > " + offset + " and id <= " + toOffset + " and notary_date>='" + fromYear + "-01-01 00:00:00' and notary_date<'" + toYear_ + "-01-01 00:00:00'")
                        .getSingleResult();
            }catch (Exception e){
                e.getMessage();
            }
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        } catch (Exception e) {
            System.out.println("loi tai countContractMasterConvert "+e.getMessage());
        }
        return result__;
    }

    @Override
    public Long countTotal(String stringFilter){
        Long result__ = 0L;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            try {
                result__ = (Long) entityManagerCurrent.createQuery("select count(1) from MasterContractBO bo " + stringFilter)
                        .getSingleResult();
            }catch (Exception e){
                e.getMessage();
            }
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
        } catch (Exception e) {
            System.out.println("loi tai countContractMasterConvert "+e.getMessage());
        }
        return result__;
    }

    @Override
    public Long countTotalDBSQL2000(String stringFilter){

        ConnectUtils cn = new ConnectUtils();
        Connection conn = cn.connect(getConfigDBMaster());
        Statement stmt = null;
        ResultSet result = null;
        Long kq = 0L;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery("select count(1) as total from REPERTOIRE r, PERSONNE, TYPE_DOSSIER tp where r.PERS_CODE *= PERSONNE.PERS_CODE and r.REP_SUITE  = tp.TYPDOSS_CODE "
                    + stringFilter);

            while (result.next()) {
                kq = result.getLong("total");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("loi tai countTotal : " + e.getMessage());
        }

        return kq;
    }

    @Override
    public List<MasterContractBO> searchInforLimit(int page, int fromYear, int toYear) {
        int ROW_PER_PAGE_INDEX = Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","row_per_page_index_master_dong_bo"));
        int toYear_ = toYear + 1;
        int offset = (ROW_PER_PAGE_INDEX) * (page - 1) ;
        int toOffset = offset + (ROW_PER_PAGE_INDEX);
        List<MasterContractBO> result = null;
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            result = entityManagerCurrent.createQuery("select bo from MasterContractBO bo WHERE id > " + offset + " and id <= " + toOffset + " and notary_date>='" + fromYear + "-01-01 00:00:00' and notary_date<'" + toYear_ + "-01-01 00:00:00' order by LENGTH(contract_number) asc, contract_number asc").getResultList();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        }catch (Exception e){
            System.out.println("selectContractMasterConvertById no result");
            return null;
        }

        return result;
    }

    @Override
    public Boolean setTypeDuplicateContent(int type_duplicate, List<MasterContractBO> list){
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            for (int i = 0; i < list.size(); i++) {
                MasterContractBO update = list.get(i);
                update.setType_duplicate_content(type_duplicate);
                entityManagerCurrent.merge(update);
                commitBat(entityManagerCurrent, i, "loi commit setTypeDuplicateContent");
            }
        }catch (Exception e){
            System.out.println("loi tai setTypeDuplicateContent: " + e.getMessage());
        }finally {
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++hoan thanh setTypeDuplicateContent +++++++++++++++++++++++++++++++++++++++++++++++");
        }
        return true;
    }

    @Override
    public Boolean addTransactionProperty(List<TransactionPropertyBo> add, List<MasterContractBO> update) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        try {
            entityManagerCurrent.getTransaction().begin();
            for(int i=0;i<add.size();i++) {
                TransactionPropertyBo transactionPropertyBo = add.get(i);
                TransactionPropertyBo check = findTransactionPropertyBo(transactionPropertyBo.getContract_number());
                ContractBO contractBO = genTransactionToContractBO(transactionPropertyBo);
                if(check == null) {
                    try {
                        entityManagerCurrent.persist(contractBO);
                        transactionPropertyBo.setContract_id(contractBO.getId());

                        entityManagerCurrent.persist(transactionPropertyBo);

                        if(transactionPropertyBo.getProperty_info() != null && !"".equals(transactionPropertyBo.getProperty_info())) {
                            PropertyBo propertyBO = genPropertyBO(transactionPropertyBo, null);
                            entityManagerCurrent.persist(propertyBO);

                            ContractPropertyBO contractPropertyBO = new ContractPropertyBO(contractBO.getId(), propertyBO.getId());
                            entityManagerCurrent.persist(contractPropertyBO);
                        }
                    } catch (Exception e) {
                        System.out.println(" loi addTransactionProperty " + e.getMessage());
                    }
                }else {
                    try {
                        transactionPropertyBo.setId(check.getTpid());
                        transactionPropertyBo.setContract_id(check.getContract_id());
                        entityManagerCurrent.merge(transactionPropertyBo);

                        contractBO.setId(check.getContract_id());
                        entityManagerCurrent.merge(contractBO);

                        if(transactionPropertyBo.getProperty_info() != null && !"".equals(transactionPropertyBo.getProperty_info())) {
                            ContractPropertyBO contractPropertyBO = findContractPropertyBO(contractBO.getId());

                            PropertyBo propertyBO = genPropertyBO(transactionPropertyBo, contractPropertyBO.getProperty_id());
                            entityManagerCurrent.merge(propertyBO);
                        }

                    } catch (Exception e) {
                        System.out.println(" loi addTransactionProperty " + e.getMessage());
                    }
                }
                commitBat(entityManagerCurrent, i, "loi commit addTransactionPropert");
            }

            for(int i=0;i<update.size();i++) {
                MasterContractBO masterContractBO = update.get(i);
                masterContractBO.setType_master_to_transaction(Constants.VALID_MASTER_TO_TRANSACTION);
                try {
                    entityManagerCurrent.merge(masterContractBO);
                } catch (Exception e) {
                    System.out.println(" loi addTransactionProperty " + e.getMessage());
                }
                commitBat(entityManagerCurrent, i, "loi commit addTransactionPropert");
            }
        }catch (Exception e){
            System.out.println(" loi addTransactionPropert " + e.getMessage());
        }finally {
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();
            System.out.println("++++++++++++++++++++++++++++++hoan thanh addTransactionProperty++++++++++++++++++++++++++++++++");
        }
        return true;
    }
    public TransactionPropertyBo findTransactionPropertyBo(String contractNumber) {
        EntityManager entityManagerCurrent = entityManagerFactory.createEntityManager();
        TransactionPropertyBo bo = null;
        try {
            entityManagerCurrent.getTransaction().begin();
            bo = (TransactionPropertyBo) entityManagerCurrent.createQuery("select bo from TransactionPropertyBo bo where contract_number=:contract_number and code_template = 60 ")
                    .setParameter("contract_number", contractNumber).getSingleResult();
            entityManagerCurrent.getTransaction().commit();
            entityManagerCurrent.clear();
            entityManagerCurrent.close();

        }catch (Exception e){
            System.out.println("findTransactionPropertyBo no result");
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
    public ContractBO genTransactionToContractBO(TransactionPropertyBo bo){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        ContractBO form = new ContractBO();
        form.setContract_template_id(60L);
        if (bo.getContract_number() != null && !"".equals(bo.getContract_number())){
            form.setContract_number(bo.getContract_number());
        }
        if (bo.getContract_value() != null && !"".equals(bo.getContract_value())){
            form.setContract_value(bo.getContract_value());
        }
        if (bo.getNotary_place() != null && !"".equals(bo.getNotary_place())){
            form.setNotary_place(bo.getNotary_place());
        }
        if (bo.getBank_id() != null && bo.getBank_id() != -1L){
            form.setBank_id(bo.getBank_id());
        }
        if (bo.getCancel_status() != null && bo.getCancel_status() != -1L){
            form.setCancel_status(bo.getCancel_status());
        }
        if (bo.getCancel_description() != null && !"".equals(bo.getCancel_description())){
            form.setCancel_description(bo.getCancel_description());
        }
        if (bo.getContract_period() != null && !"".equals(bo.getContract_period())){
            form.setContract_period(bo.getContract_period());
        }
        if (bo.getMortage_cancel_flag() != null && bo.getMortage_cancel_flag() != -1L){
            form.setMortage_cancel_flag(bo.getMortage_cancel_flag());
        }
        if (bo.getMortage_cancel_date() != null && !"".equals(bo.getMortage_cancel_date())){
            form.setMortage_cancel_date(bo.getMortage_cancel_date());
        }
        if (bo.getMortage_cancel_note() != null && !"".equals(bo.getMortage_cancel_note())){
            form.setMortage_cancel_note(bo.getMortage_cancel_note());
        }
        if (bo.getNote() != null && !"".equals(bo.getNote())){
            form.setNote(bo.getNote());
        }
        if (bo.getEntry_user_id() != null && bo.getEntry_user_id() != -1L){
            form.setEntry_user_id(bo.getEntry_user_id());
        }
        if (bo.getEntry_user_name() != null && !"".equals(bo.getEntry_user_name())){
            form.setEntry_user_name(bo.getEntry_user_name());
        }
        if (bo.getUpdate_user_id() != null && bo.getUpdate_user_id() != -1L){
            form.setUpdate_user_id(bo.getUpdate_user_id());
        }
        if (bo.getUpdate_user_name() != null && !"".equals(bo.getUpdate_user_name())){
            form.setUpdate_user_name(bo.getUpdate_user_name());
        }
        if (bo.getBank_name() != null && !"".equals(bo.getBank_name())){
            form.setBank_name(bo.getBank_name());
        }
        if (bo.getBank_code() != null && !"".equals(bo.getBank_code())){
            form.setBank_code(bo.getBank_code());
        }
        if (bo.getJson_property() != null && !"".equals(bo.getJson_property())){
            form.setJson_property(bo.getJson_property());
        }
        if (bo.getJson_person() != null && !"".equals(bo.getJson_person())){
            form.setJson_person(bo.getJson_person());
        }
        form.setNotary_date(bo.getNotary_date());
        form.setEntry_user_id(bo.getEntry_user_id());
        form.setUpdate_user_id(bo.getUpdate_user_id());
        form.setEntry_date_time(bo.getEntry_date_time());
        form.setUpdate_date_time(time);
        form.setError_status(0L);
        form.setNotary_id(1000L);
        form.setSummary("Hợp đồng chuyển đổi từ master");
        form.setNotary_place(getSystemConfigBO("notary_office_name").getConfig_value());
        form.setRelation_object_a(bo.getRelation_object());
        form.setRelation_object_b(bo.getProperty_info());
        form.setSummary(bo.getTransaction_content());

        return form;
    }
    public PropertyBo genPropertyBO(TransactionPropertyBo form, Long id){
        PropertyBo bo = new PropertyBo();
        bo.setProperty_info(form.getProperty_info());
        bo.setType("99");
        bo.setId(id);
        return bo;
    }

    @Override
    public TransactionPropertyBo genTransactionPropertyBo(MasterContractBO bo, String entry_user_id, String entry_user_name){
        TransactionPropertyBo form = new TransactionPropertyBo();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        form.setProperty_info(bo.getProperty_info());
        form.setTransaction_content("Hợp đồng chuyển đổi từ master");
        form.setNotary_office_name(bo.getNotary_office_name());
        form.setContract_number(bo.getContract_number_new());
        form.setContract_name(bo.getContract_name());
        form.setContract_kind(bo.getContract_kind());
        form.setCode_template(60L);
        form.setRelation_object(bo.getRelation_object());
        form.setNotary_person(bo.getNotary_person());
        form.setSyn_status(0);
        form.setEntry_user_id(Long.parseLong(entry_user_id));
        form.setEntry_user_name(entry_user_name);
        form.setEntry_date_time(timestamp);
        form.setUpdate_user_id(Long.parseLong(entry_user_id));
        form.setUpdate_user_name(entry_user_name);
        form.setUpdate_date_time(timestamp);
        form.setNotary_date(bo.getNotary_date());
        form.setSynchronize_id(getSystemConfigBO("system_authentication_id").getConfig_value());
        form.setContract_name("Hợp đồng chuyển đổi từ master");
        form.setMortage_cancel_flag(0L);
        form.setNotary_place(getSystemConfigBO("notary_office_name").getConfig_value());
        form.setTransaction_content(bo.getTransaction_content());

        return form;
    }

    public String convertTimeStampToString(Timestamp date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(date==null){
            return null;
        }
        else {
            String result = dateFormat.format(date);
            return result;
        }
    }

    public String convertTimeStampToStringFull(Timestamp date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");//dd/MM/yyyy HH:mm:ss
        if(date==null){
            return null;
        }
        else {
            String result = dateFormat.format(date);
            return result;
        }
    }

    public MasterContract genInfoMasterContract(MasterContractBO bo){
        MasterContract form = new MasterContract();

        if (bo.getId() != null && bo.getId() != -1L){
            form.setId(bo.getId());
        }
        if (bo.getContract_number() != null && !"".equals(bo.getContract_number())){
            form.setContract_number(bo.getContract_number());
        }
        if (bo.getContract_number_new() != null && !"".equals(bo.getContract_number_new())){
            form.setContract_number_new(bo.getContract_number_new());
        }
        if (bo.getNotary_date() != null && !"".equals(bo.getNotary_date())){
            form.setNotary_date(convertTimeStampToStringFull(bo.getNotary_date()));
        }
        if (bo.getRelation_object() != null && !"".equals(bo.getRelation_object())){
            form.setRelation_object(bo.getRelation_object());
        }
        if (bo.getContracttype_name() != null && !"".equals(bo.getContracttype_name())){
            form.setContracttype_name(bo.getContracttype_name());
        }
        if (bo.getProperty_info() != null && !"".equals(bo.getProperty_info())){
            form.setProperty_info(bo.getProperty_info());
        }
        if (bo.getNotary_person() != null && !"".equals(bo.getNotary_person())){
            form.setNotary_person(bo.getNotary_person());
        }
        if (bo.getSynchronize_id() != null && !"".equals(bo.getSynchronize_id())){
            form.setSynchronize_id(bo.getSynchronize_id());
        }
        if (bo.getNotary_office_name() != null && !"".equals(bo.getNotary_office_name())){
            form.setNotary_office_name(bo.getNotary_office_name());
        }
        if (bo.getContract_kind() != null && !"".equals(bo.getContract_kind())){
            form.setContract_kind(bo.getContract_kind());
        }
        if (bo.getContract_name() != null && !"".equals(bo.getContract_name())){
            form.setContract_name(bo.getContract_name());
        }
        if (bo.getEntry_user_id() != null && bo.getEntry_user_id() != -1L){
            form.setEntry_user_id(bo.getEntry_user_id());
        }
        if (bo.getEntry_user_name() != null && !"".equals(bo.getEntry_user_name())){
            form.setEntry_user_name(bo.getEntry_user_name());
        }
        if (bo.getEntry_date_time() != null && !"".equals(bo.getEntry_date_time())){
            form.setEntry_date_time(convertTimeStampToStringFull(bo.getEntry_date_time()));
        }
        if (bo.getUpdate_user_id() != null && bo.getUpdate_user_id() != -1L){
            form.setUpdate_user_id(bo.getUpdate_user_id());
        }
        if (bo.getUpdate_user_name() != null && !"".equals(bo.getUpdate_user_name())){
            form.setUpdate_user_name(bo.getUpdate_user_name());
        }
        if (bo.getUpdate_date_time() != null && !"".equals(bo.getUpdate_date_time())){
            form.setUpdate_date_time(convertTimeStampToStringFull(bo.getUpdate_date_time()));
        }
        if (bo.getContent() != null && !"".equals(bo.getContent())){
            form.setContent(bo.getContent());
        }
        if (bo.getType_delete() != -1L){
            form.setType_delete(bo.getType_delete());
        }
        if (bo.getType_duplicate_content() != -1L){
            form.setType_duplicate_content(bo.getType_duplicate_content());
        }
        if (bo.getType_master_to_transaction() != -1L){
            form.setType_master_to_transaction(bo.getType_master_to_transaction());
        }
        form.setTransaction_content(bo.getTransaction_content());

        return form;
    }

    public boolean commitBat(EntityManager entityManagerCurrent, int i, String comment){
        try {
            if (i > 0 && i % 50 == 0) {
                entityManagerCurrent.getTransaction().commit();
                entityManagerCurrent.getTransaction().begin();
                entityManagerCurrent.clear();
            }
        } catch (Exception e) {
            System.out.println(comment);
            e.getMessage();
            entityManagerCurrent.getTransaction().rollback();
            return false;
        }
        return true;
    }
}

