//read file new


package com.vn.osp.notarialservices.contract.util;

import com.vn.osp.notarialservices.contract.dto.Contract;
import com.vn.osp.notarialservices.contractKeyMap.domain.ContractKeyMapBO;
import com.vn.osp.notarialservices.contractKeyMap.dto.ContractReadFileDoc;
import com.vn.osp.notarialservices.contractKeyMap.service.ContractKeyMapService;
import com.vn.osp.notarialservices.contracttemplate.dto.ContractTemp;
import com.vn.osp.notarialservices.contracttemplate.service.ContractTempService;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.Converter;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese.ConverterFactory;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese.ConverterFactoryImpl;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese.VietnameseEncoding;
import com.vn.osp.notarialservices.systemmanager.service.AccessHistoryService;
import com.vn.osp.notarialservices.user.dto.User;
import com.vn.osp.notarialservices.user.service.UserService;
import com.vn.osp.notarialservices.utils.Constants;
import com.vn.osp.notarialservices.utils.SystemProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DissectFile {
    private final ContractTempService contractTempService;
    private final UserService userService;
    private final ContractKeyMapService contractKeyMapService;
    private final AccessHistoryService accessHistoryService;

    private static final Logger LOG = Logger.getLogger(DissectFile.class);

    public DissectFile(final ContractTempService contractTempService, final UserService userService,
                       final ContractKeyMapService contractKeyMapService, final AccessHistoryService accessHistoryService) {
        this.contractTempService = contractTempService;
        this.userService = userService;
        this.contractKeyMapService = contractKeyMapService;
        this.accessHistoryService = accessHistoryService;
    }


    //tai san
    private JSONObject apartment = new JSONObject();
    private JSONObject land = new JSONObject();
    private JSONObject vehicle = new JSONObject();
    private JSONObject vehicle_airplane = new JSONObject();
    private JSONObject vehicle_ship = new JSONObject();
    private JSONObject objectTaiSan = new JSONObject();
    private JSONArray arrayPropertíes = new JSONArray();
    private JSONObject objectPropertyMoiLoai = new JSONObject();
    private String thisLine = "";
    private int indexProperty = 1;

    private int indexLine = 0;

    //duong su
    JSONArray jsonArrayDuongSu = new JSONArray();
    JSONObject objectDuongSu = new JSONObject();
    JSONObject objectMoiBenDuongSu = new JSONObject();
    JSONObject objectMoiDuongSu = new JSONObject();
    JSONArray jsonArrayDuongsuMoiBen = new JSONArray();

    List<String> fileData = new ArrayList<>();
    List<ContractKeyMapBO> specialKeys = new ArrayList<>();

    int flg_nhanDienKeyStartDuongSu = 0;
    int flg_nhanDienKeyEndDuongSu = 0;
    int flg_nhanDienKeyStartTaiSan = 0;//muc dich de neu word k nhan dien dc key bat dau tai san thi tra ve thong bao: hẹ thong k nhan dien dc key bat dau de ng dung biet
    int flg_nhanDienKeyEndTaiSan = 0;//tuong tu key tren

    List<ContractTemp> listContractName = new ArrayList<>();
    String queryGetContractName = "";
    Long contract_template_id = 0l;
    String contractNameInWordFile = null;

    public ContractReadFileDoc read(File file, Contract contract, int typeContract, int typeOfFormatDoc) throws IOException {
        JSONObject jsonObjectResult = new JSONObject();
        ContractReadFileDoc contractReadFileDoc = new ContractReadFileDoc();
        try {
            specialKeys = contractKeyMapService.getContractKeyMapByFilter(" where type = " + Constants.TYPE_SPECIAL_WORD);
            fileData = readAndGetListParagraph(file, typeOfFormatDoc);

            listContractName = readContractName();
            objectDuongSu = readDuongSu();
            if (objectDuongSu.length() != 0) {//check objecDuongSu is empty
                contract.setJson_person("'" + objectDuongSu.toString() + "'");
            }
            objectTaiSan = readTaiSan();
            if (objectTaiSan.length() != 0) {//check objectTaiSan is empty
                contract.setJson_property("'" + objectTaiSan.toString() + "'");
            }
            // Long contract_template_id = readContractName();
            if (contract_template_id == 0 && !listContractName.isEmpty())
                contract_template_id = Long.valueOf(listContractName.get(0).getCode_template());
            contract.setContract_template_id(contract_template_id != 0 ? contract_template_id : null);

            contract = readThongTinKhac(contract);
            if (typeContract == Constants.TYPE_READ_WORD) {
                if (objectDuongSu.length() != 0) {//check objecDuongSu is empty
                    contract.setJson_person(objectDuongSu.toString());
                }
                objectTaiSan = readTaiSan();
                if (objectTaiSan.length() != 0) {//check objectTaiSan is empty
                    contract.setJson_property(objectTaiSan.toString());
                }
            }

            contractReadFileDoc.setContract(contract);
            contractReadFileDoc.setFlg_nhanDienKeyStartDuongSu(flg_nhanDienKeyStartDuongSu);
            contractReadFileDoc.setFlg_nhanDienKeyEndDuongSu(flg_nhanDienKeyEndDuongSu);
            contractReadFileDoc.setFlg_nhanDienKeyStartTaiSan(flg_nhanDienKeyStartTaiSan);
            contractReadFileDoc.setFlg_nhanDienKeyEndTaiSan(flg_nhanDienKeyEndTaiSan);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Lỗi tại hàm DissectFile.red(): " + e.getMessage());
        }

        return contractReadFileDoc;
    }

    int type_taiSanDetectInContractName = 0;

    public List<ContractTemp> readContractName() {
        int flg_getTenHD = 0;
        for (int i = indexLine; i < fileData.size(); i++) {
            thisLine = removeSpecialInLine(fileData.get(i)).trim();
            if (!thisLine.isEmpty()) {
                Font.getFont(thisLine);
            }
            if (thisLine.contains(Constants.TEN_HĐ_START)) {
                flg_getTenHD = 1;
                continue;
                //process for ten hop dong
            }
            if (flg_getTenHD == 1 && !thisLine.trim().isEmpty()) {
                if (thisLine.contains("----")) {
                    continue;
                } else {
                    String tenHD = thisLine.trim();
                    contractNameInWordFile = tenHD;
                    if (tenHD.toLowerCase().contains(" xe ")) {//thuoc loai phuong tien van tai
                        type_taiSanDetectInContractName = Constants.TYPE_TAI_SAN_OTO_XEMAY;
                    }
                    //process get ten hop dong
                    //call service to get template_id by name
                    //select bang contract_template theo name
                    queryGetContractName += " and name like '%" + tenHD + "%'";
                    Optional<List<ContractTemp>> contractTempBos = contractTempService.findContractTempByFilter(" where 1=1" + queryGetContractName);
                    if (contractTempBos.isPresent() && !contractTempBos.get().isEmpty()) {
                        Optional<ContractTemp> findInListByName = contractTempBos.get().stream().
                                filter(c -> c.getName().equalsIgnoreCase(tenHD)).
                                findFirst();
                        if (findInListByName.isPresent()) {
                            contract_template_id = Long.valueOf(findInListByName.get().getCode_template());
                        } else {
                            listContractName = contractTempBos.get();
                        }

                    }
                    /*if (contractTempBos.isPresent() && !contractTempBos.get().isEmpty())
                        contract_template_id = Long.valueOf(contractTempBos.get().get(0).getCode_template());*/
                    flg_getTenHD = 0;
                }

            }
        }
        return listContractName;
    }


    public JSONObject readDuongSu() {
        List<ContractKeyMapBO> listKeyDuongSu = contractKeyMapService.getContractKeyMapByFilter(" where type = " + Constants.TYPE_DUONG_SU);
        //order list theo loại key (bat dau/ket thuc/key thuong) sau do order tiep theo map_var. Mục đích để nhóm loại var cho dễ xử lý
        listKeyDuongSu = listKeyDuongSu.stream().sorted(Comparator.comparing(ContractKeyMapBO::getBegin_or_close).reversed().thenComparing(ContractKeyMapBO::getMap_var)).collect(Collectors.toList());

        int flg_duongSu = 0;
        int index = 0;
        try {


            for (int j = indexLine; j < fileData.size(); j++) {
                if (flg_duongSu == 2) {
                    indexLine = j - 1;
                    break;
                }

                thisLine = removeSpecialInLine(fileData.get(j));
                if(thisLine.contains("Hai bên tự nguyện cùng lập")){
                    System.out.println("DaiCQ");
                }
                String mapvarAddedDataInThisLine = null;// định nghĩa var đã dc thêm trên dòng này, để key sau k cần duyệt nữa, tránh trường hợp key sau trùng sẽ bị lỗi
                LOG.info("line is " + thisLine);
                for (int i = 0; i < listKeyDuongSu.size(); i++) {
                    ContractKeyMapBO thisField = listKeyDuongSu.get(i);
                    String key = thisField.getKey_name();
                    if(key.contains("Hai bên tự nguyện cùng lập")){
                        System.out.println("DaiCQ 2");
                    }
                    int indexOfFirstWord = thisLine.indexOf(thisField.getFirst_word());
                    int indexOfEndWord =  thisLine.indexOf(thisField.getEnd_word());
                    if (thisLine.contains(key) && thisLine.contains(thisField.getFirst_word())
                            && thisLine.contains(thisField.getEnd_word()) ) {//check key va next key co trung nhau k? neu k thi k add vao key nay tranh TH k tim dc index nextKey

                        //Nếu index endword nhỏ hơn hoặc bừng index firstword -> thoát
                        if(indexOfEndWord > 0 && (indexOfEndWord <= indexOfFirstWord)) continue;

                        if (!thisField.getEnd_word().equals("")) {
                            if (!thisLine.contains(thisField.getEnd_word())) {
                                continue;
                            }
                        } else {
                            List<ContractKeyMapBO> listThisKey = listKeyDuongSu.stream()                // convert list to stream
                                    .filter(contractKeysMap -> key.equals(contractKeysMap.getKey_name()) && !contractKeysMap.getEnd_word().equals("")
                                    )
                                    .collect(Collectors.toList());
                            int flg_break = 0;
                            for (ContractKeyMapBO contractKeyMapBO : listThisKey) {
                                if (thisLine.contains(contractKeyMapBO.getEnd_word()) && !Objects.equals(contractKeyMapBO, thisField)) {
                                    //thuc hien so sanh de xac dinh xem object nay co phai la objec dang duyet list fields k?
                                    flg_break = 1;
                                    break;
                                }
                            }
                            if (flg_break == 1) {
                                continue;
                            }
                        }

                        if (thisField.getBegin_or_close() == Constants.TYPE_START_DUONG_SU_OR_TAI_SAN) {
                            flg_nhanDienKeyStartDuongSu = 1;
                            index++;
                            if (flg_duongSu == 0) {//chua them du lieu duong su
                                objectMoiBenDuongSu.put("name", thisLine.substring(thisLine.toLowerCase().indexOf(Constants.TEN_BEN.toLowerCase()) + Constants.TEN_BEN.length(), thisLine.indexOf(')')).trim());
                                objectMoiBenDuongSu.put("action", thisLine.substring(thisLine.toLowerCase().indexOf(Constants.VAI_TRO_BEN.toLowerCase()) + Constants.VAI_TRO_BEN.length(), thisLine.indexOf('(')).trim());

                            } else if (flg_duongSu == 1) {//dang thuc hien them du lieu duong su
                                //thuc hien them dau du cac truong trong json object duong su
                                objectMoiDuongSu = addFullKeyForObjectDuongSu(objectMoiDuongSu);
                                //them object moi duong su vao array moi ben duong su
                                jsonArrayDuongsuMoiBen.put(objectMoiDuongSu);
                                //thuc hien them object duong su da them truoc do vao.
                                objectMoiBenDuongSu.put("persons", jsonArrayDuongsuMoiBen);
                                //sau khi them object moi ben duong su, thuc hien add duong su nay vao array duongn su
                                jsonArrayDuongSu.put(objectMoiBenDuongSu);
                                //sau khi add duong su nay vao array, refresh lai thong tin moi ben duongn su de tiep tuc add
                                objectMoiBenDuongSu = new JSONObject();
                                objectMoiBenDuongSu.put("name", thisLine.substring(thisLine.toLowerCase().indexOf(Constants.TEN_BEN.toLowerCase()) + Constants.TEN_BEN.length(), thisLine.indexOf(')')));
                                objectMoiBenDuongSu.put("action", thisLine.substring(thisLine.toLowerCase().indexOf(Constants.VAI_TRO_BEN.toLowerCase()) + Constants.VAI_TRO_BEN.length() + 1, thisLine.indexOf('(')).trim());
                                objectMoiDuongSu = new JSONObject();
                                jsonArrayDuongsuMoiBen = new JSONArray();
                            }
                            objectMoiBenDuongSu.put("id", index);
                            flg_duongSu = 1;

                            break;
                        } else if (thisField.getBegin_or_close() == Constants.TYPE_END_DUONG_SU_OR_TAI_SAN && flg_duongSu == 1) {
                            flg_nhanDienKeyEndDuongSu = 1;
                            //thuc hien them dau du cac truong trong json object duong su
                            objectMoiDuongSu = addFullKeyForObjectDuongSu(objectMoiDuongSu);
                            jsonArrayDuongsuMoiBen.put(objectMoiDuongSu);
                            objectMoiBenDuongSu.put("persons", jsonArrayDuongsuMoiBen);
                            jsonArrayDuongSu.put(objectMoiBenDuongSu);
                            objectDuongSu.put("name", "Đương sự");
                            objectDuongSu.put("privy", jsonArrayDuongSu);

                            /*ket thuc duong su, đóng luồng đọc file phần đương sự để return;*/
                            flg_duongSu = 2;

                        } else {//khong phai la tu dinh nghia cac ben duong su.(co the la cac thuoc tinh ben trong khoi duong su hoac khong.)

                            //nếu tại dòng này đã đọc dc dữ liệu cho biến var này rồi thì bỏ qua key này
                            if (mapvarAddedDataInThisLine != null && thisField.getMap_var().equals(mapvarAddedDataInThisLine)) {
                                continue;
                            }

                            if (flg_duongSu == 1) {
                                //neu trong object cua duong su nay da co key dau tien nay thi chung to duong su nay da ton tai.
                                if (objectMoiDuongSu.has(thisField.getMap_var())) {
                                    //thuc hien them dau du cac truong trong json object duong su
                                    objectMoiDuongSu = addFullKeyForObjectDuongSu(objectMoiDuongSu);
                                    //day du lieu cua object nay vao jsonArrayDuongSuA
                                    jsonArrayDuongsuMoiBen.put(objectMoiDuongSu);
                                    //sau khi day len, tiep tuc chuyen lai jsonObjDuLieuTungDuongSuA ve null de tiep tuc them vao neu co du lieu.
                                    objectMoiDuongSu = new JSONObject();
                                }

                                if (thisField.getMap_var().equals("name")) {
                                    JSONObject tempSex = new JSONObject();
                                    if (thisField.getKey_name().equalsIgnoreCase("Ông") || thisField.getKey_name().equalsIgnoreCase("cùng chồng là ông")) {
                                        tempSex.put("id", 1);
                                        tempSex.put("name", "Ông");
                                    } else if (thisField.getKey_name().equalsIgnoreCase("Bà") || thisField.getKey_name().equalsIgnoreCase("cùng vợ là bà")) {
                                        tempSex.put("id", 2);
                                        tempSex.put("name", "Bà");
                                    }
                                    objectMoiDuongSu.put("sex", tempSex);
                                }

                                //check: neu key la ten cong ty => thuong cac mau hop dong bat dau la ten cong ty thi la bat dau duong su moi
                                if (thisField.getMap_var().equals("org_name ")) {
                                    //thuc hien them dau du cac truong trong json object duong su
                                    objectMoiDuongSu = addFullKeyForObjectDuongSu(objectMoiDuongSu);
                                    //day du lieu cua object nay vao jsonArrayDuongSuA
                                    jsonArrayDuongsuMoiBen.put(objectMoiDuongSu);
                                    //sau khi day len, tiep tuc chuyen lai jsonObjDuLieuTungDuongSuA ve null de tiep tuc them vao neu co du lieu.
                                    objectMoiDuongSu = new JSONObject();
                                }
                                objectMoiDuongSu.put(thisField.getMap_var(), getValueDuongSu(thisField, thisLine));

                                mapvarAddedDataInThisLine = thisField.getMap_var();
                            }

                        }

                    }
                }
            }
            LOG.info("json result duong su is " + objectDuongSu.toString());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        return objectDuongSu;
    }

    int flg_batDauTaiSan = 0;//0: chua bat dau doc tai san; 1: dang doc tai san; 2: k doc tai san

    public JSONObject readTaiSan() {

        List<ContractKeyMapBO> listKeyTaiSan = contractKeyMapService.getContractKeyMapByFilter(" where type = " + Constants.TYPE_TAI_SAN);
        int type_TaiSan = 0;
        int flg_taiSan = 0;//0: chua them tai san, 1: dang them tai san. 2: da them xong tai san.
        int flg_moiTaiSan = 0;//0:chua tao, 1: dang tien hanh them dl, 2: da them xong

        //  listKeyTaiSan.sort(Comparator.comparing(ContractKeyMapBO::getBegin_or_close));
        //  sap xep de duyet key bat dau va ket thuc truoc
        listKeyTaiSan = listKeyTaiSan.stream().sorted(Comparator.comparing(ContractKeyMapBO::getBegin_or_close).reversed()).collect(Collectors.toList());
        if (indexLine > 0) indexLine -= 1;
        for (int j = indexLine; j < fileData.size(); j++) {
            if (flg_taiSan == 2) {
                indexLine = j - 1;
                break;
            }

            thisLine = removeSpecialInLine(fileData.get(j));
            LOG.info("this line is: " + thisLine);
            for (int k = 0; k < listKeyTaiSan.size(); k++) {
                ContractKeyMapBO thisField = listKeyTaiSan.get(k);

                if (flg_taiSan == 2) {
                    break;
                }
                if (thisField.getBegin_or_close() == 0 && thisField.getType_property() != type_TaiSan) {
                    continue;
                }

                if (thisLine.contains(thisField.getKey_name()) && thisLine.contains(thisField.getFirst_word()) && thisLine.contains(thisField.getEnd_word())) {

                    if (!thisField.getEnd_word().equals("")) {
                        if (!thisLine.contains(thisField.getEnd_word())) {
                            continue;
                        }
                    } else {
                        List<ContractKeyMapBO> listThisKey = listKeyTaiSan.stream()                // convert list to stream
                                .filter(contractKeysMap -> thisField.getKey_name().equals(contractKeysMap.getKey_name()) && !contractKeysMap.getEnd_word().equals("")
                                )
                                .collect(Collectors.toList());
                        int flg_break = 0;
                        for (ContractKeyMapBO contractKeyMapBO : listThisKey) {
                            if (thisLine.contains(contractKeyMapBO.getEnd_word())
                                    && !Objects.equals(contractKeyMapBO, thisField)) {
                                //thuc hien so sanh de xac dinh xem object nay co phai la objec dang duyet list fields k?
                                flg_break = 1;
                                break;
                            }
                        }
                        if (flg_break == 1) {
                            continue;
                        }
                    }


                    if (thisField.getBegin_or_close() == Constants.TYPE_START_DUONG_SU_OR_TAI_SAN) {
                        flg_nhanDienKeyStartTaiSan = 1;
                        if (type_taiSanDetectInContractName != 0) {//check type cua key nay
                            if (thisField.getType_property() != type_taiSanDetectInContractName) {
                                continue;
                            } else
                                type_taiSanDetectInContractName = 0;//refrest lai type tsDaDetech de lan duyet sau k nhay vao nua
                        }
                        type_TaiSan = thisField.getType_property().intValue();

                        //get contract name
                        if (contract_template_id == 0 && listContractName.size() > 1) {
                            if (type_TaiSan == Constants.TYPE_TAI_SAN_DAT) {
                                queryGetContractName += " and name like '%đất%'";

                            } else if (type_TaiSan == Constants.TYPE_TAI_SAN_DAT_TS_GLVD) {
                                queryGetContractName += " and name like '%tài sản gắn liền với đất%'";
                            } else if (type_TaiSan == Constants.TYPE_TAI_SAN) {
                                queryGetContractName += " and name like '%tài sản khác%'";
                            } else if (type_TaiSan == Constants.TYPE_TAI_SAN_OTO_XEMAY) {
                                queryGetContractName += " and name like '%phương tiện vận tải%'";
                            } else if (type_TaiSan == Constants.TYPE_TAI_SAN_MAU_CHUNG_CU) {
                                queryGetContractName += " and name like '%căn hộ, nhà chung cư%'";
                            }
                            Optional<List<ContractTemp>> contractTempBos_ = contractTempService.findContractTempByFilter(" where 1=1" + queryGetContractName);
                            if (contractTempBos_.isPresent() && !contractTempBos_.get().isEmpty()) {
                                Optional<ContractTemp> findInListByName = contractTempBos_.get().stream().
                                        filter(c -> c.getName().equalsIgnoreCase(contractNameInWordFile)).
                                        findFirst();
                                if (findInListByName.isPresent()) {
                                    contract_template_id = Long.valueOf(findInListByName.get().getCode_template());
                                } else {
                                    listContractName = contractTempBos_.get();
                                }

                            }
                        }

                        if (flg_moiTaiSan != 2) {//chua bi dong
                            //check to put data of old object and create new object moi loai tai san
                            if (flg_moiTaiSan == 1) {
                                //tien hanh put object de tao object moi
                                putData2();
                            }
                            objectPropertyMoiLoai = createObjectMoiLoaiTaiSan(type_TaiSan);
                            flg_moiTaiSan = 1;
                        }

                    } else if (thisField.getBegin_or_close() == Constants.TYPE_END_DUONG_SU_OR_TAI_SAN) {
                        flg_nhanDienKeyEndTaiSan = 1;
                        //thuc hien put data va dong tien trinh them tai san.
                        putData2();
                        objectTaiSan.put("name", "property");
                        objectTaiSan.put("properties", arrayPropertíes);
                        flg_moiTaiSan = 2;
                        flg_taiSan = 2;
                        break;

                    } else {
                        if (flg_moiTaiSan == 1 && thisField.getType_property() == type_TaiSan) {
                            //tien hanh them du lieu vao object loai tai san dang them
                            objectPropertyMoiLoai = addValue2(objectPropertyMoiLoai, thisField);
                        }
                    }
                }
            }
        }
        LOG.info("object result tai san is " + objectTaiSan);

        return objectTaiSan;
    }

    public JSONObject addValue2(JSONObject objectPropertyMoiLoai, ContractKeyMapBO field) {
        int fix = field.getMap_var().indexOf('.');
        String[] check_Key = new String[2];
        if (fix > 0) {
            check_Key[0] = field.getMap_var().substring(0, fix);
            check_Key[1] = field.getMap_var().substring(fix + 1);
        }

        String valueDissected = getValueTaiSan2(field, thisLine);
        if (valueDissected == null) return objectPropertyMoiLoai;

        if (fix > 0 && check_Key.length > 1 && check_Key[0].equals("land")) {

            land.put(check_Key[1], getValueTaiSan2(field, thisLine));
        } else if (fix > 0 && check_Key.length > 1 && check_Key[0].equals("apartment")) {

            apartment.put(check_Key[1], getValueTaiSan2(field, thisLine));
        } else if (fix > 0 && check_Key.length > 1 && check_Key[0].equals("vehicle")) {

            vehicle.put(check_Key[1], getValueTaiSan2(field, thisLine));
        }
        else if (fix > 0 && check_Key.length > 1 && check_Key[0].equals("vehicle_airplane")) {

            vehicle_airplane.put(check_Key[1], getValueTaiSan2(field, thisLine));
        }
        else if (fix > 0 && check_Key.length > 1 && check_Key[0].equals("vehicle_ship")) {

            vehicle_ship.put(check_Key[1], getValueTaiSan2(field, thisLine));
        }
        else {

            objectPropertyMoiLoai.put(field.getMap_var(), getValueTaiSan2(field, thisLine));
        }
        return objectPropertyMoiLoai;
    }


    public String getValueTaiSan2(ContractKeyMapBO fileModel, String line) {
        String result = "";
        try {
            int indexLastWordOfKey = line.indexOf(fileModel.getKey_name()) + fileModel.getKey_name().length();
            int indexOfKeyAndFirstWord = indexLastWordOfKey;
            int indexOfEndWord = 0;
            if (!fileModel.getFirst_word().equals("")) {
                List<Integer> listIndexOfFirstWord = findListIndexOfWord(line, fileModel.getFirst_word());
                Collections.sort(listIndexOfFirstWord);
                for (Integer index : listIndexOfFirstWord) {
                    if (index > (indexLastWordOfKey - 1)) {
                        indexOfKeyAndFirstWord = index + fileModel.getFirst_word().length();
                        break;
                    }
                }
            }
            if (!fileModel.getEnd_word().equals("")) {
                List<Integer> listIndexOfEndWord = findListIndexOfWord(line, fileModel.getEnd_word());
                Collections.sort(listIndexOfEndWord);
                for (Integer index : listIndexOfEndWord) {
                    if (index > indexOfKeyAndFirstWord) {
                        indexOfEndWord = index;
                        break;
                    }
                }
            }

            if (fileModel.getEnd_word().equals("")) {
                result = line.substring(indexOfKeyAndFirstWord).trim();
            } else {
                if (indexOfEndWord < indexOfKeyAndFirstWord) return null;
                else result = line.substring(indexOfKeyAndFirstWord, indexOfEndWord).trim();
            }
            //replace land_area from '.' to ''
            if (result.length() > 0 && (fileModel.getMap_var().equals("land.land_area")
                    || fileModel.getMap_var().equals("land.land_private_area")
                    || fileModel.getMap_var().equals("land.land_public_area")
                    || fileModel.getMap_var().equals("land.construction_area")
                    || fileModel.getMap_var().equals("land.building_area"))) {
                for (int i = result.length() - 1; i >= result.length() - 3; i--) {
                    if (result.charAt(i) == ',') {
                        String result0 = result.substring(0, i);
                        String result1 = result.substring(i + 1);
                        result0 = result0.replace(".", ",");
                        result = result0 + "." + result1;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("loi get data file word tai dong ham DessectFile.getValueTaiSan2(): " + line);
            e.printStackTrace();
        }

        return result;
    }

    public JSONObject createObjectMoiLoaiTaiSan(int type_taiSan) {
        if (type_taiSan == Constants.TYPE_TAI_SAN_DAT) {//is tai san dat
            objectPropertyMoiLoai.put("id", indexProperty);
            objectPropertyMoiLoai.put("type", "01");
            objectPropertyMoiLoai.put("template", Constants.TYPE_TAI_SAN_DAT);

        }

        if (type_taiSan == Constants.TYPE_TAI_SAN_KHAC) {//is tai san khac
            objectPropertyMoiLoai.put("id", indexProperty);
            objectPropertyMoiLoai.put("type", "99");
            objectPropertyMoiLoai.put("template", Constants.TYPE_TAI_SAN_KHAC);

        }

        if (type_taiSan == Constants.TYPE_TAI_SAN_OTO_XEMAY) {//tai san oto xe may
            objectPropertyMoiLoai.put("id", indexProperty);
            objectPropertyMoiLoai.put("type", "02");
            objectPropertyMoiLoai.put("template", Constants.TYPE_TAI_SAN_OTO_XEMAY);

        }
        if (type_taiSan == Constants.TYPE_TAI_SAN_MAU_CHUNG_CU) {//tai san mau chung cu
            objectPropertyMoiLoai.put("id", indexProperty);
            objectPropertyMoiLoai.put("type", "01");
            objectPropertyMoiLoai.put("template", Constants.TYPE_TAI_SAN_MAU_CHUNG_CU);
        }
        if (type_taiSan == Constants.TYPE_TAI_SAN_DAT_TS_GLVD) {//dat va tsglvd
            objectPropertyMoiLoai.put("id", indexProperty);
            objectPropertyMoiLoai.put("type", "01");
            objectPropertyMoiLoai.put("template", Constants.TYPE_TAI_SAN_DAT_TS_GLVD);
        }
        return objectPropertyMoiLoai;
    }


    public void putData2() {

        //create full key of each objects
        if (objectPropertyMoiLoai != null)
            objectPropertyMoiLoai = createFullKeyForEachObjectTaiSan(objectPropertyMoiLoai);
        objectPropertyMoiLoai.put("land", land != null ? land : new JSONObject());
        objectPropertyMoiLoai.put("apartment", apartment != null ? apartment : new JSONObject());
        objectPropertyMoiLoai.put("vehicle", vehicle != null ? vehicle : new JSONObject());
        objectPropertyMoiLoai.put("vehicle_airplane", vehicle_airplane != null ? vehicle_airplane : new JSONObject());
        objectPropertyMoiLoai.put("vehicle_ship", vehicle_ship != null ? vehicle_ship : new JSONObject());
        objectPropertyMoiLoai.put("myProperty", true);

        boolean objectPropertyIsEmptyValue = isEmptyObjectProperty(objectPropertyMoiLoai);
        if(!objectPropertyIsEmptyValue) {
            arrayPropertíes.put(objectPropertyMoiLoai);
            indexProperty += 1;
        }
        land = new JSONObject();
        vehicle = new JSONObject();
        apartment = new JSONObject();
        vehicle_airplane = new JSONObject();
        vehicle_ship = new JSONObject();
        objectPropertyMoiLoai = new JSONObject();

    }

    /**Hàm check 1 object tài sản có tồn tại trường có dữ liệu hay không*/
    public boolean isEmptyObjectProperty(JSONObject objectTaiSan){
       boolean result = true;
        JSONObject land = (JSONObject) objectTaiSan.get("land");
        JSONObject apartment = (JSONObject) objectTaiSan.get("apartment");
        JSONObject vehicle = (JSONObject) objectTaiSan.get("vehicle");
        JSONObject vehicle_airplane = (JSONObject) objectTaiSan.get("vehicle_airplane");
        JSONObject vehicle_ship = (JSONObject) objectTaiSan.get("vehicle_ship");
        Iterator<String> keysLand = land.keys();
        Iterator<String> keysApartment = apartment.keys();
        Iterator<String> keysVehicle = vehicle.keys();
        Iterator<String> keysVehicleAirplane = vehicle_airplane.keys();
        Iterator<String> keysVehicleShip = vehicle_ship.keys();

        while (keysLand.hasNext()){
            String key = keysLand.next();
            if (!StringUtils.isBlank(land.getString(key))) {
                return false;
            }
        }
        while (keysApartment.hasNext()){
            String key = keysApartment.next();
            if (!StringUtils.isBlank(apartment.getString(key))) {
                return false;
            }
        }
        while (keysVehicle.hasNext()){
            String key = keysVehicle.next();
            if (!StringUtils.isBlank(vehicle.getString(key))) {
                return false;
            }
        }
        while (keysVehicleAirplane.hasNext()){
            String key = keysVehicleAirplane.next();
            if (!StringUtils.isBlank(vehicle_airplane.getString(key))) {
                return false;
            }
        }
        while (keysVehicleShip.hasNext()){
            String key = keysVehicleShip.next();
            if (!StringUtils.isBlank(vehicle_ship.getString(key))) {
                return false;
            }
        }

        return true;
    }

    public JSONObject createFullKeyForEachObjectTaiSan(JSONObject obj) {
        List<String> landKeys = Arrays.asList("land_certificate", "land_issue_place", "land_issue_date", "land_map_number", "land_number",
                "land_address", "land_area", "land_area_text", "land_public_area", "land_private_area",
                "land_use_purpose", "land_use_period", "land_use_origin", "land_associate_property", "land_street", "land_district", "land_province",
                "land_full_of_area", "land_type", "construction_area", "building_area", "land_use_type", "land_sort");
        List<String> vehicalKeys = Arrays.asList("car_license_number", "car_regist_number", "car_issue_place", "car_issue_date", "car_frame_number",
                "car_machine_number");
        List<String> apartmentKeys = Arrays.asList("apartment_address", "apartment_number", "apartment_floor", "apartment_area_use", "apartment_area_build",
                "apartment_structure", "apartment_total_floor");
        List<String> vehicalAirplaneKeys = Arrays.asList("airplane_name", "airplane_regist_number", "airplane_type", "apartment_area_use", "airplane_max_weight",
                "airplane_producer","airplane_year_manufacture","airplane_factory_number","airplane_regist_number_vietnam");
        List<String> vehicalShipKeys = Arrays.asList("ship_name", "ship_regist_number", "ship_level", "ship_function", "ship_year_place_construction",
                "ship_design_length","ship_max_length","ship_design_height","ship_width","ship_dimension_sinking","ship_freeboard","ship_hull_material");
        List<String> commonKeys = Arrays.asList("type", "id", "template", "property_info", "owner_info",
                "other_info", "restrict","owner_info_address");


        for (String key : landKeys) {
            if (!land.has(key)) {
                land.put(key, "");
            }
        }
        for (String key : vehicalKeys) {
            if (!vehicle.has(key)) {
                vehicle.put(key, "");
            }
        }
        for (String key : apartmentKeys) {
            if (!apartment.has(key)) {
                apartment.put(key, "");
            }
        }
        for (String key : vehicalAirplaneKeys) {
            if (!vehicle_airplane.has(key)) {
                vehicle_airplane.put(key, "");
            }
        }
        for (String key : vehicalShipKeys) {
            if (!vehicle_ship.has(key)) {
                vehicle_ship.put(key, "");
            }
        }
        for (String key : commonKeys) {
            if (!obj.has(key)) {
                obj.put(key, "");
            }
        }

        return obj;
    }

    /*bóc tách các thông tin khác hiển thị phía dưới tài sản.*/
    public Contract readThongTinKhac(Contract contract) {
        List<ContractKeyMapBO> listKeyThongTinKhac = contractKeyMapService.getContractKeyMapByFilter(" where type = " + Constants.TYPE_THONG_TIN_KHAC);

        JSONObject obj = new JSONObject();
        if (indexLine > 0) indexLine -= 1;
        for (int i = indexLine; i < fileData.size(); i++) {
            thisLine = removeSpecialInLine(fileData.get(i));
            for (int j = 0; j < listKeyThongTinKhac.size(); j++) {
                ContractKeyMapBO field = listKeyThongTinKhac.get(j);
                if (thisLine.contains(field.getKey_name()) && thisLine.contains(field.getFirst_word()) && thisLine.contains(field.getEnd_word())) {

                    if (!field.getEnd_word().equals("")) {
                        if (!thisLine.contains(field.getEnd_word())) {
                            continue;
                        }
                    } else {
                        List<ContractKeyMapBO> listThisKey = listKeyThongTinKhac.stream()                // convert list to stream
                                .filter(contractKeyMapBO -> field.getKey_name().equals(contractKeyMapBO.getKey_name()) && !contractKeyMapBO.getEnd_word().equals("")
                                )
                                .collect(Collectors.toList());
                        int flg_break = 0;
                        for (ContractKeyMapBO contractKeyMapBO : listThisKey) {
                            if (thisLine.contains(contractKeyMapBO.getEnd_word()) && !Objects.equals(contractKeyMapBO, field)) {
                                //thuc hien so sanh de xac dinh xem object nay co phai la objec dang duyet list fields k?
                                flg_break = 1;
                                break;
                            }
                        }
                        if (flg_break == 1) {
                            continue;
                        }
                    }

                    obj.put(field.getMap_var(), getValueTaiSan2(field, thisLine));
                }
            }
        }
        if (obj.has("notary_date")) {
            //get value
            //process string for date format
            String date = obj.getString("notary_date").toLowerCase();
            if (date.contains("ngày")) {
                date = date.replace("ngày ", "/");
            }
            if (date.contains("tháng")) {
                date = date.replace(" tháng ", "/");
            }
            if (date.contains("năm")) {
                date = date.replace(" năm ", "/");
            }
            contract.setNotary_date(date);
        }
        if (obj.has("notary_userFullName")) {
            //get ten => get id cong chung vien
            String name = obj.getString("notary_userFullName");
            Optional<List<User>> users = userService.getUserByFilter(" WHERE CONCAT(family_name,' ',first_name) LIKE '%" + name + "%'");
            if (users.isPresent() && users.get().size() == 1) {
                contract.setNotary_id(users.get().get(0).getUserId());
            }

        }
        if (obj.has("notary_place")) {
            //notary_place
            String notaryName = obj.getString("notary_place");
            contract.setNotary_place(notaryName);
            String office_name = accessHistoryService.getConfigValue("notary_office_name").orElse("");
            if (notaryName.equalsIgnoreCase(office_name)) {
                contract.setNotary_place_flag(1l);
            } else contract.setNotary_place_flag(0l);
        }
        if (obj.has("contract_value")) {
            //contract.contract_value
            String contract_value = obj.getString("contract_value").replace(".", "");
            contract.setContract_value(contract_value);
        }
        if (obj.has("contract_number")) {
            //contract.contract_value
            contract.setContract_number(obj.getString("contract_number"));
        }
        return contract;
    }


    public String getValueDuongSu(ContractKeyMapBO fileModel, String line) {
        String result = "";
        int indexLastWordOfKey = line.indexOf(fileModel.getKey_name()) + fileModel.getKey_name().length();
        int indexOfKeyAndFirstWord = indexLastWordOfKey;
        int indexOfEndWord = 0;
        if (!fileModel.getFirst_word().equals("")) {
            List<Integer> listIndexOfFirstWord = findListIndexOfWord(line, fileModel.getFirst_word());
            Collections.sort(listIndexOfFirstWord);
            for (Integer index : listIndexOfFirstWord) {
                if (index > (indexLastWordOfKey - 1)) {
                    indexOfKeyAndFirstWord = index + fileModel.getFirst_word().length();
                    break;
                }
            }
        }
        if (!fileModel.getEnd_word().equals("")) {
            List<Integer> listIndexOfEndWord = findListIndexOfWord(line, fileModel.getEnd_word());
            Collections.sort(listIndexOfEndWord);
            for (Integer index : listIndexOfEndWord) {
                if (index > indexOfKeyAndFirstWord) {
                    indexOfEndWord = index;
                    break;
                }
            }
        }
        try {
            result = fileModel.getEnd_word().equals("") ? line.substring(indexOfKeyAndFirstWord).trim() :
                    line.substring(indexOfKeyAndFirstWord, indexOfEndWord).trim();

            //chek nếu từ khóa đó add cho tất cả đối tượng trong đương sự: ví dụ "Cùng địa chỉ thường trú" thì add thêm vào các object đương sự trc đó.
            if (fileModel.getFlg_inline() == Constants.TYPE_ADD_FOR_ALL_DUONG_SU) {
                jsonArrayDuongsuMoiBen.length();
                for (int i = 0; i < jsonArrayDuongsuMoiBen.length(); i++) {
                    jsonArrayDuongsuMoiBen.getJSONObject(i).put(fileModel.getMap_var(), result);
                }
            }
        } catch (Exception e) {
            LOG.error("Lỗi get data tại dòng: " + line);
            e.printStackTrace();
        }

        return result;
    }

    public String removeSpecialInLine(String line) {//remove special word in doc file.
        for (ContractKeyMapBO contractKeyMapBO : specialKeys) {
            line = line.replace(contractKeyMapBO.getKey_name(), "");
            while (line.indexOf("  ") != -1) line = line.replaceAll("  ", " ");
        }
        return line;
    }

    public JSONObject addFullKeyForObjectDuongSu(JSONObject obj) {//keys duong su va ca nhan nhu nhau, chi khac template
        int typeDuongSu = obj.has("org_name") ? 2 : 1;
        List<String> keys = Arrays.asList("name", "birthday", "passport", "certification_date", "certification_place",
                "address", "position", "description", "org_name", "org_address",
                "org_code", "first_registed_date", "number_change_registed", "change_registed_date", "department_issue", "template", "id");
        for (String key : keys) {
            if (!obj.has(key)) {
                if (key.equals("template")) {
                    obj.put(key, typeDuongSu == 1 ? 1 : 2);//1 is ca nhan, 2 is cong ty
                } else obj.put(key, "");
            }
        }
        return obj;
    }

    public List<Integer> findListIndexOfWord(String textString, String word) {
        List<Integer> indexes = new ArrayList<Integer>();
        String lowerCaseTextString = textString.toLowerCase();
        String lowerCaseWord = word.toLowerCase();

        int index = 0;
        while (index != -1) {
            index = lowerCaseTextString.indexOf(lowerCaseWord, index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }
        return indexes;
    }

    ConverterFactory converterFactory = new ConverterFactoryImpl();
    private Converter converter = converterFactory.build(VietnameseEncoding.Encoding.TCVN3);

    public List<String> readAndGetListParagraph(File file, int typeOfFormatWord) throws IOException {

        List<String> result = new ArrayList<>();
        File file2 = new File(file.getAbsolutePath());
        FileInputStream fs = new FileInputStream(file2);
        if (typeOfFormatWord == Constants.TYPE_FORMAT_WORD_DOC) {
            HWPFDocument document1 = new HWPFDocument(fs);
            WordExtractor extractor = new WordExtractor(document1);
            String[] fileData_ = extractor.getParagraphText();
            Range after = document1.getRange();
            int numParagraphs = after.numParagraphs();

            for (int i = 0; i < fileData_.length; i++) {
                int flg_ConvertFont = 0;
                for (int j = 0; j < numParagraphs; j++) {
                    Paragraph paragraph = after.getParagraph(j);

                    int charRuns = paragraph.numCharacterRuns();
                    for (int k = 0; k < charRuns; k++) {
                        CharacterRun run = paragraph.getCharacterRun(k);

                        if (run != null && run.getFontName() != null && run.getFontName().contains(".Vn")) {
                            flg_ConvertFont = 1;
                            break;
                        }
                    }
                    if (flg_ConvertFont == 1) break;
                }

                if (flg_ConvertFont == 1) {
                    String textConvertedFont = "";
                    textConvertedFont = converter.convert(fileData_[i]);
                    System.out.println("text converted = "+textConvertedFont);
                    result.add(textConvertedFont);
                } else {
                    result.add(fileData_[i]);
                }

            }
            document1.close();
        } else if (typeOfFormatWord == Constants.TYPE_FORMAT_WORD_DOCX) {

            XWPFDocument document = new XWPFDocument(fs);
            List<XWPFParagraph> paragraphList = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphList) {
                int flg_ConvertFont = 0;
                System.out.println("text brefore convert = "+paragraph.getParagraphText());
                for (XWPFRun run : paragraph.getRuns()) {
                    String font = run.getFontFamily();
                    if (font != null && font.contains(".Vn")) {
                        flg_ConvertFont = 1;
                        break;
                    }
                }
                if (flg_ConvertFont == 1) {
                    String textConvertedFont = converter.convert(paragraph.getParagraphText());
                    result.add(textConvertedFont);
                    System.out.println("text converted = "+textConvertedFont);
                } else {
                    result.add(paragraph.getParagraphText());
                }
            }
        }
        fs.close();
        return result;
    }

    public ContractKeyMapBO toLowerCaseString(ContractKeyMapBO contractKeyMapBO) {
        contractKeyMapBO.setKey_name(contractKeyMapBO.getKey_name().toLowerCase());
        contractKeyMapBO.setFirst_word(contractKeyMapBO.getFirst_word().toLowerCase());
        contractKeyMapBO.setEnd_word(contractKeyMapBO.getEnd_word().toLowerCase());
        return contractKeyMapBO;
    }

}
