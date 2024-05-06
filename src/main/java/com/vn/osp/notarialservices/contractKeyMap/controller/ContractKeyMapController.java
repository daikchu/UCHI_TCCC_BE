package com.vn.osp.notarialservices.contractKeyMap.controller;

import com.vn.osp.notarialservices.contractKeyMap.domain.ContractKeyMapBO;
import com.vn.osp.notarialservices.contractKeyMap.service.ContractKeyMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value ="/ContractKeyMap")
public class ContractKeyMapController {
    private final ContractKeyMapService contractKeyMapService;


    @Autowired
    public ContractKeyMapController(final ContractKeyMapService contractKeyMapService){
        this.contractKeyMapService=contractKeyMapService;
    }

    @RequestMapping(value = "/addContractKeyMap", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addContractKeyMap(@RequestBody @Valid final ContractKeyMapBO contractKeyMapBO)  {
        if(contractKeyMapBO.getFirst_word()==null) contractKeyMapBO.setFirst_word("");
        if(contractKeyMapBO.getEnd_word()==null) contractKeyMapBO.setEnd_word("");
        Boolean check =  contractKeyMapService.addContractKeyMap(contractKeyMapBO);
        return new ResponseEntity<>(check, HttpStatus.OK);
    }

    @RequestMapping(value = "/listContractKeyMap", method = RequestMethod.GET)
    public ResponseEntity<List<ContractKeyMapBO>> listContractKeyMap(@RequestParam @Valid final String search, @RequestParam @Valid final int offset, @RequestParam @Valid final int number)  {
        String stringFilter = contractKeyMapService.getStringFilterFromSearch(search);

        List<ContractKeyMapBO> list =  contractKeyMapService.listContractKeyMap(stringFilter, offset, number);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getContractKeyMapByFilter", method = RequestMethod.POST)
    public ResponseEntity<List<ContractKeyMapBO>> getContractKeyMapByFilter(@RequestBody @Valid final String stringFilter)  {

        List<ContractKeyMapBO> list =  contractKeyMapService.getContractKeyMapByFilter(stringFilter);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/countContractKeyMap", method = RequestMethod.GET)
    public ResponseEntity<Long> countContractKeyMap(@RequestParam @Valid final String search)  {
        String stringFilter = contractKeyMapService.getStringFilterFromSearch(search);

        Long result =  contractKeyMapService.countContractKeyMap(stringFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteContractKeyMaps", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteContractKeyMaps(@RequestParam @Valid final String listId)  {

        Boolean result =  contractKeyMapService.deleteContractKeyMaps(listId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/editContractKeyMap", method = RequestMethod.POST)
    public ResponseEntity<Boolean> editContractKeyMap(@RequestBody @Valid final ContractKeyMapBO bo)  {
        if(bo.getFirst_word()==null) bo.setFirst_word("");
        if(bo.getEnd_word()==null) bo.setEnd_word("");
        Boolean check =  contractKeyMapService.editContractKeyMap(bo);
        return new ResponseEntity<>(check, HttpStatus.OK);
    }

  /*  @RequestMapping(value = "/read-file-doc", method = RequestMethod.POST)
    public ResponseEntity<ContractReadFileDoc> readFileDoc(@RequestBody @Valid final MultipartFile file) throws IOException {

        DissectFile dissectFile = new DissectFile(contractTempService, userService, contractKeyMapService);

        String folder = SystemProperties.getProperty("contract_dissect_folder");
        String fileName = file.getOriginalFilename();
        File fileSave = null;
        Contract contract = new Contract();
        FileOutputStream outputStream = null;
        ContractReadFileDoc contractReadFileDoc = new ContractReadFileDoc();
        try{
            if (fileName.substring(fileName.lastIndexOf('.')).equals(".docx")) {//file is docx
                fileSave = FileUtil.createNewFileDotDocX(folder, "READ_");
                fileName = dissectFile.convertToDotDocFile(fileSave);
                Files.delete(fileSave.toPath());
                fileSave = new File(fileSave.getParent() + "\\" + fileName);
            } else {
                fileSave = FileUtil.createNewFileDotDoc(folder, "READ_");
            }
            outputStream = new FileOutputStream(fileSave);
            outputStream.write(file.getBytes());
            contract = dissectFile.read(fileSave, contract, Constants.TYPE_READ_WORD);
            String[] fileData = dissectFile.readAndGetListParagraph(fileSave);
            contractReadFileDoc.setContract(contract);
            contractReadFileDoc.setFields(fileData);
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Files.delete(fileSave.toPath());
        }
        return new ResponseEntity<ContractReadFileDoc>(contractReadFileDoc, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/check-exists-key", method = RequestMethod.POST)
    public ResponseEntity<Boolean> checkExistKey(@RequestBody @Valid final ContractKeyMapBO contractKeyMapBO) {
        Boolean check = contractKeyMapService.checkExistKey(contractKeyMapBO);

        return new ResponseEntity<>(check, HttpStatus.OK);
    }
}



