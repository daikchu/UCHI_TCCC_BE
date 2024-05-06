package com.vn.osp.notarialservices.utils;


/**
 * <P>Constants</P>
 *
 * @author  Nguyen Thanh Hai
 * @version $Revision: 20437 $
 */
public class Constants {

    /** Filter Kind */
    public enum FilterKind {
        /** Full */
        FULL
        /** Left */
        , LEFT
        /** Middle */
        , MIDDLE

        ;


        public Integer getValue() {
            switch (this) {
            case FULL:
                return 0;
            case LEFT:
                return 1;
            case MIDDLE:
                return 2;
            default:
                return null;
            }
        }


        public static FilterKind changeValue(Integer value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case 0:
                return FULL;
            case 1:
                return LEFT;
            case 2:
                return MIDDLE;
            default:
                return null;
            }
        }
    };

    public static  final String SESSION_ACTION = "action";
    public static final int ROW_PER_PAGE = 20;
    /** Authority Code */
    public static final String AUTHORITY_CONTRACT		= "01";
    public static final String AUTHORITY_PREVENT_DATA	= "02";
    public static final String AUTHORITY_BANK_REPORT	= "03";
    public static final String AUTHORITY_ADMIN 			= "04";
    public static final String AUTHORITY_ANNOUNCEMENT   = "05";
        
    /*Role*/
    public static final String CAN_BO_TIEP_NHAN   = "06";
    public static final String CONG_CHUNG_VIEN   = "07";
    public static final String LANH_DAO     = "08";
    public static final String REPORT     = "09";
    
    public static final String VAN_THU      = "010";
    public static final String OTHER        = "99";

    //Loai tai san
    public static final String NHA_DAT = "01";
    public static final String OTO_XEMAY = "02";
    public static final String TAI_SAN_KHAC = "99";
    
    /**Type Prevent */
    public static final int TIEP_NHAN = 1;
    public static final int DA_TRINH = 2;
    public static final int CHUA_XU_LY = 3;
    public static final int DA_DUYET = 4;
    public static final int KHONG_DUYET = 5;
    public static final Boolean GIAI_TOA = true;

    /** Notary Office Type */
    public static final Byte OFFICE_TYPE_JUSTICE	= 1;
    public static final Byte OFFICE_TYPE_NOTARY		= 2;
    public static final Byte OFFICE_TYPE_ORTHER		= 3;

    /** Common Integer */
    public static final int LENGTH_STRING_LIMIT = 100;
    public static final int LENGTH_OUTPUT_LIMIT = 200;

    /** Common String */
    public static final String DOT3 = "...";
    public static final String ENTER = "\n";
    public static final String SPACE = " ";
    public static final String COLON = ":";
    public static final String MASK = "\"";
    public static final String PLUS = "\\+";
    public static final String MINUS = "\\-";
    public static final String SEMI_COLON = ";";
    public static final String UNIT_SEPARATOR = "_";
    public static final String PERCENT = "%";
    public static final String VERTICAL_LINE = "|";
    public static final String SHARP = "#";
    public static final String BULLET = "-";
    
    /** Execute data key */
    public static final String PREVENT_ENTRY_KEY = "PREVENT_ENTRY";
    public static final String PREVENT_UPDATE_KEY = "PREVENT_UPDATE";
    public static final String PREVENT_DELETE_KEY = "PREVENT_DELETE";
    public static final String PREVENT_RELEASE_KEY = "PREVENT_RELEASE";
    public static final String PREVENT_SUBMIT_LEADER = "SUBMIT_LEADER";
    public static final String PREVENT_APPROVE = "APPROVE";
    public static final String PREVENT_DIVISION = "DIVISION";
    public static final String PREVENT_SUBMIT_APPROVE = "SUBMIT_APPROVE";

    /** Synchronize data key */
    public static final Byte SYNCHRONIZE_TYPE_PREVENT = 1;
    public static final Byte SYNCHRONIZE_TYPE_HISTORY = 2;
    public static final Byte SYNCHRONIZE_TYPE_ANNOUNCEMENT = 3;

    public static final Byte SYNCHRONIZE_ACTION_REGIST = 1;
    public static final Byte SYNCHRONIZE_ACTION_EDIT = 2;
    public static final Byte SYNCHRONIZE_ACTION_DELETE = 3;
    public static final Byte SYNCHRONIZE_ACTION_RELEASE = 4;

    // 3 loai quyen: quan tri HT, chuc nang, bao cao
    public static final int AUTHORITY_TYPE_SYSTEM_MANAGER = 1;
    public static final int AUTHORITY_TYPE_FUNCION = 2;
    public static final int AUTHORITY_TYPE_REPORT = 3;

    //gia tri thap phan cua role
    public static final int AUTHORITY_XEM = 64;
    public static final int AUTHORITY_THEM = 32;
    public static final int AUTHORITY_SUA = 16;
    public static final int AUTHORITY_XOA = 8;
    public static final int AUTHORITY_TIMKIEM = 4;
    public static final int AUTHORITY_IN = 2;
    public static final int AUTHORITY_BACKUP = 1;

    // max size file upload
    public static final long MAX_SIZE_FILE_UPLOAD = 20971520;

    //access history type
    public static  final int LOGIN = 0;
    public static  final int LOGOUT = 1;

    //backup
    public static final String CONFIG_BACKUP_DATABASE_FOLDER = "system_backup_database_folder";
    public static final String CHECK_BACKUP_DATABASE = "backup_check";
    public static final String CONFIG_MYSQL_DUMP_FOLDER = "system_mysql_mysqldump_folder";
    public static final String CONFIG_TIME_BACKUP = "time_bacup";
    public static final String CONFIG_DATE_BACKUP = "date_backup";
    public static final String FILE_NAME_BACKUP = "backupnow.bat";
    public static final String FILE_NAME_RESTORE = "restore.bat";

//    public static final String STP_API_LINK = "http://congchung.haugiang.gov.vn:8081/apistphgi";
     public static final String STP_API_LINK = SystemProperties.getSystemPropertyFromClassPath("system.properties","STP_API_LINK");

    public static final String VPCC_API_LINK = "http://localhost:8082/api";
    public static final String OSP_API_LINK = SystemProperties.getSystemPropertyFromClassPath("system.properties","OSP_API_LINK");

    //contract kind
    public static final int LCV_QSDD = 1;
    public static final int LCV_TAI_SAN_KHAC = 2;
    public static final int LCV_THUC_HIEN_NGHIA_VU_DAN_SU = 3;
    public static final int LCV_GIAO_DICH_THUA_KE = 4;
    public static final int LCV_LOAI_CONG_VIEC_KHAC = 5;

    //contract kind code
    public static final String LCV_QSDD_CODE = "1";
    public static final String LCV_TAI_SAN_KHAC_CODE = "2";
    public static final String LCV_THUC_HIEN_NGHIA_VU_DAN_SU_CODE = "3";
    public static final String LCV_GIAO_DICH_THUA_KE_CODE = "4";
    public static final String LCV_LOAI_CONG_VIEC_KHAC_CODE = "5";

    //Dong bo hop dong
    public static final int CREATE_CONTRACT = 1;
    public static final int UPDATE_CONTRACT = 2;
    public static final int DELETE_CONTRACT = 3;
    public static final int CANCEL_CONTRACT = 4;
    public static final int FREE_CONTRACT = 5;

    //duplicate master_contract  content
    public static final int _DUPLICATE = 0;                         // chua xac dinh
    public static final int VALID_DUPLICATE = 1;                    //bi trung
    public static final int INVALID_DUPLICATE = 2;                  //ko bi trung
    public static final int VALID_MASTER_TO_TRANSACTION = 1;        //da chuyen
    public static final int INVALID_MASTER_TO_TRANSACTION = 2;      //chua chuyen
    public static final int TYPE_DELETE = 1;                        //da xoa
    public static final int TYPE_NO_DELETE = 0;                     //chua xoa

    //dinh nghia key de boc tach file contract
    public static final String TEN_BEN = "sau đây gọi là";
    public static final String VAI_TRO_BEN = "Bên";
    public static final Integer TYPE_TAI_SAN_DAT = 1;
    public static final Integer TYPE_TAI_SAN_KHAC = 3;
    public static final Integer TYPE_TAI_SAN_OTO_XEMAY = 6;
    public static final Integer TYPE_TAI_SAN_MAU_CHUNG_CU = 8;
    public static final Integer TYPE_TAI_SAN_DAT_TS_GLVD = 10;
    public static final String lanId3 = "Thông tin tài sản";
    public static final String END_TAISAN = "ĐIỀU 2";
    public static final String TEN_HĐ_START = "Độc lập - Tự do - Hạnh phúc";
    public static final int TYPE_DUONG_SU = 1;
    public static final int TYPE_START_DUONG_SU_OR_TAI_SAN = 1;//start duong su/tai san
    public static final int TYPE_END_DUONG_SU_OR_TAI_SAN = 2;//end duong su/tai san
    public static final int TYPE_TAI_SAN = 2;
    public static final int TYPE_THONG_TIN_KHAC = 3;
    public static final int TYPE_ADD_FOR_ALL_DUONG_SU = 1;//flg: thêm 1 giá trị cho nhiều đương sự.
    public static final int TYPE_SPECIAL_WORD = 4;

    public static final int TYPE_READ_WORD = 3;//flg_inline

    public static final int TYPE_FORMAT_WORD_DOC = 1;
    public static final int TYPE_FORMAT_WORD_DOCX = 2;

    /*Config loại tổ chức: phường xã/tổ chức công chứng*/
    public static final String ORG_TYPE_TCCC = "0";
    public static final String ORG_TYPE_PHUONG_XA = "1";

    //Config cấp tài khoản chứng thực thuộc xã/huyện/tỉnh
    public static String LEVEL_CERT_XA = "XA";
    public static String LEVEL_CERT_HUYEN = "HUYEN";
    public static String LEVEL_CERT_TINH = "TINH";

    //Config trang thai hop dong online
    public static int HDOL_CHO_KY = 0;
    public static int HDOL_DA_KY = 1;
    public static int HDOL_TRA_VE = 2;
    public static int HDOL_LUU_TAM = 3;
    public static int HDOL_DONG_DAU = 4;
    public static int HDOL_HUY = 5;

    //Config trang thai hop dong offline
    public static int HDOFF_LUU_TAM = 5;

    //Config loại chứng thực
    public static int CERTIFICATE_TYPE_SIGNATURE = 1;
    public static int CERTIFICATE_TYPE_COPY = 2;

    //Config loai biến map template
    public static class TYPE_CONTRACT_TEMPLATE_FIELD_MAP{
        public static int CHUNG = 0;
        public static int TCCC = 1;
        public static int CHUNGTHUC = 2;
    }

    /** Config loại tài sản thuộc bất động sản*/
    public static class TYPE_PROPERTY_LAND{
        public static int DNDO = 1;/**Đất nền để ở*/
        public static int NORL = 2;/**Nhà ở riêng lẻ*/
        public static int CHCC = 3;/**Căn hộ chung cư*/
        public static int VPCT = 4;/**Văn phòng cho thuê*/
        public static int MBTMDV = 5;/**Mặt bằng thương mại dịch vụ*/
        public static int DNDO_phatTrienTheoDuAn = 6;/**Đất nền để ở - phát triển theo dự án*/
        public static int DNDO_trongKhuDanCu = 7;/**Đất nền để ở - trong khu dân cư*/
        public static int NORL_phatTrienTheoDuAn = 8;/**Nhà ở riêng lẻ - phát triển theo dự án*/
        public static int NORL_trongKhuDanCu = 9;/**Nhà ở riêng lẻ - trong khu dân cư*/
        public static int CHCC_dienTichDuoi70m2 = 10;/**Căn hộ chung cư - diện tích dưới 70m2*/
        public static int CHCC_dienTichTu70Den120m2 = 11;/**Căn hộ chung cư - diện tích từ 70 đến 120m2*/
        public static int CHCC_dienTichTren120m2 = 12;/**Căn hộ chung cư - diện tích trên 120m2*/
    }

    /*public static */


}
