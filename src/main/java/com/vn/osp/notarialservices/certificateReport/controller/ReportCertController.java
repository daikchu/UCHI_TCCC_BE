package com.vn.osp.notarialservices.certificateReport.controller;

//import com.strobel.decompiler.ast.Switch;
import com.vn.osp.notarialservices.certificateReport.domain.ReportLuongGDBDSBO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertCapHuyenDTO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertCapTinhDTO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertDTO;
import com.vn.osp.notarialservices.certificateReport.service.ReportCertService;
import com.vn.osp.notarialservices.utils.Constants;
import com.vn.osp.notarialservices.utils.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/reportCertificate")
public class ReportCertController {
    @Autowired
    ReportCertService reportCertService;


    @RequestMapping(value = "/tt03", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<ReportTT03CertDTO> counTT03(
            @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(name = "dateFrom", required = false, defaultValue = "") String dateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "") String dateTo) {
        String level_cert = Constants.LEVEL_CERT_XA;
        ReportTT03CertDTO result = reportCertService.countTT03ByFilter(userId, dateFrom, dateTo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/tt03-list-xa-thuoc-huyen", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<ReportTT03CertCapHuyenDTO>> counTT03CapHuyen(
            @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(name = "districtCode", required = false, defaultValue = "") String districtCode,
            @RequestParam(name = "dateFrom", required = false, defaultValue = "") String dateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "") String dateTo) {
        String level_cert = Constants.LEVEL_CERT_XA;
        List<ReportTT03CertCapHuyenDTO> result = reportCertService.countTT03CapHuyenByFilter(level_cert, districtCode, userId, dateFrom, dateTo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/tt03-list-phong-tu-phap", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<ReportTT03CertCapTinhDTO>> counTT03ListPhongTuPhap(
            @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(name = "districtCode", required = false, defaultValue = "") String districtCode,
            @RequestParam(name = "dateFrom", required = false, defaultValue = "") String dateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "") String dateTo) {
        String level_cert = Constants.LEVEL_CERT_HUYEN;
        List<ReportTT03CertCapTinhDTO> result = reportCertService.countTT03CapTinhByFilter(level_cert, districtCode, dateFrom, dateTo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/tt03-list-xa-thuoc-tinh", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<ReportTT03CertCapTinhDTO>> counTT03CapTinh(
            @RequestParam(name = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(name = "districtCode", required = false, defaultValue = "") String districtCode,
            @RequestParam(name = "dateFrom", required = false, defaultValue = "") String dateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "") String dateTo) {
        String level_cert = Constants.LEVEL_CERT_XA;
        List<ReportTT03CertCapTinhDTO> result = reportCertService.countTT03CapTinhByFilter(level_cert, districtCode, dateFrom, dateTo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/luong-giao-dich-bds", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<ReportLuongGDBDSBO>> counBCLuongGDBDS(
            @RequestParam(name = "dateFrom", required = false, defaultValue = "") String dateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "") String dateTo,
            @RequestParam(name = "district_code", required = false, defaultValue = "") String district_code,
            @RequestParam(name = "level_cert", required = false, defaultValue = "") String level_cert) {

//        if(SystemProperties.getProperty("org_type").equals(Constants.ORG_TYPE_PHUONG_XA)){
//            if(level_cert.equals(Constants.LEVEL_CERT_TINH)){
//                List<ReportLuongGDBDSBO> result = reportCertService.countLuongGDBDS(dateFrom, dateTo);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else if(level_cert.equals(Constants.LEVEL_CERT_HUYEN)){
//                List<ReportLuongGDBDSBO> result = reportCertService.countLuongGDBDS(dateFrom, dateTo);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            }else if(level_cert.equals(Constants.LEVEL_CERT_XA)){
//                List<ReportLuongGDBDSBO> result = reportCertService.countLuongGDBDS(dateFrom, dateTo);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            }
//
//        }else {
//            List<ReportLuongGDBDSBO> result = reportCertService.countBCThongKeGDBDS(dateFrom,dateTo);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        }
//        return null;
        if(SystemProperties.getProperty("org_type").equals(Constants.ORG_TYPE_PHUONG_XA)){
            List<ReportLuongGDBDSBO> result = reportCertService.countLuongGDBDS(dateFrom, dateTo);
            return new ResponseEntity<>(result, HttpStatus.OK);


        }else {
            List<ReportLuongGDBDSBO> result = reportCertService.countBCThongKeGDBDS(dateFrom,dateTo);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }

}
