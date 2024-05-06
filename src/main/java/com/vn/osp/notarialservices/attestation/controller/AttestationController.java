package com.vn.osp.notarialservices.attestation.controller;

import com.vn.osp.notarialservices.attestation.dto.AttestationDTO;
import com.vn.osp.notarialservices.attestation.dto.AttestationTemplateFieldMapDTO;
import com.vn.osp.notarialservices.attestation.service.AttestationService;
import com.vn.osp.notarialservices.utils.StringUtils;
import com.vn.osp.notarialservices.utils.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api-attestation")
public class AttestationController {
    @Autowired
    AttestationService attestationService;

    @RequestMapping(value = "/search")
    public @ResponseBody
    ResponseEntity<List<AttestationDTO>> search(
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "parent_code", required = false) String parent_code){
        List<AttestationDTO> result = new ArrayList<>();
        try{
            String filter = "";
            if(!org.apache.commons.lang3.StringUtils.isBlank(code)) filter += " and bo.code='"+code+"'";
            if(!org.apache.commons.lang3.StringUtils.isBlank(parent_code)) filter += " and bo.parent_code='"+parent_code+"'";
            result = attestationService.search(filter);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value="/count", method = RequestMethod.GET)
    public ResponseEntity<Long> count(@RequestParam(name = "code", defaultValue = "", required = false) String code)  {
        String filter = "";
        if(!org.apache.commons.lang3.StringUtils.isBlank(code)) filter += " and bo.code = '"+code+"'";
        Long result = attestationService.count(filter);
        return new ResponseEntity<Long>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/get", method = RequestMethod.GET)
    public ResponseEntity<AttestationDTO> get(@RequestParam(name = "id") Long id)  {
        AttestationDTO result = attestationService.get(id);
        return new ResponseEntity<AttestationDTO>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Boolean> add(@RequestBody @Valid final AttestationDTO item){
        if (item == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(item);
        Boolean result = attestationService.add(item);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Boolean> update(@RequestBody @Valid final AttestationDTO item){
        if (item == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        AttestationDTO attestationDB = attestationService.get(item.getId());
        if(attestationDB==null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(item);
        Boolean result = attestationService.update(item);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateSync", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Boolean> updateSync(@RequestBody @Valid final AttestationDTO item){
        if (item == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        List<AttestationDTO> attestationDBS = attestationService.search(" and bo.code = '"+item.getCode()+"'");
        if(attestationDBS==null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(item);
        try{
            AttestationDTO attestationDB = attestationDBS.get(0);
            item.setId(attestationDB.getId());
            item.setEntry_user_id(attestationDB.getEntry_user_id());
            item.setEntry_user_name(attestationDB.getEntry_user_name());
            item.setEntry_date_time(attestationDB.getEntry_date_time());
            attestationService.update(item);

            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Boolean> delete(@RequestParam Long id){
        AttestationDTO attestationDB = attestationService.get(id);
        if(attestationDB == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        Boolean result = attestationService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/getListCertTemplateFieldMap", method = RequestMethod.GET)
    public ResponseEntity<List<AttestationTemplateFieldMapDTO>> getListCertTemplateFieldMap(
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter){
        List<AttestationTemplateFieldMapDTO> result = attestationService.getAllAttestationTemplateFieldMap(filter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
