package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese;

import com.google.common.collect.Lists;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.AbstractConverter;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.FileHelper;

import java.io.File;
import java.util.List;

public class Tcvn3ToUnicodeConverter extends AbstractConverter {

    @Override
    protected String readContent(File file) {
        return FileHelper.readContent(file, FileHelper.CHARSET_WINDOW_1252);
    }

    @Override
    protected List<String> initHighPriorityCharSet() {
        return Lists.newArrayList(
                "\u0041\u00B5", "\u0041\u00B6", "\u0041\u00B7", "\u0041\u00B8", "\u0041\u00B9",
                "\u0045\u00CC", "\u0045\u00CE", "\u0045\u00CF", "\u0045\u00D0", "\u0045\u00D1",
                "\u0049\u00D7", "\u0049\u00D8", "\u0049\u00DC", "\u0049\u00DD", "\u0049\u00DE",
                "\u004F\u00DF", "\u004F\u00E2", "\u004F\u00E1", "\u004F\u00E3", "\u004F\u00E4",
                "\u0055\u00EF", "\u0055\u00F1", "\u0055\u00F2", "\u0055\u00F3", "\u0055\u00F4",
                "\u0059\u00FA", "\u0059\u00FB", "\u0059\u00FC", "\u0059\u00FD", "\u0059\u00FE",
                "\u00A1\u00BB", "\u00A1\u00BC", "\u00A1\u00BD", "\u00A1\u00BE", "\u00A1\u00C6",
                "\u00A2\u00C7", "\u00A2\u00C8", "\u00A2\u00C9", "\u00A2\u00CA", "\u00A2\u00CB",
                "\u00A3\u00D2", "\u00A3\u00D3", "\u00A3\u00D4", "\u00A3\u00D5", "\u00A3\u00D6",
                "\u00A4\u00E5", "\u00A4\u00E6", "\u00A4\u00E7", "\u00A4\u00E8", "\u00A4\u00E9",
                "\u00A5\u00EA", "\u00A5\u00EB", "\u00A5\u00EC", "\u00A5\u00ED", "\u00A5\u00EE",
                "\u00A6\u00F5", "\u00A6\u00F6", "\u00A6\u00F7", "\u00A6\u00F8", "\u00A6\u00F9"
        );
    }

    @Override
    protected List<String> initSharedCharSet() {
        return Lists.newArrayList(

        );
    }

    @Override
    protected List<String> initSourceEncoding() {
        return VietnameseEncoding.TCVN3_ENCODING;
    }

    @Override
    protected List<String> initTargetEncoding() {
        return VietnameseEncoding.UNICODE_ENCODING;
    }
}
