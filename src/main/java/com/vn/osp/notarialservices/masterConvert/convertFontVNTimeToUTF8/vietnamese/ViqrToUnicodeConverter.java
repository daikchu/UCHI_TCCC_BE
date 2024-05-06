package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese;

import com.google.common.collect.Lists;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.AbstractConverter;
import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.FileHelper;

import java.io.File;
import java.util.List;

public class ViqrToUnicodeConverter extends AbstractConverter {

    @Override
    protected String readContent(File file) {
        return FileHelper.readContent(file, FileHelper.CHARSET_US_ASCII);
    }

    @Override
    public String convert(String content) {
        content = super.convert(content);

        // Replace escape characters
        content = content.replace("\\.", "."); // '\.' => .
        content = content.replace("\\?", "?"); // '\?' => ?
        content = content.replace("\\'", "'"); // '\'' => '

        return content;
    }

    @Override
    protected List<String> initHighPriorityCharSet() {
        return Lists.newArrayList(
                "A^'", "a^'", "A^`", "a^`", "A^?", "a^?", "A^~", "a^~", "A^.", "a^.",
                "A('", "a('", "A(`", "a(`", "A(?", "a(?", "A(~", "a(~", "A(.", "a(.",
                "E^'", "e^'", "E^`", "e^`", "E^?", "e^?", "E^~", "e^~", "E^.", "e^.",
                "O^'", "o^'", "O^`", "o^`", "O^?", "o^?", "O^~", "o^~", "O^.", "o^.",
                "O+'", "o+'", "O+`", "o+`", "O+?", "o+?", "O+~", "o+~", "O+.", "o+.",
                "U+'", "u+'", "U+`", "u+`", "U+?", "u+?", "U+~", "u+~", "U+.", "u+."
        );
    }

    @Override
    protected List<String> initSharedCharSet() {
        return Lists.newArrayList();
    }

    @Override
    protected List<String> initSourceEncoding() {
        return VietnameseEncoding.VIQR_ENCODING;
    }

    @Override
    protected List<String> initTargetEncoding() {
        return VietnameseEncoding.UNICODE_ENCODING;
    }
}
