package com.vn.osp.notarialservices.authenticatedCopies.controller;

import com.vn.osp.notarialservices.authenticatedCopies.dto.AuthenticateDTO;
import com.vn.osp.notarialservices.authenticatedCopies.service.AuthenticateService;
import com.vn.osp.notarialservices.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/authenticatedCopies")
public class AuthenticatedCopiesController {
    @Autowired
    AuthenticateService authenticateService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<AuthenticateDTO> getById(@RequestParam @Valid final Long id){
        AuthenticateDTO item = new AuthenticateDTO();
        try{
            item= authenticateService.get(id);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(item , HttpStatus.NO_CONTENT);
        }
    }
    @RequestMapping(value = "/list-by-number-authentication-copies", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<AuthenticateDTO>> getByUser(@RequestParam(name = "cert_number", required = false, defaultValue = "") final  String cert_number){
        List<AuthenticateDTO> authenticateDTOS =authenticateService.findByFilter(" and bo.cert_number = '"+cert_number+"'");
        return new ResponseEntity<>(authenticateDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<AuthenticateDTO>> list(
            @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(name = "cert_number", required = false, defaultValue = "") final String cert_number,
            @RequestParam(name = "dateFrom", required = false, defaultValue = "") String dateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "") String dateTo) {

        String search = authenticateService.getStringFilter(userId, cert_number,dateFrom, dateTo);
        List<AuthenticateDTO> item=new ArrayList<>();
        try{
            item= authenticateService.findByFilter(search);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(item, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/list-page", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<AuthenticateDTO>> listPage(
                      @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
                      @RequestParam(name = "dateFrom", required = false, defaultValue = "") final String dateFrom,
                      @RequestParam(name = "dateTo", required = false, defaultValue = "") final String dateTo,
                      @RequestParam(name = "cert_number", required = false, defaultValue = "") final String cert_number,
                      @RequestParam @Valid final int offset,
                      @RequestParam @Valid final int number){
        String search = authenticateService.getStringFilter(userId, cert_number,dateFrom, dateTo);
        List<AuthenticateDTO> item=new ArrayList<>();
//        try{
//            if(!certNumber.equals("")){
//                String searchCertNumber = authenticateService.getStringCertNumber(certNumber);
//                item= authenticateService.listItemPage(searchCertNumber, offset,number);
//                return new ResponseEntity<>(item,HttpStatus.OK);
//            }else {
//                item= authenticateService.listItemPage(search, offset,number);
//                return new ResponseEntity<>(item,HttpStatus.OK);
//            }
//        }catch (Exception e){
//            return new ResponseEntity<>(item , HttpStatus.NO_CONTENT);
//        }
        try{
            item= authenticateService.listItemPage(search, offset,number);
                return new ResponseEntity<>(item,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(item , HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/counts", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Integer> countTotal(
                 @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
                 @RequestParam(name = "dateFrom", required = false, defaultValue = "") final String dateFrom,
                 @RequestParam(name = "dateTo", required = false, defaultValue = "") final String dateTo,
                 @RequestParam(name = "cert_number", required = false, defaultValue = "") final String cert_number) {

        String search = authenticateService.getStringFilter(userId, cert_number,dateFrom, dateTo);
        Integer result = authenticateService.countByFilter(search);
        return new ResponseEntity<>(result, HttpStatus.OK);

//        if (!certNumber.equals("")){
//            String searchCertNumber = authenticateService.getStringCertNumber(certNumber);
//            Integer result = authenticateService.countByFilter(searchCertNumber);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        }else {
//            Integer result = authenticateService.countByFilter(search);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Boolean> add(@RequestBody @Valid final AuthenticateDTO item) {
        if (item == null)
            return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        List<AuthenticateDTO> listByCertNumber = authenticateService.findByFilter(" and bo.cert_number = '"+item.getCert_number()+"'");
        if(listByCertNumber.size()>0)
            return new ResponseEntity<>(false, HttpStatus.OK);
        StringUtils.trimAllFieldOfObject(item);
        Boolean result = authenticateService.add(item);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Boolean> update(@RequestBody @Valid final AuthenticateDTO item) {
        if (item == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        AuthenticateDTO authenticateDB = authenticateService.get(item.getId());
        if(authenticateDB==null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(item);
        Boolean result = authenticateService.update(item);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public  @ResponseBody ResponseEntity<Boolean> delete(@RequestParam(name = "id") @Valid final Long id) {
        AuthenticateDTO authenticateDB = authenticateService.get(id);
        if(authenticateDB == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        Boolean result = authenticateService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
