package co.dhan.dto;

import co.dhan.constant.LegName;
import co.dhan.constant.OrderFlag;
import co.dhan.constant.OrderType;
import co.dhan.constant.Validity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ModifyForeverOrderRequest {

    private String orderId;

    private OrderFlag orderFlag = OrderFlag.SINGLE;
    private OrderType orderType;
    private LegName legName;
    private Validity validity = Validity.DAY;

    private int quantity;
    /**
     * Number shares visible in the market depth
     */
    private int disclosedQuantity;

    private BigDecimal price;
    private BigDecimal triggerPrice;
}
