package co.dhan.dto;

import co.dhan.constant.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class NewOrderRequest {

    /**
     * Exchange standard identification for each scrip
     */
    private String securityId;
    private ExchangeSegment exchangeSegment;
    private TransactionType transactionType;

    private OrderType orderType;
    private ProductType productType;
    private Validity validity = Validity.DAY;

    private BigDecimal price;
    private BigDecimal triggerPrice;
    private int quantity;
    /**
     * Number shares visible in the market depth
     */
    private int disclosedQuantity;

    private boolean afterMarketOrder;
    private AMOTime amoTime;

    private BigDecimal boProfitValue;
    private BigDecimal boStopLossValue;

    /**
     * The user/partner generated id for tracking back.
     * Its length >=0 and <=25 characters.
     */
    private String correlationId;

}
