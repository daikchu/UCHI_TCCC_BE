package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8;

import java.io.File;

public interface Converter {

    String convert(String content);

    String convert(File file);
}
