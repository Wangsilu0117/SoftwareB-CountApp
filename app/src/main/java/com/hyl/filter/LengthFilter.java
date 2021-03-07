package com.hyl.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class LengthFilter implements InputFilter {

    /** 输入框小数的位数, 保留2位小数*/
    private static final int DECIMAL_COUNT = 2;

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
            return null;
        }
        String destValue = dest.toString();
        String[] splitArray = destValue .split("\\.");
        if (splitArray.length > 1) {
            String result = splitArray[1];
            int d = result.length() + 1 - DECIMAL_COUNT;
            if (d > 0) {
                return source.subSequence(start, end - d);
            }
        }
        return null;
    }
}