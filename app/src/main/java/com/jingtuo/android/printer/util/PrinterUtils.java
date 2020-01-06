package com.jingtuo.android.printer.util;

import java.text.NumberFormat;
import java.util.Locale;

public class PrinterUtils {

    private PrinterUtils() {

    }

    /**
     * 转换成毫米
     * @param mils (thousandths of an inch).
     * @return
     */
    public static float convertToMill(int mils) {
        return 25.4f * mils / 1000;
    }

    /**
     * 转换成英寸的千分之一
     * @param mill 毫米
     * @return
     */
    public static float convertToMils(float mill) {
        return mill * 1000 / 25.4f;
    }

    /**
     * units are in points (1/72 of an inch)
     * @param mill
     * @return
     */
    public static float convertToPosition(float mill) {
       return mill * 72 / 25.4f;
    }

    /**
     *
     * @param value
     * @return
     */
    public static String formatNumber(float value) {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.getDefault());
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(value);
    }
}
