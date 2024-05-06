package com.vn.osp.notarialservices.utils;

import com.vn.osp.notarialservices.citizenVerificationOrder.dto.CitizenVerifyOrderDTO;
import com.vn.osp.notarialservices.common.util.PagingResult;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class OspQueryFactory {

    /**Citizen Verify Order*/
    public static PagingResult getPageCitizenVerifyOrder(String notary_office_code, int page, String order_id
            , String transaction_status, String status, String update_by, String order_time_from
            , String order_time_to) {
        StringBuilder urlParams = new StringBuilder();
        urlParams.append("?page=").append(page);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(notary_office_code)) urlParams.append("&notary_office_code=").append(notary_office_code);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(order_id)) urlParams.append("&order_id=").append(order_id);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(transaction_status)) urlParams.append("&transaction_status=").append(transaction_status);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(status)) urlParams.append("&status=").append(status);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(update_by)) urlParams.append("&update_by=").append(update_by);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(order_time_from)) urlParams.append("&order_time_from=").append(order_time_from);
        if(StringUtils.isNoneBlank(order_time_to)) urlParams.append("&order_time_to=").append(order_time_to);

        String url = Constants.OSP_API_LINK + "/citizen-verify-orders/page"+urlParams.toString();
        PagingResult pagingResult = new PagingResult();
        try{
            pagingResult = APIUtil.getReturnObject(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pagingResult;
    }

    public static void exportCitizenVerifyOrder(String notary_office_code, String order_id, String transaction_status, String status,
                                                String update_by, String order_time_from, String order_time_to){
        StringBuilder urlParams = new StringBuilder();
        if(StringUtils.isNoneBlank(notary_office_code)) urlParams.append("&notary_office_code=").append(notary_office_code);
        if(StringUtils.isNoneBlank(order_id)) urlParams.append("&order_id=").append(order_id);
        if(StringUtils.isNoneBlank(transaction_status)) urlParams.append("&transaction_status=").append(transaction_status);
        if(StringUtils.isNoneBlank(status)) urlParams.append("&status=").append(status);
        if(StringUtils.isNoneBlank(update_by)) urlParams.append("&update_by=").append(update_by);
        if(StringUtils.isNoneBlank(order_time_from)) urlParams.append("&order_time_from=").append(order_time_from);
        if(StringUtils.isNoneBlank(order_time_to)) urlParams.append("&order_time_to=").append(order_time_to);
        String url = Constants.OSP_API_LINK + "/citizen-verify-orders/export-for-notary-office?1=1"+urlParams.toString();
        APIUtil.callApiGet(url);
    }

    public static Map getDataExportCitizenVerifyOrder(String notary_office_code, String order_id, String transaction_status, String status,
                                               String update_by, String order_time_from, String order_time_to){
        StringBuilder urlParams = new StringBuilder();
        if(StringUtils.isNoneBlank(notary_office_code)) urlParams.append("&notary_office_code=").append(notary_office_code);
        if(StringUtils.isNoneBlank(order_id)) urlParams.append("&order_id=").append(order_id);
        if(StringUtils.isNoneBlank(transaction_status)) urlParams.append("&transaction_status=").append(transaction_status);
        if(StringUtils.isNoneBlank(status)) urlParams.append("&status=").append(status);
        if(StringUtils.isNoneBlank(update_by)) urlParams.append("&update_by=").append(update_by);
        if(StringUtils.isNoneBlank(order_time_from)) urlParams.append("&order_time_from=").append(order_time_from);
        if(StringUtils.isNoneBlank(order_time_to)) urlParams.append("&order_time_to=").append(order_time_to);
        String url = Constants.OSP_API_LINK + "/citizen-verify-orders/export-data-for-notary-office-side?1=1"+urlParams.toString();
        return APIUtil.getReturnMap(url);
    }

    /**END Citizen Verify Order*/

    /**Citizen Verifìcation*/
    public static PagingResult getPageCitizenVerification(int page,
                                                          int numberPerPage,
                                                          String verify_id,
                                                          String notary_office_id,
                                                          String verify_status,
                                                          String verify_by,
                                                          String citizen_verify_fullname,
                                                          String citizen_verify_cccd,
                                                          String verify_date_from,
                                                          String verify_date_to,
                                                          String order_id) throws UnsupportedEncodingException {
        StringBuilder urlParams = new StringBuilder();
        urlParams.append("?page=").append(page);
        urlParams.append("&numberPerPage=").append(numberPerPage);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(verify_id)) urlParams.append("&verify_id=").append(verify_id);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(notary_office_id)) urlParams.append("&notary_office_id=").append(notary_office_id);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(verify_status)) urlParams.append("&verify_status=").append(verify_status);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(verify_by)) urlParams.append("&verify_by=").append(verify_by);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(citizen_verify_fullname)) urlParams.append("&citizen_verify_fullname=").append(URLEncoder.encode(citizen_verify_fullname, "UTF-8"));
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(citizen_verify_cccd)) urlParams.append("&citizen_verify_cccd=").append(citizen_verify_cccd);
        if(StringUtils.isNoneBlank(verify_date_from)) urlParams.append("&verify_date_from=").append(verify_date_from);
        if(StringUtils.isNoneBlank(verify_date_to)) urlParams.append("&verify_date_to=").append(verify_date_to);
        if(StringUtils.isNoneBlank(order_id)) urlParams.append("&order_id=").append(order_id);

        String url = Constants.OSP_API_LINK + "/citizen-verifications/page"+urlParams.toString();
        PagingResult pagingResult = new PagingResult();
        try{
            pagingResult = APIUtil.getReturnObject(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pagingResult;
    }

    public static Map getDataExportCitizenVerification(String verify_id,
                                                       String notary_office_id,
                                                       String verify_status,
                                                       String verify_by,
                                                       String citizen_verify_fullname,
                                                       String citizen_verify_cccd,
                                                       String verify_date_from,
                                                       String verify_date_to,
                                                       String order_id){
        StringBuilder urlParams = new StringBuilder();
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(verify_id)) urlParams.append("&verify_id=").append(verify_id);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(notary_office_id)) urlParams.append("&notary_office_id=").append(notary_office_id);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(verify_status)) urlParams.append("&verify_status=").append(verify_status);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(verify_by)) urlParams.append("&verify_by=").append(verify_by);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(citizen_verify_fullname)) urlParams.append("&citizen_verify_fullname=").append(citizen_verify_fullname);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(citizen_verify_cccd)) urlParams.append("&citizen_verify_cccd=").append(citizen_verify_cccd);
        if(StringUtils.isNoneBlank(verify_date_from)) urlParams.append("&verify_date_from=").append(verify_date_from);
        if(StringUtils.isNoneBlank(verify_date_to)) urlParams.append("&verify_date_to=").append(verify_date_to);
        if(StringUtils.isNoneBlank(order_id)) urlParams.append("&order_id=").append(order_id);

        String url = Constants.OSP_API_LINK + "/citizen-verifications/export-data-for-notary-office-side?1=1"+urlParams.toString();
        return APIUtil.getReturnMap(url);
    }

    /**END Citizen Verifìcation*/
}
