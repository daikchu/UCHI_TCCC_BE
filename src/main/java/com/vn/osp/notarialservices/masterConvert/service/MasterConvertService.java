package com.vn.osp.notarialservices.masterConvert.service;

import com.vn.osp.notarialservices.manual.dto.Manual;
import com.vn.osp.notarialservices.masterConvert.domain.MasterContractBO;
import com.vn.osp.notarialservices.masterConvert.domain.ReperToire;
import com.vn.osp.notarialservices.masterConvert.dto.ConfigDBMaster;
import com.vn.osp.notarialservices.masterConvert.dto.MasterContract;
import com.vn.osp.notarialservices.systemmanager.domain.SystemConfigBO;
import com.vn.osp.notarialservices.transaction.domain.TransactionPropertyBo;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface MasterConvertService {
    ConfigDBMaster getConfigDBMaster();
    Boolean updateConfigDBMaster(ConfigDBMaster configDBMaster);
    Boolean connectTestConfigDBMaster(ConfigDBMaster configDBMaster);
    List<ReperToire> selectReperToire(String stringFilter);             //hàm lấy dữ liệu bảng ReperToire DB master
    List<MasterContract> selectContractMasterConvert(String stringFilter, int offset, int number);
    List<MasterContractBO> selectContractMasterConvert(String stringFilter);
    Long countContractMasterConvert(String stringFilter);
    String getStringFilterFromSearch(String search, String type);
    Boolean saveOrUpdateMasterContractBO(List<MasterContractBO> masterContractBO);
    Boolean editContractMasterConvert(List<MasterContractBO> masterContractBO);
    void deleteWhereContractNumberMasterNew(String fromYear, String toYaer);
    List<MasterContract> selectContractMasterConvertById(Long id);
    Boolean deleteContractMasterConvertById(String listIdSelect);
    Long countMaxContractMasterConvert(String stringFilter);
    Long countMinContractMasterConvert(String stringFilter);
    Long countTotal(int page, int fromYear, int toYear);
    Long countTotal(String stringFilter);
    Long countTotalDBSQL2000(String stringFilter);
    List<MasterContractBO> searchInforLimit(int page, int fromYear, int toYear);
    Boolean saveOrUpdateContractNumberNew(List<MasterContractBO> masterContractBO);
    Boolean updateContractNumberNew(int fromYear, int toYear);
    Boolean setTypeDuplicateContent(int type, List<MasterContractBO> list);
    Boolean addTransactionProperty(List<TransactionPropertyBo> add, List<MasterContractBO> update);
    TransactionPropertyBo genTransactionPropertyBo(MasterContractBO bo, String entry_user_id, String entry_user_name);
    SystemConfigBO getSystemConfigBO(String config_key);
}
