package co.dhan.dto;

import co.dhan.helper.BigDecimalUtils;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class FundSummary {
    private String dhanClientId;
    /**
     * Available balance to trade
     */
    @Getter private BigDecimal availabelBalance;
    /**
     * Start of the day amount
     */
    @Getter private BigDecimal sodLimit;
    /**
     * Amount received against collateral
     */
    @Getter private BigDecimal collateralAmount;
    /**
     * Amount available against selling deliveries
     */
    @Getter private BigDecimal receiveableAmount;
    /**
     * Amount utilised in the day
     */
    @Getter private BigDecimal utilizedAmount;
    /**
     * Amount blocked against payout request
     */
    @Getter private BigDecimal blockedPayoutAmount;
    /**
     * Amount available to withdraw to bank
     */
    @Getter private BigDecimal withdrawableBalance;

    public void setAvailabelBalance(String decimalValue) {
        this.availabelBalance = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setSodLimit(String decimalValue) {
        this.sodLimit = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setCollateralAmount(String decimalValue) {
        this.collateralAmount = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setReceiveableAmount(String decimalValue) {
        this.receiveableAmount = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setUtilizedAmount(String decimalValue) {
        this.utilizedAmount = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setBlockedPayoutAmount(String decimalValue) {
        this.blockedPayoutAmount = BigDecimalUtils.toBigDecimal(decimalValue);
    }

    public void setWithdrawableBalance(String decimalValue) {
        this.withdrawableBalance = BigDecimalUtils.toBigDecimal(decimalValue);
    }
}
