//package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.vietnamese;
//
//import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.Converter;
//import com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8.FileHelper;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.io.File;
//
//public class VpsToUnicodeConverterTest {
//
//    private static Converter converter;
//
//    @BeforeClass
//    public static void initConverter() {
//        ConverterFactory converterFactory = new ConverterFactoryImpl();
//        converter = converterFactory.build(VietnameseEncoding.Encoding.VPS);
//    }
//
//    @Test
//    public void testConvertTextFile_input1() throws Exception {
//        File inputFile = new File(getClass().getResource("/input/input_1/input_1_vps.txt").getPath());
//        File outputFile = new File(getClass().getResource("/output/output_1.txt").getPath());
//
//        String expectedContent = FileHelper.readContent(outputFile, FileHelper.CHARSET_UTF_8);
//        String convertedContent = converter.convert(inputFile);
//        Assert.assertTrue(expectedContent.equals(convertedContent));
//    }
//
//    @Test
//    public void testConvertTextFile_input2() throws Exception {
//        File inputFile = new File(getClass().getResource("/input/input_2/input_2_vps.txt").getPath());
//        File outputFile = new File(getClass().getResource("/output/output_2.txt").getPath());
//
//        String expectedContent = FileHelper.readContent(outputFile, FileHelper.CHARSET_UTF_8);
//        String convertedContent = converter.convert(inputFile);
//        Assert.assertTrue(expectedContent.equals(convertedContent));
//    }
//}