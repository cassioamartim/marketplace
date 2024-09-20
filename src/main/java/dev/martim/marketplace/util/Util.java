package dev.martim.marketplace.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Util {

    public static String formatNumber(double value) {
        return new DecimalFormat("#,###.#", new DecimalFormatSymbols(Locale.US)).format(value);
    }

    public static boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
