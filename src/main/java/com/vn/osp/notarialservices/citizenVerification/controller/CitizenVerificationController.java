package com.vn.osp.notarialservices.citizenVerification.controller;


import com.vn.osp.notarialservices.auth.ApiResponse;
import com.vn.osp.notarialservices.citizenVerification.dto.CitizenVerificationsDTO;
import com.vn.osp.notarialservices.citizenVerification.service.CitizenVerficationService;
import com.vn.osp.notarialservices.common.exception.BadRequestException;
import com.vn.osp.notarialservices.common.util.PagingResult;
import com.vn.osp.notarialservices.systemmanager.service.AccessHistoryService;
import com.vn.osp.notarialservices.utils.OspQueryFactory;
import com.vn.osp.notarialservices.utils.TimeUtil;
import net.sf.jett.transform.ExcelTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping(value = "/citizen-verifications")
public class CitizenVerificationController {
    @Autowired
    CitizenVerficationService citizenVerficationService;
    @Autowired
    AccessHistoryService systemConfigService;


    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<PagingResult> page(
            @RequestParam(name = "page", required = true, defaultValue = "1") int page,
            @RequestParam(name = "numberPerPage", required = true, defaultValue = "20") int numberPerPage,
            @RequestParam(name = "order_id", required = false) String order_id,
            @RequestParam(name = "verify_id", required = false) String verify_id,
            @RequestParam(name = "notary_office_id", required = false) String notary_office_id,
            @RequestParam(name = "verify_status", required = false) String verify_status,
            @RequestParam(name = "verify_by", required = false) String verify_by,
            @RequestParam(name = "citizen_verify_fullname", required = false) String citizen_verify_fullname,
            @RequestParam(name = "citizen_verify_cccd", required = false) String citizen_verify_cccd,
            @RequestParam(name = "verify_date_from", required = false) String verify_date_from,
            @RequestParam(name = "verify_date_to", required = false) String verify_date_to) throws UnsupportedEncodingException {
        PagingResult pageResult = citizenVerficationService.page(page, numberPerPage, verify_id, notary_office_id,
                verify_status, verify_by,citizen_verify_fullname, citizen_verify_cccd,
                verify_date_from, verify_date_to, order_id).orElse(new PagingResult());
        return ResponseEntity.ok(pageResult);
    }

    /*@RequestMapping(value = "/{verify_id}", method = RequestMethod.GET)
    public ResponseEntity<CitizenVerificationsDTO> getDetail(@PathVariable("verify_id") String verify_id){
        CitizenVerificationsDTO result = citizenVerficationService.getDetail(verify_id).orElse(null);
        return ResponseEntity.ok(result);
    }*/

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(@RequestParam(name = "verify_id", required = false) String verify_id,
                       @RequestParam(name = "transaction_id", required = false) String transaction_id,
                       @RequestParam(name = "notary_office_id", required = false) String notary_office_id,
                       @RequestParam(name = "verify_status", required = false) String verify_status,
                       @RequestParam(name = "verify_by", required = false) String verify_by,
                       @RequestParam(name = "citizen_verify_fullname", required = false) String citizen_verify_fullname,
                       @RequestParam(name = "citizen_verify_cccd", required = false) String citizen_verify_cccd,
                       @RequestParam(name = "verify_date_from", required = false) String verify_date_from,
                       @RequestParam(name = "verify_date_to", required = false) String verify_date_to,
                       HttpServletResponse response, HttpServletRequest request) {
        try {
            if(StringUtils.isBlank(notary_office_id)) {
                notary_office_id = systemConfigService.getConfigValue("system_authentication_id").orElse(null);
                if(StringUtils.isBlank(notary_office_id)) {
                    throw new BadRequestException("no found notary_office_code in notary office side", null);
                }
            }
            Map<String, Object> dataExport = OspQueryFactory.getDataExportCitizenVerification(verify_id, notary_office_id,
                    verify_status, verify_by,citizen_verify_fullname, citizen_verify_cccd,
                    verify_date_from, verify_date_to, transaction_id);

            Map<String, Object> beans = new HashMap<String, Object>();
//            beans.put("report", dataExport.containsKey("items") ? dataExport.get("items") : new ArrayList<>());
            beans.put("report", dataExport.containsKey("items") ? dataExport.get("items") : new ArrayList<>());
            beans.put("total", dataExport.getOrDefault("count", 0));
            Date date = new Date(); // your date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            beans.put("year", year);
            beans.put("month", month);
            beans.put("day", day);
            beans.put("verify_date_from", verify_date_from);
            beans.put("verify_date_to", verify_date_to);

            String office_name=systemConfigService.getConfigValue("notary_office_name").orElse(null);
            beans.put("agency",office_name);
            /*Resource ClassPathResource se lay dc thu muc resource trong project, ngay ca khi chay app*/
            Resource resource = new ClassPathResource("file/report/BaoCaoSoLuotXacThucDanhTinh_template.xls");

            InputStream fileIn = resource.getInputStream();

            ExcelTransformer transformer = new ExcelTransformer();
            Workbook workbook = transformer.transform(fileIn, beans);

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + "BaoCaoSoLuotXacThucDanhTinh.xls");
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/save-file-verify",method = RequestMethod.POST)
    public ResponseEntity<Object> saveFileVerify(@RequestHeader(value = "Authorization") String authorization, @RequestBody final CitizenVerificationsDTO citizenInformationDTO, HttpServletRequest request){
        File file = getFileJustAuthen();
        if(file != null) {
            String fileUrlToView = "";
            Map map = OspQueryFactory.sendFileAuthenFaceId(authorization, file, citizenInformationDTO.getVerify_id(), citizenInformationDTO.getNotary_office_id(), citizenInformationDTO.getCitizen_verify_cccd());
            return new ResponseEntity<>(new ApiResponse(true, "upload file authen successfully", fileUrlToView), HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(new ApiResponse(true, "there is no file", null), HttpStatus.UNAUTHORIZED);
        }
    }

    private File getFileJustAuthen() {
        String filePath = systemConfigService.getConfigValue("faceId_file_path").orElse(null);
        String today = TimeUtil.toYearMonthDay(new Date());
        String fullFileUrl = filePath + "/" + today + "/PDF";

        // Create a File object for the folder
        File folder = new File(fullFileUrl);

        if (folder.isDirectory()) {
            // List all files in the folder
            File[] files = folder.listFiles();

            // Sort the files based on last modified timestamp
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

            if (files.length > 0) {
                // Get the newest file
                File newestFile = files[0];
                System.out.println("Newest file: " + newestFile.getName());
                return newestFile;
            } else {
                System.out.println("Folder is empty.");
            }
        } else {
            System.out.println("Invalid folder path.");
        }
        return null;
    }
}
