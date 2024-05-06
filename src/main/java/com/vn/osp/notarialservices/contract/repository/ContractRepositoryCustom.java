package com.vn.osp.notarialservices.contract.repository;

import com.vn.osp.notarialservices.contract.domain.ContractBO;
import com.vn.osp.notarialservices.contract.domain.ContractHistoryInfoBO;
import com.vn.osp.notarialservices.contract.domain.ContractKindBO;
import com.vn.osp.notarialservices.contract.domain.ContractTemplateBO;
import com.vn.osp.notarialservices.contract.dto.ReportByNotaryPerson;
import com.vn.osp.notarialservices.contract.dto.ReportByUser;
import com.vn.osp.notarialservices.contract.dto.*;
import com.vn.osp.notarialservices.masterConvert.domain.PropertyBo;
import com.vn.osp.notarialservices.transaction.domain.TransactionPropertyBo;
import com.vn.osp.notarialservices.transaction.dto.TransactionProperty;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Created by TienManh on 5/11/2017.
 */
public interface ContractRepositoryCustom {
    Optional<List<ContractBO>> findLatestContract(Long countNumber);
    Optional<List<ContractBO>> getAllContract();
    Optional<Integer> addContractWrapper(String xml_contract, String xml_property);

    Optional<List<ContractKindBO>> selectContractKindByFilter(String stringFilter);
    Optional<BigInteger> countByContractNumber(String contract_number);
    Optional<Long> countContractNumber(String contractNumber);

    Optional<List<ContractTemplateBO>> selectContractTeamplateByFilter(String stringFilter);
    Optional<BigInteger> countHistoryContract(String stringFilter) ;
    List<ContractHistory> selectByFilter(String stringFilter);

    List<ReportByNotaryPerson> getReportByNotary(String stringFilter);
    List<ReportByNotaryPerson> getReportByDraft(String stringFilter);

    Optional<BigInteger> countTotalReportByNotary(String stringFilter);
    Optional<BigInteger> countTotalReportByDraft(String stringFilter);

    List<ReportByUser> getReportByUser(String stringFilter);

    Optional<BigInteger> countTotalReportByUser(String stringFilter);

//    Optional<Integer> addContract(String xml_contract,String xml_transaction_property,String xml_contract_history);
//    Optional<Integer> editContract(String xml_contract,String xml_transaction_property,String xml_contract_history);
//    Optional<Integer> cancelContract(String contract_cancel_id,String xml_contract,String xml_transaction_property,String xml_contract_history);
    Optional<Integer> addContract(ContractBO contract, TransactionPropertyBo trans, ContractHistoryInfoBO his);
    Optional<Integer> editContract(ContractBO contract, TransactionPropertyBo trans, ContractHistoryInfoBO his);
    Optional<Integer> cancelContract(long contract_cancel_id , ContractBO contract, TransactionPropertyBo trans, ContractHistoryInfoBO his);

    Boolean deleteContract(String id, String xml_contract_history);

    List<ReportByGroupTotal> selectReportByGroupTotal(ReportByGroupTotalList reportByGroupTotalList, String filter);

    BigInteger numberOfNotaryPerson(ReportByTT20List reportByTT20List);
    BigInteger numberOfContractLand(ReportByTT20List reportByTT20List);
    BigInteger numberOfContractOther(ReportByTT20List reportByTT20List);
    BigInteger numberOfContractDanSu(ReportByTT20List reportByTT20List);
    BigInteger numberOfThuaKe(ReportByTT20List reportByTT20List);
    BigInteger numberOfOther(ReportByTT20List reportByTT20List);
    BigDecimal tongPhiCongChung(ReportByTT20List reportByTT20List);

    BigInteger numberOfNotaryPerson03(ReportByTT03List reportByTT03List);
    BigDecimal numThuLaoCongChungTT03(ReportByTT03List reportByTT03List);
    /*BigInteger numberOfNotaryPersonHopDanh03(ReportByTT03List reportByTT03List);*/
    BigInteger numberOfContractLand03(ReportByTT03List reportByTT03List);
    BigInteger numberOfContractOther03(ReportByTT03List reportByTT03List);
    BigInteger numberOfContractWorkOther03(ReportByTT03List reportByTT03List);
    BigInteger numberOfContractDanSu03(ReportByTT03List reportByTT03List);
    BigInteger numberOfThuaKe03(ReportByTT03List reportByTT03List);
    BigInteger numberOfOther03(ReportByTT03List reportByTT03List);
    BigDecimal tongPhiCongChung03(ReportByTT03List reportByTT03List);
    BigInteger numberOfTotalContract03(ReportByTT03List reportByTT03List);

    BigInteger numberOfNotaryPerson04(ReportByTT04List reportByTT20List);
    BigDecimal numThuLaoCongChung(ReportByTT04List reportByTT20List);
    BigInteger numberOfNotaryPersonHopDanh04(ReportByTT04List reportByTT20List);
    BigInteger numberOfContractLand04(ReportByTT04List reportByTT20List);
    BigInteger numberOfContractOther04(ReportByTT04List reportByTT20List);
    BigInteger numberOfContractWorkOther04(ReportByTT04List reportByTT20List);
    BigInteger numberOfContractDanSu04(ReportByTT04List reportByTT20List);
    BigInteger numberOfThuaKe04(ReportByTT04List reportByTT20List);
    BigInteger numberOfOther04(ReportByTT04List reportByTT20List);
    BigDecimal tongPhiCongChung04(ReportByTT04List reportByTT20List);
    BigInteger numberOfTotalContract04(ReportByTT04List reportByTT04List);


    List<ContractError> getReportContractError(String stringFilter);

    Optional<ContractBO> getContractById(String id);
    Optional<ContractTemplateBO> getContractTemplateById(String id);
    Optional<ContractTemplateBO> getContractTemplateByCodeTemp(String code_temp);
    List<ContractTemplate> getContractTemplateByParentCodeTemplate(String kind_id);
    Optional<ContractKindBO> getContractKindByContractTempId(String id);
    Optional<ContractKindBO> getContractKindByContractTempCode(int code);

    List<ContractAdditional> getReportContractAdditional(String stringFilter);

    Optional<List<ContractKindBO>> listContractKind();
    Optional<List<ContractTemplateBO>> listContractTemplateByContractKindId(String id);
    Optional<List<ContractTemplateBO>> listContractTemplateByContractKindCode(String code);
    Optional<List<ContractTemplateBO>> listContractTemplateSameKind(int code);
    Optional<List<ContractTemplateBO>> listContractTemplate();
    List<ContractCertify> getReportContractCertify(String stringFilter);

    List<ContractStastics> getContractStasticsDrafter(String notaryDateFromFilter, String notaryDateToFilter);
    List<ContractStastics> getContractStasticsNotary(String notaryDateFromFilter, String notaryDateToFilter);
    List<ContractStasticsOfWards> getContractStasticsNotaryOfWards(String district_code, String notaryDateFromFilter, String notaryDateToFilter);
    List<ContractStasticsBank> getContractStasticsBank(String notaryDateFromFilter, String notaryDateToFilter);

    ContractBO genInfo(ContractBO item, ContractBO itemResult);

    BigInteger countAll();
    Optional<BigInteger> checkUserExistContract (int userId);
    Long addContractFromBackUpTransaction(ContractBO contractBO, PropertyBo propertyBo);

    List<SalesByNotarys> getSalesByNotary(String fromDate, String toDate);
    List<SalesByDrafts> getSalesByDraft(String fromDate, String toDate);

    List<SalesByKindContract> getSalesByKindContract( String fromDate,String toDate);

    PropertyBo getPropertyByContractId(Long contract_id);
}
