package com.vn.osp.notarialservices.masterConvert.convertFontVNTimeToUTF8;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractConverter implements Converter {

    protected final List<String> highPriorityCharSet;
    protected final List<String> sharedCharSet;

    protected final List<String> sourceCharSet;
    protected final List<String> targetCharSet;

    public AbstractConverter() {
        highPriorityCharSet = initHighPriorityCharSet();
        sharedCharSet = initSharedCharSet();
        sourceCharSet = initSourceEncoding();
        targetCharSet = initTargetEncoding();
    }

    @Override
    public String convert(String content) {
        List<Boolean> indexes = new LinkedList<>();
        for (int i = 0; i < content.length(); i++) {
            indexes.add(i, false);
        }

        for (String findWhat : highPriorityCharSet) {
            String replaceWith = targetCharSet.get(sourceCharSet.indexOf(findWhat));
            content = convert_replaceCharacters(content, findWhat, replaceWith, indexes);
        }

        for (String findWhat : sourceCharSet) {
            if (!highPriorityCharSet.contains(findWhat) && !sharedCharSet.contains(findWhat)) {
                String replaceWith = targetCharSet.get(sourceCharSet.indexOf(findWhat));
                content = convert_replaceCharacters(content, findWhat, replaceWith, indexes);
            }
        }

        return content;
    }

    @Override
    public String convert(File file) {
        return convert(readContent(file));
    }
    
    public String convertString(String str) {
        return convert(str);
    }

    protected abstract String readContent(File file);

    protected abstract List<String> initHighPriorityCharSet();

    protected abstract List<String> initSharedCharSet();

    protected abstract List<String> initSourceEncoding();

    protected abstract List<String> initTargetEncoding();

    private String convert_replaceCharacters(String content, String findWhat, String replaceWith, List<Boolean> indexes) {
        int lastIndex = 0;
        int index = content.indexOf(findWhat, lastIndex);
        while (index >= 0) {
            int matchLength = findWhat.length();
            boolean containsReplacedString = false;
            for (int j = 0; j < matchLength; j++) {
                if (indexes.get(index + j)) {
                    containsReplacedString = true;
                    break;
                }
            }

            if (!containsReplacedString) {
                content = content.substring(0, index) + replaceWith + content.substring(index + matchLength);
                for (int j = 0; j < matchLength; j++) {
                    indexes.remove(index);
                }

                for (int j = 0; j < replaceWith.length(); j++) {
                    indexes.add(index, true);
                }
            }

            lastIndex = index + replaceWith.length();
            index = content.indexOf(findWhat, lastIndex);
        }
        return content;
    }
}
