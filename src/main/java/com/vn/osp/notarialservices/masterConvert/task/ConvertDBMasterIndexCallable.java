package com.vn.osp.notarialservices.masterConvert.task;

import com.vn.osp.notarialservices.masterConvert.connectDBMaster.ConnectUtils;
import com.vn.osp.notarialservices.masterConvert.domain.MasterContractBO;
import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.masterConvert.service.MasterConvertServiceImpl;
import com.vn.osp.notarialservices.utils.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

public class ConvertDBMasterIndexCallable implements Callable {
    public static Long max;
    public static Long min;
    public static int threads= Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","index_thread_total_master"));
    public static int ROW_PER_PAGE_INDEX = Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","row_per_page_index_master_dong_bo"));
    private MasterConvertService masterConvertService;
    private int page;
    private static ConvertDBMasterIndexCallable instance;

    public ConvertDBMasterIndexCallable(int pages, MasterConvertService masterConvertService) {
        this.page = pages;
        this.masterConvertService=masterConvertService;
    }

    public Boolean call() throws Exception {
        ConvertDBMasterThread convertDBMasterThread = new ConvertDBMasterThread(masterConvertService);
        convertDBMasterThread.setMasterConvertService(masterConvertService);

        List<MasterContractBO> list =null;
        if(page*this.ROW_PER_PAGE_INDEX >= this.min) {
            int total= convertDBMasterThread.countTotal(page);
            System.out.println("tong so ban ghi thread "+page +" la: "+total);
            if(total>0) {
                list = convertDBMasterThread.searchInforLimit(page);
                masterConvertService.saveOrUpdateMasterContractBO(list);
            }
        }

        sleep(1000);
        return true;
    }

    public static synchronized ConvertDBMasterIndexCallable getInstance(int pages, MasterConvertService masterConvertService) {
        if (instance != null) {
            instance.setMasterConvertService(masterConvertService);
        }
        if (instance == null) {
            instance = new ConvertDBMasterIndexCallable(pages,masterConvertService);
        }
        return instance;
    }

    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public MasterConvertService getMasterConvertService() {
        return masterConvertService;
    }
    public void setMasterConvertService(MasterConvertService masterConvertService) {
        this.masterConvertService = masterConvertService;
    }
    public static Long getMax() {
        return max;
    }
    public static void setMax(Long max) {
        ConvertDBMasterIndexCallable.max = max;
    }
    public static Long getMin() {
        return min;
    }
    public static void setMin(Long min) {
        ConvertDBMasterIndexCallable.min = min;
    }
}
