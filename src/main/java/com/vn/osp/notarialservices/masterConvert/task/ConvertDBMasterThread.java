package com.vn.osp.notarialservices.masterConvert.task;

import com.vn.osp.notarialservices.masterConvert.connectDBMaster.ConnectUtils;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.Converter;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese.ConverterFactory;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese.ConverterFactoryImpl;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese.VietnameseEncoding;
import com.vn.osp.notarialservices.masterConvert.domain.MasterContractBO;
import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.masterConvert.service.MasterConvertServiceImpl;
import com.vn.osp.notarialservices.utils.Constants;
import com.vn.osp.notarialservices.utils.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class ConvertDBMasterThread implements Runnable {
    static ConnectUtils connectUtils = new ConnectUtils();
    static String SQL_WHERE_MASTER = "";
    public static String entry_user_id;
    public static String entry_user_name;
    public static int fromYear;
    public static int toYear;
    private static final Logger logger = Logger.getLogger(ConvertDBMasterThread.class.getName());
    public static Long max;
    public static Long min;
    public static int threads= Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","index_thread_total_master"));
    public static int ROW_PER_PAGE_INDEX = Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","row_per_page_index_master_dong_bo"));

    public static void setSqlWhereMaster() {
        String sqlWhereMaster="";
        if(toYear != fromYear) {
            sqlWhereMaster = "REP_AN_LEGAL>="+ fromYear +" and REP_AN_LEGAL<=" + toYear;
        }else {
            sqlWhereMaster = "REP_AN_LEGAL ="+ fromYear;
        }
        SQL_WHERE_MASTER = sqlWhereMaster;
    }

    ConverterFactory converterFactory = new ConverterFactoryImpl();
    private Converter converter = converterFactory.build(VietnameseEncoding.Encoding.TCVN3);
    private MasterConvertService masterConvertService;
    @Autowired
    public ConvertDBMasterThread(final MasterConvertService masterConvertService) {
        this.masterConvertService = masterConvertService;
    }

    public void run(){
        setSqlWhereMaster();
        this.max=count_max();
        this.min = count_min_REP_REF_UNIQUE();
        int numberPage = countPageIndex(this.max.intValue());
        int threads= this.threads;
        if(numberPage<threads){
            threads=numberPage;
        }
        if(threads > 0) {
            ExecutorService pool = Executors.newFixedThreadPool(threads);
            List<Future<Boolean>> list = new ArrayList<Future<Boolean>>();
            for (int i = 1; i <= numberPage; i++) {
                System.out.println("Thead run:" + i);
                ConvertDBMasterIndexCallable callable = new ConvertDBMasterIndexCallable(i, masterConvertService);
                callable.setMax(max);
                callable.setMin(min);
                Future<Boolean> future = pool.submit(callable);

                list.add(future);
            }

            for (Future<Boolean> fut : list) {
                try {
                    logger.info(new Date() + "::" + fut.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            pool.shutdown();
            ConvertContractNumberNewThread convertContractNumberNewThread = new ConvertContractNumberNewThread(masterConvertService);
            convertContractNumberNewThread.setMasterConvertService(masterConvertService);
            convertContractNumberNewThread.setFromYear(fromYear);
            convertContractNumberNewThread.setToYear(toYear);
            convertContractNumberNewThread.run();
        }
    }

    public List<MasterContractBO> searchInforLimit(int page) {
        List<MasterContractBO> list = new ArrayList<>();
        int offset = (ROW_PER_PAGE_INDEX) * (page - 1);
        int toOffset = offset + (ROW_PER_PAGE_INDEX);

        String filter = "";
        filter = "select * from REPERTOIRE r, PERSONNE, TYPE_DOSSIER tp where r.PERS_CODE *= PERSONNE.PERS_CODE and r.REP_SUITE  = tp.TYPDOSS_CODE "
                + " AND r.REP_REF_UNIQUE > " + offset + " and r.REP_REF_UNIQUE <= " + toOffset
                + " AND " + SQL_WHERE_MASTER + " order by REP_LEGAL asc"
        ;

        //lay list
        Connection conn = connectUtils.connect(masterConvertService.getConfigDBMaster());
        Statement stmt = null;
        ResultSet result = null;

        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery( filter);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            while (result.next()) {
                MasterContractBO add = new MasterContractBO();
                //tách các bên và nội dung
                String relation_object = null;
                String property_info = null;
                String contracttype_name = null;
                String notary_person = null;
                String contract_number = null;
                String _relation_object_or_content_ = null;
                String transaction_content = null;
                try {
                    contracttype_name = result.getString("REP_LIBEL")!=null?converter.convert(result.getString("REP_LIBEL")):null;
                    notary_person = result.getString("PERS_LIBEL")!=null?converter.convert(result.getString("PERS_LIBEL")):null;
                    contract_number= converter.convert(result.getString("REP_LEGAL"));

                    _relation_object_or_content_ = converter.convert(result.getString("TEXTE")).replaceAll("\r\n","");

                    String keyConfig = masterConvertService.getSystemConfigBO("master_convert_contents").getConfig_value();

                    String[] listKeyConfig = keyConfig.split("##");

                    String[] relation_object_or_content = new String[2];
                    relation_object_or_content[0] = null;
                    relation_object_or_content[1] = null;

                    //nếu tài sản ở đầu
                    if(_relation_object_or_content_.substring(0,7).equals("Tài sản:")){
                        int checkProperty = _relation_object_or_content_.indexOf("Bên");
                        relation_object_or_content[0] = _relation_object_or_content_.substring(checkProperty);
                        relation_object_or_content[1] = _relation_object_or_content_.substring(7,checkProperty);
                    }
                    if(_relation_object_or_content_.substring(0,8).equals("Tài sản :")){
                        int checkProperty = _relation_object_or_content_.indexOf("Bên");
                        relation_object_or_content[0] = _relation_object_or_content_.substring(checkProperty);
                        relation_object_or_content[1] = _relation_object_or_content_.substring(8,checkProperty);
                    }

                    //chạy for lấy bên liên quan và tài sản
                    if(relation_object_or_content[0] == null && relation_object_or_content[1] == null) {
                        for (int i = 0; i <= listKeyConfig.length; i++) {
                            if (relation_object_or_content[0] == null && relation_object_or_content[1] == null) {
                                int checkProperty = _relation_object_or_content_.indexOf(listKeyConfig[i]);
                                if(checkProperty > 0) {
                                    relation_object_or_content[0] = _relation_object_or_content_.substring(0, checkProperty);
                                    relation_object_or_content[1] = _relation_object_or_content_.substring(checkProperty).replaceAll("Nội dung:","").replaceAll("Nội dung :","");
                                }
                            }else {
                                break;
                            }
                        }
                    }
                    relation_object = relation_object_or_content[0];
                    property_info = relation_object_or_content[1];

                    //lấy nội dung
                    int checkTansactionContent = _relation_object_or_content_.indexOf("Bên Hợp đồng");
                    if(checkTansactionContent < 0) {
                        checkTansactionContent = _relation_object_or_content_.indexOf("Bên HĐ");
                        if(checkTansactionContent > 0)
                            transaction_content = _relation_object_or_content_.substring(checkTansactionContent);
                    }else {
                        transaction_content = _relation_object_or_content_.substring(checkTansactionContent);
                    }
                    if(transaction_content == null) {
                        checkTansactionContent = _relation_object_or_content_.indexOf("Nội dung:");

                        int checkExitTansactionContent = _relation_object_or_content_.indexOf("Nội dung:Tài sản");
                        if(checkExitTansactionContent < 0) {
                            checkExitTansactionContent = _relation_object_or_content_.indexOf("Nội dung: Tài sản");
                        }
                        if(checkExitTansactionContent < 0) {
                            checkExitTansactionContent = _relation_object_or_content_.indexOf("Nội dung :Tài sản");
                        }
                        if(checkExitTansactionContent < 0) {
                            checkExitTansactionContent = _relation_object_or_content_.indexOf("Nội dung : Tài sản");
                        }

                        if(checkExitTansactionContent < 0) {
                            if (checkTansactionContent < 0) {
                                checkTansactionContent = _relation_object_or_content_.indexOf("Nội dung :");
                                if (checkTansactionContent > 0)
                                    transaction_content = _relation_object_or_content_.substring(checkTansactionContent + 10);
                            } else {
                                transaction_content = _relation_object_or_content_.substring(checkTansactionContent + 9);
                            }
                        }
                    }

                }catch (Exception e){
                    System.out.println("ban ghi ko co noi dung tai san" + e.getMessage());
                }

                add.setId(result.getLong("REP_REF_UNIQUE"));
                add.setContract_number(contract_number);
                add.setNotary_date(result.getTimestamp("REP_DATE"));
                add.setRelation_object(relation_object);
                add.setContracttype_name(contracttype_name);
                add.setProperty_info(property_info);
                add.setNotary_person(notary_person);
                /*add.setSynchronize_id(result.getString(""));
                add.setContract_name(result.getString(""));*/
                add.setContract_kind("5");
                add.setEntry_user_id(Long.parseLong(getEntry_user_id()));
                add.setEntry_user_name(getEntry_user_name());
                add.setEntry_date_time(timestamp);
                add.setUpdate_user_id(Long.parseLong(getEntry_user_id()));
                add.setUpdate_user_name(getEntry_user_name());
                add.setUpdate_date_time(timestamp);
                add.setContent(_relation_object_or_content_);
                add.setType_master_to_transaction(Constants.INVALID_MASTER_TO_TRANSACTION);
                add.setType_duplicate_content(Constants._DUPLICATE);
                add.setType_delete(Constants.TYPE_NO_DELETE);
                add.setTransaction_content(transaction_content);
                list.add(add);
            }

            conn.close();

        } catch (Exception e) {
            System.out.println("loi tai searchInforLimit : " + e.getMessage());
        }

        return list;
    }
    public int countTotal(int page){
        int offset = (ROW_PER_PAGE_INDEX) * (page - 1) ;
        int toOffset = offset + (ROW_PER_PAGE_INDEX);

        Connection conn = connectUtils.connect(masterConvertService.getConfigDBMaster());
        Statement stmt = null;
        ResultSet result = null;
        int kq = 0;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery("select count(1) as total from REPERTOIRE r, PERSONNE, TYPE_DOSSIER tp where r.PERS_CODE *= PERSONNE.PERS_CODE and r.REP_SUITE  = tp.TYPDOSS_CODE "
                    + " AND r.REP_REF_UNIQUE > " + offset + " and r.REP_REF_UNIQUE <= " + toOffset
                    + " AND " + SQL_WHERE_MASTER);

            while (result.next()) {
                kq = result.getInt("total");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("loi tai countTotal : " + e.getMessage());
        }

        return kq;
    }
    public Long count_max(){
        Connection conn = connectUtils.connect(masterConvertService.getConfigDBMaster());
        Statement stmt = null;
        ResultSet result = null;
        Long kq = 0L;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery("select max(r.REP_REF_UNIQUE) as total from REPERTOIRE r, PERSONNE, TYPE_DOSSIER tp where r.PERS_CODE *= PERSONNE.PERS_CODE and r.REP_SUITE  = tp.TYPDOSS_CODE "
                    + " AND " + SQL_WHERE_MASTER);

            while (result.next()) {
                kq = result.getLong("total");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("loi tai count_max : " + e.getMessage());
        }

        return kq;
    }
    public Long count_min_REP_REF_UNIQUE(){
        Connection conn = connectUtils.connect(masterConvertService.getConfigDBMaster());
        Statement stmt = null;
        ResultSet result = null;
        Long kq = 0L;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery("select min(r.REP_REF_UNIQUE) as total from REPERTOIRE r, PERSONNE, TYPE_DOSSIER tp where r.PERS_CODE *= PERSONNE.PERS_CODE and r.REP_SUITE  = tp.TYPDOSS_CODE "
                    + " AND " + SQL_WHERE_MASTER);

            while (result.next()) {
                kq = result.getLong("total");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("loi tai count_min_REP_REF_UNIQUE : " + e.getMessage());
        }

        return kq;
    }
    public int countPageIndex(int total) {
        int rowPerpage = (ROW_PER_PAGE_INDEX);
        int result = 0;
        result = total / rowPerpage;
        int temp = total % rowPerpage;
        if (temp > 0) {
            result = result + 1;
            return result;
        } else return result;
    }


    public MasterConvertService getMasterConvertService() {
        return masterConvertService;
    }
    public void setMasterConvertService(MasterConvertService masterConvertService) {
        this.masterConvertService = masterConvertService;
    }
    public static String getEntry_user_id() {
        return entry_user_id;
    }
    public static void setEntry_user_id(String entry_user_id) {
        ConvertDBMasterThread.entry_user_id = entry_user_id;
    }
    public static String getEntry_user_name() {
        return entry_user_name;
    }
    public static void setEntry_user_name(String entry_user_name) {
        ConvertDBMasterThread.entry_user_name = entry_user_name;
    }
    public static int getFromYear() {
        return fromYear;
    }
    public static void setFromYear(int fromYear) {
        ConvertDBMasterThread.fromYear = fromYear;
    }
    public static int getToYear() {
        return toYear;
    }
    public static void setToYear(int toYear) {
        ConvertDBMasterThread.toYear = toYear;
    }
    public static String getSqlWhereMaster() {
        return SQL_WHERE_MASTER;
    }
}
