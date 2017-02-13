package com.mastercard.labs.mpqrmerchant.data.model;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
enum TipInfo {
    PROMPTED_TO_ENTER_TIP,
    FLAT_CONVENIENCE_FEE,
    PERCENTAGE_CONVENIENCE_FEE,
    UNKNOWN;

    public static TipInfo fromString(String text) {
        if (text != null) {
            for (TipInfo value : TipInfo.values()) {
                if (text.equalsIgnoreCase(value.name())) {
                    return value;
                }
            }
        }
        return UNKNOWN;
    }
}
