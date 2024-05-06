package com.vn.osp.notarialservices.masterConvert.task;

import com.vn.osp.notarialservices.masterConvert.domain.MasterContractBO;
import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.transaction.domain.TransactionPropertyBo;
import com.vn.osp.notarialservices.utils.SystemProperties;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

public class MasterContractToTransactionCallable implements Callable {
    public static int threads= Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","index_thread_total_master"));
    public static int ROW_PER_PAGE_INDEX = Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","row_per_page_index_master_dong_bo"));
    private MasterConvertService masterConvertService;
    private int page;
    public static Long min;
    private static MasterContractToTransactionCallable instance;
    public static String entry_user_id;
    public static String entry_user_name;

    public MasterContractToTransactionCallable(int pages, MasterConvertService masterConvertService) {
        this.page = pages;
        this.masterConvertService=masterConvertService;
    }

    public Boolean call() throws Exception {
        if(page*this.ROW_PER_PAGE_INDEX >= this.min) {
            System.out.println("MasterContractToTransactionCallable page: "+page);
            int offset = ROW_PER_PAGE_INDEX * (page - 1);
            int toOffset = offset + ROW_PER_PAGE_INDEX;
            String stringFilter = " WHERE id >" + offset + " and id <= " + toOffset;
            List<MasterContractBO> list = masterConvertService.selectContractMasterConvert(stringFilter);
            List<TransactionPropertyBo> add = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                MasterContractBO bo = list.get(i);
                //if(bo.getType_master_to_transaction() == 1) continue;
                TransactionPropertyBo form = masterConvertService.genTransactionPropertyBo(bo, entry_user_id, entry_user_name);
                add.add(form);
            }
            masterConvertService.addTransactionProperty(add, list);
        }

        sleep(1000);
        return true;
    }

    public static synchronized MasterContractToTransactionCallable getInstance(int pages, MasterConvertService masterConvertService) {
        if (instance != null) {
            instance.setMasterConvertService(masterConvertService);
        }
        if (instance == null) {
            instance = new MasterContractToTransactionCallable(pages, masterConvertService);
        }
        return instance;
    }

    public MasterConvertService getMasterConvertService() {
        return masterConvertService;
    }
    public void setMasterConvertService(MasterConvertService masterConvertService) {
        this.masterConvertService = masterConvertService;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public static Long getMin() {
        return min;
    }
    public static void setMin(Long min) {
        MasterContractToTransactionCallable.min = min;
    }
    public static String getEntry_user_id() {
        return entry_user_id;
    }
    public static void setEntry_user_id(String entry_user_id) {
        MasterContractToTransactionCallable.entry_user_id = entry_user_id;
    }
    public static String getEntry_user_name() {
        return entry_user_name;
    }
    public static void setEntry_user_name(String entry_user_name) {
        MasterContractToTransactionCallable.entry_user_name = entry_user_name;
    }
}
