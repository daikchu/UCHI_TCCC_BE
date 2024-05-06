package com.vn.osp.notarialservices.masterConvert.task;

import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.utils.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class ConvertContractNumberNewThread implements Runnable {
    public static Long max;
    public static Long min;
    public static int threads= Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","index_thread_total_master"));
    public static int ROW_PER_PAGE_INDEX = Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","row_per_page_index_master_dong_bo"));
    public static int fromYear;
    public static int toYear;
    private static final Logger logger = Logger.getLogger(ConvertContractNumberNewThread.class.getName());
    private MasterConvertService masterConvertService;
    @Autowired
    public ConvertContractNumberNewThread(final MasterConvertService masterConvertService) {
        this.masterConvertService = masterConvertService;
    }

    public void run() {
        int toYear_ = toYear + 1;
        String stringFilter = " WHERE notary_date>='" + fromYear + "-01-01 00:00:00' and notary_date<'" + toYear_ + "-01-01 00:00:00'";
        this.max = masterConvertService.countMaxContractMasterConvert(stringFilter);
        this.min = masterConvertService.countMinContractMasterConvert(stringFilter);
        int numberPage = countPageIndex(max.intValue());
        int threads= this.threads;
        if(numberPage<threads){
            threads=numberPage;
        }
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<Boolean>> list = new ArrayList<Future<Boolean>>();
        for(int i=1;i<=numberPage;i++){
            System.out.println("Thead run:"+i);
            ConvertContractNumberNewIndexCallable callable = new ConvertContractNumberNewIndexCallable(i,masterConvertService);
            callable.setFromYear(fromYear);
            callable.setToYear(toYear);
            callable.setMin(min);
            Future<Boolean> future = pool.submit(callable);

            list.add(future);
        }

        for(Future<Boolean> fut : list){
            try {
                logger.info(new Date()+ "::"+fut.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
        UpdateContractNumberNewThread updateContractNumberNewThread = new UpdateContractNumberNewThread();
        updateContractNumberNewThread.setFromYear(fromYear);
        updateContractNumberNewThread.setFromYear_begin(fromYear);
        updateContractNumberNewThread.setToYear(toYear);
        updateContractNumberNewThread.setMasterConvertService(masterConvertService);
        updateContractNumberNewThread.run();
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
    public static int getFromYear() {
        return fromYear;
    }
    public static void setFromYear(int fromYear) {
        ConvertContractNumberNewThread.fromYear = fromYear;
    }
    public static int getToYear() {
        return toYear;
    }
    public static void setToYear(int toYear) {
        ConvertContractNumberNewThread.toYear = toYear;
    }
}
