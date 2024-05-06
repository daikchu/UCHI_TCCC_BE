package com.vn.osp.notarialservices.masterConvert.task;

import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.utils.Constants;
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

public class MasterContractToTransactionThread implements Runnable {
    public static Long max;
    public static Long min;
    public static int threads= Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","index_thread_total_master"));
    public static int ROW_PER_PAGE_INDEX = Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","row_per_page_index_master_dong_bo"));
    private MasterConvertService masterConvertService;
    private static final Logger logger = Logger.getLogger(MasterContractToTransactionThread.class.getName());
    public static String entry_user_id;
    public static String entry_user_name;

    @Autowired
    public MasterContractToTransactionThread(final MasterConvertService masterConvertService) {
        this.masterConvertService = masterConvertService;
    }

    public void run() {
        String stringFilter = " Where type_duplicate_content = " + Constants.INVALID_DUPLICATE + " ";
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
            System.out.println("Thead MasterContractToTransactionThread run:"+i);
            MasterContractToTransactionCallable callable = new MasterContractToTransactionCallable(i,masterConvertService);
            callable.setEntry_user_id(entry_user_id);
            callable.setEntry_user_name(entry_user_name);
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

    public static Long getMax() {
        return max;
    }
    public static void setMax(Long max) {
        MasterContractToTransactionThread.max = max;
    }
    public static Long getMin() {
        return min;
    }
    public static void setMin(Long min) {
        MasterContractToTransactionThread.min = min;
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
        MasterContractToTransactionThread.entry_user_id = entry_user_id;
    }
    public static String getEntry_user_name() {
        return entry_user_name;
    }
    public static void setEntry_user_name(String entry_user_name) {
        MasterContractToTransactionThread.entry_user_name = entry_user_name;
    }
}
