package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese;

import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.Converter;

public class ConverterFactoryImpl implements ConverterFactory {

    @Override
    public Converter build(VietnameseEncoding.Encoding encoding) {
        switch (encoding) {
            case TCVN3:
                return new Tcvn3ToUnicodeConverter();
            case VIQR:
                return new ViqrToUnicodeConverter();
            case VISCII:
                return new VisciiToUnicodeConverter();
            case VNI:
                return new VniToUnicodeConverter();
            case VPS:
                return new VpsToUnicodeConverter();
            default:
                throw new RuntimeException("Encoding is not supported.");
        }
    }
}
