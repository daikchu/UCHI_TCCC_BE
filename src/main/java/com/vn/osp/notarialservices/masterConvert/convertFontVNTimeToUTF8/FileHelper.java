package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {

    public static final Charset CHARSET_WINDOW_1252 = Charset.forName("Cp1252");
    public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;
    public static final Charset CHARSET_US_ASCII = StandardCharsets.US_ASCII;
    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    private FileHelper() {
        // prevent constructor
    }

    public static boolean isHTML(File file) {
        return file.getName().toLowerCase().endsWith(".html") || file.getName().toLowerCase().endsWith(".htm");
    }

    public static boolean isRTF(File file) {
        return file.getName().toLowerCase().endsWith(".rtf");
    }

    public static String readContent(File file, Charset charset) {
        try {
            byte[] content = Files.readAllBytes(Paths.get(file.getCanonicalPath()));
            return new String(content, charset);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
