package com.vn.osp.notarialservices.citizenVerificationOrder.controller;

import com.vn.osp.notarialservices.citizenVerificationOrder.dto.CitizenVerifyOrderDTO;
import com.vn.osp.notarialservices.citizenVerificationOrder.service.CitizenVerifyOrderService;
import com.vn.osp.notarialservices.common.exception.BadRequestException;
import com.vn.osp.notarialservices.common.util.PagingResult;
import com.vn.osp.notarialservices.systemmanager.service.AccessHistoryService;
import com.vn.osp.notarialservices.utils.OspQueryFactory;
import net.sf.jett.transform.ExcelTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/citizen-verify-orders")
public class CitizenVerifyOrderController {

    @Autowired
    private CitizenVerifyOrderService citizenVerifyOrderService;
    @Autowired
    private AccessHistoryService systemConfigService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CitizenVerifyOrderDTO> add(@RequestBody final CitizenVerifyOrderDTO citizenVerifyOrderDTO){
        CitizenVerifyOrderDTO result = citizenVerifyOrderService.insert(citizenVerifyOrderDTO).orElse(null);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<CitizenVerifyOrderDTO> update(@RequestBody final CitizenVerifyOrderDTO citizenVerifyOrderDTO){
        CitizenVerifyOrderDTO result = citizenVerifyOrderService.update(citizenVerifyOrderDTO).orElse(null);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<PagingResult> page(
            @RequestParam(name = "page", required = true, defaultValue = "1") int page,
            @RequestParam(name = "numberPerPage", required = true, defaultValue = "20") int numberPerPage,
            @RequestParam(name = "order_id", required = false) String order_id,
            @RequestParam(name = "transaction_status", required = false) String transaction_status,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "update_by", required = false) String update_by,
            @RequestParam(name = "order_time_from", required = false) String order_time_from,
            @RequestParam(name = "order_time_to", required = false) String order_time_to)
    {
        PagingResult pageResult = citizenVerifyOrderService.page(page, numberPerPage, order_id, transaction_status, status,
                update_by, order_time_from, order_time_to).orElse(new PagingResult());
        return ResponseEntity.ok(pageResult);
    }

    @RequestMapping(value = "/{order_id}", method = RequestMethod.GET)
    public ResponseEntity<CitizenVerifyOrderDTO> getDetail(@PathVariable String order_id){
        CitizenVerifyOrderDTO result = citizenVerifyOrderService.getDetail(order_id).orElse(null);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportForNotaryOffice(@RequestParam(name = "order_id", required = false) String order_id,
                                      @RequestParam(name = "transaction_status", required = false) String transaction_status,
                                      @RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "update_by", required = false) String update_by,
                                      @RequestParam(name = "order_time_from", required = false) String order_time_from,
                                      @RequestParam(name = "order_time_to", required = false) String order_time_to,
                                      HttpServletResponse response, HttpServletRequest request) {
        try {
            String notary_office_code = systemConfigService.getConfigValue("system_authentication_id").orElse(null);
            if(StringUtils.isBlank(notary_office_code)) {
                throw new BadRequestException("no found notary_office_code in notary office side", null);
            }
            Map<String, Object> dataExport = OspQueryFactory.getDataExportCitizenVerifyOrder(notary_office_code, order_id, transaction_status, status,
                    update_by, order_time_from, order_time_to);

            Map<String, Object> beans = new HashMap<String, Object>();
//            beans.put("report", dataExport.containsKey("items") ? dataExport.get("items") : new ArrayList<>());
            beans.put("report", dataExport.containsKey("items") ? dataExport.get("items") : new ArrayList<>());
            beans.put("total", dataExport.getOrDefault("count", 0));
            beans.put("totalData", dataExport.get("totalData"));
            Date date = new Date(); // your date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            beans.put("year", year);
            beans.put("month", month);
            beans.put("day", day);
            beans.put("order_time_from", order_time_from);
            beans.put("order_time_to", order_time_to);

            String office_name=systemConfigService.getConfigValue("notary_office_name").orElse(null);
            beans.put("agency", office_name);
            /*Resource ClassPathResource se lay dc thu muc resource trong project, ngay ca khi chay app*/
            Resource resource = new ClassPathResource("file/report/BaoCaoGiaoDichPhiXacThucDanhTinh_TCCC.xls");

            InputStream fileIn = resource.getInputStream();

            ExcelTransformer transformer = new ExcelTransformer();
            Workbook workbook = transformer.transform(fileIn, beans);

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + "BaoCaoGiaoDichPhiXacThucDanhTinh.xls");
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
