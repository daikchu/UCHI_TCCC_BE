package com.vn.osp.notarialservices.certificate.controller;

import com.vn.osp.notarialservices.certificate.dto.CertDTO;
import com.vn.osp.notarialservices.certificate.dto.CertNumberDTO;
import com.vn.osp.notarialservices.certificate.service.CertService;
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
@RequestMapping(value="/certificate")
public class CertController {
    @Autowired
    CertService certService;

    @RequestMapping( value = "/certificateNumber", method = RequestMethod.GET)
    public ResponseEntity<CertNumberDTO> getCertificateNumber(
            @RequestParam (value = "userId", defaultValue = "", required = false) final Long userId,
            @RequestParam (value = "cert_type", defaultValue = "", required = false) final Long cert_type
    ){

        List<CertNumberDTO> items = new ArrayList<>();
        CertNumberDTO result = new CertNumberDTO();
        Long cert_number = 0l;
        try{
            items = certService.getCertNumber(userId,cert_type);
            if (items != null && items.size() > 0){
                result = items.get(0);
                cert_number = items.get(0).getCert_number() + 1;
                result.setCert_number(cert_number);
            }
        }catch (Exception e){
            result.setCert_number(1l);
        }
        finally {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/addCertNumber", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Boolean> addCertNumber(@RequestBody @Valid final CertNumberDTO item){
        if (item == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(item);

        if(item.getUser_id() != null && !item.getKind_id().equals("")){
            List<CertNumberDTO> itemDBs = certService.checkExistCertnumber(item.getKind_id(),item.getUser_id(),item.getCert_type());

            if(itemDBs.size() > 0){
                CertNumberDTO itemDB = itemDBs.get(0);
                itemDB.setCert_number(item.getCert_number());
                itemDB.setKind_id(item.getKind_id());
                itemDB.setCert_type(item.getCert_type());
                itemDB.setDistrict_code(item.getDistrict_code());
                itemDB.setVillage_code(item.getVillage_code());

                Boolean result = certService.updateCertNumber(itemDB);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else {
                Boolean result = certService.addCertNumber(item);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

        }else {
            return new ResponseEntity<>(false,HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<CertDTO> getById(@RequestParam @Valid final Long id) {
        CertDTO item= new CertDTO();
        try{
            item= certService.get(id);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new CertDTO(), HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/list-by-cert-number", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<CertDTO>> getByUser(@RequestParam(name = "cert_number", required = false, defaultValue = "") final String cert_number,
                                                                 @RequestParam(name = "type", required = false, defaultValue = "") final Integer type,
                                                                 @RequestParam(name = "entryUserId", required = false, defaultValue = "") final Integer entryUserId,
                                                                 @RequestParam(name = "cert_book", required = false, defaultValue = "") final String cert_book) {
        String query = "";
        if (!org.apache.commons.lang3.StringUtils.isBlank(cert_number)) query += " and bo.cert_number = '"+cert_number+"'";
        if (type != null) query += " and bo.type = '"+type+"'";
        if (entryUserId != null) query += " and bo.entry_user_id = "+entryUserId;
        if (!org.apache.commons.lang3.StringUtils.isBlank(cert_book)) query += " and bo.notary_book = '"+cert_book+"'";
        List<CertDTO> certDTOS = certService.findByFilter(query);
        return new ResponseEntity<>(certDTOS, HttpStatus.OK);
    }

   /* @RequestMapping(value = "/list-by-filter", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<List<ReportTT03CertDTO>> getByUser(@RequestBody @Valid final String stringFilter) {
        List<ReportTT03CertDTO> signCertDTOS = certService.findByFilter(stringFilter);
        return new ResponseEntity<>(signCertDTOS, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<CertDTO>> list(
            @RequestParam(name = "cert_number", required = false, defaultValue = "") String cert_number,
            @RequestParam(name = "notary_book", required = false, defaultValue = "") String notary_book,
            @RequestParam(name = "dateFrom", required = false, defaultValue = "") String dateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "") String dateTo,
            @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(name = "type", required = false, defaultValue = "") Integer type,
            @RequestParam(name = "attestation_template_code", required = false, defaultValue = "") String attestation_template_code) {

        String search = certService.getStringFilter(userId, cert_number, notary_book, dateFrom, dateTo, type, "");
        List<CertDTO> item=new ArrayList<>();
        try{
            item= certService.findByFilter(search);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(item, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/list-page", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<CertDTO>> listPage(
            @RequestParam(name = "cert_number", required = false, defaultValue = "") String cert_number,
            @RequestParam(name = "notary_book", required = false, defaultValue = "") String notary_book,
            @RequestParam(name = "dateFrom", required = false, defaultValue = "") String dateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "") String dateTo,
            @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(name = "type", required = false, defaultValue = "") Integer type,
            @RequestParam @Valid final int offset,
            @RequestParam @Valid final int number) {

        String search = certService.getStringFilter(userId, cert_number, notary_book, dateFrom, dateTo, type, "");
        List<CertDTO> item=new ArrayList<>();
        try{
            item= certService.listItemPage(search, offset,number);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(item, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Integer> counTotal(
            @RequestParam(name = "cert_number", required = false, defaultValue = "") String cert_number,
            @RequestParam(name = "notary_book", required = false, defaultValue = "") String notary_book,
            @RequestParam(name = "dateFrom", required = false, defaultValue = "") String dateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "") String dateTo,
            @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(name = "type", required = false, defaultValue = "") Integer type,
            @RequestParam(name = "attestation_template_code", required = false, defaultValue = "") String attestation_template_code) {

        String search = certService.getStringFilter(userId, cert_number, notary_book, dateFrom, dateTo, type, attestation_template_code);
        Integer result = certService.countByFilter(search);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Boolean> add(@RequestBody @Valid final CertDTO item) {
        if (item == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        /*List<CertDTO> listByCertNumber = certService.findByFilter(" and bo.cert_number = '"+item.getCert_number()+"'");
        if(listByCertNumber.size()>0) return new ResponseEntity<>(false, HttpStatus.OK);*/
        StringUtils.trimAllFieldOfObject(item);
        Boolean result = certService.add(item);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Boolean> update(@RequestBody @Valid final CertDTO item) {
        if (item == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        CertDTO signCertDB = certService.get(item.getId());
        if(signCertDB==null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(item);
        Boolean result = certService.update(item);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Boolean> delete(@RequestParam(name = "id") @Valid final Long id) {
        CertDTO signCertDB = certService.get(id);
        if(signCertDB==null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        Boolean result = certService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
