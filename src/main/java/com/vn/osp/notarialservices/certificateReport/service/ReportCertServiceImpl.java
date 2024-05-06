package com.vn.osp.notarialservices.certificateReport.service;

import com.vn.osp.notarialservices.certificateReport.conveter.ReportTT03CertCapHuyenConverter;
import com.vn.osp.notarialservices.certificateReport.conveter.ReportTT03CertCapTinhConverter;
import com.vn.osp.notarialservices.certificateReport.conveter.ReportTT03CertConverter;
import com.vn.osp.notarialservices.certificateReport.domain.ReportLuongGDBDSBO;
import com.vn.osp.notarialservices.certificateReport.domain.ReportTT03CertBO;
import com.vn.osp.notarialservices.certificateReport.domain.ReportTT03CertCapHuyenBO;
import com.vn.osp.notarialservices.certificateReport.domain.ReportTT03CertCapTinhBO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertCapHuyenDTO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertCapTinhDTO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertDTO;
import com.vn.osp.notarialservices.utils.Constants;
import com.vn.osp.notarialservices.utils.SystemProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReportCertServiceImpl implements ReportCertService {
    private Logger logger = LogManager.getLogger(ReportCertServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    ReportTT03CertConverter reportTT03CertConverter;
    @Autowired
    ReportTT03CertCapHuyenConverter reportTT03CertCapHuyenConverter;
    @Autowired
    ReportTT03CertCapTinhConverter reportTT03CertCapTinhConverter;

    @Override
    public ReportTT03CertDTO countTT03ByFilter(String userId, String dateFrom, String dateTo) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (!StringUtils.isBlank(dateFrom)) {
                dateFrom = format2.format(format1.parse(dateFrom)) + " 00:00:00";
            }
            if (!StringUtils.isBlank(dateTo)) {
                dateTo = format2.format(format1.parse(dateTo)) + " 23:59:59";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ReportTT03CertDTO result = new ReportTT03CertDTO();
        String hql = "select count(1) as cert_contract_number,\n" +
                "(select count(1) from npo_certificate where type = " + Constants.CERTIFICATE_TYPE_COPY + " and cert_date >= '" + dateFrom + "' and cert_date <= '" + dateTo + "' and entry_user_id = " + userId + ") as cert_copies_number,\n" +
                "(select count(1) from npo_certificate where type = " + Constants.CERTIFICATE_TYPE_SIGNATURE + " and cert_date >= '" + dateFrom + "' and cert_date <= '" + dateTo + "'  and entry_user_id = " + userId + ") as cert_signature_number\n" +
                "from npo_transaction_property where notary_date >= '" + dateFrom + "' and notary_date <= '" + dateTo + "' and entry_user_id = " + userId + " ";
        try {
            ReportTT03CertBO reportTT03CertBO = (ReportTT03CertBO) entityManager.createNativeQuery(hql, ReportTT03CertBO.class).getSingleResult();
            result = reportTT03CertConverter.convert(reportTT03CertBO);

        } catch (Exception e) {
            logger.error("Have an error in method ReportTT03CertServiceImpl.countTT03ByFilter: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<ReportTT03CertCapHuyenDTO> countTT03CapHuyenByFilter(String level_cert, String districtCode, String userId, String dateFrom, String dateTo) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (!StringUtils.isBlank(dateFrom)) {
                dateFrom = format2.format(format1.parse(dateFrom)) + " 00:00:00";
            }
            if (!StringUtils.isBlank(dateTo)) {
                dateTo = format2.format(format1.parse(dateTo)) + " 23:59:59";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String hqlQueryByUser = "";
        if (!StringUtils.isBlank(level_cert)) hqlQueryByUser += " and user.level_cert = '" + level_cert + "' ";
        if (!StringUtils.isBlank(districtCode)) hqlQueryByUser += " and user.district_code = '" + districtCode + "' ";
        if (!StringUtils.isBlank(userId)) hqlQueryByUser += " and user.id = " + userId;
        List<ReportTT03CertCapHuyenDTO> result = new ArrayList<>();
        String hql = "select user.id as user_id, user.first_name as user_first_name, user.family_name as user_family_name, user.district_code as user_district_code, user.level_cert as user_level_cert, \n" +
                "(select count(1) from npo_certificate where type = " + Constants.CERTIFICATE_TYPE_COPY + " and cert_date >= '" + dateFrom + "' and cert_date <= '" + dateTo + "' and entry_user_id = user.id) as cert_copies_number,\n" +
                "(select count(1) from npo_certificate where type = " + Constants.CERTIFICATE_TYPE_SIGNATURE + " and cert_date >= '" + dateFrom + "' and cert_date <= '" + dateTo + "' and entry_user_id = user.id) as cert_signature_number,\n" +
                "(select count(1) from npo_transaction_property where notary_date >= '" + dateFrom + "' and notary_date <= '" + dateTo + "' and entry_user_id = user.id) as cert_contract_number\n" +
                "from npo_user user where 1=1 " + hqlQueryByUser;
        try {
            List<ReportTT03CertCapHuyenBO> reportTT03CertCapHuyenBOS = entityManager.createNativeQuery(hql, ReportTT03CertCapHuyenBO.class).getResultList();
            if (reportTT03CertCapHuyenBOS != null && reportTT03CertCapHuyenBOS.size() > 0) {
                for (int i = 0; i < reportTT03CertCapHuyenBOS.size(); i++) {
                    result.add(reportTT03CertCapHuyenConverter.convert(reportTT03CertCapHuyenBOS.get(i)));
                }
            }


        } catch (Exception e) {
            logger.error("Have an error in method ReportTT03CertServiceImpl.countTT03CapHuyenByFilter: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<ReportTT03CertCapTinhDTO> countTT03CapTinhByFilter(String level_cert, String districtCode, String dateFrom, String dateTo) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (!StringUtils.isBlank(dateFrom)) {
                dateFrom = format2.format(format1.parse(dateFrom)) + " 00:00:00";
            }
            if (!StringUtils.isBlank(dateTo)) {
                dateTo = format2.format(format1.parse(dateTo)) + " 23:59:59";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String hqlQuery = "";
        if (!StringUtils.isBlank(districtCode)) hqlQuery += " and district.code = '" + districtCode + "' ";
        List<ReportTT03CertCapTinhDTO> result = new ArrayList<>();
        String hql = "select district.name as district_name, district.code as district_code,\n" +
                " (select count(1) from npo_certificate copies join npo_user sub_user on copies.entry_user_id = sub_user.id where copies.type = " + Constants.CERTIFICATE_TYPE_COPY + " and copies.cert_date >= '" + dateFrom + "' and copies.cert_date <= '" + dateTo + "' and sub_user.district_code = district.code and sub_user.level_cert = '" + level_cert + "') as cert_copies_number,\n" +
                " (select count(1) from npo_certificate sign join npo_user sub_user on sign.entry_user_id = sub_user.id where sign.type = " + Constants.CERTIFICATE_TYPE_SIGNATURE + " and sign.cert_date >= '" + dateFrom + "' and sign.cert_date <= '" + dateTo + "' and sub_user.district_code = district.code and sub_user.level_cert = '" + level_cert + "') as cert_signature_number,\n" +
                " (select count(1) from npo_transaction_property trans join npo_user sub_user on trans.entry_user_id = sub_user.id where trans.notary_date >= '" + dateFrom + "' and trans.notary_date <= '" + dateTo + "' and sub_user.district_code = district.code and sub_user.level_cert = '" + level_cert + "') as cert_contract_number\n" +
                " from npo_district district where 1=1  \n" + hqlQuery +
                " order by district.code asc";
        try {
            List<ReportTT03CertCapTinhBO> reportTT03CertCapTinhBOS = entityManager.createNativeQuery(hql, ReportTT03CertCapTinhBO.class).getResultList();
            if (reportTT03CertCapTinhBOS != null && reportTT03CertCapTinhBOS.size() > 0) {
                for (int i = 0; i < reportTT03CertCapTinhBOS.size(); i++) {
                    result.add(reportTT03CertCapTinhConverter.convert(reportTT03CertCapTinhBOS.get(i)));
                }
            }


        } catch (Exception e) {
            logger.error("Have an error in method ReportTT03CertServiceImpl.countTT03CapTinhByFilter: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<ReportLuongGDBDSBO> countLuongGDBDS(String dateFrom, String dateTo) {
        List<ReportLuongGDBDSBO> result = new ArrayList<>();

        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (!StringUtils.isBlank(dateFrom)) {
                dateFrom = format2.format(format1.parse(dateFrom)) + " 00:00:00";
            }
            if (!StringUtils.isBlank(dateTo)) {
                dateTo = format2.format(format1.parse(dateTo)) + " 23:59:59";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String hqlQuery = "";

        String hql = "select district.name as district_name, district.code as district_code,\n" +
                " (select count(1) from npo_transaction_property trans1 join npo_user sub_user on trans1.entry_user_id = sub_user.id " +
                " where trans1.notary_date >= '" + dateFrom + "' and trans1.notary_date <= '" + dateTo + "' " +
                " and sub_user.district_code = district.code and trans1.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.DNDO+"\"%' and trans1.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.DNDO_phatTrienTheoDuAn+"\"%') as count_DNDO_phatTrienTheoDuAn, \n"+

                " (select count(1) from npo_transaction_property trans2 join npo_user sub_user on trans2.entry_user_id = sub_user.id " +
                " where trans2.notary_date >= '" + dateFrom + "' and trans2.notary_date <= '" + dateTo + "' and sub_user.district_code = district.code " +
                " and trans2.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.DNDO+"\"%' and trans2.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.DNDO_trongKhuDanCu+"\"%') as count_DNDO_trongKhuDanCu, \n"+

                " (select count(1) from npo_transaction_property trans3 join npo_user sub_user on trans3.entry_user_id = sub_user.id " +
                " where trans3.notary_date >= '" + dateFrom + "' and trans3.notary_date <= '" + dateTo + "' and sub_user.district_code = district.code " +
                " and trans3.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.NORL+"\"%' and trans3.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.NORL_phatTrienTheoDuAn+"\"%') as count_NORL_phatTrienTheoDuAn, \n"+

                " (select count(1) from npo_transaction_property trans4 join npo_user sub_user on trans4.entry_user_id = sub_user.id " +
                " where trans4.notary_date >= '" + dateFrom + "' and trans4.notary_date <= '" + dateTo + "' and sub_user.district_code = district.code " +
                " and trans4.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.NORL+"\"%' and trans4.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.NORL_trongKhuDanCu+"\"%') as count_NORL_trongKhuDanCu, \n"+

                " (select count(1) from npo_transaction_property trans5 join npo_user sub_user on trans5.entry_user_id = sub_user.id " +
                " where trans5.notary_date >= '" + dateFrom + "' and trans5.notary_date <= '" + dateTo + "' and sub_user.district_code = district.code " +
                " and trans5.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.CHCC+"\"%' and trans5.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.CHCC_dienTichDuoi70m2+"\"%') as count_CHCC_dienTichDuoi70m2, \n"+

                " (select count(1) from npo_transaction_property trans6 join npo_user sub_user on trans6.entry_user_id = sub_user.id " +
                " where trans6.notary_date >= '" + dateFrom + "' and trans6.notary_date <= '" + dateTo + "' and sub_user.district_code = district.code " +
                " and trans6.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.CHCC+"\"%' and trans6.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.CHCC_dienTichTu70Den120m2+"\"%') as count_CHCC_dienTichTu70Den120m2, \n"+

                " (select count(1) from npo_transaction_property trans7 join npo_user sub_user on trans7.entry_user_id = sub_user.id " +
                " where trans7.notary_date >= '" + dateFrom + "' and trans7.notary_date <= '" + dateTo + "' and sub_user.district_code = district.code " +
                " and trans7.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.CHCC+"\"%' and trans7.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.CHCC_dienTichTren120m2+"\"%') as count_CHCC_dienTichTren120m2, \n"+

                " (select count(1) from npo_transaction_property trans8 join npo_user sub_user on trans8.entry_user_id = sub_user.id " +
                " where trans8.notary_date >= '" + dateFrom + "' and trans8.notary_date <= '" + dateTo + "' and sub_user.district_code = district.code " +
                " and trans8.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.VPCT+"\"%') as count_vanPhongChoThue, \n"+

                " (select count(1) from npo_transaction_property trans9 join npo_user sub_user on trans9.entry_user_id = sub_user.id " +
                " where trans9.notary_date >= '" + dateFrom + "' and trans9.notary_date <= '" + dateTo + "' and sub_user.district_code = district.code " +
                " and trans9.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.MBTMDV+"\"%') as count_matBangThuongMaiDichVu \n"+

                " from npo_district district where 1=1  \n" + hqlQuery +
                " order by district.code asc";
        try {
            List<Object[]> listObj = entityManager.createNativeQuery(hql).getResultList();
            listObj.stream().forEach((record) -> {
                ReportLuongGDBDSBO bo = new ReportLuongGDBDSBO();
                bo.setDistrict_name(record[0]!=null?(String) record[0]:"");
                bo.setDistrict_code(record[1]!=null?(String) record[1]:"");
                bo.setCount_DNDO_phatTrienTheoDuAn(record[2]!=null?(BigInteger) record[2]:BigInteger.valueOf(0));
                bo.setCount_DNDO_trongKhuDanCu(record[3]!=null?(BigInteger) record[3]:BigInteger.valueOf(0));
                bo.setCount_NORL_phatTrienTheoDuAn(record[4]!=null?(BigInteger) record[4]:BigInteger.valueOf(0));
                bo.setCount_NORL_trongKhuDanCu(record[5]!=null?(BigInteger) record[5]:BigInteger.valueOf(0));
                bo.setCount_CHCC_dienTichDuoi70m2(record[6]!=null?(BigInteger) record[6]:BigInteger.valueOf(0));
                bo.setCount_CHCC_dienTichTu70Den120m2(record[7]!=null?(BigInteger) record[7]:BigInteger.valueOf(0));
                bo.setCount_CHCC_dienTichTren120m2(record[8]!=null?(BigInteger) record[8]:BigInteger.valueOf(0));
                bo.setCount_vanPhongChoThue(record[9]!=null?(BigInteger) record[9]:BigInteger.valueOf(0));
                bo.setCount_matBangThuongMaiDichVu(record[10]!=null?(BigInteger) record[10]:BigInteger.valueOf(0));
                result.add(bo);
            });

        } catch (Exception e) {
            logger.error("Have an error in method ReportTT03CertServiceImpl.countLuongGDBDS: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<ReportLuongGDBDSBO> countBCThongKeGDBDS(String dateFrom,String dateTo){
        List<ReportLuongGDBDSBO> result = new ArrayList<>();

        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (!StringUtils.isBlank(dateFrom)) {
                dateFrom = format2.format(format1.parse(dateFrom)) + " 00:00:00";
            }
            if (!StringUtils.isBlank(dateTo)) {
                dateTo = format2.format(format1.parse(dateTo)) + " 23:59:59";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String hsql = "";
        String query = "select config_value, \n" +
                " (select count(1) from npo_transaction_property trans1 join npo_user sub_user on trans1.entry_user_id = sub_user.id " +
                " where trans1.notary_date >= '" + dateFrom + "' and trans1.notary_date <= '" + dateTo + "' " +
                " and trans1.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.DNDO+"\"%' and trans1.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.DNDO_phatTrienTheoDuAn+"\"%') as count_DNDO_phatTrienTheoDuAn, \n"+

                " (select count(1) from npo_transaction_property trans2 join npo_user sub_user on trans2.entry_user_id = sub_user.id " +
                " where trans2.notary_date >= '" + dateFrom + "' and trans2.notary_date <= '" + dateTo + "'" +
                " and trans2.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.DNDO+"\"%' and trans2.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.DNDO_trongKhuDanCu+"\"%') as count_DNDO_trongKhuDanCu, \n"+

                " (select count(1) from npo_transaction_property trans3 join npo_user sub_user on trans3.entry_user_id = sub_user.id " +
                " where trans3.notary_date >= '" + dateFrom + "' and trans3.notary_date <= '" + dateTo + "'" +
                " and trans3.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.NORL+"\"%' and trans3.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.NORL_phatTrienTheoDuAn+"\"%') as count_NORL_phatTrienTheoDuAn, \n"+

                " (select count(1) from npo_transaction_property trans4 join npo_user sub_user on trans4.entry_user_id = sub_user.id " +
                " where trans4.notary_date >= '" + dateFrom + "' and trans4.notary_date <= '" + dateTo + "'" +
                " and trans4.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.NORL+"\"%' and trans4.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.NORL_trongKhuDanCu+"\"%') as count_NORL_trongKhuDanCu, \n"+

                " (select count(1) from npo_transaction_property trans5 join npo_user sub_user on trans5.entry_user_id = sub_user.id " +
                " where trans5.notary_date >= '" + dateFrom + "' and trans5.notary_date <= '" + dateTo + "'" +
                " and trans5.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.CHCC+"\"%' and trans5.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.CHCC_dienTichDuoi70m2+"\"%') as count_CHCC_dienTichDuoi70m2, \n"+

                " (select count(1) from npo_transaction_property trans6 join npo_user sub_user on trans6.entry_user_id = sub_user.id " +
                " where trans6.notary_date >= '" + dateFrom + "' and trans6.notary_date <= '" + dateTo + "'" +
                " and trans6.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.CHCC+"\"%' and trans6.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.CHCC_dienTichTu70Den120m2+"\"%') as count_CHCC_dienTichTu70Den120m2, \n"+

                " (select count(1) from npo_transaction_property trans7 join npo_user sub_user on trans7.entry_user_id = sub_user.id " +
                " where trans7.notary_date >= '" + dateFrom + "' and trans7.notary_date <= '" + dateTo + "'" +
                " and trans7.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.CHCC+"\"%' and trans7.json_property like '%\"type_real_estate_sub\":\""+Constants.TYPE_PROPERTY_LAND.CHCC_dienTichTren120m2+"\"%') as count_CHCC_dienTichTren120m2, \n"+

                " (select count(1) from npo_transaction_property trans8 join npo_user sub_user on trans8.entry_user_id = sub_user.id " +
                " where trans8.notary_date >= '" + dateFrom + "' and trans8.notary_date <= '" + dateTo + "'" +
                " and trans8.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.VPCT+"\"%') as count_vanPhongChoThue, \n"+

                " (select count(1) from npo_transaction_property trans9 join npo_user sub_user on trans9.entry_user_id = sub_user.id " +
                " where trans9.notary_date >= '" + dateFrom + "' and trans9.notary_date <= '" + dateTo + "'" +
                " and trans9.json_property like '%\"type_real_estate\":\""+Constants.TYPE_PROPERTY_LAND.MBTMDV+"\"%') as count_matBangThuongMaiDichVu \n"+

                " from npo_system_config WHERE config_key = 'notary_office_name'";
        try {
            List<Object[]> listObj = entityManager.createNativeQuery(query).getResultList();
            listObj.stream().forEach((record) -> {
                ReportLuongGDBDSBO bo = new ReportLuongGDBDSBO();
                bo.setDistrict_name(record[0]!=null?(String) record[0]:"");
                bo.setDistrict_code(null);
                bo.setCount_DNDO_phatTrienTheoDuAn(record[1]!=null?(BigInteger) record[1]:BigInteger.valueOf(0));
                bo.setCount_DNDO_trongKhuDanCu(record[2]!=null?(BigInteger) record[2]:BigInteger.valueOf(0));
                bo.setCount_NORL_phatTrienTheoDuAn(record[3]!=null?(BigInteger) record[3]:BigInteger.valueOf(0));
                bo.setCount_NORL_trongKhuDanCu(record[4]!=null?(BigInteger) record[4]:BigInteger.valueOf(0));
                bo.setCount_CHCC_dienTichDuoi70m2(record[5]!=null?(BigInteger) record[5]:BigInteger.valueOf(0));
                bo.setCount_CHCC_dienTichTu70Den120m2(record[6]!=null?(BigInteger) record[6]:BigInteger.valueOf(0));
                bo.setCount_CHCC_dienTichTren120m2(record[7]!=null?(BigInteger) record[7]:BigInteger.valueOf(0));
                bo.setCount_vanPhongChoThue(record[8]!=null?(BigInteger) record[8]:BigInteger.valueOf(0));
                bo.setCount_matBangThuongMaiDichVu(record[9]!=null?(BigInteger) record[9]:BigInteger.valueOf(0));
                result.add(bo);
            });

        } catch (Exception e) {
            logger.error("Have an error in method ReportTT03CertServiceImpl.countBCThongKeGDBDS: " + e.getMessage());
        }
        return result;

    }
}
