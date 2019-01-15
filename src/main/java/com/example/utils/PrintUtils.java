package com.example.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintUtils {

    public static void print(int width, String... rows) {
        System.out.println(StringUtils.rightPad("", rows.length*(width+1), "-"));
        System.out.print("⎢");
        Arrays.stream(rows).forEach(field -> {
            String str = StringUtils.rightPad(field, width);
            System.out.print(str);
//            StringUtils.
//            String temp = "";
//            System.arraycopy(str, 0, temp, 0, field.length()+include(field));
//            System.out.println(temp);
            System.out.print("⎢");
        });
        System.out.println();
    }

    public static void printFooter(int width, int count) {
        System.out.println(StringUtils.rightPad("", count*(width+1), "-"));
    }

    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;

        return flg;
    }

    public static String convert(String str) {
        for (int i = 0; i < str.length(); i++) {
            if(isChinese(String.valueOf(str.charAt(i)))) {
                str = str.replace(String.valueOf(str.charAt(i)), "^^");
            }
        }
        return str;
    }

    public static String back(String newStr, String oldStr) {
        for (int i = 0; i < oldStr.length(); i++) {
            if(isChinese(String.valueOf(oldStr.charAt(i)))) {
                newStr = newStr.replaceAll("\\^{2}?",String.valueOf(oldStr.charAt(i)));
            }
        }
        return newStr;
    }

    public static int include(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if(isChinese(String.valueOf(str.charAt(i)))) {
                count++;
            }
        }
        count = count;
        return count;
    }

    public static void main(String[] args) {
        PrintUtils.print(10, "a", "b", "b", "b");
        PrintUtils.print(10, "你好\t", "d", "b", "b");

        System.out.println("你".getBytes());
        System.out.println("^".getBytes());

        System.out.println(2%60);

    }

}
