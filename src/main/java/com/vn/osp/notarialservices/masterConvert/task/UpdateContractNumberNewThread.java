package com.vn.osp.notarialservices.masterConvert.task;

import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.utils.SystemProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class UpdateContractNumberNewThread implements Runnable {
    public static int fromYear;
    public static int toYear;
    private static final Logger logger = Logger.getLogger(UpdateContractNumberNewThread.class.getName());
    private MasterConvertService masterConvertService;
    public static int fromYear_begin;
    public static int threads= Integer.parseInt(SystemProperties.getSystemPropertyFromClassPath("system.properties","index_thread_total_master"));


    public void run() {
        int threads= this.threads;
        int numberPage = (toYear - fromYear) + 1;
        if(numberPage<threads){
            threads=numberPage;
        }
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<Boolean>> list = new ArrayList<Future<Boolean>>();
        for(int i=1;i<=numberPage;i++){
            System.out.println("Thead run UpdateContractNumberNewThread:"+i);
            UpdateContractNumberNewCallable callable = new UpdateContractNumberNewCallable(i,masterConvertService);
            callable.setMasterConvertService(masterConvertService);
            callable.setFromYear(fromYear);
            Future<Boolean> future = pool.submit(callable);
            list.add(future);
            fromYear = fromYear + 1;
        }

        for(Future<Boolean> fut : list){
            try {
                logger.info(new Date()+ "::"+fut.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();

        MasterContractDuplicateContentThread masterContractDuplicateContentThread = new MasterContractDuplicateContentThread();
        masterContractDuplicateContentThread.setFromYear(fromYear_begin);
        masterContractDuplicateContentThread.setToYear(toYear);
        masterContractDuplicateContentThread.setMasterConvertService(masterConvertService);
        masterContractDuplicateContentThread.run();
    }

    public static int getFromYear() {
        return fromYear;
    }

    public static void setFromYear(int fromYear) {
        UpdateContractNumberNewThread.fromYear = fromYear;
    }

    public static int getToYear() {
        return toYear;
    }

    public static void setToYear(int toYear) {
        UpdateContractNumberNewThread.toYear = toYear;
    }

    public MasterConvertService getMasterConvertService() {
        return masterConvertService;
    }

    public void setMasterConvertService(MasterConvertService masterConvertService) {
        this.masterConvertService = masterConvertService;
    }

    public static int getFromYear_begin() {
        return fromYear_begin;
    }

    public static void setFromYear_begin(int fromYear_begin) {
        UpdateContractNumberNewThread.fromYear_begin = fromYear_begin;
    }
}
