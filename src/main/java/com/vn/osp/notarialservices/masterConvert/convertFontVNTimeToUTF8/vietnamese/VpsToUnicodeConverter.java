package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese;

import com.google.common.collect.Lists;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.AbstractConverter;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.FileHelper;

import java.io.File;
import java.util.List;

public class VpsToUnicodeConverter extends AbstractConverter {

    @Override
    protected String readContent(File file) {
        return FileHelper.readContent(file, FileHelper.CHARSET_ISO_8859_1);
    }

    @Override
    protected List<String> initHighPriorityCharSet() {
        return Lists.newArrayList();
    }

    @Override
    protected List<String> initSharedCharSet() {
        return Lists.newArrayList(
                "\u00C1", "\u00C2", "\u00C9", "\u00CA",
                "\u00D4", "\u00DA", "\u00DD",
                "\u00E0", "\u00E1", "\u00E2", "\u00E3", "\u00E8", "\u00E9", "\u00EA", "\u00EC",
                "\u00ED", "\u00F2", "\u00F3", "\u00F4", "\u00F5", "\u00F9", "\u00FA"
        );
    }

    @Override
    protected List<String> initSourceEncoding() {
        return VietnameseEncoding.VPS_ENCODING;
    }

    @Override
    protected List<String> initTargetEncoding() {
        return VietnameseEncoding.UNICODE_ENCODING;
    }
}
