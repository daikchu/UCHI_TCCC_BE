package com.vn.osp.notarialservices.district.controller;

import com.vn.osp.notarialservices.contract.domain.ContractTemplateBO;
import com.vn.osp.notarialservices.district.dto.District;
import com.vn.osp.notarialservices.district.service.DistrictService;
import com.vn.osp.notarialservices.systemmanager.service.AccessHistoryService;
import com.vn.osp.notarialservices.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/district")
public class DistrictController {
    @Autowired
    AccessHistoryService accessHistoryService;
    @Autowired
    DistrictService districtService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<District> getPropertyTemplateById(@RequestParam  @Valid final Long id) {
        District item=new District();
        try{
            item= districtService.get(id).orElse(new District());
            return new ResponseEntity<District>(item,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<District>(item , HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/list-by-province", method = RequestMethod.GET)
    public ResponseEntity<List<District>> listRole() {
        List<District> result = districtService.findByFilter("").get();
        return new ResponseEntity<List<District>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/list-by-filter", method = RequestMethod.POST)
    public ResponseEntity<List<District>> getReportByUser(@RequestBody @Valid final String stringFilter) {
        List<District> result = new ArrayList<>();
        List<District> districts = districtService.findByFilter(stringFilter).get();
       /* for (District dis: districts) {
            if(dis.getCode().equals(stringFilter)){
                result.add(dis);
                return new ResponseEntity<List<District>>(result, HttpStatus.OK);
            }
        }*/
        return new ResponseEntity<List<District>>(districts, HttpStatus.OK);
    }

    @RequestMapping(value = "/list-page", method = RequestMethod.GET)
    public ResponseEntity<List<District>> listContractTemplatePage(@RequestParam (name = "offset", defaultValue = "1", required = false) @Valid final String offset,
                                                                             @RequestParam(name = "limit", defaultValue = "25", required = false)  @Valid final String limit,
                                                                             @RequestParam(name = "filter", defaultValue = "", required = false)  @Valid final String filter) {
        List<District> item=new ArrayList<>();
        try{
            item= districtService.listItemPage(filter, offset,limit).orElse(new ArrayList<>());
            return new ResponseEntity<List<District>>(item,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<List<District>>(item , HttpStatus.NO_CONTENT);
        }

    }

    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public ResponseEntity<Integer> counTotal(@RequestBody final String stringFilter) {
        Integer result = districtService.countByFilter(stringFilter).orElse(Integer.valueOf(0));
        return new ResponseEntity<Integer>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Boolean> add(@RequestBody @Valid final District item) {
        if (item == null) return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(item);
        boolean checkDistrictCode = checkDistrictCodeAdd(item.getCode());
        if (!checkDistrictCode) {
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }
        Boolean result = districtService.add(item).orElse(false);

        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> update(@RequestBody @Valid final District item) {
        if (item == null) return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(item);
        List<District> itemDBs = districtService.findByCode(item.getCode()).orElse(new ArrayList<>());
        boolean checkDistrictCode = checkDistrictCodeUpdate(item);
        if (!checkDistrictCode) {
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }
        Boolean result = districtService.update(item).orElse(false);

        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@RequestParam(name = "id") @Valid final Long id) {
        Boolean result = districtService.delete(id).orElse(false);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    public boolean checkDistrictCodeAdd(String code){
        List<District> districts = districtService.findByCode(code).orElse(new ArrayList<>());
        if(!districts.isEmpty()) return false;
        else return true;
    }

    public boolean checkDistrictCodeUpdate(District item){
        List<District> districts = districtService.findByCode(item.getCode()).orElse(new ArrayList<>());
        if(districts.isEmpty() || (districts.size()==1 && districts.get(0).getId().equals(item.getId()))) return true;
        else return false;
    }


}
