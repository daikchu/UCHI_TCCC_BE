package com.vn.osp.notarialservices.masterConvert.task;

import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.utils.SystemProperties;

import java.util.concurrent.Callable;
import static java.lang.Thread.sleep;

public class UpdateContractNumberNewCallable implements Callable {
    public static int fromYear;
    private static UpdateContractNumberNewCallable instance;
    private MasterConvertService masterConvertService;
    private int page;


    public UpdateContractNumberNewCallable(int pages, MasterConvertService masterConvertService) {
        this.page = pages;
        this.masterConvertService=masterConvertService;
    }

    public Boolean call() throws Exception {
        masterConvertService.updateContractNumberNew(fromYear, fromYear);
        sleep(1000);
        return true;
    }

    public static synchronized UpdateContractNumberNewCallable getInstance(int pages, MasterConvertService masterConvertService) {
        if (instance != null) {
            instance.setMasterConvertService(masterConvertService);
        }
        if (instance == null) {
            instance = new UpdateContractNumberNewCallable(pages,masterConvertService);
        }
        return instance;
    }

    public static int getFromYear() {
        return fromYear;
    }

    public static void setFromYear(int fromYear) {
        UpdateContractNumberNewCallable.fromYear = fromYear;
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
}
