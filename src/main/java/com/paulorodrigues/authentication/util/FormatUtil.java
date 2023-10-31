package com.paulorodrigues.authentication.util;

public class FormatUtil {

    public static StringBuilder removeLastComma(StringBuilder sb) {
        int length = sb.length();
        if (length > 2 && sb.charAt(length - 2) == ',' && sb.charAt(length - 1) == ' ') {
            sb.setLength(length - 2);
        }
        return sb;
    }
}
