package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.OptionType;
import co.dhan.constant.PositionType;
import co.dhan.constant.ProductType;
import co.dhan.helper.BigDecimalUtils;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class Position {

    private String dhanClientId;

    /**
     * Exchange standard trading symbol
     */
    private String tradingSymbol;

    /**
     * Exchange standard identification for each scrip
     */
    private String securityId;

    private PositionType positionType;
    private ExchangeSegment exchangeSegment;
    private ProductType productType;
    private int buyQty;
    private int sellQty;
    /**
     * netQty = buyQty - sellQty
     */
    private int netQty;
    /**
     * Multiplying factor for currency F&O
     */
    private int multiplier;
    /**
     * Carry forward F&O long quantities
     */
    private int carryForwardBuyQty;
    /**
     * Carry forward F&O short quantities
     */
    private int carryForwardSellQty;
    /**
     * Carry forward F&O long value
     */
    private int carryForwardBuyValue;
    /**
     * Carry forward F&O short value
     */
    private int carryForwardSellValue;
    /**
     * Quantities bought today
     */
    private int dayBuyQty;
    /**
     * Quantities sold today
     */
    private int daySellQty;
    /**
     * For F&O, expiry date of contract
     */
    private String drvExpiryDate;
    private OptionType drvOptionType;
    private boolean crossCurrency;

    /**
     * Average buy price
     */
    @Getter private BigDecimal buyAvg;
    @Getter private BigDecimal sellAvg;
    @Getter private BigDecimal costPrice;
    /**
     * Profit or loss booked
     */
    @Getter private BigDecimal realizedProfit;
    /**
     * Profit or loss standing for open position
     */
    @Getter private BigDecimal unrealizedProfit;
    /**
     * RBI mandated reference rate for forex
     */
    @Getter private BigDecimal rbiReferenceRate;
    /**
     * Value of quantities bought today
     */
    @Getter private BigDecimal dayBuyValue;
    /**
     * Value of quantities sold today
     */
    @Getter private BigDecimal daySellValue;
    /**
     * For Options, Strike Price
     */
    @Getter private BigDecimal drvStrikePrice;

    public void setDrvStrikePrice(String decimalValue) {
        this.drvStrikePrice = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setDaySellValue(String decimalValue) {
        this.daySellValue = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setDayBuyValue(String decimalValue) {
        this.dayBuyValue = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setRbiReferenceRate(String decimalValue) {
        this.rbiReferenceRate = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setUnrealizedProfit(String decimalValue) {
        this.unrealizedProfit = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setRealizedProfit(String decimalValue) {
        this.realizedProfit = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setCostPrice(String decimalValue) {
        this.costPrice = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setSellAvg(String decimalValue) {
        this.sellAvg = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setBuyAvg(String decimalValue) {
        this.buyAvg = BigDecimalUtils.toBigDecimal(decimalValue);
    }
}
