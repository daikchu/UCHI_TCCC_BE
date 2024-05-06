package com.vn.osp.notarialservices.masterConvert.task;

import com.vn.osp.notarialservices.masterConvert.domain.MasterContractBO;
import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.utils.SystemProperties;

import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

public class ConvertContractNumberNewIndexCallable implements Callable {
    public static Long min;
    public static int threads= Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","index_thread_total_master"));
    public static int ROW_PER_PAGE_INDEX = Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","row_per_page_index_master_dong_bo"));
    private MasterConvertService masterConvertService;
    private int page;
    public static int fromYear;
    public static int toYear;
    private static ConvertContractNumberNewIndexCallable instance;

    public ConvertContractNumberNewIndexCallable(int pages, MasterConvertService masterConvertService) {
        this.page = pages;
        this.masterConvertService=masterConvertService;
    }

    public Boolean call() throws Exception {
        ConvertContractNumberNewThread convertContractNumberNewThread = new ConvertContractNumberNewThread(masterConvertService);
        convertContractNumberNewThread.setMasterConvertService(masterConvertService);

        List<MasterContractBO> list =null;
        if(page*this.ROW_PER_PAGE_INDEX >= this.min) {
            Long total= masterConvertService.countTotal(page, fromYear, toYear);
            System.out.println("tong so ban ghi thread "+page +" contractNumber new  la: "+total);
            if(total>0) {
                list = masterConvertService.searchInforLimit(page, fromYear, toYear);
                masterConvertService.saveOrUpdateContractNumberNew(list);
            }
        }
        sleep(1000);
        return true;
    }

    public static synchronized ConvertContractNumberNewIndexCallable getInstance(int pages, MasterConvertService masterConvertService) {
        if (instance != null) {
            instance.setMasterConvertService(masterConvertService);
        }
        if (instance == null) {
            instance = new ConvertContractNumberNewIndexCallable(pages,masterConvertService);
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
    public static ConvertContractNumberNewIndexCallable getInstance() {
        return instance;
    }
    public static void setInstance(ConvertContractNumberNewIndexCallable instance) {
        ConvertContractNumberNewIndexCallable.instance = instance;
    }
    public static int getFromYear() {
        return fromYear;
    }
    public static void setFromYear(int fromYear) {
        ConvertContractNumberNewIndexCallable.fromYear = fromYear;
    }
    public static int getToYear() {
        return toYear;
    }
    public static void setToYear(int toYear) {
        ConvertContractNumberNewIndexCallable.toYear = toYear;
    }
    public static Long getMin() {
        return min;
    }
    public static void setMin(Long min) {
        ConvertContractNumberNewIndexCallable.min = min;
    }
}
