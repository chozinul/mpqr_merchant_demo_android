package com.mastercard.labs.mpqrmerchant.utils;

/**
 * Created by kaile on 23/3/17.
 */

public class CalculateCode {

    public static String calculate8digit(String longCode) {

        if (!longCode.isEmpty())
        {
            String shortCode = "";

            for (int i = 0; i < longCode.length(); i++) {
                if (i % 2 == 0) {
                    shortCode += longCode.charAt(i);
                }
            }

            return shortCode;
        }

        else return null;

    }
}
