package com.vn.osp.notarialservices.masterConvert.controller;

import com.vn.osp.notarialservices.masterConvert.domain.MasterContractBO;
import com.vn.osp.notarialservices.masterConvert.dto.ConfigDBMaster;
import com.vn.osp.notarialservices.masterConvert.dto.MasterContract;
import com.vn.osp.notarialservices.masterConvert.service.MasterConvertService;
import com.vn.osp.notarialservices.masterConvert.task.ConvertDBMasterThread;
import com.vn.osp.notarialservices.masterConvert.task.MasterContractToTransactionThread;
import com.vn.osp.notarialservices.transaction.domain.TransactionPropertyBo;
import com.vn.osp.notarialservices.utils.Constants;
import com.vn.osp.notarialservices.utils.EditString;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/masterConvert")
public class MasterConvertController {
    private static final Logger LOGGER = Logger.getLogger(MasterConvertController.class);

    private final MasterConvertService masterConvertService;

    @Autowired
    public MasterConvertController(final MasterConvertService masterConvertService){
        this.masterConvertService = masterConvertService;
    }

    @RequestMapping(value="/getConfigDBMaster", method = RequestMethod.GET)
    public ResponseEntity<ConfigDBMaster> getConfigDBMaster(){
        ConfigDBMaster configDBMaster = masterConvertService.getConfigDBMaster();
        return new ResponseEntity<ConfigDBMaster>(configDBMaster, HttpStatus.OK);
    }

    @RequestMapping(value="/updateConfigDBMaster", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateConfigDBMaster(@RequestBody @Valid final ConfigDBMaster configDBMaster){
        Boolean check = masterConvertService.updateConfigDBMaster(configDBMaster);
        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }

    @RequestMapping(value="/connectTestConfigDBMaster", method = RequestMethod.POST)
    public ResponseEntity<Boolean> connectTestConfigDBMaster(@RequestBody @Valid final ConfigDBMaster configDBMaster){
        Boolean check = masterConvertService.connectTestConfigDBMaster(configDBMaster);
        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }

    @RequestMapping(value="/selectContractMasterConvert", method = RequestMethod.GET)
    public ResponseEntity<List<MasterContract>> selectContractMasterConvert(@RequestParam @Valid final String search, @RequestParam @Valid final String type, @RequestParam @Valid final int offset, @RequestParam @Valid final int number){

        String stringFilter = masterConvertService.getStringFilterFromSearch(search, type);

        List<MasterContract> reperToire = masterConvertService.selectContractMasterConvert(stringFilter,offset,number);
        return new ResponseEntity<List<MasterContract>>(reperToire, HttpStatus.OK);
    }

    @RequestMapping(value="/countContractMasterConvert", method = RequestMethod.GET)
    public ResponseEntity<Long> countReperToire(@RequestParam @Valid final String search, @RequestParam @Valid final String type){

        String stringFilter = masterConvertService.getStringFilterFromSearch(search, type);

        Long reperToire = masterConvertService.countContractMasterConvert(stringFilter);
        return new ResponseEntity<Long>(reperToire, HttpStatus.OK);
    }

    @RequestMapping(value="/convertDBMaster", method = RequestMethod.GET)
    public void convertDBMaster(@RequestParam @Valid String entry_user_id, @RequestParam @Valid String entry_user_name, @RequestParam @Valid String fromYear, @RequestParam @Valid String toYear, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            masterConvertService.deleteWhereContractNumberMasterNew(fromYear,toYear);
            ConvertDBMasterThread convertDBMasterThread = new ConvertDBMasterThread(masterConvertService);
            convertDBMasterThread.setEntry_user_id(entry_user_id);
            convertDBMasterThread.setEntry_user_name(entry_user_name);
            convertDBMasterThread.setFromYear(Integer.parseInt(fromYear));
            convertDBMasterThread.setToYear(Integer.parseInt(toYear));
            convertDBMasterThread.run();

            response.getWriter().write("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/init-index-convertDBMaster-handle", method = RequestMethod.GET)
    public void indexingPreventHandle(@RequestParam @Valid String fromYear, @RequestParam @Valid String toYear, HttpServletRequest request, HttpServletResponse response) {
        int toYear_ = Integer.parseInt(toYear) + 1;
        String stringFilter = " WHERE notary_date>='" + fromYear + "-01-01 00:00:00' and notary_date<'" + toYear_ + "-01-01 00:00:00' and contract_number_new is not null";
        String stringFilter_ = " WHERE notary_date>='" + fromYear + "-01-01 00:00:00' and notary_date<'" + toYear_ + "-01-01 00:00:00' and contract_number_new is null";
        String stringFilterDB2000 = " AND REP_AN_LEGAL>=" + fromYear + " and REP_AN_LEGAL<=" + toYear;
        Long total = masterConvertService.countTotal(stringFilter);
        Long total_ = masterConvertService.countTotal(stringFilter_);
        Long totalDB2000 = masterConvertService.countTotalDBSQL2000(stringFilterDB2000);
        if (totalDB2000 != 0){
            int percent = 0;
            if(total==0) {
                percent = (int) (((float)total_ / (float)totalDB2000) * 50);
            }else {
                int percent1 = (int) (((float)total / (float)totalDB2000) * 50);
                percent = 50 + percent1;
            }
            System.out.println("phan tram da chay----------===========================:      " + percent);
            if (percent <= 90) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                try {
                    if (percent < 5) {
                        response.getWriter().write(String.valueOf("1"));
                    } else {
                        response.getWriter().write(String.valueOf(percent));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                try {
                    response.getWriter().write(String.valueOf("100"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try {
                response.getWriter().write(String.valueOf("100"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value="/selectContractMasterConvertById", method = RequestMethod.GET)
    public ResponseEntity<MasterContract> selectContractMasterConvertById(@RequestParam @Valid final Long id){
        List<MasterContract> reperToire = masterConvertService.selectContractMasterConvertById(id);
        return new ResponseEntity<MasterContract>(reperToire.get(0), HttpStatus.OK);
    }

    @RequestMapping(value="/editContractMasterConvert", method = RequestMethod.POST)
    public ResponseEntity<Boolean> editContractMasterConvert(@RequestBody final MasterContractBO bo){
        List<MasterContractBO> list = new ArrayList<>();
        list.add(bo);
        Boolean check = masterConvertService.editContractMasterConvert(list);
        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }

    @RequestMapping(value="/deleteContractMasterConvertById", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteContractMasterConvertById(@RequestParam @Valid final String listIdSelect){
        Boolean check = masterConvertService.deleteContractMasterConvertById(listIdSelect);
        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }

    @RequestMapping(value="/addMasterToTransaction", method = RequestMethod.GET)
    public ResponseEntity<Boolean> addMasterToTransaction(@RequestParam @Valid final String object, HttpServletRequest request, HttpServletResponse response){
        Boolean check = true;
        try {
            JSONObject searchObject=new JSONObject(object);
            if(searchObject.has("typeMasterToTransaction") && !StringUtils.isBlank(searchObject.get("typeMasterToTransaction").toString())) {
                String basic = searchObject.get("typeMasterToTransaction").toString();
                String entry_user_id = searchObject.get("entry_user_id").toString();
                String entry_user_name = searchObject.get("entry_user_name").toString();
                if (EditString.isNumber(basic)) {
                    if(Integer.parseInt(basic) == 1){   //chuyển đổi tất cả
                        MasterContractToTransactionThread thread = new MasterContractToTransactionThread(masterConvertService);
                        thread.setEntry_user_id(entry_user_id);
                        thread.setEntry_user_name(entry_user_name);
                        thread.run();
                    }else if(Integer.parseInt(basic) == 2){//chuyển đổi chọn lọc
                        String[] listID = searchObject.get("data").toString().split(";");
                        List<TransactionPropertyBo> add = new ArrayList<>();
                        List<MasterContractBO> update = new ArrayList<>();
                        for(int i = 0; i<listID.length;i++) {
                            MasterContractBO masterContractBO = (masterConvertService.selectContractMasterConvert(" Where id = " + listID[i])).get(0);
                            //if(masterContractBO.getType_master_to_transaction() == 1) continue;
                            TransactionPropertyBo transactionPropertyBo = masterConvertService.genTransactionPropertyBo(masterContractBO, entry_user_id, entry_user_name);
                            add.add(transactionPropertyBo);
                            update.add(masterContractBO);
                        }
                        check = masterConvertService.addTransactionProperty(add, update);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(" loi tai controller addMasterToTransaction " + e.getMessage());
        }
        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }

    @RequestMapping(value = "/init-index-addMasterToTransaction-handle", method = RequestMethod.GET)
    public void indexingMasterToTransaction(HttpServletRequest request, HttpServletResponse response) {
        String stringFilter_yes = " WHERE type_delete = " + Constants.TYPE_NO_DELETE + " and type_master_to_transaction = " + Constants.VALID_MASTER_TO_TRANSACTION;
        String stringFilterTotal = " WHERE type_delete = " + Constants.TYPE_NO_DELETE + " ";
        Long total_yes = masterConvertService.countTotal(stringFilter_yes);
        Long total = masterConvertService.countTotal(stringFilterTotal);

        int percent = (int) (((float)total_yes / (float)total) * 100);
        System.out.println("phan tram da chay----------===========================:      " + percent);
        if (percent <= 90) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try {
                if (percent < 5) {
                    response.getWriter().write(String.valueOf("1"));
                } else {
                    response.getWriter().write(String.valueOf(percent));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try {
                response.getWriter().write(String.valueOf("100"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
