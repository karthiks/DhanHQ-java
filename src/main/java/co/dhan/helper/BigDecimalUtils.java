package co.dhan.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {
    public static final int DEFAULT_SCALE = 2;
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal toBigDecimal(String value) {
        return toBigDecimal(value, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public static BigDecimal toBigDecimal(String value, int scale, RoundingMode roundingMode) {
        if (value == null || value.isEmpty()) {
            value = "0.0";
        }
        try {
            BigDecimal bd = new BigDecimal(value);
            return bd.setScale(scale, roundingMode);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format: " + value, e);
        }
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a.add(b).toString());
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a.subtract(b).toString());
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a.multiply(b).toString());
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a.divide(b, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE).toString());
    }
}
