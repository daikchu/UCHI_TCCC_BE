package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese;

import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.Converter;

public interface ConverterFactory {

    Converter build(VietnameseEncoding.Encoding encoding);
}
