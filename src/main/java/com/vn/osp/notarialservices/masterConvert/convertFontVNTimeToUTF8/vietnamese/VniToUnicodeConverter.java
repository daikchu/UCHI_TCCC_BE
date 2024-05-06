package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese;

import com.google.common.collect.Lists;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.AbstractConverter;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.FileHelper;

import java.io.File;
import java.util.List;

public class VniToUnicodeConverter extends AbstractConverter {

    @Override
    protected String readContent(File file) {
        return FileHelper.readContent(file, FileHelper.CHARSET_WINDOW_1252);
    }

    @Override
    protected List<String> initHighPriorityCharSet() {
        return Lists.newArrayList(
                "\u0041\u00C0", "\u0041\u00C1", "\u0041\u00C2", "\u0041\u00C3", "\u0041\u00C8", "\u0041\u00C9", "\u0041\u00CA", "\u0041\u00DA", "\u0041\u00D5", "\u0041\u00D9",
                "\u0045\u00C0", "\u0045\u00C1", "\u0045\u00C2", "\u0045\u00C3",
                "\u0045\u00D5", "\u0045\u00D9",
                "\u004F\u00C0", "\u004F\u00C1", "\u004F\u00C2", "\u004F\u00C3", "\u004F\u00D9",
                "\u0055\u00D5", "\u0055\u00D9",
                "\u0059\u00D5", "\u0059\u00D9",
                "\u0061\u00E0", "\u0061\u00E1", "\u0061\u00E3", "\u0061\u00E8", "\u0061\u00E9", "\u0061\u00EA", "\u0061\u00F5", "\u0061\u00F9", "\u0061\u00FA",
                "\u0065\u00E0", "\u0065\u00E1", "\u0065\u00E2", "\u0065\u00E3", "\u0065\u00F5", "\u0065\u00F9",
                "\u006F\u00E0", "\u006F\u00E1", "\u006F\u00E2", "\u006F\u00E3", "\u006F\u00F9",
                "\u0075\u00F5", "\u0075\u00F9",
                "\u0079\u00F5", "\u0079\u00F9",
                "\u00D4\u00D9", "\u00D4\u00D8", "\u00D4\u00DB", "\u00D4\u00D5", "\u00D4\u00CF",
                "\u00D6\u00D5", "\u00D6\u00D9",
                "\u00F4\u00EF", "\u00F4\u00F5", "\u00F4\u00F8", "\u00F4\u00F9", "\u00F4\u00FB",
                "\u00F6\u00EF", "\u00F6\u00F5", "\u00F6\u00F8", "\u00F6\u00F9", "\u00F6\u00FB",
                "\u00D2", "\u00D3", "\u00D4", "\u00F2", "\u00F3", "\u00F4", "\u00F6"
        );
    }

    @Override
    protected List<String> initSharedCharSet() {
        return Lists.newArrayList(
                "\u00CC", "\u00CD", "\u00EC", "\u00ED"
        );
    }

    @Override
    protected List<String> initSourceEncoding() {
        return VietnameseEncoding.VNI_ENCODING;
    }

    @Override
    protected List<String> initTargetEncoding() {
        return VietnameseEncoding.UNICODE_ENCODING;
    }
}
