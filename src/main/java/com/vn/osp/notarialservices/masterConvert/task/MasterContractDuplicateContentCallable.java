package com.vn.osp.notarialservices.masterConvert.task;

import com.vn.osp.notarialservices.masterConvert.domain.MasterContractBO;
import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.utils.Constants;
import static java.lang.Thread.sleep;

import java.util.List;
import java.util.concurrent.Callable;

public class MasterContractDuplicateContentCallable implements Callable {
    public static int fromYear;
    private static MasterContractDuplicateContentCallable instance;
    private MasterConvertService masterConvertService;
    private int page;

    public MasterContractDuplicateContentCallable(int pages, MasterConvertService masterConvertService) {
        this.page = pages;
        this.masterConvertService=masterConvertService;
    }

    @Override
    public Boolean call() throws Exception {
        //set type trùng nội dung
        String stringFilter = " WHERE YEAR(bo.notary_date)= " + fromYear + " and EXISTS "
                + " (SELECT b.content FROM MasterContractBO b WHERE bo.content=b.content and YEAR(b.notary_date) = " + fromYear
                + " GROUP BY b.content HAVING COUNT(b.content) > 1 ) "
                + " ORDER BY bo.content";
        List<MasterContractBO> list = masterConvertService.selectContractMasterConvert(stringFilter);
        masterConvertService.setTypeDuplicateContent(Constants.VALID_DUPLICATE, list);
        //set type ko trùng nội dung
        stringFilter = " WHERE YEAR(bo.notary_date)= " + fromYear + " and (bo.type_duplicate_content = " + Constants._DUPLICATE + " or bo.type_duplicate_content is null) ";
        list = masterConvertService.selectContractMasterConvert(stringFilter);
        masterConvertService.setTypeDuplicateContent(Constants.INVALID_DUPLICATE, list);
        sleep(1000);
        return true;
    }

    public static synchronized MasterContractDuplicateContentCallable getInstance(int pages, MasterConvertService masterConvertService) {
        if (instance != null) {
            instance.setMasterConvertService(masterConvertService);
        }
        if (instance == null) {
            instance = new MasterContractDuplicateContentCallable(pages,masterConvertService);
        }
        return instance;
    }

    public static int getFromYear() {
        return fromYear;
    }
    public static void setFromYear(int fromYear) {
        MasterContractDuplicateContentCallable.fromYear = fromYear;
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
