package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese;

import com.google.common.collect.Lists;

import java.util.List;

public class VietnameseEncoding {

    public static final List<String> UNICODE_ENCODING;

    public static final List<String> VNI_ENCODING;

    public static final List<String> VPS_ENCODING;

    public static final List<String> VISCII_ENCODING;

    public static final List<String> TCVN3_ENCODING;

    public static final List<String> VIQR_ENCODING;

    public enum Encoding {
        TCVN3, VIQR, VISCII, VNI, VPS
    }

    static {
        UNICODE_ENCODING = Lists.newArrayList(
                "\u00C0", "\u00C1", "\u00C2", "\u00C3", "\u00C8", "\u00C9", "\u00CA", "\u00CC", "\u00CD",
                "\u00D2", "\u00D3", "\u00D4", "\u00D5", "\u00D9", "\u00DA", "\u00DD",
                "\u00E0", "\u00E1", "\u00E2", "\u00E3", "\u00E8", "\u00E9", "\u00EA", "\u00EC",
                "\u00ED", "\u00F2", "\u00F3", "\u00F4", "\u00F5", "\u00F9", "\u00FA", "\u00FD",
                "\u0102", "\u0103", "\u0110", "\u0111", "\u0128", "\u0129", "\u0168", "\u0169",
                "\u01A0", "\u01A1", "\u01AF", "\u01B0",
                "\u1EA0", "\u1EA1", "\u1EA2", "\u1EA3", "\u1EA4", "\u1EA5", "\u1EA6", "\u1EA7",
                "\u1EA8", "\u1EA9", "\u1EAA", "\u1EAB", "\u1EAC",

                "\u1EAD", "\u1EAE", "\u1EAF", "\u1EB0", "\u1EB1", "\u1EB2", "\u1EB3", "\u1EB4",
                "\u1EB5", "\u1EB6",

                "\u1EB7", "\u1EB8", "\u1EB9", "\u1EBA", "\u1EBB", "\u1EBC", "\u1EBD", "\u1EBE", "\u1EBF",
                "\u1EC0", "\u1EC1", "\u1EC2", "\u1EC3", "\u1EC4", "\u1EC5", "\u1EC6", "\u1EC7",
                "\u1EC8", "\u1EC9", "\u1ECA", "\u1ECB", "\u1ECC", "\u1ECD", "\u1ECE", "\u1ECF",
                "\u1ED0", "\u1ED1", "\u1ED2", "\u1ED3", "\u1ED4", "\u1ED5", "\u1ED6", "\u1ED7",
                "\u1ED8", "\u1ED9", "\u1EDA", "\u1EDB", "\u1EDC", "\u1EDD", "\u1EDE", "\u1EDF",
                "\u1EE0", "\u1EE1", "\u1EE2", "\u1EE3", "\u1EE4", "\u1EE5", "\u1EE6", "\u1EE7",
                "\u1EE8", "\u1EE9", "\u1EEA", "\u1EEB", "\u1EEC", "\u1EED", "\u1EEE", "\u1EEF",
                "\u1EF0", "\u1EF1", "\u1EF2", "\u1EF3", "\u1EF4",

                "\u1EF5", "\u1EF6", "\u1EF7", "\u1EF8", "\u1EF9"
        );

        VNI_ENCODING = Lists.newArrayList(
                "\u0041\u00D8", "\u0041\u00D9", "\u0041\u00C2", "\u0041\u00D5", "\u0045\u00D8", "\u0045\u00D9", "\u0045\u00C2", "\u00CC", "\u00CD",
                "\u004F\u00D8", "\u004F\u00D9", "\u004F\u00C2", "\u004F\u00D5", "\u0055\u00D8", "\u0055\u00D9", "\u0059\u00D9",
                "\u0061\u00F8", "\u0061\u00F9", "\u0061\u00E2", "\u0061\u00F5", "\u0065\u00F8", "\u0065\u00F9", "\u0065\u00E2", "\u00EC",
                "\u00ED", "\u006F\u00F8", "\u006F\u00F9", "\u006F\u00E2", "\u006F\u00F5", "\u0075\u00F8", "\u0075\u00F9", "\u0079\u00F9",
                "\u0041\u00CA", "\u0061\u00EA", "\u00D1", "\u00F1", "\u00D3", "\u00F3", "\u0055\u00D5", "\u0075\u00F5",
                "\u00D4", "\u00F4", "\u00D6", "\u00F6",
                "\u0041\u00CF", "\u0061\u00EF", "\u0041\u00DB", "\u0061\u00FB", "\u0041\u00C1", "\u0061\u00E1", "\u0041\u00C0", "\u0061\u00E0",
                "\u0041\u00C5", "\u0061\u00E5", "\u0041\u00C3", "\u0061\u00E3", "\u0041\u00C4",

                "\u0061\u00E4", "\u0041\u00C9", "\u0061\u00E9", "\u0041\u00C8", "\u0061\u00E8", "\u0041\u00DA", "\u0061\u00FA", "\u0041\u00DC",
                "\u0061\u00FC", "\u0041\u00CB",

                "\u0061\u00EB", "\u0045\u00CF", "\u0065\u00EF", "\u0045\u00DB", "\u0065\u00FB", "\u0045\u00D5", "\u0065\u00F5", "\u0045\u00C1", "\u0065\u00E1",
                "\u0045\u00C0", "\u0065\u00E0", "\u0045\u00C5", "\u0065\u00E5", "\u0045\u00C3", "\u0065\u00E3", "\u0045\u00C4", "\u0065\u00E4",
                "\u00C6", "\u00E6", "\u00D2", "\u00F2", "\u004F\u00CF", "\u006F\u00EF", "\u004F\u00DB", "\u006F\u00FB",
                "\u004F\u00C1", "\u006F\u00E1", "\u004F\u00C0", "\u006F\u00E0", "\u004F\u00C5", "\u006F\u00E5", "\u004F\u00C3", "\u006F\u00E3",
                "\u004F\u00C4", "\u006F\u00E4", "\u00D4\u00D9", "\u00F4\u00F9", "\u00D4\u00D8", "\u00F4\u00F8", "\u00D4\u00DB", "\u00F4\u00FB",
                "\u00D4\u00D5", "\u00F4\u00F5", "\u00D4\u00CF", "\u00F4\u00EF", "\u0055\u00CF", "\u0075\u00EF", "\u0055\u00DB", "\u0075\u00FB",
                "\u00D6\u00D9", "\u00F6\u00F9", "\u00D6\u00D8", "\u00F6\u00F8", "\u00D6\u00DB", "\u00F6\u00FB", "\u00D6\u00D5", "\u00F6\u00F5",
                "\u00D6\u00CF", "\u00F6\u00EF", "\u0059\u00D8", "\u0079\u00F8", "\u00CE",

                "\u00EE", "\u0059\u00DB", "\u0079\u00FB", "\u0059\u00D5", "\u0079\u00F5"
        );

        VPS_ENCODING = Lists.newArrayList(
                "\u0080", "\u00C1", "\u00C2", "\u0082", "\u00D7", "\u00C9", "\u00CA", "\u00B5", "\u00B4",
                "\u00BC", "\u00B9", "\u00D4", "\u00BE", "\u00A8", "\u00DA", "\u00DD",
                "\u00E0", "\u00E1", "\u00E2", "\u00E3", "\u00E8", "\u00E9", "\u00EA", "\u00EC",
                "\u00ED", "\u00F2", "\u00F3", "\u00F4", "\u00F5", "\u00F9", "\u00FA", "\u009A",
                "\u0088", "\u00E6", "\u00F1", "\u00C7", "\u00B8", "\u00EF", "\u00AC", "\u00DB",
                "\u00F7", "\u00D6", "\u00D0", "\u00DC",
                "\u0002", "\u00E5", "\u0081", "\u00E4", "\u0083", "\u00C3", "\u0084", "\u00C0",
                "\u0085", "\u00C4", "\u001C", "\u00C5", "\u0003",

                "\u00C6", "\u008D", "\u00A1", "\u008E", "\u00A2", "\u008F", "\u00A3", "\u00F0",
                "\u00A4", "\u0004",

                "\u00A5", "\u0005", "\u00CB", "\u00DE", "\u00C8", "\u00FE", "\u00EB", "\u0090", "\u0089",
                "\u0093", "\u008A", "\u0094", "\u008B", "\u0095", "\u00CD", "\u0006", "\u008C",
                "\u00B7", "\u00CC", "\u0010", "\u00CE", "\u0011", "\u0086", "\u00BD", "\u00D5",
                "\u0096", "\u00D3", "\u0097", "\u00D2", "\u0098", "\u00B0", "\u0099", "\u0087",
                "\u0012", "\u00B6", "\u009D", "\u00A7", "\u009E", "\u00A9", "\u009F", "\u00AA",
                "\u00A6", "\u00AB", "\u0013", "\u00AE", "\u0014", "\u00F8", "\u00D1", "\u00FB",
                "\u00AD", "\u00D9", "\u00AF", "\u00D8", "\u00B1", "\u00BA", "\u001D", "\u00BB",
                "\u0015", "\u00BF", "\u00B2", "\u00FF", "\u0019",

                "\u009C", "\u00FD", "\u009B", "\u00B3", "\u00CF"
        );

        VISCII_ENCODING = Lists.newArrayList(
                "\u00C0", "\u00C1", "\u00C2", "\u00C3", "\u00C8", "\u00C9", "\u00CA", "\u00CC", "\u00CD",
                "\u00D2", "\u00D3", "\u00D4", "\u00A0", "\u00D9", "\u00DA", "\u00DD",
                "\u00E0", "\u00E1", "\u00E2", "\u00E3", "\u00E8", "\u00E9", "\u00EA", "\u00EC",
                "\u00ED", "\u00F2", "\u00F3", "\u00F4", "\u00F5", "\u00F9", "\u00FA", "\u00FD",
                "\u00C5", "\u00E5", "\u00D0", "\u00F0", "\u00CE", "\u00EE", "\u009D", "\u00FB",
                "\u00B4", "\u00BD", "\u00BF", "\u00DF",
                "\u0080", "\u00D5", "\u00C4", "\u00E4", "\u0084", "\u00A4", "\u0085", "\u00A5",
                "\u0086", "\u00A6", "\u0006", "\u00E7", "\u0087",

                "\u00A7", "\u0081", "\u00A1", "\u0082", "\u00A2", "\u0002", "\u00C6", "\u0005",
                "\u00C7", "\u0083",

                "\u00A3", "\u0089", "\u00A9", "\u00CB", "\u00EB", "\u0088", "\u00A8", "\u008A", "\u00AA",
                "\u008B", "\u00AB", "\u008C", "\u00AC", "\u008D", "\u00AD", "\u008E", "\u00AE",
                "\u009B", "\u00EF", "\u0098", "\u00B8", "\u009A", "\u00F7", "\u0099", "\u00F6",
                "\u008F", "\u00AF", "\u0090", "\u00B0", "\u0091", "\u00B1", "\u0092", "\u00B2",
                "\u0093", "\u00B5", "\u0095", "\u00BE", "\u0096", "\u00B6", "\u0097", "\u00B7",
                "\u00B3", "\u00DE", "\u0094", "\u00FE", "\u009E", "\u00F8", "\u009C", "\u00FC",
                "\u00BA", "\u00D1", "\u00BB", "\u00D7", "\u00BC", "\u00D8", "\u00FF", "\u00E6",
                "\u00B9", "\u00F1", "\u009F", "\u00CF", "\u001E",

                "\u00DC", "\u0014", "\u00D6", "\u0019", "\u00DB"
        );

        TCVN3_ENCODING = Lists.newArrayList(
                "\u0041\u00B5", "\u0041\u00B8", "\u00A2", "\u0041\u00B7", "\u0045\u00CC", "\u0045\u00D0", "\u00A3", "\u0049\u00D7", "\u0049\u00DD",
                "\u004F\u00DF", "\u004F\u00E3", "\u00A4", "\u004F\u00E2", "\u0055\u00EF", "\u0055\u00F3", "\u0059\u00FD",
                "\u00B5", "\u00B8", "\u00A9", "\u00B7", "\u00CC", "\u00D0", "\u00AA", "\u00D7",
                "\u00DD", "\u00DF", "\u00E3", "\u00AB", "\u00E2", "\u00EF", "\u00F3", "\u00FD",
                "\u00A1", "\u00A8", "\u00A7", "\u00AE", "\u0049\u00DC", "\u00DC", "\u0055\u00F2", "\u00F2",
                "\u00A5", "\u00AC", "\u00A6", "\u00AD",
                "\u0041\u00B9", "\u00B9", "\u0041\u00B6", "\u00B6", "\u00A2\u00CA", "\u00CA", "\u00A2\u00C7", "\u00C7",
                "\u00A2\u00C8", "\u00C8", "\u00A2\u00C9", "\u00C9", "\u00A2\u00CB",

                "\u00CB", "\u00A1\u00BE", "\u00BE", "\u00A1\u00BB", "\u00BB", "\u00A1\u00BC", "\u00BC", "\u00A1\u00BD",
                "\u00BD", "\u00A1\u00C6",

                "\u00C6", "\u0045\u00D1", "\u00D1", "\u0045\u00CE", "\u00CE", "\u0045\u00CF", "\u00CF", "\u00A3\u00D5", "\u00D5",
                "\u00A3\u00D2", "\u00D2", "\u00A3\u00D3", "\u00D3", "\u00A3\u00D4", "\u00D4", "\u00A3\u00D6", "\u00D6",
                "\u0049\u00D8", "\u00D8", "\u0049\u00DE", "\u00DE", "\u004F\u00E4", "\u00E4", "\u004F\u00E1", "\u00E1",
                "\u00A4\u00E8", "\u00E8", "\u00A4\u00E5", "\u00E5", "\u00A4\u00E6", "\u00E6", "\u00A4\u00E7", "\u00E7",
                "\u00A4\u00E9", "\u00E9", "\u00A5\u00ED", "\u00ED", "\u00A5\u00EA", "\u00EA", "\u00A5\u00EB", "\u00EB",
                "\u00A5\u00EC", "\u00EC", "\u00A5\u00EE", "\u00EE", "\u0055\u00F4", "\u00F4", "\u0055\u00F1", "\u00F1",
                "\u00A6\u00F8", "\u00F8", "\u00A6\u00F5", "\u00F5", "\u00A6\u00F6", "\u00F6", "\u00A6\u00F7", "\u00F7",
                "\u00A6\u00F9", "\u00F9", "\u0059\u00FA", "\u00FA", "\u0059\u00FE",

                "\u00FE", "\u0059\u00FB", "\u00FB", "\u0059\u00FC", "\u00FC"
        );

        VIQR_ENCODING = Lists.newArrayList(
                "A`", "A'", "A^", "A~", "E`", "E'", "E^", "I`", "I'",
                "O`", "O'", "O^", "O~", "U`", "U'", "Y'",
                "a`", "a'", "a^", "a~", "e`", "e'", "e^", "i`",
                "i'", "o`", "o'", "o^", "o~", "u`", "u'", "y'",
                "A(", "a(", "DD", "dd", "I~", "i~", "U~", "u~",
                "O+", "o+", "U+", "u+",
                "A.", "a.", "A?", "a?", "A^'", "a^'", "A^`", "a^`",
                "A^?", "a^?", "A^~", "a^~", "A^.",

                "a^.", "A('", "a('", "A(`", "a(`", "A(?", "a(?", "A(~",
                "a(~", "A(.",

                "a(.", "E.", "e.", "E?", "e?", "E~", "e~", "E^'", "e^'",
                "E^`", "e^`", "E^?", "e^?", "E^~", "e^~", "E^.", "e^.",
                "I?", "i?", "I.", "i.", "O.", "o.", "O?", "o?",
                "O^'", "o^'", "O^`", "o^`", "O^?", "o^?", "O^~", "o^~",
                "O^.", "o^.", "O+'", "o+'", "O+`", "o+`", "O+?", "o+?",
                "O+~", "o+~", "O+.", "o+.", "U.", "u.", "U?", "u?",
                "U+'", "u+'", "U+`", "u+`", "U+?", "u+?", "U+~", "u+~",
                "U+.", "u+.", "Y`", "y`", "Y.",

                "y.", "Y?", "y?", "Y~", "y~"
        );
    }
}
