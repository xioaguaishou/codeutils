package org.lam.code.util;

public class BaseUtil {

    public static void main(String[] args) {
        System.out.println(palindromeString("    "));
        System.out.println(palindromeString(1234554321));
    }

    /**
     * 判断回文, 字符串和数组
     * @param tarStr
     * @return
     */
    public static boolean palindromeString(String tarStr) {
        tarStr = tarStr.trim();
        if (tarStr.length() <= 1) {
            return false;
        }
        String leftStr = new StringBuilder(tarStr.substring(0, (tarStr.length() / 2))).reverse().toString();

        int rightIdx = (tarStr.length() % 2) == 0 ? tarStr.length() / 2 : (tarStr.length() / 2 + 1);

        return leftStr.equals(tarStr.substring(rightIdx));

    }

    public static boolean palindromeString(int tarNum) {
        return palindromeString(tarNum + "");
    }

}
