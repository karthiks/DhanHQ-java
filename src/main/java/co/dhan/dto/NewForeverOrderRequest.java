package co.dhan.dto;

import co.dhan.constant.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class NewForeverOrderRequest {

    /**
     * Exchange standard identification for each scrip
     */
    private String securityId;
    /**
     * The user/partner generated id for tracking back.
     * Its length >=0 and <=25 characters.
     */
    private String correlationId;
    private ExchangeSegment exchangeSegment;

    private OrderFlag orderFlag = OrderFlag.SINGLE;
    private OrderType orderType;
    private Validity validity = Validity.DAY;

    private TransactionType transactionType;
    private ProductType productType;

    private int quantity;
    private int quantity1;
    /**
     * Number shares visible in the market depth
     */
    private int disclosedQuantity;

    private BigDecimal price;
    private BigDecimal price1;

    private BigDecimal triggerPrice;
    private BigDecimal triggerPrice1;

}
