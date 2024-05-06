package com.vn.osp.notarialservices.notaryBook.controller;


import com.vn.osp.notarialservices.notaryBook.dto.NotaryBookDTO;
import com.vn.osp.notarialservices.notaryBook.service.NotaryBookService;
import com.vn.osp.notarialservices.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/notaryBook")
public class NotaryBookController {
    @Autowired
    NotaryBookService notaryBookService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<NotaryBookDTO> getById(@RequestParam @Valid final Long id){
        NotaryBookDTO item = new NotaryBookDTO();
        try{
            item= notaryBookService.get(id);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(item , HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/getAllNotaryBook", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<NotaryBookDTO>> getAllNotaryBook(){
        List<NotaryBookDTO> item = new ArrayList<>();
        try{
            item= notaryBookService.getAll();
            return new ResponseEntity<>(item, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(item , HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<NotaryBookDTO>> list(
            @RequestParam(name = "notary_book", required = false, defaultValue = "") String notaryBook,
            @RequestParam(name = "type", required = false, defaultValue = "") Integer type) {

        List<NotaryBookDTO> item=new ArrayList<>();
        try{
            String search = notaryBookService.getStringFilter(notaryBook, type);
            item= notaryBookService.findByFilter(search);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(item, HttpStatus.NO_CONTENT);
        }
    }
    @RequestMapping(value = "/listSattusNotarybook", method = RequestMethod.GET)
    public ResponseEntity<List<NotaryBookDTO>> listAllType(
            @RequestParam(name = "notary_book", required = false, defaultValue = "") String notaryBook,
            @RequestParam(name = "type", required = false, defaultValue = "") Integer type) {

        List<NotaryBookDTO> item=new ArrayList<>();
        try{
            String search = notaryBookService.getStringFilter(notaryBook, type);
            item= notaryBookService.findByFilterSattusOpen(search);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(item, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/list-by-number-notary-book", method = RequestMethod.GET)
    public ResponseEntity<List<NotaryBookDTO>> getByUser(@RequestParam(name = "notary_book", required = false, defaultValue = "") final  String notaryBook){
        List<NotaryBookDTO> notaryBookDTOS =notaryBookService.findByFilter(" and bo.notary_book = '"+notaryBook+"'");
        return new ResponseEntity<>(notaryBookDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/list-page", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<NotaryBookDTO>> listPage(
            @RequestParam(name = "notary_book", required = false, defaultValue = "") String notaryBook,
            @RequestParam @Valid final int offset,
            @RequestParam @Valid final int number) {

        String search = "";
        List<NotaryBookDTO> item=new ArrayList<>();
        try{
            if(!notaryBook.equals("")){
                search = notaryBookService.getStringFilter(notaryBook,null);
                item= notaryBookService.listItemPage(search,offset,number);
                return new ResponseEntity<>(item, HttpStatus.OK);
            }else {
                item= notaryBookService.listItemPage(search,offset,number);
                return new ResponseEntity<>(item, HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(item, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/counts", method = RequestMethod.GET)
    public ResponseEntity<Integer> countTotal(
            @RequestParam(name = "notary_book", required = false, defaultValue = "") final String notaryBook) {

        String search = "";
        if (!notaryBook.equals("")){
            String searchNotaryBook = notaryBookService.getStringFilter(notaryBook,null);
            Integer result = notaryBookService.countByFilter(searchNotaryBook);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            Integer result = notaryBookService.countByFilter(search);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Boolean> add(@RequestBody @Valid final NotaryBookDTO item) {
        if (item == null)
            return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        List<NotaryBookDTO> listByNotaryBook = notaryBookService.findByFilter(" and bo.notary_book = '"+item.getNotary_book()+"' and bo.type = '"+item.getType()+"'");
        if(listByNotaryBook.size()>0)
            return new ResponseEntity<>(false, HttpStatus.OK);
        StringUtils.trimAllFieldOfObject(item);
        Boolean result = notaryBookService.add(item);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Boolean> update(@RequestBody @Valid final NotaryBookDTO item) {
        if (item == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        NotaryBookDTO notaryDB = notaryBookService.get(item.getId());
        if(notaryDB==null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        StringUtils.trimAllFieldOfObject(item);
        Boolean result = notaryBookService.update(item);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public  @ResponseBody ResponseEntity<Boolean> delete(@RequestParam(name = "id") @Valid final Long id) {
        NotaryBookDTO notaryDB = notaryBookService.get(id);
        if(notaryDB == null) return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        Boolean result = notaryBookService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/checkNotaryNumber", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkAddNotaryNumber(
            @RequestParam(name = "notary_book", required = false, defaultValue = "") String notary_book,
            @RequestParam(name = "id", required = false, defaultValue = "") Long id,
            @RequestParam(name = "type", required = false, defaultValue = "") Long type) {
        try{
            Integer count = notaryBookService.countByNotaryNumber(notary_book,id,type);
            if (count > 0) {
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            }
        }catch (Exception ex){
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/checkDeleteNotaryNumber", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkDeleteNotaryNumber(
            @RequestParam(name = "notary_book", required = false, defaultValue = "") String notary_book,
            @RequestParam(name = "type", required = false, defaultValue = "") Long type) {
        try{
            Integer count = notaryBookService.checkDeleteNotaryNumber(notary_book,type);
            if (count > 0) {
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/checkDeleteNotaryNumberContract", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkDeleteNotaryNumberContract(
            @RequestParam(name = "notary_book", required = false, defaultValue = "") String notary_book,
            @RequestParam(name = "type", required = false, defaultValue = "") Long type) {
        try{
            Integer count = notaryBookService.checkDeleteNotaryNumberContract(notary_book,type);
            if (count > 0) {
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/checkValidateStatusNotary", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkValidateStatusNotary(
            @RequestParam(name = "status", required = false, defaultValue = "") Long status,
            @RequestParam(name = "type", required = false, defaultValue = "") Long type) {
        try{
            Integer count = notaryBookService.checkValidateStatusNotary(status,type);
            if (count > 0) {
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }
}
