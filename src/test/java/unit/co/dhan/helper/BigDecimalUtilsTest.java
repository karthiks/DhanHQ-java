package co.dhan.helper;

import co.dhan.UnitTestRoot;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BigDecimalUtilsTest extends UnitTestRoot {
    @Test
    void toBigDecimal_FromDouble() {
        double input = 123.456;
        assertThat(BigDecimalUtils.toBigDecimal(input))
                .isEqualTo(new BigDecimal("123.46"));
    }

    @Test
    void toBigDecimal_FromString() {
        String input = "123.456";
        assertThat(BigDecimalUtils.toBigDecimal(input))
                .isEqualTo(new BigDecimal("123.46"));
    }

    @Test
    void toBigDecimal_FromStringWithCustomScaleAndRounding() {
        String input = "123.456789";
        int scale = 4;
        RoundingMode roundingMode = RoundingMode.DOWN;

        BigDecimal result = BigDecimalUtils.toBigDecimal(input, scale, roundingMode);
        assertThat(result).isEqualTo(new BigDecimal("123.4567"));
    }

    @Test
    void testToBigDecimal_FromNullString() {
        String input = null;
        assertThat(BigDecimalUtils.toBigDecimal(input))
                .isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void testToBigDecimal_FromEmptyString() {
        String input = "";
        assertThat(BigDecimalUtils.toBigDecimal(input))
                .isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void testToBigDecimal_FromInvalidString() {
        String input = "invalid";
        assertThatThrownBy(() -> BigDecimalUtils.toBigDecimal(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid price format");
    }

    @Test
    void testAdd() {
        BigDecimal a = new BigDecimal("10.50");
        BigDecimal b = new BigDecimal("20.25");

        BigDecimal result = BigDecimalUtils.add(a, b);
        assertThat(result).isEqualTo(new BigDecimal("30.75"));
    }

    @Test
    void testSubtract() {
        BigDecimal a = new BigDecimal("30.75");
        BigDecimal b = new BigDecimal("10.50");

        BigDecimal result = BigDecimalUtils.subtract(a, b);
        assertThat(result).isEqualTo(new BigDecimal("20.25"));
    }

    @Test
    void testMultiply() {
        BigDecimal a = new BigDecimal("10.50");
        BigDecimal b = new BigDecimal("2");

        BigDecimal result = BigDecimalUtils.multiply(a, b);
        assertThat(result).isEqualTo(new BigDecimal("21.00"));
    }

    @Test
    void testDivide() {
        BigDecimal a = new BigDecimal("21.00");
        BigDecimal b = new BigDecimal("2");

        BigDecimal result = BigDecimalUtils.divide(a, b);
        assertThat(result).isEqualTo(new BigDecimal("10.50"));
    }

    @Test
    void testDivide_ByZero() {
        BigDecimal a = new BigDecimal("21.00");
        BigDecimal b = new BigDecimal("0");

        assertThatThrownBy(() -> BigDecimalUtils.divide(a, b))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("by zero");
    }
}
