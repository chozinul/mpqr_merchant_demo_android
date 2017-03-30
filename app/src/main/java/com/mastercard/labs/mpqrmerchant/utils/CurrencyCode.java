package com.mastercard.labs.mpqrmerchant.utils;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/2/17
 */
public enum CurrencyCode {
    XXX("No currency involved", "999"),
    XUA("ADB Unit of Account", "965"),
    AFN("Afghani", "971"),
    DZD("Algerian Dinar", "012"),
    AMD("Armenian Dram", "051"),
    ARS("Argentine Peso", "032"),
    AUD("Australian Dollar", "036"),
    AWG("Aruban Florin", "533"),
    AZN("Azerbaijani Manat", "944"),
    BSD("Bahamian Dollar", "044"),
    THB("Baht", "764"),
    PAB("Balboa", "999"),
    BBD("Barbados Dollar", "052"),
    BHD("Bahraini Dinar", "048"),
    BYN("Belarusian Ruble", "933"),
    BYR("Belarusian Ruble", "974"),
    BZD("Belize Dollar", "084"),
    BMD("Bermudian Dollar", "060"),
    VEB("Bolivar", "937"),
    BOB("Boliviano", "068"),
    XBA("Bond Markets Unit European Composite Unit (EURCO)", "955"),
    XBB("Bond Markets Unit European Monetary Unit (E.M.U.-6)", "956"),
    XBC("Bond Markets Unit European Unit of Account 9 (E.U.A.-9)", "957"),
    XBD("Bond Markets Unit European Unit of Account 17 (E.U.A.-17)", "958"),
    BRL("Brazilian Real", "986"),
    BND("Brunei Dollar", "096"),
    BGN("Bulgarian Lev", "975"),
    BIF("Burundi Franc", "108"),
    CVE("Cabo Verde Escudo", "132"),
    CAD("Canadian Dollar", "124"),
    KYD("Cayman Islands Dollar", "136"),
    XOF("CFA Franc BCEAO", "952"),
    XAF("CFA Franc BEAC", "950"),
    XPF("CFP Franc", "953"),
    CLP("Chilean Peso", "152"),
    COP("Colombian Peso", "170"),
    KMF("Comoro Franc", "174"),
    BAM("Convertible Mark", "977"),
    CDF("Congolese Franc", "957"),
    NIO("Cordoba Oro", "558"),
    CRC("Costa Rican Colon", "188"),
    CUP("Cuban Peso", "192"),
    CZK("Czech Koruna", "203"),
    GMD("Dalasi", "270"),
    DKK("Danish Krone", "208"),
    MKD("Denar", "807"),
    DJF("Djibouti Franc", "262"),
    STD("Dobra", "678"),
    DOP("Dominican Peso", "214"),
    VND("Dong", "704"),
    XCD("East Caribbean Dollar", "951"),
    EGP("Egyptian Pound", "818"),
    SVC("El Salvador Colon", "222"),
    ETB("Ethiopian Birr", "230"),
    EUR("Euro", "978"),
    FKP("Falkland Islands Pound", "238"),
    FJD("Fiji Dollar", "242"),
    HUF("Forint", "348"),
    GHS("Ghana Cedi", "936"),
    XAU("Gold", "959"),
    HTG("Gourde", "332"),
    GIP("Gibraltar Pound", "292"),
    PYG("Guarani", "600"),
    GNF("Guinea Franc", "324"),
    GYD("Guyana Dollar", "328"),
    HKD("Hong Kong Dollar", "344"),
    UAH("Hryvnia", "980"),
    INR("Indian Rupee", "356"),
    IQD("Iraqi Dinar", "368"),
    IRR("Iranian Rial", "364"),
    ISK("Iceland Krona", "352"),
    JMD("Jamaican Dollar", "388"),
    JOD("Jordanian Dinar", "400"),
    KES("Kenyan Shilling", "404"),
    PGK("Kina", "598"),
    LAK("Kip", "418"),
    HRK("Kuna", "191"),
    KWD("Kuwaiti Dinar", "414"),
    AOA("Kwanza", "973"),
    MMK("Kyat", "104"),
    GEL("Lari", "981"),
    LBP("Lebanese Pound", "422"),
    ALL("Lek", "008"),
    HNL("Lempira", "340"),
    SLL("Leone", "964"),
    LRD("Liberian Dollar", "430"),
    LYD("Libyan Dinar", "434"),
    SZL("Lilangeni", "748"),
    LSL("Loti", "426"),
    MGA("Malagasy Ariary", "969"),
    MWK("Malawi Kwacha", "454"),
    MYR("Malaysian Ringgit", "458"),
    MUR("Mauritius Rupee", "480"),
    MXN("Mexican Peso", "484"),
    MXV("Mexican Unidad de Inversion (UDI)", "979"),
    MDL("Moldovan Leu", "498"),
    MAD("Moroccan Dirham", "504"),
    MZM("Mozambique Metical", "943"),
    BOV("Mvdol", "984"),
    NGN("Naira", "566"),
    ERN("Nakfa", "232"),
    NAD("Namibia Dollar", "516"),
    NPR("Nepalese Rupee", "524"),
    ANG("Netherlands Antillean Gulden", "532"),
    ILS("New Israeli Sheqel", "376"),
    TWD("New Taiwan Dollar", "901"),
    NZD("New Zealand Dollar", "554"),
    BTN("Ngultrum", "064"),
    KPW("North Korean Won", "408"),
    NOK("Norwegian Krone", "578"),
    MRO("Ouguiya", "478"),
    TOP("Pa’anga", "776"),
    PKR("Pakistan Rupee", "586"),
    XPD("Palladium", "964"),
    MOP("Pataca", "446"),
    CUC("Peso Convertible", "931"),
    UYU("Peso Uruguayo", "858"),
    PHP("Philippine Peso", "608"),
    XPT("Platinum", "962"),
    GBP("Pound Sterling", "826"),
    BWP("Pula", "072"),
    QAR("Qatari Rial", "634"),
    GTQ("Quetzal", "320"),
    ZAR("Rand", "710"),
    OMR("Rial Omani", "512"),
    KHR("Riel", "116"),
    RON("Romanian Leu", "946"),
    MVR("Rufiyaa", "462"),
    IDR("Rupiah", "360"),
    RUB("Russian Ruble", "643"),
    RWF("Rwanda Franc", "646"),
    SHP("Saint Helena Pound", "654"),
    SAR("Saudi Riyal", "682"),
    RSD("Serbian Dinar", "941"),
    SCR("Seychelles Rupee", "690"),
    XAG("Silver", "961"),
    SGD("Singapore Dollar", "702"),
    PEN("Sol", "604"),
    SBD("Solomon Islands Dollar", "090"),
    KGS("Som", "417"),
    SOS("Somali Shilling", "706"),
    TJS("Somoni", "972"),
    SSP("South Sudanese Pound", "728"),
    XDR("Special Drawing Right (SDR)", "960"),
    LKR("Sri Lanka Rupee", "144"),
    XSU("Sucre", "994"),
    SDG("Sudanese Pound", "938"),
    SRD("Surinam Dollar", "968"),
    SEK("Swedish Krona", "752"),
    CHF("Swiss Franc", "756"),
    SYP("Syrian Pound", "760"),
    BDT("Taka", "050"),
    WST("Tala", "882"),
    TZS("Tanzanian Shilling", "834"),
    KZT("Tenge", "398"),
    TTD("Trinidad and Tobago Dollar", "780"),
    MNT("Tugrik", "496"),
    TND("Tunisian Dinar", "788"),
    TRY("Turkish Lira", "949"),
    TMT("Turkmenistan New Manat", "934"),
    AED("UAE Dirham", "784"),
    UGX("Uganda Shilling", "800"),
    CLF("Unidad de Fomento", "990"),
    COU("Unidad de Valor Real", "970"),
    UYI("Uruguay Peso en Unidades Indexadas (URUIURUI)", "940"),
    USD("US Dollar", "840"),
    UZS("Uzbekistani Sum", "860"),
    VUV("Vatu", "548"),
    CHE("WIR Euro", "947"),
    CHW("WIR Franc", "948"),
    KRW("Won", "410"),
    JPY("Yen", "392"),
    YER("Yemeni Rial", "886"),
    CNY("Yuan Renminbi", "156"),
    ZMW("Zambian Kwacha", "967"),
    ZML("Zimbabwe Dollar", "932"),
    PLN("Zloty", "985");

    String numericCode;
    String currencyName;

    CurrencyCode(String currencyName, String numericCode) {
        this.currencyName = currencyName;
        this.numericCode = numericCode;
    }

    public static CurrencyCode fromNumericCode(String numericCode) {
        for (CurrencyCode currency : CurrencyCode.values()) {
            if (currency.numericCode.equals(numericCode)) {
                return currency;
            }
        }
        return null;
    }

    public String getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public static String formatAmount(Double amount, String currencyNumericCode) {
        CurrencyCode currencyCode = CurrencyCode.fromNumericCode(currencyNumericCode);
        String balanceAmount = String.format(Locale.getDefault(), "%.2f", amount);;
        if (currencyCode != null) {
            try {
                Currency currency = Currency.getInstance(currencyCode.toString());
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(currency);
                balanceAmount = format.format(amount);
            } catch (Exception ex) {
                // Ignore this
            }
        }

        return balanceAmount;
    }
}
