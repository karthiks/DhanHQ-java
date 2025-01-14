package co.dhan.dto;

import co.dhan.constant.Exchange;
import co.dhan.helper.BigDecimalUtils;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class Holding {
    private Exchange exchange;

    /**
     * Exchange standard trading symbol
     */
    private String tradingSymbol;
    /**
     * Exchange standard identification for each scrip
     */
    private String securityId;
    /**
     * International Securities Identification Number
     */
    private String isin;
    /**
     * Total number of shares in holding for given stock
     */
    private int totalQty;
    /**
     * Quantities present in depository
     */
    private int dpQty;
    /**
     * Quantities not delivered to depository
     */
    private int t1Qty;
    /**
     * Quantities available for transactions
     */
    private int availableQty;
    /**
     * Quantities placed as collateral with broker
     */
    private int collateralQty;

    /**
     * Average Buy Price of total quantities
     */
    @Getter private BigDecimal avgCostPrice;
    /**
     * Last Traded Price (LTP) for the scrip
     */
    @Getter private BigDecimal lastTradedPrice;

    public void setLastTradedPrice(String decimalValue) {
        this.lastTradedPrice = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setAvgCostPrice(String decimalValue) {
        this.avgCostPrice = BigDecimalUtils.toBigDecimal(decimalValue);
    }
}
