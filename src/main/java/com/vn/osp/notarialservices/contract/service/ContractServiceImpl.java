package com.vn.osp.notarialservices.contract.service;

import com.vn.osp.notarialservices.auth.JwtRequest;
import com.vn.osp.notarialservices.contract.domain.ContractBO;
import com.vn.osp.notarialservices.contract.dto.*;
import com.vn.osp.notarialservices.contract.repository.ContractRepository;
import com.vn.osp.notarialservices.district.dto.District;
import com.vn.osp.notarialservices.district.service.DistrictService;
import com.vn.osp.notarialservices.masterConvert.domain.ContractPropertyBO;
import com.vn.osp.notarialservices.masterConvert.domain.PropertyBo;
import com.vn.osp.notarialservices.systemmanager.service.AccessHistoryService;
import com.vn.osp.notarialservices.transaction.dto.*;
import com.vn.osp.notarialservices.transaction.service.TransactionPropertyConverter;
import com.vn.osp.notarialservices.transaction.service.TransactionPropertyService;
import com.vn.osp.notarialservices.user.dto.User;
import com.vn.osp.notarialservices.user.service.UserService;
import com.vn.osp.notarialservices.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Created by TienManh on 5/11/2017.
 */
@Component
public class ContractServiceImpl implements ContractService {
    private static final Logger LOGGER = Logger.getLogger(ContractServiceImpl.class);
    private ContractConverter contractConverter;
    private ContractRepository contractRepository;
    private final ContractKindConverter contractKindConverter;
    private final ContractTemplateConverter contractTemplateConverter;
    private final TransactionPropertyService transactionPropertyService;
    private final AccessHistoryService accessHistoryService;
    private final UserService userService;
    private final ContractKindService contractKindService;
    private final TransactionPropertyConverter transactionPropertyConverter;
    private final ContractHistoryConverter contractHistoryConverter;

    @Autowired DistrictService districtService;

    @Autowired
    public ContractServiceImpl(final ContractConverter contractConverter,
                               final ContractRepository contractRepository,
                               final ContractKindConverter contractKindConverter,
                               final ContractTemplateConverter contractTemplateConverter,
                               final TransactionPropertyService transactionPropertyService,
                               final AccessHistoryService accessHistoryService,UserService userService,
                               final ContractKindService contractKindService,final TransactionPropertyConverter transactionPropertyConverter,
                               final ContractHistoryConverter contractHistoryConverter){

        this.contractConverter=contractConverter;
        this.contractRepository=contractRepository;
        this.contractKindConverter = contractKindConverter;
        this.contractTemplateConverter = contractTemplateConverter;
        this.transactionPropertyService = transactionPropertyService;
        this.accessHistoryService=accessHistoryService;
        this.userService=userService;
        this.contractKindService=contractKindService;
        this.transactionPropertyConverter=transactionPropertyConverter;
        this.contractHistoryConverter=contractHistoryConverter;
    }

    @Override
    public Optional<List<Contract>> getAllContract(){
        List<ContractBO> listBO= contractRepository.getAllContract().orElse(new ArrayList<>());
        ArrayList<Contract> lst=new ArrayList<>();
        if(listBO!=null && listBO.size()>0){
            for(int i=0;i<listBO.size();i++)
                lst.add(Optional.ofNullable(listBO.get(i)).map(contractConverter::convert).orElse(new Contract()));
        }

        return Optional.of(lst);
    }

    @Override
    public Optional<List<Contract>> findLatestContract(Long countNumber) {
        List<ContractBO> listBO12 = contractRepository.findLatestContract(countNumber).orElse(new ArrayList<>());
        ArrayList<Contract> list23 = new ArrayList<>();
        if (listBO12 != null && listBO12.size() > 0) {
            for (int i = 0; i < listBO12.size(); i++) {
                list23.add(Optional.ofNullable(listBO12.get(i)).map(contractConverter::convert).orElse(new Contract()));
            }
        }
        return Optional.of(list23);
    }
    @Override
    public Optional<Integer> addContractWrapper(String xml_contract, String xml_property){
        return contractRepository.addContractWrapper(xml_contract,xml_property);
    }

    @Override
    public Optional<BigInteger> countByContractNumber(String contract_number) {
        return contractRepository.countByContractNumber(contract_number);
    }

    @Override
    public Optional<Long> countContractNumber(String contractNumber) {
        return contractRepository.countContractNumber(contractNumber);
    }

    @Override
    public Optional<List<ContractKind>> selectContractKindByFilter(String stringFilter) {
        return contractRepository.selectContractKindByFilter(stringFilter)
                .map(contractKindConverter::converts);
    }

    @Override
    public Optional<List<ContractTemplate>> selectContractTeamplateByFilter(String stringFilter) {
        return contractRepository.selectContractTeamplateByFilter(stringFilter)
                .map(contractTemplateConverter::converts);
    }
    @Override
    public Optional<BigInteger> countHistoryContract(String stringFilter) {
        return contractRepository.countHistoryContract(stringFilter);

    }
    @Override
    public List<ContractHistory> selectByFilter(String stringFilter) {
        return contractRepository.selectByFilter(stringFilter);
    }
    @Override
    public List<ReportByNotaryPerson> getReportByNotary(String stringFilter){
        return contractRepository.getReportByNotary(stringFilter);
    }
    @Override
    public List<ReportByNotaryPerson> getReportByDraft(String stringFilter){
        return contractRepository.getReportByDraft(stringFilter);
    }
    @Override
    public Optional<BigInteger> countTotalReportByNotary(String stringFilter){
        return contractRepository.countTotalReportByNotary(stringFilter);
    }
    @Override
    public Optional<BigInteger> countTotalReportByDraft(String stringFilter){
        return contractRepository.countTotalReportByDraft(stringFilter);
    }
    @Override
    public List<ReportByUser> getReportByUser(String stringFilter){
        return contractRepository.getReportByUser(stringFilter);
    }
    @Override
    public Optional<BigInteger> countTotalReportByUser(String stringFilter){
        return contractRepository.countTotalReportByUser(stringFilter);
    }
    @Override
    public ReportByTT20List reportByTT20(ReportByTT20List reportByTT20List) {

        int numberOfNotaryPerson = contractRepository.numberOfNotaryPerson(reportByTT20List).intValue();

        int numberOfContractLand = contractRepository.numberOfContractLand(reportByTT20List).intValue();

        int numberOfContractOther = contractRepository.numberOfContractOther(reportByTT20List).intValue();

        int numberOfContractDanSu = contractRepository.numberOfContractDanSu(reportByTT20List).intValue();

        int numberOfThuaKe = contractRepository.numberOfThuaKe(reportByTT20List).intValue();

        int numberOfOther = contractRepository.numberOfOther(reportByTT20List).intValue();

        long tongPhiCongChung = contractRepository.tongPhiCongChung(reportByTT20List).longValue();



        int tongNopNganSach;
        int numberOfTotalContract = numberOfContractLand +
                numberOfContractOther +
                numberOfContractDanSu +
                numberOfThuaKe +
                numberOfOther;

        reportByTT20List.setNumberOfContractLand(numberOfContractLand);
        reportByTT20List.setNumberOfContractOther(numberOfContractOther);
        reportByTT20List.setNumberOfContractDanSu(numberOfContractDanSu);
        reportByTT20List.setNumberOfThuaKe(numberOfThuaKe);
        reportByTT20List.setNumberOfOther(numberOfOther);
        reportByTT20List.setNumberOfTotalContract(numberOfTotalContract);
        reportByTT20List.setNumberOfNotaryPerson(numberOfNotaryPerson);
        reportByTT20List.setTongPhiCongChung(tongPhiCongChung);
        return reportByTT20List;
    }

    @Override
    public ReportByTT03List reportByTT03(ReportByTT03List reportByTT03List) {
        /*int numberOfNotaryPersonHopDanh04 = contractRepository.numberOfNotaryPersonHopDanh04(reportByTT03List).intValue();*/
        BigDecimal numThuLaoCongChung = contractRepository.numThuLaoCongChungTT03(reportByTT03List);

        BigInteger numberOfNotaryPersonB = contractRepository.numberOfNotaryPerson03(reportByTT03List);
        int numberOfNotaryPerson = numberOfNotaryPersonB!= null? numberOfNotaryPersonB.intValue() : 0;

        BigDecimal tongPhiCongChungB = contractRepository.tongPhiCongChung03(reportByTT03List);
        int tongPhiCongChung = tongPhiCongChungB != null ? tongPhiCongChungB.intValue() : 0;

        BigInteger numberOfContractLandB = contractRepository.numberOfContractLand03(reportByTT03List);
        int numberOfContractLand = numberOfContractLandB != null ? numberOfContractLandB.intValue() : 0;

        BigInteger numberOfContractOtherB = contractRepository.numberOfContractOther03(reportByTT03List);
        int numberOfContractOther = numberOfContractOtherB != null ? numberOfContractOtherB.intValue() : 0;

        BigInteger numberOfContractDanSuB = contractRepository.numberOfContractDanSu03(reportByTT03List);
        int numberOfContractDanSu = numberOfContractDanSuB != null ? numberOfContractDanSuB.intValue() : 0;

        BigInteger numberOfThuaKeB = contractRepository.numberOfThuaKe03(reportByTT03List);
        int numberOfThuaKe = numberOfThuaKeB != null ? numberOfThuaKeB.intValue() : 0;
        //int numberOfOther = contractRepository.numberOfOther04(reportByTT04List).intValue();


        int numberOfWorkOther = contractRepository.numberOfContractWorkOther03(reportByTT03List).intValue();

        /*int numberOfTotalContract =
                numberOfContractLand +
                numberOfContractOther +
                numberOfContractDanSu +
                numberOfThuaKe +
                numberOfWorkOther;*/

        BigInteger numberOfTotalContractB = contractRepository.numberOfTotalContract03(reportByTT03List);
        int numberOfTotalContract = numberOfTotalContractB != null ? numberOfTotalContractB.intValue() : 0;

        reportByTT03List.setThuLaoCongChung(numThuLaoCongChung != null ? numThuLaoCongChung.intValue() : 0);
        /*reportByTT03List.setNumberOfNotaryPersonHopDanh(numberOfNotaryPersonHopDanh04);*/
        reportByTT03List.setNumberOfContractLand(numberOfContractLand);
        reportByTT03List.setNumberOfContractOther(numberOfContractOther);
        reportByTT03List.setNumberOfContractDanSu(numberOfContractDanSu);
        reportByTT03List.setNumberOfThuaKe(numberOfThuaKe);
        reportByTT03List.setNumberOfOther(numberOfContractOther);
        reportByTT03List.setNumberOfTotalContract(numberOfTotalContract);
        reportByTT03List.setNumberOfNotaryPerson(numberOfNotaryPerson);
        reportByTT03List.setTongPhiCongChung(tongPhiCongChung);
        return reportByTT03List;
    }

    @Override
    public ReportByTT04List reportByTT04(ReportByTT04List reportByTT04List) {
        int numberOfNotaryPersonHopDanh04 = contractRepository.numberOfNotaryPersonHopDanh04(reportByTT04List).intValue();
        BigDecimal numThuLaoCongChung = contractRepository.numThuLaoCongChung(reportByTT04List);

        BigInteger numberOfNotaryPersonB = contractRepository.numberOfNotaryPerson04(reportByTT04List);
        int numberOfNotaryPerson = numberOfNotaryPersonB!= null? numberOfNotaryPersonB.intValue() : 0;

        BigDecimal tongPhiCongChungB = contractRepository.tongPhiCongChung04(reportByTT04List);
        int tongPhiCongChung = tongPhiCongChungB != null ? tongPhiCongChungB.intValue() : 0;

        BigInteger numberOfContractLandB = contractRepository.numberOfContractLand04(reportByTT04List);
        int numberOfContractLand = numberOfContractLandB != null ? numberOfContractLandB.intValue() : 0;

        BigInteger numberOfContractOtherB = contractRepository.numberOfContractOther04(reportByTT04List);
        int numberOfContractOther = numberOfContractOtherB != null ? numberOfContractOtherB.intValue() : 0;

        BigInteger numberOfContractDanSuB = contractRepository.numberOfContractDanSu04(reportByTT04List);
        int numberOfContractDanSu = numberOfContractDanSuB != null ? numberOfContractDanSuB.intValue() : 0;

        BigInteger numberOfThuaKeB = contractRepository.numberOfThuaKe04(reportByTT04List);
        int numberOfThuaKe = numberOfThuaKeB != null ? numberOfThuaKeB.intValue() : 0;
        //int numberOfOther = contractRepository.numberOfOther04(reportByTT04List).intValue();


        int numberOfWorkOther = contractRepository.numberOfContractWorkOther04(reportByTT04List).intValue();

        /*int numberOfTotalContract =
                numberOfContractLand +
                numberOfContractOther +
                numberOfContractDanSu +
                numberOfThuaKe +
                numberOfWorkOther;*/

        BigInteger numberOfTotalContractB = contractRepository.numberOfTotalContract04(reportByTT04List);
        int numberOfTotalContract = numberOfTotalContractB != null ? numberOfTotalContractB.intValue() : 0;

        reportByTT04List.setThuLaoCongChung(numThuLaoCongChung != null ? numThuLaoCongChung.intValue() : 0);
        reportByTT04List.setNumberOfNotaryPersonHopDanh(numberOfNotaryPersonHopDanh04);
        reportByTT04List.setNumberOfContractLand(numberOfContractLand);
        reportByTT04List.setNumberOfContractOther(numberOfContractOther);
        reportByTT04List.setNumberOfContractDanSu(numberOfContractDanSu);
        reportByTT04List.setNumberOfThuaKe(numberOfThuaKe);
        reportByTT04List.setNumberOfOther(numberOfContractOther);
        reportByTT04List.setNumberOfTotalContract(numberOfTotalContract);
        reportByTT04List.setNumberOfNotaryPerson(numberOfNotaryPerson);
        reportByTT04List.setTongPhiCongChung(tongPhiCongChung);
        return reportByTT04List;
    }

    @Override
    public List<ReportByGroupTotal> selectReportByGroupTotal(ReportByGroupTotalList reportByGroupTotalList) {
        String nhomHD = reportByGroupTotalList.getNhomHD();
        String tenHD = reportByGroupTotalList.getTenHD();
        String donVi = reportByGroupTotalList.getDonVi();
        String source = reportByGroupTotalList.getSource();
        String district_code = reportByGroupTotalList.getDistrict_code();
        String level_cert = reportByGroupTotalList.getLevel_cert();
        Long user_id = reportByGroupTotalList.getUserId();

        String filter = "";
        filter += "  1=1 ";
        if(!level_cert.equals("XA")){
            if (!nhomHD.equals(String.valueOf(0))) filter += " and trans.contract_kind =" + nhomHD + " ";
            if (!tenHD.equals(String.valueOf(0)))
                filter += "and  trans.code_template = "+tenHD ;
            /*if (!tenHD.equals(String.valueOf(0)))
            filter += "and  trans.contract_name = '" + contractRepository.selectContractTeamplateByFilter(" where code_template=" + tenHD).orElse(null).get(0).getName() + "' ";*/

            if (!StringUtils.isBlank(district_code))
                filter += " and u.district_code = '"+district_code+"' " ;
        }else {
            filter += " and u.id ="+user_id+"";
        }

        return contractRepository.selectReportByGroupTotal(reportByGroupTotalList, filter);
        //return transactionPropertyService.findTransactionByFilter(filter).orElse(new ArrayList<TransactionProperty>());
    }

    @Override
    public List<com.vn.osp.notarialservices.transaction.dto.TransactionProperty> selectDetailReportByGroup(ReportByGroupTotalList reportByGroupTotalList) {
        String nhomHD = reportByGroupTotalList.getNhomHD();
        String codeTemplate = reportByGroupTotalList.getCodeTemplate();
        String tenHD = reportByGroupTotalList.getTenHD();
        String donVi = reportByGroupTotalList.getDonVi();
        String source = reportByGroupTotalList.getSource();
        String notaryDateFrom = reportByGroupTotalList.getNotaryDateFromFilter();
        String notaryDateTo = reportByGroupTotalList.getNotaryDateToFilter();
        int page = reportByGroupTotalList.getPage();
        int totalPage = reportByGroupTotalList.getTotalPage();

        int offset = 20 * (page - 1);
        String filter = "";
        filter += " where  1=1 ";
        filter += "and a.notary_date >= '"+notaryDateFrom+"'and a.notary_date <= '"+notaryDateTo +"' ";
        if (!nhomHD.equals(String.valueOf(0))) filter += " and contract_kind =" + nhomHD + " ";
        /*if (!tenHD.equals(String.valueOf(0)))
            filter += "and  contract_name = '" +tenHD+ "' ";*/
        /*filter += "and  contract_name like '" + contractRepository.selectContractTeamplateByFilter(" where id=" + tenHD).orElse(null).get(0).getName() + "' ";*/
        if(codeTemplate!= null) filter += "and  code_template = '" +codeTemplate+ "' ";
        /*filter += " ORDER BY a.notary_date DESC, LENGTH(a.contract_number) desc, a.contract_number DESC ";*/
        filter += " ORDER BY LENGTH(a.contract_number) asc, a.contract_number ASC ";//update sắp xếp lại theo yêu cầu của vpcc kim ngân-thanh hóa
        if(page != 0){
            filter += "LIMIT "+offset+",20";
        }


        return transactionPropertyService.findTransactionByFilter(filter).orElse(new ArrayList<com.vn.osp.notarialservices.transaction.dto.TransactionProperty>());
    }

    @Override
    public List<com.vn.osp.notarialservices.transaction.dto.TransactionProperty> selectAllDetailReportByGroup(ReportByGroupTotalList reportByGroupTotalList) {
        String nhomHD = reportByGroupTotalList.getNhomHD();
        String tenHD = reportByGroupTotalList.getTenHD();
        String donVi = reportByGroupTotalList.getDonVi();
        String source = reportByGroupTotalList.getSource();
        int page = reportByGroupTotalList.getPage();
        int totalPage = reportByGroupTotalList.getTotalPage();
        int offset = 20 * (page - 1);
        String filter = "";
        filter += " where  1=1";
        if (!nhomHD.equals(String.valueOf(0))) filter += " and contract_kind =" + nhomHD + " ";
        if (!tenHD.equals(String.valueOf(0)))
            filter += "and  contract_name = '" + contractRepository.selectContractTeamplateByFilter(" where id=" + tenHD).orElse(null).get(0).getName() + "' ";


        return transactionPropertyService.findTransactionByFilter(filter).orElse(new ArrayList<com.vn.osp.notarialservices.transaction.dto.TransactionProperty>());
    }

    @Override
    public BigInteger countDetailReportByGroup(ReportByGroupTotalList reportByGroupTotalList) {
        String nhomHD = reportByGroupTotalList.getNhomHD();
        String codeTemplate = reportByGroupTotalList.getCodeTemplate();
        String tenHD = reportByGroupTotalList.getTenHD();
        String donVi = reportByGroupTotalList.getDonVi();
        String source = reportByGroupTotalList.getSource();
        String notaryDateFrom = reportByGroupTotalList.getNotaryDateFromFilter();
        String notaryDateTo = reportByGroupTotalList.getNotaryDateToFilter();

        String filter = "";
        filter += " where  1=1 ";
        filter += "and a.notary_date >= '"+notaryDateFrom+"'and a.notary_date <= '"+notaryDateTo +"' ";
        if (!nhomHD.equals(String.valueOf(0))) filter += " and contract_kind =" + nhomHD + " ";
        if(codeTemplate!=null) filter += "and  code_template = '" + codeTemplate + "' ";
        /*if (!tenHD.equals(String.valueOf(0)))
            filter += "and  contract_name like '" + tenHD + "' ";*/
        /*filter += "and  contract_name like '" + contractRepository.selectContractTeamplateByFilter(" where id=" + tenHD).orElse(null).get(0).getName() + "' ";*/

        return transactionPropertyService.countTotalByFilter(filter).orElse(BigInteger.valueOf(0));
    }


    @Override
    public Optional<Integer> addContract(Contract contract, com.vn.osp.notarialservices.transaction.dto.TransactionProperty trans, ContractHistoryInfor his) {
        return contractRepository.addContract(contractConverter.convert(contract),transactionPropertyConverter.convert(trans),contractHistoryConverter.convert(his));
    }

    @Override
    public Optional<Integer> editContract(Contract contract, com.vn.osp.notarialservices.transaction.dto.TransactionProperty trans, ContractHistoryInfor his) {
        return contractRepository.editContract(contractConverter.convert(contract),transactionPropertyConverter.convert(trans),contractHistoryConverter.convert(his));
    }

    @Override
    public Optional<Integer> cancelContract(long contract_cancel_id, Contract contract, com.vn.osp.notarialservices.transaction.dto.TransactionProperty trans, ContractHistoryInfor his) {
        return contractRepository.cancelContract(contract_cancel_id,contractConverter.convert(contract),transactionPropertyConverter.convert(trans),contractHistoryConverter.convert(his));
    }

    @Override
    public Boolean deleteContract(String id,String xml_contract_history){
        return contractRepository.deleteContract(id,xml_contract_history);
    }



    @Override
    public com.vn.osp.notarialservices.transaction.dto.TransactionProperty genaralInfoToTransactionProperty(Contract contract, com.vn.osp.notarialservices.transaction.dto.TransactionProperty trans){

        return genInfoTrans(contract,trans);
    }

    @Override
    public ContractHistoryInfor genaralHistoryInfo(Contract contract,ContractHistoryInfor his,String action){

        return genHistory(contract,his,action);
    }

    public ContractHistoryInfor genHistory(Contract con,ContractHistoryInfor his,String action){
        his.setContract_number(con.getContract_number());
        his.setExecute_content(action);
        his.setExecute_date_time(con.getUpdate_date_time().toString());
        his.setExecute_person(con.getUpdate_user_id().toString());
        StringBuilder contract_content=new StringBuilder("");
        if(con.getContract_template_id()!= null){
            ContractTemplate contract_template=getContractTemplateByCodeTemp(con.getContract_template_id().toString()).orElse(null);
            ContractKind contractKind=getContractKindByContractTempCode(con.getContract_template_id().intValue()).orElse(null);

            if(contractKind!=null)  contract_content.append("-Nhóm hợp đồng: "+contractKind.getName()+"\\n");
            if(contract_template!=null) contract_content.append("-Tên hợp đồng: "+contract_template.getName()+"\\n");
        }

        if(con.getContract_number()!=null && !con.getContract_number().equals("")) contract_content.append("-Số hợp đồng: "+con.getContract_number()+"\\n");
        if(con.getReceived_date()!=null && !con.getReceived_date().equals("")) contract_content.append("-Ngày thụ lý: "+con.getReceived_date() + "\\n");
        if(con.getNotary_date()!=null && !con.getNotary_date().equals("")) contract_content.append("-Ngày công chứng: "+con.getNotary_date() +"\\n");
//        String info=genInfoPrivyAndProperty(con.getJson_person(),con.getJson_property());
//        contract_content.append(info);
        contract_content.append(con.getRelation_object_a());
        contract_content.append(con.getRelation_object_b());
        his.setContract_content(contract_content.toString());
        return his;
    }

//    private String genInfoPrivyAndProperty(String json_privy,String json_property){
////        String property="'{\"name\":\"property\",\"properties\":[{\"type\":\"01\",\"id\":1,\"property_info\":\"\",\"owner_info\":\"\",\"other_info\":\"\",\"land\":{\"land_certificate\":\"\",\"land_issue_place\":\"\",\"land_issue_date\":\"\",\"land_map_number\":\"\",\"land_number\":\"\",\"land_address\":\"\",\"land_area\":\"\",\"land_public_area\":\"\",\"land_private_area\":\"\",\"land_use_purpose\":\"\",\"land_use_period\":\"\",\"land_use_origin\":\"\",\"land_associate_property\":\"\",\"land_street\":\"\",\"land_district\":\"\",\"land_province\":\"\",\"land_full_of_area\":\"\"},\"vehicle\":{\"car_license_number\":\"\",\"car_regist_number\":\"\",\"car_issue_place\":\"\",\"car_issue_date\":\"\",\"car_frame_number\":\"\",\"car_machine_number\":\"\"}}]}'";
////        String privy="'{\"name\":\"Đương sự\",\"privy\":[{\"id\":1,\"name\":\"Bên A\",\"action\":\"\",\"persons\":[{\"id\":\"\",\"name\":\"Teo\",\"birthday\":\"05/07/2017\",\"passport\":\"ds2324\",\"certification_date\":\"12/07/2017\",\"certification_place\":\"hn\",\"address\":\"\",\"description\":\"\"}]},{\"id\":2,\"name\":\"Bên B\",\"action\":\"\",\"persons\":[{\"id\":\"\",\"name\":\"ty\",\"birthday\":\"05/07/2017\",\"passport\":\"r3451235\",\"certification_date\":\"15/07/2017\",\"certification_place\":\"rtf\",\"address\":\"\",\"description\":\"\"}]}]}'";
////        String pro="'{\"name\":\"property\",\"properties\":[{\"type\":\"01\",\"id\":1,\"type_view\":\"1\",\"property_info\":\"\",\"owner_info\":\"\",\"other_info\":\"\",\"restrict\":\"không\",\"apartment\":{\"apartment_address\":\"Cầu Giấy\",\"apartment_number\":\"64\",\"apartment_floor\":\"77\",\"apartment_area_use\":\"100\",\"apartment_area_build\":\"120\",\"apartment_structure\":\"1 tầng\",\"apartment_total_floor\":\"100\"},\"land\":{\"land_certificate\":\"4623623\",\"land_issue_place\":\"HN\",\"land_issue_date\":\"20/10/2000\",\"land_map_number\":\"756536\",\"land_number\":\"4535\",\"land_address\":\"365346\",\"land_area\":\"100\",\"land_area_text\":\"một trăm mét\",\"land_public_area\":\"0\",\"land_private_area\":\"100\",\"land_use_purpose\":\"ở\",\"land_use_period\":\"2020\",\"land_use_origin\":\"lâu rồi\",\"land_associate_property\":\"\",\"land_street\":\"\",\"land_district\":\"\",\"land_province\":\"\",\"land_full_of_area\":\"\"},\"vehicle\":{\"car_license_number\":\"\",\"car_regist_number\":\"\",\"car_issue_place\":\"\",\"car_issue_date\":\"\",\"car_frame_number\":\"\",\"car_machine_number\":\"\"}}]}'";
////        String vy2="'{\"name\":\"Đương sự\",\"privy\":[{\"name\":\"Bên A\",\"id\":0,\"action\":\"bán\",\"persons\":[{\"type\":0,\"id\":\"\",\"name\":\"Tèo\",\"birthday\":\"1980\",\"passport\":\"52352525\",\"certification_date\":\"\",\"certification_place\":\"\",\"address\":\"\",\"position\":\"\",\"description\":\"\",\"org_name\":\"\",\"org_address\":\"\",\"org_code\":\"\",\"first_registed_date\":\"\",\"number_change_registed\":\"\",\"change_registed_date\":\"\",\"department_issue\":\"\"}]},{\"name\":\"Bên B\",\"id\":1,\"action\":\"mua\",\"persons\":[{\"type\":0,\"id\":\"\",\"name\":\"Tý\",\"birthday\":\"170\",\"passport\":\"6262536\",\"certification_date\":\"\",\"certification_place\":\"\",\"address\":\"\",\"position\":\"\",\"description\":\"\",\"org_name\":\"\",\"org_address\":\"\",\"org_code\":\"\",\"first_registed_date\":\"\",\"number_change_registed\":\"\",\"change_registed_date\":\"\",\"department_issue\":\"\"}]}]}'";
//        json_privy=json_privy.substring(1,json_privy.length()-1);
//        json_property=json_property.substring(1,json_property.length()-1);
//        StringBuilder contract_content=new StringBuilder("");
//        try{
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode node = objectMapper.readValue(json_privy, JsonNode.class);
//            JsonNode node1=objectMapper.readValue(json_property,JsonNode.class);
//            JsonNode nodePrivys = node.get("privy");
//            JsonNode nodeProperties=node1.get("properties");
//
//            if(nodePrivys.isArray()){
//                for(JsonNode nodePrivy : nodePrivys){
//                    JsonNode nodePrivyName=nodePrivy.get("name");
//                    if(!StringUtils.isBlank(nodePrivyName.asText())){
//                        contract_content.append("-"+nodePrivyName.asText()+":<br>");
//                    }
//                    JsonNode nodePersons = nodePrivy.get("persons");
//                    if(nodePersons.isArray()){
//                        for(JsonNode nodePerson:nodePersons){
//                            JsonNode id=nodePerson.get("id");
//                            JsonNode name=nodePerson.get("name");
//                            JsonNode birthday=nodePerson.get("birthday");
//                            JsonNode passport=nodePerson.get("passport");
//                            JsonNode certification_date=nodePerson.get("certification_date");
//                            JsonNode certification_place=nodePerson.get("certification_place");
//                            JsonNode address=nodePerson.get("address");
//                            JsonNode description=nodePerson.get("description");
//                            JsonNode position=nodePerson.get("position");
//                            JsonNode org_name=nodePerson.get("org_name");
//                            JsonNode org_address=nodePerson.get("org_address");
//                            JsonNode org_code=nodePerson.get("org_code");
//                            JsonNode first_registed_date=nodePerson.get("first_registed_date");
//                            JsonNode number_change_registed=nodePerson.get("number_change_registed");
//                            JsonNode change_registed_date=nodePerson.get("change_registed_date");
//                            JsonNode department_issue=nodePerson.get("department_issue");
//
//                            if(checkNotNullNode(org_name)){
//                                contract_content.append("Công ty:"+org_name.asText()+";");
//                            }
//                            if(checkNotNullNode(org_address)){
//                                contract_content.append("Địa chỉ cty:"+org_address.asText()+";");
//                            }
//                            if(checkNotNullNode(org_code)){
//                                contract_content.append("Mã số thuế:"+org_code.asText()+";");
//                            }
//                            if(checkNotNullNode(first_registed_date)){
//                                contract_content.append("Đăng ký lần đầu ngày:"+first_registed_date.asText()+";");
//                            }
//                            if(checkNotNullNode(number_change_registed)){
//                                contract_content.append("Số lần thay đăng ký:"+number_change_registed.asText()+";");
//                            }
//                            if(checkNotNullNode(change_registed_date)){
//                                contract_content.append("Ngày đổi đăng ký:"+change_registed_date.asText()+";");
//                            }
//                            if(checkNotNullNode(department_issue)){
//                                contract_content.append("Theo:"+department_issue.asText()+";");
//                            }
//
//                            if(checkNotNullNode(name)){
//                                contract_content.append("Tên:"+name.asText()+";");
//                            }
//                            if(checkNotNullNode(position)){
//                                contract_content.append("Chức danh:"+position.asText()+";");
//                            }
//
//                            if(checkNotNullNode(birthday)){
//                                contract_content.append("Ngày sinh:"+birthday.asText()+";");
//                            }
//                            if(checkNotNullNode(passport)){
//                                contract_content.append("CTM:"+passport.asText()+";");
//                            }
//                            if(checkNotNullNode(certification_date)){
//                                contract_content.append("Ngày cấp:"+certification_date.asText()+";");
//                            }
//                            if(checkNotNullNode(certification_place)){
//                                contract_content.append("Nơi cấp:"+certification_place.asText()+";");
//                            }
//                            if(checkNotNullNode(address)){
//                                contract_content.append("Địa chỉ:"+address.asText()+";");
//                            }
//                            if(checkNotNullNode(description)){
//                                contract_content.append("Mô tả:"+description.asText()+";");
//                            }
//                        }
//                    }
//                    contract_content.append("<br>");
//                }
//            }
//
//            if(nodeProperties.isArray()){
//                contract_content.append("-Tài sản:<br>");
//                for(JsonNode nodeProperty:nodeProperties){
//                    JsonNode type=nodeProperty.get("type");
//                    JsonNode id=nodeProperty.get("id");
//                    if(checkNotNullNode(id)){
//                        contract_content.append(id.asText()+".");
//                    }
//                    if(type!=null){
//                        switch (type.asText()){
//                            case "01":
//                                JsonNode restrict=nodeProperty.get("restrict");
//                                JsonNode apartment=nodeProperty.get("apartment");
//                                JsonNode land=nodeProperty.get("land");
//                                JsonNode apartment_address=apartment.get("apartment_address");
//                                JsonNode apartment_number=apartment.get("apartment_number");
//                                JsonNode apartment_floor=apartment.get("apartment_floor");
//                                JsonNode apartment_area_use=apartment.get("apartment_area_use");
//                                JsonNode apartment_area_build=apartment.get("apartment_area_build");
//                                JsonNode apartment_structure=apartment.get("apartment_structure");
//                                JsonNode apartment_total_floor=apartment.get("apartment_total_floor");
//                                JsonNode land_certificate=land.get("land_certificate");
//                                JsonNode land_issue_place=land.get("land_issue_place");
//                                JsonNode land_issue_date=land.get("land_issue_date");
//                                JsonNode land_map_number=land.get("land_map_number");
//                                JsonNode land_number=land.get("land_number");
//                                JsonNode land_address=land.get("land_address");
//                                JsonNode land_area=land.get("land_area");
////                                JsonNode land_area_text=nodeProperty.get("land_area_text");
//                                JsonNode land_public_area=land.get("land_public_area");
//                                JsonNode land_private_area=land.get("land_private_area");
//                                JsonNode land_use_purpose=land.get("land_use_purpose");
//                                JsonNode land_use_period=land.get("land_use_period");
//                                JsonNode land_use_origin=land.get("land_use_origin");
//                                JsonNode land_associate_property=land.get("land_associate_property");
//                                JsonNode land_street=land.get("land_street");
//                                JsonNode land_district=land.get("land_district");
//                                JsonNode land_province=land.get("land_province");
//                                JsonNode land_full_of_area=land.get("land_full_of_area");
//
//                                if(checkNotNullNode(apartment_address)){
//                                    contract_content.append("Địa chỉ căn hộ:"+apartment_address.asText()+";");
//                                }
//                                if(checkNotNullNode(apartment_number)){
//                                    contract_content.append("Số căn hộ:"+apartment_number.asText()+";");
//                                }
//                                if(checkNotNullNode(apartment_floor)){
//                                    contract_content.append("Tầng số:"+apartment_floor.asText()+";");
//                                }
//                                if(checkNotNullNode(apartment_area_use)){
//                                    contract_content.append("Diện tích căn hộ:"+apartment_area_use.asText()+";");
//                                }
//                                if(checkNotNullNode(apartment_area_build)){
//                                    contract_content.append("Diện tích xây dựng:"+apartment_area_build.asText()+";");
//                                }
//                                if(checkNotNullNode(apartment_structure)){
//                                    contract_content.append("Kết cấu căn hộ:"+apartment_structure.asText()+";");
//                                }
//                                if(checkNotNullNode(apartment_total_floor)){
//                                    contract_content.append("Tổng số tầng:"+apartment_total_floor.asText()+";");
//                                }
//                                if(checkNotNullNode(land_certificate)){
//                                    contract_content.append("Giấy chứng nhận đất:"+land_certificate.asText()+";");
//                                }
//                                if(checkNotNullNode(land_issue_place)){
//                                    contract_content.append("Nơi cấp:"+land_issue_place.asText()+";");
//                                }
//                                if(checkNotNullNode(land_issue_date)){
//                                    contract_content.append("Ngày cấp:"+land_issue_date.asText()+";");
//                                }
//                                if(checkNotNullNode(land_map_number)){
//                                    contract_content.append("Tờ bản đồ số:"+land_map_number.asText()+";");
//                                }
//                                if(checkNotNullNode(land_number)){
//                                    contract_content.append("Thửa đất số:"+land_number.asText()+";");
//                                }
//                                if(checkNotNullNode(land_address)){
//                                    contract_content.append("Địa chỉ:"+land_address.asText()+";");
//                                }
//                                if(checkNotNullNode(land_area)){
//                                    contract_content.append("Diện tích:"+land_area.asText()+";");
//                                }
//                                if(checkNotNullNode(land_public_area)){
//                                    contract_content.append("Sử dụng chung:"+land_public_area.asText()+";");
//                                }
//                                if(checkNotNullNode(land_private_area)){
//                                    contract_content.append("Sử dụng riêng:"+land_private_area.asText()+";");
//                                }
//                                if(checkNotNullNode(land_use_purpose)){
//                                    contract_content.append("Mục đích dùng:"+land_use_purpose.asText()+";");
//                                }
//                                if(checkNotNullNode(land_use_period)){
//                                    contract_content.append("Thời hạn:"+land_use_period.asText()+";");
//                                }
//                                if(checkNotNullNode(land_use_origin)){
//                                    contract_content.append("Nguồn gốc:"+land_use_origin.asText()+";");
//                                }
//                                if(checkNotNullNode(land_private_area)){
//                                    contract_content.append("Sử dụng riêng:"+land_private_area.asText()+";");
//                                }
//                                if(checkNotNullNode(restrict)){
//                                    contract_content.append("Hạn chế(nếu có):"+restrict.asText()+";");
//                                }
//
//
//                                break;
//                            case "02":
//                                JsonNode vehicle=nodeProperty.get("vehicle");
//                                JsonNode car_license_number=vehicle.get("car_license_number");
//                                JsonNode car_regist_number=vehicle.get("car_regist_number");
//                                JsonNode car_issue_place=vehicle.get("car_issue_place");
//                                JsonNode car_issue_date=vehicle.get("car_issue_date");
//                                JsonNode car_frame_number=vehicle.get("car_frame_number");
//                                JsonNode car_machine_number=vehicle.get("car_machine_number");
//                                if(checkNotNullNode(car_license_number)){
//                                    contract_content.append("Biển kiểm soát:"+car_license_number.asText()+";");
//                                }
//                                if(checkNotNullNode(car_regist_number)){
//                                    contract_content.append("Số đăng ký:"+car_regist_number.asText()+";");
//                                }
//                                if(checkNotNullNode(car_issue_place)){
//                                    contract_content.append("Nơi cấp:"+car_issue_place.asText()+";");
//                                }
//                                if(checkNotNullNode(car_issue_date)){
//                                    contract_content.append("Ngày cấp:"+car_issue_date.asText()+";");
//                                }
//                                if(checkNotNullNode(car_frame_number)){
//                                    contract_content.append("Số khung:"+car_frame_number.asText()+";");
//                                }
//                                if(checkNotNullNode(car_machine_number)){
//                                    contract_content.append("Số máy:"+car_machine_number.asText()+";");
//                                }
//                                break;
//                            default:
//                                break;
//                        }
//
//                        JsonNode property_info=nodeProperty.get("property_info");
//                        JsonNode owner_info=nodeProperty.get("owner_info");
//                        JsonNode other_info=nodeProperty.get("other_info");
//                        if(checkNotNullNode(property_info)){
//                            contract_content.append("Thông tin tài sản:"+property_info.asText()+";");
//                        }
//                        if(checkNotNullNode(owner_info)){
//                            contract_content.append("Chủ sở hữu:"+owner_info.asText()+";");
//                        }
//                        if(checkNotNullNode(other_info)){
//                            contract_content.append("Thông tin khác:"+other_info.asText()+";");
//                        }
//                        contract_content.append("<br>");
//                    }
//
//                }
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return contract_content.toString();
//    }

//    private  boolean checkNotNullNode(JsonNode node){
//        if(node!=null){
//            if(StringUtils.isBlank(node.asText()) || node.asText().equals("\"\"") || node.asText().equals("\"")){
//                return false;
//            }else{
//                return true;
//            }
//        }
//        return false;
//    }

    public com.vn.osp.notarialservices.transaction.dto.TransactionProperty genInfoTrans(Contract contract, com.vn.osp.notarialservices.transaction.dto.TransactionProperty trans){

        String authentication = accessHistoryService.getConfigValue("system_authentication_id").orElse("");
        String office_name=accessHistoryService.getConfigValue("notary_office_name").orElse("");
        User notary=new User();
        if(trans.getSynchronize_id()== null){
            trans.setSynchronize_id(authentication);
        }
        /*trans.setSynchronize_id(authentication);*/
        trans.setTransaction_content(contract.getSummary());
        trans.setNotary_date(contract.getNotary_date());
        trans.setNotary_office_name(office_name);
        trans.setContract_number(contract.getContract_number());
        if(contract.getContract_template_id() != null){
            ContractTemplate contract_template=getContractTemplateByCodeTemp(contract.getContract_template_id().toString()).orElse(null);
            trans.setCode_template(contract.getContract_template_id());
            if(contract_template!=null){
                trans.setContract_name(contract_template.getName());
                trans.setContract_kind(contract_template.getCode().toString());
            }
        }


        trans.setContract_value(contract.getContract_value());
        trans.setRelation_object(contract.getRelation_object_a());
        trans.setProperty_info(contract.getRelation_object_b());//truong nay o font-end code thay the luon cho property_info
        if(contract.getNotary_id()!=null){
            notary=userService.getUserById(contract.getNotary_id().toString()).orElse(null);
            if(notary!=null) trans.setNotary_person(notary.getFamily_name()+" "+notary.getFirst_name());
        }
        trans.setNotary_place(contract.getNotary_place());
        trans.setNotary_fee(contract.getCost_tt91());
        trans.setRemuneration_cost(contract.getCost_draft());
        trans.setNotary_cost_total(contract.getCost_total());
        trans.setNote(contract.getNote());
        trans.setContract_period(contract.getContract_period());
        trans.setMortage_cancel_flag(contract.getMortage_cancel_flag());
        trans.setMortage_cancel_date(contract.getMortage_cancel_date());
        trans.setMortage_cancel_note(contract.getMortage_cancel_note());
        trans.setCancel_status(contract.getCancel_status());
        trans.setCancel_description(contract.getCancel_description());
        trans.setEntry_user_id(contract.getEntry_user_id());
        trans.setEntry_user_name(contract.getEntry_user_name());
        if(contract.getEntry_date_time() != null){
            trans.setEntry_date_time(contract.getEntry_date_time().toString());
        }
        else {
            trans.setEntry_date_time("");
        }
        trans.setUpdate_user_id(contract.getUpdate_user_id());
        trans.setUpdate_user_name(contract.getUpdate_user_name());
        trans.setUpdate_date_time(contract.getUpdate_date_time().toString());
        trans.setBank_code(contract.getBank_code());
        trans.setBank_name(contract.getBank_name());
        trans.setSyn_status(0);
        trans.setJson_property(contract.getJson_property());
        trans.setJson_person(contract.getJson_person());

        if(SystemProperties.getProperty("org_type").equals(Constants.ORG_TYPE_PHUONG_XA)) {
            User user=userService.getUserById(contract.getNotary_id().toString()).orElse(null);
            if(notary!=null) {
                String disTrictName = "";
                if(user.getDistrict_code()!=null) {
                    List<District> districts = districtService.findByCode(user.getDistrict_code()).get();
                    if(!districts.isEmpty()) disTrictName = districts.get(0).getName()+" ";
                }
                trans.setNotary_office_name(disTrictName+""+ user.getFamily_name()+" "+user.getFirst_name());

            }
            trans.setNotary_person(contract.getContract_signer());
        }

        return trans;
    }
    @Override
    public List<ContractError> getReportContractError(String stringFilter){
        return contractRepository.getReportContractError(stringFilter);
    }

    @Override
    public Optional<Contract> getContractById(String id){
        Contract con=contractRepository.getContractById(id).map(contractConverter::convert).orElse(null);
        return Optional.of(con);
    }

    @Override
    public Optional<Contract> genContractForEdit(Contract contractDb,Contract contractEdit) {
        contractDb.setNotary_id(contractEdit.getNotary_id());
        contractDb.setContract_template_id(contractEdit.getContract_template_id());
        contractDb.setError_status(contractEdit.getError_status());
        contractDb.setError_description(contractEdit.getError_description());
        contractDb.setAddition_status(contractEdit.getAddition_status());
        contractDb.setAddition_description(contractEdit.getAddition_description());
        contractDb.setCancel_status(contractEdit.getCancel_status());
        contractDb.setCancel_description(contractEdit.getCancel_description());
        contractDb.setContract_period(contractEdit.getContract_period());
        contractDb.setMortage_cancel_flag(contractEdit.getMortage_cancel_flag());
        contractDb.setMortage_cancel_date(contractEdit.getMortage_cancel_date());
        contractDb.setMortage_cancel_note(contractEdit.getMortage_cancel_note());
        contractDb.setUpdate_user_id(contractEdit.getUpdate_user_id());
        contractDb.setUpdate_date_time(contractEdit.getUpdate_date_time());
        contractDb.setUpdate_user_name(contractEdit.getUpdate_user_name());

        return Optional.of(contractDb);
    }

    @Override
    public Optional<ContractTemplate> getContractTemplateById(String id){
        ContractTemplate contractTemp=contractRepository.getContractTemplateById(id).map(contractTemplateConverter::convert).orElse(null);
        return  Optional.of(contractTemp) ;
    }

    @Override
    public Optional<ContractTemplate> getContractTemplateByCodeTemp(String code_temp) {
        ContractTemplate contractTemp=contractRepository.getContractTemplateByCodeTemp(code_temp).map(contractTemplateConverter::convert).orElse(null);
        return  Optional.of(contractTemp) ;
    }

    @Override
    public Optional<List<ContractTemplate>> getContractTemplateByParentCodeTemplate(String kind_id) {
        List<ContractTemplate> contractTemp = contractRepository.getContractTemplateByParentCodeTemplate(kind_id);
        return  Optional.of(contractTemp);
    }

    @Override
    public Optional<ContractKind> getContractKindByContractTempId(String id){
        ContractKind contractKind=contractRepository.getContractKindByContractTempId(id).map(contractKindConverter::convert).orElse(null);
        return Optional.of(contractKind);
    }

    @Override
    public Optional<ContractKind> getContractKindByContractTempCode(int code) {
        ContractKind contractKind=contractRepository.getContractKindByContractTempCode(code).map(contractKindConverter::convert).orElse(null);
        return Optional.of(contractKind);
    }

    @Override
    public List<ContractAdditional> getReportContractAdditional(String stringFilter){
        return contractRepository.getReportContractAdditional(stringFilter);
    }

    @Override
    public Optional<List<ContractKind>> listContractKind(){
        List<ContractKind> lstContractKind= contractRepository.listContractKind().map(contractKindConverter::converts).orElse(null);
        return Optional.of(lstContractKind);
    }

    @Override
    public Optional<List<ContractTemplate>> listContractTemplateByContractKindId(String id){
        List<ContractTemplate> lstContractKind= contractRepository.listContractTemplateByContractKindId(id).map(contractTemplateConverter::converts).orElse(null);
        return Optional.of(lstContractKind);
    }

    @Override
    public Optional<List<ContractTemplate>> listContractTemplateByContractKindCode(String code) {
        List<ContractTemplate> list= contractRepository.listContractTemplateByContractKindCode(code).map(contractTemplateConverter::converts).orElse(null);
        return Optional.of(list);
    }

    @Override
    public Optional<List<ContractTemplate>> listContractTemplateSameKind(int id){
        List<ContractTemplate> lstContractTempalte= contractRepository.listContractTemplateSameKind(id).map(contractTemplateConverter::converts).orElse(null);
        return Optional.of(lstContractTempalte);
    }
    @Override
    public Optional<List<ContractTemplate>> listContractTemplate(){
        List<ContractTemplate> lstContractTempalte= contractRepository.listContractTemplate().map(contractTemplateConverter::converts).orElse(null);
        return Optional.of(lstContractTempalte);
    }

    @Override
    public SynchronizeContractTag genSynchronizeContractTagByTrans(com.vn.osp.notarialservices.transaction.dto.TransactionProperty property,int action){
        try{
            Optional<List<User>> usersEntry = userService.getUserByFilter(" Where id = " + property.getEntry_user_id());
            Optional<List<User>> usersUpdate = userService.getUserByFilter(" Where id = " + property.getUpdate_user_id());

            SynchronizeContractTag contractTag = new SynchronizeContractTag();
            contractTag.setNotaryOfficeCode(property.getSynchronize_id());
            contractTag.setContractKindCode(property.getContract_kind());
            contractTag.setCode_template(property.getCode_template());
            //add gia tri hop dong
            contractTag.setContractValue(property.getContract_value());
            //get contractKind code
//        ContractKind contractKind=contractKindService.getContractKindById(property.getContract_kind()).orElse(new ContractKind());
//        if(contractKind!=null){
//            contractTag.setContractKindCode(contractKind.getContract_kind_code());
//        }
            contractTag.setContractNumber(property.getContract_number());
            contractTag.setTypeSynchronize(action);
            contractTag.setContractName(property.getContract_name());
            contractTag.setTransactionContent(property.getTransaction_content());
            if(!StringUtils.isBlank(property.getNotary_date())){
//            contractTag.setNotaryDate(RelateDateTime.formatDate(property.getNotary_date(), "dd/MM/yyyy", "yyyy-MM-dd"));
                contractTag.setNotaryDate(RelateDateTime.formatDate1(property.getNotary_date()));
            }
            contractTag.setNotaryOfficeName(property.getNotary_office_name());
            contractTag.setNotaryPerson(property.getNotary_person());
            contractTag.setNotaryPlace(property.getNotary_place());
            contractTag.setNotaryFee(property.getNotary_fee());
            contractTag.setNotaryCostTotal(property.getNotary_cost_total());
            contractTag.setRemunerationCost(property.getRemuneration_cost());
            contractTag.setRelationObjects(property.getRelation_object());
            contractTag.setContractPeriod(property.getContract_period());
            contractTag.setMortageCancelFlag(property.getMortage_cancel_flag());
            contractTag.setMortageCancelDate(property.getMortage_cancel_date());
        /*if(!StringUtils.isBlank(property.getMortage_cancel_date())){
            if(RelateDateTime.checkFormatDDMMYYYY(property.getMortage_cancel_date())){
                contractTag.setMortageCancelDate(RelateDateTime.formatDate(property.getMortage_cancel_date(), "dd/MM/yyyy", "yyyy-MM-dd"));
            }else{
                contractTag.setMortageCancelDate(RelateDateTime.formatDate(property.getMortage_cancel_date(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
            }
        }*/

            contractTag.setMortageCancelNote(property.getMortage_cancel_note());
            contractTag.setCancelStatus(property.getCancel_status());
            contractTag.setCancelDescription(property.getCancel_description());
            if(usersEntry != null) {
                contractTag.setEntryUserName(property.getEntry_user_name() + "(" + usersEntry.get().get(0).getAccount() + ")");
            }

            if(!StringUtils.isBlank(property.getEntry_date_time())){
                if(RelateDateTime.checkFormatDDMMYYYY(property.getEntry_date_time())){
                    contractTag.setEntryDateTime(RelateDateTime.formatDate(property.getEntry_date_time(), "dd/MM/yyyy", "yyyy-MM-dd"));
                }else{
                    contractTag.setEntryDateTime(RelateDateTime.formatDate(property.getEntry_date_time(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
                }
            }
            if(usersUpdate != null) {
                contractTag.setUpdateUserName(property.getUpdate_user_name() + "(" + usersUpdate.get().get(0).getAccount() + ")");
            }
            if(!StringUtils.isBlank(property.getUpdate_date_time())){
                if(RelateDateTime.checkFormatDDMMYYYY(property.getEntry_date_time())){
                    contractTag.setUpdateDateTime(RelateDateTime.formatDate(property.getUpdate_date_time(), "dd/MM/yyyy", "yyyy-MM-dd"));
                }else{
                    contractTag.setUpdateDateTime(RelateDateTime.formatDate(property.getUpdate_date_time(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
                }
            }


            contractTag.setBankCode(property.getBank_code());
            contractTag.setContractNote(property.getNote());
            contractTag.setPropertyInfo(property.getProperty_info());
            contractTag.setJson_property(property.getJson_property());
            contractTag.setJson_person(property.getJson_person());
            return contractTag;
        }catch (Exception e){
            LOGGER.error("Co loi xay ra khi Convert du lieu tu dong bo method: genSynchronizeContractTagByTrans"+e.getMessage());
            return new SynchronizeContractTag();
        }
    }

    @Override
    public SynchronizeContractTag genSynchronizeContractTagByTransDelete(com.vn.osp.notarialservices.transaction.dto.TransactionProperty property,int action, String userId){
        try{
            Optional<List<User>> users = userService.getUserByFilter(" Where id = " + userId);

            SynchronizeContractTag contractTag = new SynchronizeContractTag();
            contractTag.setNotaryOfficeCode(property.getSynchronize_id());
            contractTag.setContractKindCode(property.getContract_kind());
            contractTag.setCode_template(property.getCode_template());
            //add gia tri hop dong
            contractTag.setContractValue(property.getContract_value());
            //get contractKind code
//        ContractKind contractKind=contractKindService.getContractKindById(property.getContract_kind()).orElse(new ContractKind());
//        if(contractKind!=null){
//            contractTag.setContractKindCode(contractKind.getContract_kind_code());
//        }
            contractTag.setContractNumber(property.getContract_number());
            contractTag.setTypeSynchronize(action);
            contractTag.setContractName(property.getContract_name());
            contractTag.setTransactionContent(property.getTransaction_content());
            if(!StringUtils.isBlank(property.getNotary_date())){
//            contractTag.setNotaryDate(RelateDateTime.formatDate(property.getNotary_date(), "dd/MM/yyyy", "yyyy-MM-dd"));
                contractTag.setNotaryDate(RelateDateTime.formatDate1(property.getNotary_date()));
            }
            contractTag.setNotaryOfficeName(property.getNotary_office_name());
            contractTag.setNotaryPerson(property.getNotary_person());
            contractTag.setNotaryPlace(property.getNotary_place());
            contractTag.setNotaryFee(property.getNotary_fee());
            contractTag.setNotaryCostTotal(property.getNotary_cost_total());
            contractTag.setRemunerationCost(property.getRemuneration_cost());
            contractTag.setRelationObjects(property.getRelation_object());
            contractTag.setContractPeriod(property.getContract_period());
            contractTag.setMortageCancelFlag(property.getMortage_cancel_flag());
            contractTag.setMortageCancelDate(property.getMortage_cancel_date());
        /*if(!StringUtils.isBlank(property.getMortage_cancel_date())){
            if(RelateDateTime.checkFormatDDMMYYYY(property.getMortage_cancel_date())){
                contractTag.setMortageCancelDate(RelateDateTime.formatDate(property.getMortage_cancel_date(), "dd/MM/yyyy", "yyyy-MM-dd"));
            }else{
                contractTag.setMortageCancelDate(RelateDateTime.formatDate(property.getMortage_cancel_date(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
            }
        }*/

            contractTag.setMortageCancelNote(property.getMortage_cancel_note());
            contractTag.setCancelStatus(property.getCancel_status());
            contractTag.setCancelDescription(property.getCancel_description());

            if(!StringUtils.isBlank(property.getEntry_date_time())){
                if(RelateDateTime.checkFormatDDMMYYYY(property.getEntry_date_time())){
                    contractTag.setEntryDateTime(RelateDateTime.formatDate(property.getEntry_date_time(), "dd/MM/yyyy", "yyyy-MM-dd"));
                }else{
                    contractTag.setEntryDateTime(RelateDateTime.formatDate(property.getEntry_date_time(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
                }
            }

            String name = "";
            if(users.get().get(0).getFamily_name() != null) {
                name = name + users.get().get(0).getFamily_name();
            }
            if(users.get().get(0).getFirst_name() != null) {
                if(name.equals("")) {
                    name = users.get().get(0).getFirst_name();
                } else {
                    name = name + " " + users.get().get(0).getFirst_name();
                }
            }
            if(name.equals("")) {
                name = users.get().get(0).getUpdate_user_name() == null ? "":users.get().get(0).getUpdate_user_name();
            }
            if(name.equals("")) {
                name = users.get().get(0).getEntry_user_name() == null ? "":users.get().get(0).getEntry_user_name();
            }
            contractTag.setEntryUserName(name + "(" + users.get().get(0).getAccount() + ")");
            contractTag.setUpdateUserName(name + "(" + users.get().get(0).getAccount() + ")");

            if(!StringUtils.isBlank(property.getUpdate_date_time())){
                if(RelateDateTime.checkFormatDDMMYYYY(property.getEntry_date_time())){
                    contractTag.setUpdateDateTime(RelateDateTime.formatDate(property.getUpdate_date_time(), "dd/MM/yyyy", "yyyy-MM-dd"));
                }else{
                    contractTag.setUpdateDateTime(RelateDateTime.formatDate(property.getUpdate_date_time(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
                }
            }


            contractTag.setBankCode(property.getBank_code());
            contractTag.setContractNote(property.getNote());
            contractTag.setPropertyInfo(property.getProperty_info());
            contractTag.setJson_property(property.getJson_property());
            contractTag.setJson_person(property.getJson_person());

            return contractTag;
        }catch (Exception e){
            LOGGER.error("Co loi xay ra khi Convert du lieu tu dong bo method: genSynchronizeContractTagByTransDelete"+e.getMessage());
            return new SynchronizeContractTag();
        }
    }

    @Override
    public Boolean synchronizeContractTags(List<SynchronizeContractTag> contractTags){
        SynchonizeContractList contractList = new SynchonizeContractList();
        try{
            contractList.setSynAccount(accessHistoryService.getConfigValue("synchronize_account").orElse(null));
            String synPass = accessHistoryService.getConfigValue("synchronize_password").orElse(null);
            /*contractList.setSynPass(Crypter.EncryptText(synPass));*//*comment by daicq encrypt syn pass*/
            contractList.setSynPass(synPass); /*add by daicq no encrypt syn pass*/
            contractList.setSynchonizeContractList(contractTags);
            JwtRequest beanAcc= new JwtRequest();
            beanAcc.setUsername(accessHistoryService.getConfigValue("synchronize_account").orElse(null));
            beanAcc.setPassword(Crypter.DecryptText(accessHistoryService.getConfigValue("synchronize_password").orElse(null)));

            String token= STPQueryFactory.authenticationSTP(beanAcc.generateJson());

            List<SynchonizeContractKey> results = STPQueryFactory.synchronizeContract(token,contractList.convertToJson());
//            if(results != null && results.size()>0){
//                for (int i=0;i<results.size();i++){
//                    transactionPropertyService.synchronizeOk(results.get(i).getNotaryOfficeCode(), Integer.parseInt(results.get(i).getContractKindCode()), results.get(i).getContractNumber()).orElse(false);
//                }
//            }
            if(results!=null && results.size()>0){
                return true;
            }
            return false;
        }catch (Exception e){
            LOGGER.error("Have error when synchronize contract in ContractServiceImpl.synchronizeContractTags method: "+e.getMessage());
            return false;
        }

    }

    @Override
    public List<ContractCertify> getReportContractCertify(String stringFilter){
        return contractRepository.getReportContractCertify(stringFilter);
    }

    @Override
    public List<ContractStastics> getContractStasticsDrafter(String notaryDateFromFilter, String notaryDateToFilter){
        return contractRepository.getContractStasticsDrafter(notaryDateFromFilter, notaryDateToFilter);
    }

    @Override
    public List<ContractStastics> getContractStasticsNotary(String notaryDateFromFilter, String notaryDateToFilter){
        return contractRepository.getContractStasticsNotary(notaryDateFromFilter, notaryDateToFilter);
    }

    @Override
    public List<ContractStasticsOfWards> getContractStasticsNotaryOfWards(String district_code, String notaryDateFromFilter, String notaryDateToFilter) {
        return contractRepository.getContractStasticsNotaryOfWards(district_code, notaryDateFromFilter, notaryDateToFilter);
    }

    @Override
    public List<ContractStasticsBank> getContractStasticsBank(String notaryDateFromFilter, String notaryDateToFilter){
        return contractRepository.getContractStasticsBank(notaryDateFromFilter, notaryDateToFilter);
    }
    @Override
    public Optional<BigInteger> checkUserExistContract(int userId){
        return contractRepository.checkUserExistContract(userId);
    }
    @Override
    public Long addContractFromBackUpTransaction(com.vn.osp.notarialservices.transaction.dto.TransactionProperty transactionProperty){
        Contract contract = convertContractByTransaction(transactionProperty);
        PropertyBo propertyBo = null;
        if(transactionProperty.getProperty_info() != null && !"".equals(transactionProperty.getProperty_info())){
            propertyBo = convertPropertyBoByTransaction(transactionProperty);
        }
        return contractRepository.addContractFromBackUpTransaction(contractConverter.convert(contract), propertyBo);
    }
    @Override
    public Optional<SalesWrapper> getListSalesReport(String fromDate, String toDate){
        SalesWrapper salesWapper = new SalesWrapper();
        List<SalesByNotarys> saleByNotarys= contractRepository.getSalesByNotary(fromDate,toDate);
        List<SalesByDrafts> salesByDrafts = contractRepository.getSalesByDraft(fromDate,toDate);
        List<SalesByKindContract> salesByKindContracts = contractRepository.getSalesByKindContract(fromDate,toDate);
        salesWapper.setSalesByNotarysList(saleByNotarys);
        salesWapper.setSalesByDraftsList(salesByDrafts);
        salesWapper.setSalesByKindContracts(salesByKindContracts);

        return Optional.of((SalesWrapper)(salesWapper));
    }

    @Override
    public PropertyBo getPropertyByContractId(Long contractId) {
        PropertyBo propertyBo = contractRepository.getPropertyByContractId(contractId);
        return propertyBo;
    }

    public Contract convertContractByTransaction(com.vn.osp.notarialservices.transaction.dto.TransactionProperty transactionProperty){

        Long notaryId = userService.creatUserBackUpSTP();
        Contract contract = new Contract();
        contract.setContract_number(transactionProperty.getContract_number());
        contract.setRelation_object_a(transactionProperty.getRelation_object());
        contract.setRelation_object_b(transactionProperty.getProperty_info());
        contract.setSummary(transactionProperty.getTransaction_content());
        contract.setNotary_date(transactionProperty.getNotary_date());
        contract.setReceived_date(transactionProperty.getNotary_date());
        contract.setContract_value(transactionProperty.getContract_value());
        contract.setContract_template_id(transactionProperty.getCode_template());
        contract.setNotary_id(notaryId);
        contract.setNotary_place(transactionProperty.getNotary_place());
        try{
            contract.setCost_tt91(transactionProperty.getNotary_fee());
        }catch (Exception e){
            LOGGER.info("Have error cost_notary in function convertContractByTransaction at ContractServiceImpl method");
            contract.setCost_tt91(null);
        }
        try{
            contract.setCost_draft(transactionProperty.getRemuneration_cost());
        }catch (Exception e){
            LOGGER.info("Have error cost_notary in function convertContractByTransaction at ContractServiceImpl method");
            contract.setCost_draft(null);
        }
        try{
            contract.setCost_total(transactionProperty.getNotary_cost_total());
        }catch (Exception e){
            LOGGER.info("Have error cost_notary in function convertContractByTransaction at ContractServiceImpl method");
            contract.setCost_total(null);
        }
        contract.setCost_notary_outsite(Long.valueOf(0));
        contract.setCost_other_determine(Long.valueOf(0));
        contract.setNote(transactionProperty.getNote());
        contract.setContract_period(transactionProperty.getContract_period());
        if(transactionProperty.getMortage_cancel_flag() != null){
            contract.setMortage_cancel_flag(transactionProperty.getMortage_cancel_flag());
        }else {
            contract.setMortage_cancel_flag(Long.valueOf(0));
        }
        contract.setMortage_cancel_date(transactionProperty.getMortage_cancel_date());
        contract.setMortage_cancel_note(transactionProperty.getMortage_cancel_note());
        if(transactionProperty.getCancel_status()!=null){
            contract.setCancel_status(transactionProperty.getCancel_status());
        }else {
            contract.setCancel_status(Long.valueOf(0));
        }

        contract.setError_status(Long.valueOf(0));
        contract.setAddition_status(Long.valueOf(0));
        contract.setCancel_description(transactionProperty.getCancel_description());
        contract.setEntry_user_id(transactionProperty.getEntry_user_id());
        contract.setEntry_user_name(transactionProperty.getEntry_user_name());
        contract.setEntry_date_time(convertStringToTimeStamp(transactionProperty.getEntry_date_time()));
        contract.setUpdate_user_id(transactionProperty.getUpdate_user_id());
        contract.setUpdate_user_name(transactionProperty.getUpdate_user_name());
        contract.setUpdate_date_time(convertStringToTimeStamp(transactionProperty.getUpdate_date_time()));
        contract.setBank_code(transactionProperty.getBank_code());
        contract.setBank_name(transactionProperty.getBank_name());
        contract.setJson_person(transactionProperty.getJson_person());
        contract.setJson_property(transactionProperty.getJson_property());

        if(SystemProperties.getProperty("org_type").equals(Constants.ORG_TYPE_PHUONG_XA)) {
            contract.setContract_signer(transactionProperty.getNotary_person());
        }


        return contract;
    }
    public PropertyBo convertPropertyBoByTransaction(com.vn.osp.notarialservices.transaction.dto.TransactionProperty form){
        PropertyBo bo = new PropertyBo();

        if (form.getType() != null && !"".equals(form.getType())){
            bo.setType(form.getType());
        }else {
            bo.setType("99");
        }
        if (form.getProperty_info() != null && !"".equals(form.getProperty_info())){
            bo.setProperty_info(form.getProperty_info());
        }
        if (form.getLand_street() != null && !"".equals(form.getLand_street())){
            bo.setLand_street(form.getLand_street());
        }
        if (form.getLand_district() != null && !"".equals(form.getLand_district())){
            bo.setLand_district(form.getLand_district());
        }
        if (form.getLand_province() != null && !"".equals(form.getLand_province())){
            bo.setLand_province(form.getLand_province());
        }
        if (form.getLand_full_of_area() != null && !"".equals(form.getLand_full_of_area())){
            bo.setLand_full_of_area(form.getLand_full_of_area());
        }
        if(form.getJson_property() != null && !"".equals(form.getJson_property())){
            try {
                String jsonProperty = form.getJson_property().substring(1, form.getJson_property().length() - 2);
                JSONObject searchObject = new JSONObject(jsonProperty);
                if (searchObject.has("owner_info") && !StringUtils.isBlank(searchObject.get("owner_info").toString())) {
                    bo.setOwner_info(searchObject.get("owner_info").toString());
                }
                if (searchObject.has("other_info") && !StringUtils.isBlank(searchObject.get("other_info").toString())) {
                    bo.setOther_info(searchObject.get("other_info").toString());
                }
                if (searchObject.has("land_certificate") && !StringUtils.isBlank(searchObject.get("land_certificate").toString())) {
                    bo.setLand_certificate(searchObject.get("land_certificate").toString());
                }
                if (searchObject.has("land_issue_place") && !StringUtils.isBlank(searchObject.get("land_issue_place").toString())) {
                    bo.setLand_issue_place(searchObject.get("land_issue_place").toString());
                }
                if (searchObject.has("land_map_number") && !StringUtils.isBlank(searchObject.get("land_map_number").toString())) {
                    bo.setLand_map_number(searchObject.get("land_map_number").toString());
                }
                if (searchObject.has("land_number") && !StringUtils.isBlank(searchObject.get("land_number").toString())) {
                    bo.setLand_number(searchObject.get("land_number").toString());
                }
                if (searchObject.has("land_address") && !StringUtils.isBlank(searchObject.get("land_address").toString())) {
                    bo.setLand_address(searchObject.get("land_address").toString());
                }
                if (searchObject.has("land_area") && !StringUtils.isBlank(searchObject.get("land_area").toString())) {
                    bo.setLand_area(searchObject.get("land_area").toString());
                }
                if (searchObject.has("land_public_area") && !StringUtils.isBlank(searchObject.get("land_public_area").toString())) {
                    bo.setLand_public_area(searchObject.get("land_public_area").toString());
                }
                if (searchObject.has("land_use_purpose") && !StringUtils.isBlank(searchObject.get("land_use_purpose").toString())) {
                    bo.setLand_use_purpose(searchObject.get("land_use_purpose").toString());
                }
                if (searchObject.has("land_use_period") && !StringUtils.isBlank(searchObject.get("land_use_period").toString())) {
                    bo.setLand_use_period(searchObject.get("land_use_period").toString());
                }
                if (searchObject.has("land_use_origin") && !StringUtils.isBlank(searchObject.get("land_use_origin").toString())) {
                    bo.setLand_use_origin(searchObject.get("land_use_origin").toString());
                }
                if (searchObject.has("land_associate_property") && !StringUtils.isBlank(searchObject.get("land_associate_property").toString())) {
                    bo.setLand_associate_property(searchObject.get("land_associate_property").toString());
                }
                if (searchObject.has("car_license_number") && !StringUtils.isBlank(searchObject.get("car_license_number").toString())) {
                    bo.setCar_license_number(searchObject.get("car_license_number").toString());
                }
                if (searchObject.has("car_regist_number") && !StringUtils.isBlank(searchObject.get("car_regist_number").toString())) {
                    bo.setOwner_info(searchObject.get("car_regist_number").toString());
                }
                if (searchObject.has("car_issue_place") && !StringUtils.isBlank(searchObject.get("car_issue_place").toString())) {
                    bo.setCar_regist_number(searchObject.get("car_issue_place").toString());
                }
                if (searchObject.has("car_frame_number") && !StringUtils.isBlank(searchObject.get("car_frame_number").toString())) {
                    bo.setCar_frame_number(searchObject.get("car_frame_number").toString());
                }
                if (searchObject.has("car_machine_number") && !StringUtils.isBlank(searchObject.get("car_machine_number").toString())) {
                    bo.setCar_machine_number(searchObject.get("car_machine_number").toString());
                }

                if (searchObject.has("land_issue_date") && !StringUtils.isBlank(searchObject.get("land_issue_date").toString())) {
                    bo.setLand_issue_date(convertStringToTimeStampFull(searchObject.get("land_issue_date").toString()));
                }
                if (searchObject.has("car_issue_date") && !StringUtils.isBlank(searchObject.get("car_issue_date").toString())) {
                    bo.setCar_issue_date(convertStringToTimeStampFull(searchObject.get("car_issue_date").toString()));
                }

            }catch (Exception e){
                System.out.println(" loi tai convertPropertyBoByTransaction: " + e.getMessage());
            }
        }

        return bo;
    }
    public Timestamp convertStringToTimeStamp(String dateString){
        try {
            if(dateString == "" || dateString == null) return null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = null;
            parsedDate = dateFormat.parse(dateString);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return  timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Timestamp convertStringToTimeStampFull(String dateString){
        try {
            if(dateString == "" || dateString == null) return null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = null;
            parsedDate = dateFormat.parse(dateString);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return  timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //sonnv
    @Override
    public Boolean deleteContract(List<SynchronizeContractTag> contractTags){
        SynchonizeContractList contractList = new SynchonizeContractList();
        try{
            contractList.setSynAccount(accessHistoryService.getConfigValue("synchronize_account").orElse(null));
            String synPass = accessHistoryService.getConfigValue("synchronize_password").orElse(null);
            contractList.setSynPass(Crypter.EncryptText(synPass));
            contractList.setSynchonizeContractList(contractTags);
            String token=null;
            JwtRequest beanAcc= new JwtRequest();
            beanAcc.setUsername(accessHistoryService.getConfigValue("synchronize_account").orElse(null));
            beanAcc.setPassword(Crypter.DecryptText(accessHistoryService.getConfigValue("synchronize_password").orElse(null)));

            token= STPQueryFactory.authenticationSTP(beanAcc.generateJson());
            List<SynchonizeContractKey> results = STPQueryFactory.deleteContract(token,contractList.convertToJson());

            if(results!=null && results.size()>0){
                return true;
            }
            return false;
        }catch (Exception e){
            LOGGER.error("Have error when synchronize contract in ContractServiceImpl.deleteContract method: "+e.getMessage());
            return false;
        }

    }
}
