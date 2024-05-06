package com.vn.osp.notarialservices.contract.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.vn.osp.notarialservices.contract.dto.*;
import com.vn.osp.notarialservices.contract.service.*;
import com.vn.osp.notarialservices.contract.util.DissectFile;
import com.vn.osp.notarialservices.contractKeyMap.dto.ContractReadFileDoc;
import com.vn.osp.notarialservices.contractKeyMap.service.ContractKeyMapService;
import com.vn.osp.notarialservices.contracttemplate.service.ContractTempService;
import com.vn.osp.notarialservices.district.dto.District;
import com.vn.osp.notarialservices.district.service.DistrictService;
import com.vn.osp.notarialservices.masterConvert.domain.PropertyBo;
import com.vn.osp.notarialservices.systemmanager.service.AccessHistoryService;
import com.vn.osp.notarialservices.transaction.dto.SynchronizeContractTag;
import com.vn.osp.notarialservices.transaction.dto.TransactionProperty;
import com.vn.osp.notarialservices.transaction.service.TransactionPropertyService;
import com.vn.osp.notarialservices.user.service.UserService;
import com.vn.osp.notarialservices.utils.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigInteger;
import java.net.URLConnection;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 5/8/2017.
 */
@Controller
@RequestMapping(value = "/contract")
public class ContractController {
    private static final Logger LOGGER = Logger.getLogger(ContractController.class);

    private ContractService contractService;
    private SynchronizeService synchronizeService;
    private PropertyService propertyService;
    private TemporaryContractService temporaryContractService;
    private TransactionPropertyService transactionPropertyService;
    private final ContractKindService contractKindService;
    private final ContractTempService contractTempService;
    private final UserService userService;
    private final ContractKeyMapService contractKeyMapService;
    private final AccessHistoryService accessHistoryService;
    private SuggestPropertyService suggestPropertyService;
    @Autowired
    DistrictService districtService;

    @Autowired
    public ContractController(ContractService contractService, SynchronizeService synchronizeService,
                              PropertyService propertyService, TemporaryContractService temporaryContractService,
                              TransactionPropertyService transactionPropertyService,
                              ContractKindService contractKindService, ContractTempService contractTempService,
                              UserService userService, ContractKeyMapService contractKeyMapService,
                              AccessHistoryService accessHistoryService,SuggestPropertyService suggestPropertyService) {
        this.contractService = contractService;
        this.synchronizeService = synchronizeService;
        this.propertyService = propertyService;
        this.temporaryContractService = temporaryContractService;
        this.transactionPropertyService = transactionPropertyService;
        this.contractKindService = contractKindService;
        this.contractTempService = contractTempService;
        this.userService = userService;
        this.contractKeyMapService = contractKeyMapService;
        this.accessHistoryService = accessHistoryService;
        this.suggestPropertyService = suggestPropertyService;

    }

    @RequestMapping(value = "/all-contract", method = RequestMethod.GET)
    public ResponseEntity<String> getAllContract() {
        String functionName = "ContractController.getAllContract()";
        ResponseEntity<String> respone = null;
        try {
            List<Contract> result = contractService.getAllContract().orElse(null);
            if (result != null) {
                String jsonString = StringUtils.getJson(result);
                respone = new ResponseEntity<String>(jsonString, HttpStatus.OK);
            } else {
                respone = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    /*@RequestMapping(value = "/addSuggestPrivy", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addSuggestPrivy(@RequestBody final SuggestPrivy suggestPrivy) {
        Boolean result = suggestPrivyService.addSuggestPrivy(suggestPrivy).orElse(Boolean.valueOf(false));
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
    @RequestMapping(value = "/searchSuggestPrivy", method = RequestMethod.GET)
    public ResponseEntity<List<SuggestPrivy>> searchSuggestPrivy(@RequestParam final String template,@RequestParam final String searchKey) {
        List<SuggestPrivy> result = suggestPrivyService.searchSuggestPrivy(template,searchKey).orElse(new ArrayList<SuggestPrivy>());
        return new ResponseEntity<List<SuggestPrivy>>(result, HttpStatus.OK);
    }
    @RequestMapping(value = "/addSuggestProperty", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addSuggestProperty(@RequestBody final SuggestProperty suggestProperty) {

        Boolean result = suggestPropertyService.addSuggestProperty(suggestProperty).orElse(Boolean.valueOf(false));
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
    @RequestMapping(value = "/searchSuggestProperty", method = RequestMethod.GET)
    public ResponseEntity<List<SuggestProperty>> searchSuggestProperty(@RequestParam (name = "type", defaultValue = "", required = false) @Valid final String type,
                                                                       @RequestParam (name = "countNumber", defaultValue = "", required = false) @Valid final String searchKey) {

        List<SuggestProperty> result = suggestPropertyService.searchSuggestProperty(type,searchKey).orElse(new ArrayList<SuggestProperty>());
        return new ResponseEntity<List<SuggestProperty>>(result, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/findLatestContract", method = RequestMethod.POST)
    public ResponseEntity<List<Contract>> findLatestContract(@RequestBody final Long countNumber) {
        List<Contract> contracts1 = contractService.findLatestContract(countNumber).orElse(new ArrayList<Contract>());

        return new ResponseEntity<List<Contract>>(contracts1, HttpStatus.OK);
    }

    @RequestMapping(value = "/all-synchronize", method = RequestMethod.GET)
    public ResponseEntity<String> getAllSynchronize() {
        String functionName = "ContractController.getAllSynchronize()";
        ResponseEntity<String> respone = null;
        // check add
        /*int m = 4;
        if(m==3){
            Synchronize synchronize = new Synchronize();
            synchronize.setAction(3);
            synchronize.setAuthentication_id("3");
            synchronize.setData_id("3");
            synchronize.setEntry_date_time(convertStringToTimeStamp("11-11-2011"));
            synchronize.setHistory_id(3);
            synchronize.setStatus(3);
            synchronizeService.addSynchronize(synchronize);
        }
        int n=4;
        if (n==3){
            synchronizeService.removeSynchronize("3");
        }*/
        try {
            List<Synchronize> result = synchronizeService.getAllSynchronize().orElse(null);
            if (result != null) {
                String jsonString = StringUtils.getJson(result);
                respone = new ResponseEntity<String>(jsonString, HttpStatus.OK);
            } else {
                respone = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    public Timestamp convertStringToTimeStamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date parsedDate = null;
            parsedDate = dateFormat.parse(dateString);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            return timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*@RequestMapping(value = "/add-synchronize", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addSynchronize(@RequestBody @Valid final Synchronize synchronize) {

        Boolean check = (Boolean)synchronizeService.addSynchronize(synchronize).orElse(Boolean.valueOf(false));

        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }*/


    @RequestMapping(value = "/all-property", method = RequestMethod.GET)
    public ResponseEntity<String> getAllProperty() {
        String functionName = "ContractController.getAllProperty()";
        ResponseEntity<String> respone = null;
        try {
            List<Property> result = propertyService.getAllProperty().orElse(null);
            if (result != null) {
                String jsonString = StringUtils.getJson(result);
                respone = new ResponseEntity<String>(jsonString, HttpStatus.OK);
            } else {
                respone = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/all-temporary-contract", method = RequestMethod.GET)
    public ResponseEntity<String> getAllTemporaryContract() {
        String functionName = "ContractController.getAllTemporaryContract()";
        ResponseEntity<String> respone = null;
        try {
            List<TemporaryContract> result = temporaryContractService.getAllTemporaryContract().orElse(null);
            if (result != null) {
                String jsonString = StringUtils.getJson(result);
                respone = new ResponseEntity<String>(jsonString, HttpStatus.OK);
            } else {
                respone = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }


    @RequestMapping(value = "/getContractKindByFilter", method = RequestMethod.POST)
    public ResponseEntity<List<ContractKind>> getContractKindByFilter(@RequestBody String stringFilter) {
        List<ContractKind> result = contractService.selectContractKindByFilter(stringFilter).orElse(new ArrayList<ContractKind>());
        return new ResponseEntity<List<ContractKind>>(result, HttpStatus.OK);

    }

    @RequestMapping(value = "/getContractTemplateByFilter", method = RequestMethod.POST)
    public ResponseEntity<List<ContractTemplate>> getContractTemplateByFilter(@RequestBody String stringFilter) {
        List<ContractTemplate> result = contractService.selectContractTeamplateByFilter(stringFilter).orElse(new ArrayList<ContractTemplate>());
        return new ResponseEntity<List<ContractTemplate>>(result, HttpStatus.OK);

    }


    @RequestMapping(value = "/selectReportByGroupTotal", method = RequestMethod.POST)
    public ResponseEntity<List<ReportByGroupTotal>> selectReportByGroupTotal(@RequestBody final ReportByGroupTotalList reportByGroupTotalList) {
        List<ReportByGroupTotal> reportByGroupTotals = contractService.selectReportByGroupTotal(reportByGroupTotalList);

        return new ResponseEntity<List<ReportByGroupTotal>>(reportByGroupTotals, HttpStatus.OK);
    }

    @RequestMapping(value = "/selectDetailReportByGroup", method = RequestMethod.POST)
    public ResponseEntity<List<com.vn.osp.notarialservices.transaction.dto.TransactionProperty>> selectDetailReportByGroup(@RequestBody final ReportByGroupTotalList reportByGroupTotalList) {
        List<com.vn.osp.notarialservices.transaction.dto.TransactionProperty> reportByGroupTotals = contractService.selectDetailReportByGroup(reportByGroupTotalList);

        return new ResponseEntity<List<com.vn.osp.notarialservices.transaction.dto.TransactionProperty>>(reportByGroupTotals, HttpStatus.OK);
    }

    @RequestMapping(value = "/selectAllDetailReportByGroup", method = RequestMethod.POST)
    public ResponseEntity<List<com.vn.osp.notarialservices.transaction.dto.TransactionProperty>> selectAllDetailReportByGroup(@RequestBody final ReportByGroupTotalList reportByGroupTotalList) {
        List<com.vn.osp.notarialservices.transaction.dto.TransactionProperty> reportByGroupTotals = contractService.selectAllDetailReportByGroup(reportByGroupTotalList);

        return new ResponseEntity<List<com.vn.osp.notarialservices.transaction.dto.TransactionProperty>>(reportByGroupTotals, HttpStatus.OK);
    }

    @RequestMapping(value = "/countDetailReportByGroup", method = RequestMethod.POST)
    public ResponseEntity<BigInteger> countDetailReportByGroup(@RequestBody final ReportByGroupTotalList reportByGroupTotalList) {
        BigInteger result = contractService.countDetailReportByGroup(reportByGroupTotalList);

        return new ResponseEntity<BigInteger>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/countTotalByFilter", method = RequestMethod.POST)
    public ResponseEntity<BigInteger> countHistoryContract(@RequestBody String stringFilter) {
        BigInteger result = contractService.countHistoryContract(stringFilter).orElse(BigInteger.valueOf(0));

        return new ResponseEntity<BigInteger>(result, HttpStatus.OK);

    }

    @RequestMapping(value = "/selectContractHistoryByFilter", method = RequestMethod.POST)
    @ApiOperation(
            value = "Lấy lịch sử thay đổi hợp đồng",
            notes = "Chỉ có thể được gọi bởi người dùng trên Sở tư pháp."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lấy lịch sử thành công!"),
            @ApiResponse(code = 400, message = "Có lỗi xảy ra.")
    })
    public ResponseEntity<List<ContractHistory>> selectContractHistoryByFilter(@RequestBody final String stringFilter) {
        List<ContractHistory> contractHistoryInfors = contractService.selectByFilter(stringFilter);
        return new ResponseEntity<List<ContractHistory>>(contractHistoryInfors, HttpStatus.OK);
    }

    @RequestMapping(value = "/selectReportByNotary", method = RequestMethod.POST)
    public ResponseEntity<List<ReportByNotaryPerson>> selectReportByNotary(@RequestBody @Valid final String stringFilter) {
        List<ReportByNotaryPerson> result = contractService.getReportByNotary(stringFilter);
        return new ResponseEntity<List<ReportByNotaryPerson>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/selectReportByDraft", method = RequestMethod.POST)
    public ResponseEntity<List<ReportByNotaryPerson>> selectReportByDraft(@RequestBody @Valid final String stringFilter) {
        List<ReportByNotaryPerson> result = contractService.getReportByDraft(stringFilter);
        return new ResponseEntity<List<ReportByNotaryPerson>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/countTotalReportByNotary", method = RequestMethod.POST)
    public ResponseEntity<BigInteger> countTotalReportByNotary(@RequestBody final String stringFilter) {

        return new ResponseEntity<BigInteger>((BigInteger) contractService.countTotalReportByNotary(stringFilter).orElse(BigInteger.valueOf(0)), HttpStatus.OK);
    }

    @RequestMapping(value = "/countTotalReportByDraft", method = RequestMethod.POST)
    public ResponseEntity<BigInteger> countTotalReportByDraft(@RequestBody final String stringFilter) {

        return new ResponseEntity<BigInteger>((BigInteger) contractService.countTotalReportByDraft(stringFilter).orElse(BigInteger.valueOf(0)), HttpStatus.OK);
    }

    @RequestMapping(value = "/selectReportByUser", method = RequestMethod.POST)
    public ResponseEntity<List<ReportByUser>> getReportByUser(@RequestBody @Valid final String stringFilter) {
        List<ReportByUser> result = contractService.getReportByUser(stringFilter);
        return new ResponseEntity<List<ReportByUser>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/countTotalReportByUser", method = RequestMethod.POST)
    public ResponseEntity<BigInteger> countTotalReportByUser(@RequestBody final String stringFilter) {

        return new ResponseEntity<BigInteger>((BigInteger) contractService.countTotalReportByUser(stringFilter).orElse(BigInteger.valueOf(0)), HttpStatus.OK);
    }


    @RequestMapping(value = "/report-tt-20", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<ReportByTT20List>> reportByTT20(@RequestBody final ReportByTT20List reportByTT20List) {
        ReportByTT20List reportByTT20 = contractService.reportByTT20(reportByTT20List);
        ArrayList<ReportByTT20List> reportByTT20Lists = new ArrayList<ReportByTT20List>();
        reportByTT20Lists.add(reportByTT20);
        return new ResponseEntity<ArrayList<ReportByTT20List>>(reportByTT20Lists, HttpStatus.OK);
    }

    @RequestMapping(value = "/report-tt-03", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<ReportByTT03List>> reportByTT03(@RequestBody final ReportByTT03List reportByTT03List) {
        ReportByTT03List reportByTT03 = contractService.reportByTT03(reportByTT03List);
        ArrayList<ReportByTT03List> reportByTT03Lists = new ArrayList<>();
        reportByTT03Lists.add(reportByTT03);
        return new ResponseEntity<ArrayList<ReportByTT03List>>(reportByTT03Lists, HttpStatus.OK);
    }

    @RequestMapping(value = "/report-tt-04", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<ReportByTT04List>> reportByTT04(@RequestBody final ReportByTT04List reportByTT04List) {
        ReportByTT04List reportByTT04 = contractService.reportByTT04(reportByTT04List);
        ArrayList<ReportByTT04List> reportByTT04Lists = new ArrayList<ReportByTT04List>();
        reportByTT04Lists.add(reportByTT04);
        return new ResponseEntity<ArrayList<ReportByTT04List>>(reportByTT04Lists, HttpStatus.OK);
    }

    //    @RequestMapping(value = "/contract_number", method = RequestMethod.GET)
//    public ResponseEntity<Integer> countTotalReportByUser() {
//        SimpleDateFormat.getDateInstance();
//        return new ResponseEntity<Integer>((Integer) temporaryContractService.getContractNumberValue().orElse(Integer.valueOf(0)), HttpStatus.OK);
//    }
    @RequestMapping(value = "/contractNumber", method = RequestMethod.GET)
    public ResponseEntity<Integer> getContractNumber(@RequestParam @Valid final long year,
                                                     @RequestParam (value = "userId", defaultValue = "", required = false) final long userId) {
        int contractNumber = 0;
        String key = year + "";
        try {
            if (1900 <= year || year <= 2100) {
                if (userId > 0) key = year + "/" + userId;
                contractNumber = temporaryContractService.getContractNumber(key).orElse(Integer.valueOf(0));
            }
        } catch (Exception e) {
            return new ResponseEntity<Integer>(0, HttpStatus.OK);
        }

        return new ResponseEntity<Integer>(Integer.valueOf(contractNumber + 1), HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Integer> addContract(@RequestBody @Valid final Contract contract) {

        if (contract == null) return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(contract);
        boolean checkContractNumber = checkContractNumber(contract.getContract_number());
        if (!checkContractNumber) {
            return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        }
        TransactionProperty trans = new com.vn.osp.notarialservices.transaction.dto.TransactionProperty();
        trans = contractService.genaralInfoToTransactionProperty(contract, trans);
        if(SystemProperties.getProperty("org_type").equals(Constants.ORG_TYPE_PHUONG_XA)) {
            trans.setNotary_person(contract.getContract_signer());
           // trans.setNotary_office_name();
        }
        ContractHistoryInfor history = new ContractHistoryInfor();
        history = contractService.genaralHistoryInfo(contract, history, "Đăng ký");
        /*convert xml*/

        Integer id = contractService.addContract(contract, trans, history).orElse(null);
        // chỉ hoạt động khi chay thử uchi v3 song song với uchi v2
        /*if(id>0 && (SystemProperties.getProperty("status.synchronize").equals("1"))){
            Synchronize synchronize = new Synchronize();
            synchronize.setAuthentication_id(accessHistoryService.getConfigValue("system_authentication_id").orElse(""));
            synchronize.setAction(Constants.SYNCHRONIZE_ACTION_REGIST);
            synchronize.setType(Constants.SYNCHRONIZE_TYPE_CONTRACT);
            synchronize.setEntry_date_time(new Timestamp(System.currentTimeMillis()));
            synchronize.setData_id(trans.getSynchronize_id()+id);
            synchronizeService.addSynchronize(synchronize);

        }*/
        if (id > 0) {
            // dong bo du lieu
            SynchronizeContractTag contractTag = contractService.genSynchronizeContractTagByTrans(trans, Constants.CREATE_CONTRACT);
            List<SynchronizeContractTag> contractTags = new ArrayList<>();
            contractTags.add(contractTag);
            boolean checkSynch = contractService.synchronizeContractTags(contractTags);
            if (checkSynch) {
                boolean ch = transactionPropertyService.updateSynchStatus(Long.valueOf(id), Long.valueOf(1)).orElse(false);
            }
        }
        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<ResultUpload> uploadFile(@RequestBody @Valid final MultipartFile file) {
        if (file == null) return new ResponseEntity<ResultUpload>(new ResultUpload(), HttpStatus.NO_CONTENT);
        String folder = SystemProperties.getProperty("system_folder");
        ResultUpload item = new ResultUpload();
        try {

            if (!file.isEmpty()) {
                File fileSave = FileUtil.createNewFile(folder, "CTR_");
                String localName = EditString.convertUnicodeToASCII(file.getOriginalFilename());
                String path = "";
                try {
                    FileOutputStream outputStream = null;
                    outputStream = new FileOutputStream(fileSave);
                    outputStream.write(file.getBytes());
                    path = fileSave.getAbsolutePath();
                    item.setName(localName);
                    item.setPath(path);
                    return new ResponseEntity<ResultUpload>(item, HttpStatus.OK);
                } catch (IOException e) {
                    LOGGER.error("Loi tai api upload file: "+e.getMessage());
                    return new ResponseEntity<ResultUpload>(item, HttpStatus.OK);
                }
            }


        } catch (Exception e) {
            return new ResponseEntity<ResultUpload>(item, HttpStatus.OK);
        }
        return new ResponseEntity<ResultUpload>(item, HttpStatus.OK);
    }


    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    public ResponseEntity<List<ResultUpload>> uploadFiles(@RequestParam("file") MultipartFile[] files) throws IOException {
        if (files == null) return new ResponseEntity<List<ResultUpload>>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        String folder = SystemProperties.getProperty("system_folder");
        List<ResultUpload> items = new ArrayList<>();
        FileOutputStream outputStream = null;
        try {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    ResultUpload item = new ResultUpload();
                    File fileSave = FileUtil.createNewFile(folder, "CTR_");
                    String localName = EditString.convertUnicodeToASCII(file.getOriginalFilename());
                    String path = "";

                    try {
                        outputStream = new FileOutputStream(fileSave);
                        outputStream.write(file.getBytes());
                        path = fileSave.getAbsolutePath();
                        item.setName(localName);
                        item.setPath(path);
                        items.add(item);
                    } catch (IOException e) {
                        LOGGER.error("ERROR AT API UPLOAD FILE CONTRACT: "+e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            return new ResponseEntity<List<ResultUpload>>(items, HttpStatus.OK);
        } finally {
            if(outputStream!=null) outputStream.close();
        }
        return new ResponseEntity<List<ResultUpload>>(items, HttpStatus.OK);
    }


    @RequestMapping(value = "/read-and-dissect-file", method = RequestMethod.POST)
    public ResponseEntity<ContractReadFileDoc> readAndDissectFile(@RequestBody @Valid final MultipartFile file) throws IOException {
        DissectFile dissectFile = new DissectFile(contractTempService, userService, contractKeyMapService, accessHistoryService);
        String folder = SystemProperties.getProperty("contract_dissect_folder");
        String fileName = file.getOriginalFilename();
        File fileSave = null;
        Contract contract = new Contract();
        ContractReadFileDoc contractReadFileDoc = new ContractReadFileDoc();
        FileOutputStream outputStream = null;
        int typeOfFormatWord = 0;
        LOGGER.info("daik chu test file .war");
        try{
            //= null;
            if (fileName.substring(fileName.lastIndexOf('.')).equals(".docx")) {//file is docx
                fileSave = FileUtil.createNewFileDotDocX(folder, "READ_");
                typeOfFormatWord = Constants.TYPE_FORMAT_WORD_DOCX;

            } else {
                fileSave = FileUtil.createNewFileDotDoc(folder, "READ_");
                typeOfFormatWord = Constants.TYPE_FORMAT_WORD_DOC;
            }
            outputStream = new FileOutputStream(fileSave);
            outputStream.write(file.getBytes());
          //  contract = dissectFile.read(fileSave, contract, 1, typeOfFormatWord);//typeContract = 1 is offline
            contractReadFileDoc = dissectFile.read(fileSave, contract, 1, typeOfFormatWord);//typeContract = 1 is offline

        }catch (Exception e) {
            LOGGER.error("ERROR AT API READ AND DISSECT CONTRACT: " + e.getMessage());
        } finally {
            if(outputStream!=null) outputStream.close();
            if(fileSave != null) Files.delete(fileSave.toPath());
        }
        return new ResponseEntity<>(contractReadFileDoc, HttpStatus.OK);
    }

    @RequestMapping(value = "/temporary/read-and-dissect-file", method = RequestMethod.POST)
    public ResponseEntity<ContractReadFileDoc> temporaryReadAndDissectFile(@RequestBody @Valid final MultipartFile file) throws IOException {
        DissectFile dissectFile = new DissectFile(contractTempService, userService, contractKeyMapService, accessHistoryService);
        String folder = SystemProperties.getProperty("contract_dissect_folder");
        String fileName = file.getOriginalFilename();
        File fileSave = null;
        int typeOfFormatWord = 0;
        TemporaryContract temporaryContract = new TemporaryContract();
        Contract contract = new Contract();
        ContractReadFileDoc contractReadFileDoc = new ContractReadFileDoc();
        FileOutputStream outputStream = null;

        try{
            if (fileName.substring(fileName.lastIndexOf('.')).equals(".docx")) {//file is docx
                fileSave = FileUtil.createNewFileDotDocX(folder, "READ_");
                typeOfFormatWord = Constants.TYPE_FORMAT_WORD_DOCX;
            } else {
                fileSave = FileUtil.createNewFileDotDoc(folder, "READ_");
                typeOfFormatWord = Constants.TYPE_FORMAT_WORD_DOC;
            }
            outputStream = new FileOutputStream(fileSave);
            outputStream.write(file.getBytes());
            contractReadFileDoc = dissectFile.read(fileSave, contract, 2, typeOfFormatWord);//typeContract = 2 is online
            contract = contractReadFileDoc.getContract();
            temporaryContract.setJson_person(contract.getJson_person());
            temporaryContract.setJson_property(contract.getJson_property());
            temporaryContract.setContract_value(contract.getContract_value());
            temporaryContract.setNotary_date(contract.getNotary_date());
            temporaryContract.setNotary_id(contract.getNotary_id());
            temporaryContract.setNotary_place_flag(contract.getNotary_place_flag());
            temporaryContract.setNotary_place(contract.getNotary_place());
            temporaryContract.setContract_number(contract.getContract_number());
            temporaryContract.setContract_template_id(contract.getContract_template_id());

            contractReadFileDoc.setTemporaryContract(temporaryContract);
        } catch (Exception e){
            LOGGER.error("ERROR AT API READ AND DISSECT TEMPORARY CONTRACT: " + e.getMessage());
        } finally {
            if(outputStream!=null) outputStream.close();
            if(fileSave!=null) Files.delete(fileSave.toPath());
        }
        return new ResponseEntity<>(contractReadFileDoc, HttpStatus.OK);
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream inputStream = null;
        try {
            Contract item = contractService.getContractById(id.toString()).orElse(null);
            if (item == null) return;
            File file = new File(item.getFile_path());
            if (!file.exists()) {
                String errorMessage = "Sorry. The file you are looking for does not exist";
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(errorMessage.getBytes());
                outputStream.close();
                return;
            }
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + item.getFile_name() + "\""));
            response.setContentLength((int) file.length());
            inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null) inputStream.close();
        }
    }

    @RequestMapping(value = "/viewfile", method = RequestMethod.GET)
    public void viewFile(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream inputStream = null;
        try {
            Contract item = contractService.getContractById(id.toString()).orElse(null);
            if (item == null) return;
            File file = new File(item.getFile_path());
            if (!file.exists()) {
                String errorMessage = "Sorry. The file you are looking for does not exist";
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(errorMessage.getBytes());
                outputStream.close();
                return;
            }
            // Blob blob = file.getName();
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + item.getFile_name() + "\""));
            response.setContentLength((int) file.length());
            inputStream = new BufferedInputStream(new FileInputStream(file));

            FileCopyUtils.copyToByteArray(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public ResponseEntity<Boolean> removeFile(@RequestParam @Valid final String id) {
        try {
            Contract item = contractService.getContractById(id.toString()).orElse(null);
            if (item == null) return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            File file = new File(item.getFile_path());
            if (!file.exists()) {
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            } else {
                item.setFile_name("");
                item.setFile_path("");
                if (!org.apache.commons.lang3.StringUtils.isBlank(item.getKindhtml())) {
                    item.setKindhtml(item.getKindhtml().replaceAll("&lt;", "<"));
                    item.setKindhtml(item.getKindhtml().replaceAll("&gt;", ">"));
                    item.setKindhtml(item.getKindhtml().replaceAll("&amp;nbsp;", " "));
                }
                TransactionProperty trans = transactionPropertyService.getByContractId(item.getid().toString()).orElse(null);
                trans = contractService.genaralInfoToTransactionProperty(item, trans);
                ContractHistoryInfor history = new ContractHistoryInfor();
                history = contractService.genaralHistoryInfo(item, history, "Gỡ file");
                /*convert xml*/

                Integer idTran = contractService.editContract(item, trans, history).orElse(null);
                Files.delete(file.toPath());
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<Integer> editContract(@RequestBody @Valid final Contract contract) {
        if (contract == null || contract.getid() == null || contract.getid() == 0)
            return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        Contract item = contractService.getContractById(contract.getid().toString()).orElse(null);
        if (item == null) return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);

        StringUtils.trimAllFieldOfObject(contract);
        item = contractService.genContractForEdit(item, contract).orElse(item);

        TransactionProperty trans = transactionPropertyService.getByContractId(item.getid().toString()).orElse(null);
        trans = contractService.genaralInfoToTransactionProperty(item, trans);
        if(SystemProperties.getProperty("org_type").equals(Constants.ORG_TYPE_PHUONG_XA)) {
            trans.setNotary_person(contract.getContract_signer());
        }
        ContractHistoryInfor history = new ContractHistoryInfor();
        history = contractService.genaralHistoryInfo(item, history, "Chỉnh sửa");

        Integer id = contractService.editContract(item, trans, history).orElse(null);
        // chỉ hoạt động khi chay thử uchi v3 song song với uchi v2
        /*if(id>0 && (SystemProperties.getProperty("status.synchronize").equals("1"))){
            Synchronize synchronize = new Synchronize();
            synchronize.setAuthentication_id(accessHistoryService.getConfigValue("system_authentication_id").orElse(""));
            synchronize.setAction(Constants.SYNCHRONIZE_ACTION_EDIT);
            synchronize.setType(Constants.SYNCHRONIZE_TYPE_CONTRACT);
            synchronize.setEntry_date_time(new Timestamp(System.currentTimeMillis()));
            synchronize.setData_id(trans.getSynchronize_id()+id);
            synchronizeService.addSynchronize(synchronize);

        }*/
        if (id > 0) {
            //dong bo du lieu
            //set sys_status=0
            boolean ch = transactionPropertyService.updateSynchStatus(trans.getTpid(), Long.valueOf(0)).orElse(false);
            SynchronizeContractTag contractTag = contractService.genSynchronizeContractTagByTrans(trans, Constants.UPDATE_CONTRACT);
            List<SynchronizeContractTag> contractTags = new ArrayList<>();
            contractTags.add(contractTag);
            boolean checkSynch = contractService.synchronizeContractTags(contractTags);
            if (checkSynch) {
                ch = transactionPropertyService.updateSynchStatus(Long.valueOf(id), Long.valueOf(1)).orElse(false);
            }
        }

        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ResponseEntity<Integer> cancelContract(@RequestBody @Valid final Contract contract) {
        if (contract == null || contract.getid() == null || contract.getid() == 0)
            return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(contract);
        boolean checkContractNumber = checkContractNumber(contract.getContract_number());
        if (!checkContractNumber) {
            return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        }
        Contract contractCancel = contractService.getContractById(contract.getid().toString()).orElse(null);

        TransactionProperty transOfContractCancel = transactionPropertyService.getByContractId(contractCancel.getid().toString()).orElse(null);
        if (contractCancel == null || transOfContractCancel == null)
            return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        contract.setId(null);

        TransactionProperty trans = new TransactionProperty();
        trans = contractService.genaralInfoToTransactionProperty(contract, trans);
        if(SystemProperties.getProperty("org_type").equals(Constants.ORG_TYPE_PHUONG_XA)) {
            trans.setNotary_person(contract.getContract_signer());
        }
        ContractHistoryInfor history = new ContractHistoryInfor();
        history = contractService.genaralHistoryInfo(contract, history, "Huỷ HĐ");
        /*convert xml*/

        Integer id = contractService.cancelContract(contractCancel.getid(), contract, trans, history).orElse(null);

        if (id > 0) {
            //update transactionOfCancel for syn to stp
            transOfContractCancel.setCancel_status(Long.valueOf(1));
            transOfContractCancel.setCancel_description("Đã hủy bởi hợp đồng số " + contract.getContract_number());
            transOfContractCancel.setUpdate_user_id(trans.getUpdate_user_id());
            transOfContractCancel.setUpdate_user_name(trans.getUpdate_user_name());
            transOfContractCancel.setSyn_status(0);
            //update syn_status of contract cancel=0
            trans.setTpid(Long.valueOf(0));
            SynchronizeContractTag contractTag = contractService.genSynchronizeContractTagByTrans(trans, Constants.CREATE_CONTRACT);
            SynchronizeContractTag contractCancelSynch = contractService.genSynchronizeContractTagByTrans(transOfContractCancel, Constants.UPDATE_CONTRACT);
            ArrayList<SynchronizeContractTag> contractTags = new ArrayList<>();
            contractTags.add(contractTag);
            contractTags.add(contractCancelSynch);
            boolean check = contractService.synchronizeContractTags(contractTags);
            if (check) {
                boolean ch = transactionPropertyService.updateSynchStatus(Long.valueOf(id), Long.valueOf(1)).orElse(false);
                ch = transactionPropertyService.updateSynchStatus(transOfContractCancel.getTpid(), Long.valueOf(1)).orElse(false);
            }
        }

        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteContract(@RequestParam @Valid final String id, @RequestParam @Valid final String userId) {
        Contract contract = contractService.getContractById(id).orElse(null);
        if (contract == null) {
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }
        ContractHistoryInfor history = new ContractHistoryInfor();
        history = contractService.genaralHistoryInfo(contract, history, "Xóa HĐ");
        history.setExecute_person(userId);
        /*convert xml*/
        XmlFriendlyNameCoder nameCoder = new XmlFriendlyNameCoder("ddd", "_");
        XStream xXStream = new XStream(new Dom4JDriver(nameCoder));
        xXStream.autodetectAnnotations(true);
        String xml_history = xXStream.toXML(history);
        Boolean status = contractService.deleteContract(contract.getid().toString(), xml_history);

        //đồng bộ lên stp
        TransactionProperty transactionProperty = new TransactionProperty();
        TransactionProperty tran = contractService.genaralInfoToTransactionProperty(contract, transactionProperty);
        if(SystemProperties.getProperty("org_type").equals(Constants.ORG_TYPE_PHUONG_XA)) {
            tran.setNotary_person(contract.getContract_signer());
        }

        SynchronizeContractTag contractTag = contractService.genSynchronizeContractTagByTransDelete(tran, Constants.DELETE_CONTRACT, userId);
        List<SynchronizeContractTag> contractTags = new ArrayList<>();
        contractTags.add(contractTag);
        boolean checkSynch = contractService.deleteContract(contractTags);

        return new ResponseEntity<Boolean>(checkSynch, HttpStatus.OK);
    }


    @RequestMapping(value = "add-property", method = RequestMethod.POST)
    public ResponseEntity<Integer> addProperty() {
        return new ResponseEntity<Integer>(0, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-contract-by-id", method = RequestMethod.GET)
    public ResponseEntity<Contract> getContractById(@RequestParam final String id) {
        String functionName = "ContractController.getContractById()";
        ResponseEntity<Contract> respone = new ResponseEntity<Contract>(HttpStatus.NO_CONTENT);
        try {
            Contract result = contractService.getContractById(id).orElse(null);
//            if(!org.apache.commons.lang3.StringUtils.isBlank(result.getKindhtml())){
//                result.setKindhtml(result.getKindhtml().replaceAll("&lt;", "<"));
//                result.setKindhtml(result.getKindhtml().replaceAll("&gt;", ">"));
//                result.setKindhtml(result.getKindhtml().replaceAll("&amp;nbsp;", " "));
//            }

            respone = new ResponseEntity<Contract>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/get-property-by-contract-id", method = RequestMethod.GET)
    public ResponseEntity<Property> getPropertyByContractId(@RequestParam final String id) {
        String functionName = "ContractController.getPropertyByContractId()";
        ResponseEntity<Property> respone = new ResponseEntity<Property>(HttpStatus.NO_CONTENT);
        try {
            Property result = propertyService.getPropertyByContractId(id).orElse(null);
            respone = new ResponseEntity<Property>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/get-contract-template-by-id", method = RequestMethod.GET)
    public ResponseEntity<ContractTemplate> getContractTemplateByContractId(@RequestParam final String id) {
        String functionName = "ContractController.getContractTemplateById()";
        ResponseEntity<ContractTemplate> respone = new ResponseEntity<ContractTemplate>(HttpStatus.NO_CONTENT);
        try {
            ContractTemplate result = contractService.getContractTemplateById(id).orElse(null);
            respone = new ResponseEntity<ContractTemplate>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/get-contract-template-by-code-template", method = RequestMethod.GET)
    public ResponseEntity<ContractTemplate> getContractTemplateByCodeTemplate(@RequestParam final String code_temp) {
        String functionName = "ContractController.getContractTemplateByCodeTemplate()";
        ResponseEntity<ContractTemplate> respone = new ResponseEntity<ContractTemplate>(HttpStatus.NO_CONTENT);
        try {
            ContractTemplate result = contractService.getContractTemplateByCodeTemp(code_temp).orElse(null);
            respone = new ResponseEntity<ContractTemplate>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/get-contract-template-by-parent-code-template", method = RequestMethod.GET)
    public ResponseEntity<List<ContractTemplate>> getContractTemplateByParentCodeTemplate(@RequestParam final String kind_id) {
        String functionName = "ContractController.getContractTemplateByParentCodeTemplate()";
        ResponseEntity<List<ContractTemplate>> respone = new ResponseEntity<List<ContractTemplate>>(HttpStatus.NO_CONTENT);
        try {
            List<ContractTemplate> result = contractService.getContractTemplateByParentCodeTemplate(kind_id).orElse(null);
            respone = new ResponseEntity<List<ContractTemplate>>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/get-contract-kind-by-contract-template-id", method = RequestMethod.GET)
    public ResponseEntity<ContractKind> getContractKindByContractTemplateId(@RequestParam final String id) {
        String functionName = "ContractController.getContractKindByContractTemplateId()";
        ResponseEntity<ContractKind> respone = new ResponseEntity<ContractKind>(HttpStatus.NO_CONTENT);
        try {
            ContractKind result = contractService.getContractKindByContractTempId(id).orElse(null);
            respone = new ResponseEntity<ContractKind>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/get-contract-kind-by-contract-template-code", method = RequestMethod.GET)
    public ResponseEntity<ContractKind> getContractKindByCode(@RequestParam final int code) {
        String functionName = "ContractController.getContractKindByCode()";
        ResponseEntity<ContractKind> respone = new ResponseEntity<ContractKind>(HttpStatus.NO_CONTENT);
        try {
            ContractKind result = contractService.getContractKindByContractTempCode(code).orElse(null);
            respone = new ResponseEntity<ContractKind>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/list-contract-kind", method = RequestMethod.GET)
    public ResponseEntity<List<ContractKind>> listContractKind() {
        String functionName = "ContractController.listContractKind()";
        ResponseEntity<List<ContractKind>> respone = new ResponseEntity<List<ContractKind>>(HttpStatus.NO_CONTENT);
        try {
            List<ContractKind> result = contractService.listContractKind().orElse(null);
            respone = new ResponseEntity<List<ContractKind>>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/list-contract-template-by-contract-kind", method = RequestMethod.GET)
    public ResponseEntity<List<ContractTemplate>> listContractTemplateByContractKind(@RequestParam final String id) {
        String functionName = "ContractController.listContractTemplateByContractKind()";
        ResponseEntity<List<ContractTemplate>> respone = new ResponseEntity<List<ContractTemplate>>(HttpStatus.NO_CONTENT);
        try {
            List<ContractTemplate> result = contractService.listContractTemplateByContractKindId(id).orElse(null);
            respone = new ResponseEntity<List<ContractTemplate>>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/list-contract-template-by-contract-kind-code", method = RequestMethod.GET)
    public ResponseEntity<List<ContractTemplate>> listContractTemplateByContractKindCode(@RequestParam final String code) {
        String functionName = "ContractController.listContractTemplateByContractKindCode()";
        ResponseEntity<List<ContractTemplate>> respone = new ResponseEntity<List<ContractTemplate>>(HttpStatus.NO_CONTENT);
        try {
            List<ContractTemplate> result = contractService.listContractTemplateByContractKindCode(code).orElse(null);
            respone = new ResponseEntity<List<ContractTemplate>>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/list-contract-template-same-kind", method = RequestMethod.GET)
    public ResponseEntity<List<ContractTemplate>> listContractTemplateSameKind(@RequestParam final int code_temp) {
        String functionName = "ContractController.listContractTemplateSameKind()";
        ResponseEntity<List<ContractTemplate>> respone = new ResponseEntity<List<ContractTemplate>>(HttpStatus.NO_CONTENT);
        try {
            List<ContractTemplate> result = contractService.listContractTemplateSameKind(code_temp).orElse(null);
            respone = new ResponseEntity<List<ContractTemplate>>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/list-property-type", method = RequestMethod.GET)
    public ResponseEntity<List<PropertyType>> listPropertyType() {
        String functionName = "ContractController.listPropertyType()";
        ResponseEntity<List<PropertyType>> respone = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        try {
            List<PropertyType> result = propertyService.listPropertyType().orElse(null);
            respone = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    /**
     * Khu vuc hop dong online
     *
     * @return
     */
    @RequestMapping(value = "/temporary/get-by-type", method = RequestMethod.GET)
    public ResponseEntity<List<TemporaryContract>> getTempoByType(@RequestParam final String type) {
        String functionName = "ContractController.getTempoByType()";
        ResponseEntity<List<TemporaryContract>> respone = new ResponseEntity<List<TemporaryContract>>(HttpStatus.NO_CONTENT);
        try {
            List<TemporaryContract> result = temporaryContractService.getTemporaryByType(type).orElse(null);
            respone = new ResponseEntity<List<TemporaryContract>>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/temporary/count-by-type", method = RequestMethod.GET)
    public ResponseEntity<BigInteger> countTempoByType(@RequestParam final String type) {
        String functionName = "ContractController.getTempoByType()";
        ResponseEntity<BigInteger> respone = new ResponseEntity<>(BigInteger.valueOf(0), HttpStatus.NO_CONTENT);
        try {
            BigInteger result = temporaryContractService.countTemporaryByType(type).orElse(BigInteger.valueOf(0));
            respone = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }


    @RequestMapping(value = "/temporary/get-by-filter", method = RequestMethod.GET)
    public ResponseEntity<List<TemporaryContract>> getTempoByFilter(@RequestParam final String filter) {
        String functionName = "ContractController.getTempoByFilter()";
        ResponseEntity<List<TemporaryContract>> respone = new ResponseEntity<List<TemporaryContract>>(HttpStatus.NO_CONTENT);
        try {
            List<TemporaryContract> result = temporaryContractService.getTemporaryByFilter(filter).orElse(null);
            respone = new ResponseEntity<List<TemporaryContract>>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/temporary/get-by-code_template", method = RequestMethod.GET)
    public ResponseEntity<List<TemporaryContract>> getTempoByContractTemplateId(@RequestParam final String code_template) {
        String functionName = "ContractController.getTempoByFilter()";
        ResponseEntity<List<TemporaryContract>> respone = new ResponseEntity<List<TemporaryContract>>(HttpStatus.NO_CONTENT);
        String filter = " where contract_template_id = "+code_template;
        try {
            List<TemporaryContract> result = temporaryContractService.getTemporaryByFilter(filter).orElse(null);
            respone = new ResponseEntity<List<TemporaryContract>>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/list-contract-template", method = RequestMethod.GET)
    public ResponseEntity<List<ContractTemplate>> listContractTemplate() {
        String functionName = "ContractController.listContractTemplate()";
        ResponseEntity<List<ContractTemplate>> respone = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        try {
            List<ContractTemplate> result = contractService.listContractTemplate().orElse(null);
            respone = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/temporary", method = RequestMethod.GET)
    public ResponseEntity<TemporaryContract> getTemporaryById(@RequestParam final String id) {
        String functionName = "ContractController.getTemporaryById()";
        ResponseEntity<TemporaryContract> respone = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        try {
            TemporaryContract result = temporaryContractService.getTemporaryById(id).orElse(null);
            if (result == null || result.getTcid() == null)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            respone = new ResponseEntity<TemporaryContract>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Method error " + functionName + ": " + e.getMessage());
        }
        return respone;
    }

    @RequestMapping(value = "/temporary", method = RequestMethod.POST)
    public ResponseEntity<Integer> addTemporary(@RequestBody final String temporaryStr) {
        ObjectMapper mapper = new ObjectMapper();
        TemporaryContract temporary = null;
        try {
            temporary = mapper.readValue(temporaryStr, TemporaryContract.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (temporary == null) return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(temporary);
        boolean checkContractNumber = true;
        checkContractNumber = checkContractNumber(temporary.getContract_number());

        if (!checkContractNumber) {
            return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        }
        Integer id = temporaryContractService.addTemporary(temporary).orElse(null);
        //type = 3 is "Luu tam"
        if(temporary.getType()==4 && id != null && SystemProperties.getProperty("org_type").equals("1")){
            TemporaryContract temporaryContract = temporaryContractService.getTemporaryById(id.toString()).orElse(null);
            id = processMarkTemporary(temporaryContract);
        }
        return id == null ? new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT) : new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

/*    @RequestMapping(value = "/temporary/addkey", method = RequestMethod.POST)
    public ResponseEntity<Integer> addTemporaryKey(@RequestBody @Valid final String temporaryKey) {
        if (temporaryKey == null) return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(temporaryKey);

        Integer id = temporaryContractService.addTemporary(temporary).orElse(null);
        Integer id =
        return id == null ? new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT) : new ResponseEntity<Integer>(id, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/temporaryNotCheck", method = RequestMethod.POST)
    public ResponseEntity<Integer> addTemporaryNotCheck(@RequestBody final TemporaryContract temporary) {
        /*ObjectMapper mapper = new ObjectMapper();
        TemporaryContract temporary = null;
        try {
            temporary = mapper.readValue(temporaryStr, TemporaryContract.class);
        } catch (IOException e) {
                e.printStackTrace();
        }*/
        LOGGER.info("temporary add TemporaryNotCheck is: "+temporary);
        if (temporary == null) return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(temporary);
        Integer id = null;
        if (temporary.getTcid(  ) != null) {
            id = temporaryContractService.editTemporary(temporary).orElse(null);
        } else {
            id = temporaryContractService.addTemporary(temporary).orElse(null);
        }

        //DaiCQ update cho phuong xa ngay 22/05/2020
        //type = 3 is "Luu tam online", type = 5 is "luu tam offline"
        if(temporary.getType()!=Constants.HDOL_LUU_TAM && temporary.getType()!=Constants.HDOFF_LUU_TAM && id != null && SystemProperties.getProperty("org_type").equals("1")){
            TemporaryContract temporaryContract = temporaryContractService.getTemporaryById(id.toString()).orElse(null);
            id = processMarkTemporary(temporaryContract);
        }
        //END DaiCQ update cho phuong xa ngay 22/05/2020

        LOGGER.info("Result add TemporaryNotCheck is: "+id);
        return id == null ? new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT) : new ResponseEntity<>(id, HttpStatus.OK);
    }

    @RequestMapping(value = "/temporary", method = RequestMethod.PUT)
    public ResponseEntity<Integer> editTemporary(@RequestBody @Valid final TemporaryContract temporary) {
        if (temporary == null || temporary.getTcid() == null || temporary.getTcid() == 0)
            return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        TemporaryContract item = temporaryContractService.getTemporaryById(temporary.getTcid().toString()).orElse(null);
        if (item == null || item.getTcid() == null) return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(temporary);

        Integer id = temporaryContractService.editTemporary(temporary).orElse(null);
        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

    //DaiCQ edited cho phuong xa ngay 22/05/2020
    @RequestMapping(value = "/temporary/mark", method = RequestMethod.PUT)
    public ResponseEntity<Integer> markTemporary(@RequestBody @Valid final TemporaryContract temporary) {
        if (temporary == null || temporary.getTcid() == null || temporary.getTcid() == 0)
            return new ResponseEntity<Integer>(0, HttpStatus.NO_CONTENT);
        Integer id = processMarkTemporary(temporary);
        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

    Integer processMarkTemporary(TemporaryContract temporary){
        if(org.apache.commons.lang3.StringUtils.isBlank(temporary.getNotary_date())){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            temporary.setNotary_date(dateFormat.format(new Date()));
        }
        TemporaryContract item = temporaryContractService.getTemporaryById(temporary.getTcid().toString()).orElse(null);
        StringUtils.trimAllFieldOfObject(temporary);
        Contract contract = temporaryContractService.genContractFromTemporary(temporary);
        TransactionProperty trans = new com.vn.osp.notarialservices.transaction.dto.TransactionProperty();
        trans = contractService.genaralInfoToTransactionProperty(contract, trans);
        ContractHistoryInfor history = new ContractHistoryInfor();
        history = contractService.genaralHistoryInfo(contract, history, "Đăng ký");
        Long id = temporaryContractService.addTemporaryMark(temporary.getTcid(), contract, trans, history).orElse(null);
        if (id > 0) {
            //dong bo du lieu
            SynchronizeContractTag contractTag = contractService.genSynchronizeContractTagByTrans(trans, Constants.CREATE_CONTRACT);
            List<SynchronizeContractTag> contractTags = new ArrayList<>();
            contractTags.add(contractTag);
            boolean checkSynch = contractService.synchronizeContractTags(contractTags);
            if (checkSynch) {
                boolean ch = transactionPropertyService.updateSynchStatus(Long.valueOf(id), Long.valueOf(1)).orElse(false);
            }
        }
        return Integer.valueOf(id.intValue());
    }
    //END DaiCQ edited cho phuong xa ngay 22/05/2020

    @RequestMapping(value = "/temporary", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteTemporary(@RequestParam @Valid final String id) {
        if (id == null || id.equals("") || !EditString.isNumber(id)) {
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }
        Boolean status = temporaryContractService.deleteTemporary(Long.valueOf(id));
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @RequestMapping(value = "/temporary/remove", method = RequestMethod.GET)
    public ResponseEntity<Boolean> removeFileonline(@RequestParam @Valid final String id) {
        try {
            TemporaryContract item = temporaryContractService.getTemporaryById(id.toString()).orElse(null);
            if (item == null) return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            File file = new File(item.getFile_path());
            if (!file.exists()) {
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            } else {
                item.setFile_name("");
                item.setFile_path("");
                Integer idTem = temporaryContractService.editTemporary(item).orElse(null);
                Files.delete(file.toPath());
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error("ERROR AT API REMOVE TEMPORARY CONTRACT: "+e.getMessage());
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.OK);
    }

    @RequestMapping(value = "/temporary/download/{id}", method = RequestMethod.GET)
    public void downloadFileOnline(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream inputStream = null;
        try {
            TemporaryContract item = temporaryContractService.getTemporaryById(id.toString()).orElse(null);
            if (item == null) return;
            File file = new File(item.getFile_path());
            if (!file.exists()) {
                String errorMessage = "Sorry. The file you are looking for does not exist";
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(errorMessage.getBytes());
                outputStream.close();
                return;
            }
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + item.getFile_name() + "\""));
            response.setContentLength((int) file.length());
            inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null) inputStream.close();
        }
    }


    @RequestMapping(value = "/selectReportByContractError", method = RequestMethod.POST)
    public ResponseEntity<List<ContractError>> getReportContractError(@RequestBody @Valid final String stringFilter) {
        List<ContractError> result = contractService.getReportContractError(stringFilter);
        return new ResponseEntity<List<ContractError>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/selectReportByContractAdditional", method = RequestMethod.POST)
    public ResponseEntity<List<ContractAdditional>> getReportContractAdditional(@RequestBody @Valid final String stringFilter) {
        List<ContractAdditional> result = contractService.getReportContractAdditional(stringFilter);
        return new ResponseEntity<List<ContractAdditional>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/selectReportByContractCertify", method = RequestMethod.POST)
    public ResponseEntity<List<ContractCertify>> getReportContractCertify(@RequestBody @Valid final String stringFilter) {
        List<ContractCertify> result = contractService.getReportContractCertify(stringFilter);
        return new ResponseEntity<List<ContractCertify>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/contractStatisticsDrafter", method = RequestMethod.POST)
    public ResponseEntity<List<ContractStastics>> getContractStatisticsDrafter(@RequestBody @Valid final NotaryDateFilter notaryDateFilter) {
        List<ContractStastics> result = contractService.getContractStasticsDrafter(notaryDateFilter.getNotaryDateFromFilter(), notaryDateFilter.getNotaryDateToFilter());
        return new ResponseEntity<List<ContractStastics>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/contractStatisticsNotary", method = RequestMethod.POST)
    public ResponseEntity<List<ContractStastics>> getContractStatisticsNotary(@RequestBody @Valid final NotaryDateFilter notaryDateFilter) {
        List<ContractStastics> result = contractService.getContractStasticsNotary(notaryDateFilter.getNotaryDateFromFilter(), notaryDateFilter.getNotaryDateToFilter());
        return new ResponseEntity<List<ContractStastics>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/contractStatisticsNotaryOfWards", method = RequestMethod.POST)
    public ResponseEntity<List<ContractStasticsOfDistricts>> getContractStatisticsNotaryOfWards(@RequestBody @Valid final NotaryDateFilter notaryDateFilter) {
        List<District> districts = districtService.getAll().get();
        List<ContractStasticsOfDistricts> result = new ArrayList<>();
        for (District district: districts) {
            List<ContractStasticsOfWards> thisResult = contractService.getContractStasticsNotaryOfWards(district.getCode(), notaryDateFilter.getNotaryDateFromFilter(), notaryDateFilter.getNotaryDateToFilter());
            Integer countContractOfDistrict = 0;
            for (ContractStasticsOfWards thisContractStasticsOfWards: thisResult) {
                countContractOfDistrict += thisContractStasticsOfWards.getNumber_of_contract();
            }
            if(thisResult  != null && thisResult.size()>0){
                ContractStasticsOfDistricts thisResultDistrict = new ContractStasticsOfDistricts();
                thisResultDistrict.setContractStasticsOfWards(thisResult);
                thisResultDistrict.setCountContractOfDistrict(countContractOfDistrict);
                thisResultDistrict.setDistrict_code(district.getCode());
                thisResultDistrict.setDistrict_name(district.getName());
                result.add(thisResultDistrict);
            }
        }

        return new ResponseEntity<List<ContractStasticsOfDistricts>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/contractStatisticsBank", method = RequestMethod.POST)
    public ResponseEntity<List<ContractStasticsBank>> getContractStatisticsBank(@RequestBody @Valid final NotaryDateFilter notaryDateFilter) {
        List<ContractStasticsBank> result = contractService.getContractStasticsBank(notaryDateFilter.getNotaryDateFromFilter(), notaryDateFilter.getNotaryDateToFilter());
        return new ResponseEntity<List<ContractStasticsBank>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/AddContractKind", method = RequestMethod.POST)
    public ResponseEntity<Boolean> ContractKindAdd(@RequestBody @Valid final AddContractKind addContractKind) {


        return new ResponseEntity<Boolean>((Boolean) contractKindService.ContractKindAdd(addContractKind.getId(), addContractKind.getName(), addContractKind.getUpdate_user_id(), addContractKind.getUpdate_user_name(), addContractKind.getCode()).orElse(Boolean.valueOf(false)), HttpStatus.OK);
    }

    @RequestMapping(value = "/UpdateContractKind", method = RequestMethod.POST)
    public ResponseEntity<Boolean> UpdateContractKind(@RequestBody @Valid final UpdateContractKind updateContractKind) {
        return new ResponseEntity<Boolean>((Boolean) contractKindService.UpdateContractKind(updateContractKind.getCkId(), updateContractKind.getName(), updateContractKind.getUpdate_user_id(), updateContractKind.getUpdate_user_name(), updateContractKind.getContract_kind_code()).orElse(Boolean.valueOf(false)), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteContractKind", method = RequestMethod.POST)
    public ResponseEntity<Boolean> deleteProvince(@RequestBody @Valid final Long id) {
        return new ResponseEntity<>((Boolean) contractKindService.deleteContractKind(id).orElse(Boolean.valueOf(false)), HttpStatus.OK);
    }

    @RequestMapping(value = "/findContractKindByFilter", method = RequestMethod.POST)
    public ResponseEntity<List<ContractKind>> findContractKindByFilter(@RequestBody final String stringFilter) {
        List<ContractKind> contractKinds = contractKindService.findContractKindByFilter(stringFilter).orElse(new ArrayList<ContractKind>());
        return new ResponseEntity<>(contractKinds, HttpStatus.OK);
    }

    @RequestMapping(value = "/countContractKindByFilter", method = RequestMethod.POST)
    public ResponseEntity<BigInteger> countContractKindByFilter(@RequestBody final String stringFilter) {
        BigInteger result = contractKindService.countContractKindByFilter(stringFilter).orElse(BigInteger.valueOf(0));

        return new ResponseEntity<BigInteger>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/countTemporaryBySearch", method = RequestMethod.GET)
    public ResponseEntity<BigInteger> countTemporaryBySearch(@RequestParam final String search, @RequestParam final String type) {

        /*for search*/
        String stringFilter = temporaryContractService.getStringFilterFromSearch(search, type);

        BigInteger transactionByFilter = temporaryContractService.countTemporaryByFilter(stringFilter).orElse(BigInteger.valueOf(0));
        return new ResponseEntity<BigInteger>(transactionByFilter, HttpStatus.OK);
    }

    @RequestMapping(value = "/countTemporaryBySearchOffline", method = RequestMethod.GET)
    public ResponseEntity<BigInteger> countTemporaryBySearchOffline(@RequestParam final String search, @RequestParam final String type) {

        /*for search*/
        String stringFilter = temporaryContractService.getStringFilterFromSearchOffline(search, type);

        BigInteger transactionByFilter = temporaryContractService.countTemporaryByFilter(stringFilter).orElse(BigInteger.valueOf(0));
        return new ResponseEntity<BigInteger>(transactionByFilter, HttpStatus.OK);
    }

    @RequestMapping(value = "/temporarysBySearch", method = RequestMethod.GET)
    public ResponseEntity<List<TemporaryContract>> temporarysBySearch(@RequestParam final String search, @RequestParam final String type, @RequestParam final int offset, @RequestParam final int number) {

        String stringFilter = temporaryContractService.getStringFilterFromSearch(search, type);

        if (number > 0) {
            stringFilter += "limit " + offset + "," + number + " ";
        } else {
            stringFilter += "limit 0,25 ";
        }

        List<TemporaryContract> temporaryContracts = temporaryContractService.getTemporaryByFilter(stringFilter).orElse(null);
        return new ResponseEntity<List<TemporaryContract>>(temporaryContracts, HttpStatus.OK);
    }

    @RequestMapping(value = "/temporarysBySearchOffline", method = RequestMethod.GET)
    public ResponseEntity<List<TemporaryContract>> temporarysBySearchOffline(@RequestParam final String search, @RequestParam final String type, @RequestParam final int offset, @RequestParam final int number) {

        String stringFilter = temporaryContractService.getStringFilterFromSearchOffline(search, type);

        if (number > 0) {
            stringFilter += "limit " + offset + "," + number + " ";
        } else {
            stringFilter += "limit 0,25 ";
        }

        List<TemporaryContract> temporaryContracts = temporaryContractService.getTemporaryByFilter(stringFilter).orElse(null);
        return new ResponseEntity<List<TemporaryContract>>(temporaryContracts, HttpStatus.OK);
    }


    @RequestMapping(value = "/checkContractNumber", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkAddContractNumber(@RequestParam final String contract_number) {
        try {

            BigInteger count = contractService.countByContractNumber(contract_number).orElse(BigInteger.valueOf(0));
            if (count.compareTo(BigInteger.valueOf(0)) > 0) {
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            } else {
                    /*BigInteger countOnline = temporaryContractService.countTemporaryByFilter("where contract_number='" + contract_number + "'").orElse(BigInteger.valueOf(0));
                    if (countOnline.compareTo(BigInteger.valueOf(0)) > 0) {
                        return new ResponseEntity<Boolean>(false, HttpStatus.OK);
                    } else if (countOnline.compareTo(BigInteger.valueOf(0)) == 0) {
                        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
                    }*/
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/checkUserExistContract", method = RequestMethod.GET)
    public ResponseEntity<BigInteger> checkUserExistContract(@RequestParam @Valid final int userId) {
        return new ResponseEntity<>((BigInteger) contractService.checkUserExistContract(userId).orElse(BigInteger.valueOf(0)), HttpStatus.OK);
    }

    @RequestMapping(value = "/getListSalesReport", method = RequestMethod.GET)
    public ResponseEntity<SalesWrapper> getListSalesReport(@RequestParam final String fromDate, @RequestParam final String toDate) {


        SalesWrapper salesWappers = contractService.getListSalesReport(fromDate, toDate).orElse(null);
        return new ResponseEntity<SalesWrapper>(salesWappers, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-Property-by-contract-id", method = RequestMethod.GET)
    public ResponseEntity<PropertyBo> getPropertyByContractId(@RequestParam @Valid final Long contractId) {
        return new ResponseEntity<PropertyBo>(contractService.getPropertyByContractId(contractId), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-list-property-real-estate-type", method = RequestMethod.GET)
    public ResponseEntity<List<PropertyRealEstateType>> getListPropertyRealEstateType(@RequestParam(name = "parent_code", defaultValue = "", required = false) Integer parent_code){
        return new ResponseEntity<List<PropertyRealEstateType>>(propertyService.getListPropertyRealEstateType(parent_code), HttpStatus.OK);
    }


    /*@RequestMapping(value = "/searchContractOfflineTemporary", method = RequestMethod.GET)
    public ResponseEntity<List<TemporaryContract>> searchContractOfflineTemporary(@RequestParam @Valid final String search , @RequestParam @Valid final int offset, @RequestParam @Valid final int number) {

        String filter = temporaryContractService.getStringFilterFromSearchOffline()
        return new ResponseEntity<BigInteger>((BigInteger)contractService.checkUserExistContract(userId).orElse(BigInteger.valueOf(0)), HttpStatus.OK);
    }*/


    private boolean checkContractNumber(String contract_number) {
        try {
            BigInteger count = contractService.countByContractNumber(contract_number).orElse(BigInteger.valueOf(0));
            if (count.compareTo(BigInteger.valueOf(0)) > 0) {
                return false;
            } else {
                /*BigInteger countOnline = temporaryContractService.countTemporaryByFilter("where contract_number='" + contract_number+"'").orElse(BigInteger.valueOf(0));
                if (countOnline.compareTo(BigInteger.valueOf(0)) > 0) {
                   return false;
                }else if(countOnline.compareTo(BigInteger.valueOf(0)) == 0){
                    return true;
                }*/
                return true;
            }
        } catch (Exception e) {
            return false;
        }

    }
    /* check ca 2 bang contract va temporary
    private boolean checkContractNumber(String contract_number) {
        try{
            BigInteger count = contractService.countByContractNumber(contract_number).orElse(BigInteger.valueOf(0));
            if (count.compareTo(BigInteger.valueOf(0)) > 0) {
                return false;
            } else {
                BigInteger countOnline = temporaryContractService.countTemporaryByFilter("where contract_number='" + contract_number+"'").orElse(BigInteger.valueOf(0));
                if (countOnline.compareTo(BigInteger.valueOf(0)) > 0) {
                    return false;
                }else if(countOnline.compareTo(BigInteger.valueOf(0)) == 0){
                    return true;
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }*/


}
