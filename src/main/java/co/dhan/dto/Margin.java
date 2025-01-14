package co.dhan.dto;

import co.dhan.helper.BigDecimalUtils;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class Margin {
    /**
     * Applicable leverage
     */
    private String leverage;
    /**
     * Total available margin to trade
     */
    @Getter private BigDecimal totalMargin;
    /**
     * Available span margin
     */
    @Getter private BigDecimal spanMargin;
    /**
     * Available exposure margin
     */
    @Getter private BigDecimal exposureMargin;
    /**
     * Available balance to trade
     */
    @Getter private BigDecimal availableBalance;
    @Getter private BigDecimal variableMargin;
    @Getter private BigDecimal insufficientBalance;

    public void setTotalMargin(String decimalValue) {
        this.totalMargin = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setSpanMargin(String decimalValue) {
        this.spanMargin = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setExposureMargin(String decimalValue) {
        this.exposureMargin = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setAvailableBalance(String decimalValue) {
        this.availableBalance = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setVariableMargin(String decimalValue) {
        this.variableMargin = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setInsufficientBalance(String decimalValue) {
        this.insufficientBalance = BigDecimalUtils.toBigDecimal(decimalValue);
    }
}
