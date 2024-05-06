package com.vn.osp.notarialservices.certificateReport.service;

import com.vn.osp.notarialservices.certificateReport.domain.ReportLuongGDBDSBO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertCapHuyenDTO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertCapTinhDTO;
import com.vn.osp.notarialservices.certificateReport.dto.ReportTT03CertDTO;

import java.util.List;

public interface ReportCertService {
    ReportTT03CertDTO countTT03ByFilter(String userId, String dateFrom, String dateTo);
    List<ReportTT03CertCapHuyenDTO> countTT03CapHuyenByFilter(String level_cert, String districtCode, String userId, String dateFrom, String dateTo);
    List<ReportTT03CertCapTinhDTO> countTT03CapTinhByFilter(String level_cert, String districtCode, String dateFrom, String dateTo);
    List<ReportLuongGDBDSBO> countLuongGDBDS(String dateFrom, String dateTo);
    List<ReportLuongGDBDSBO> countBCThongKeGDBDS(String dateFrom,String dateTo);
}
