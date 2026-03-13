package co.dhan.dto;

import co.dhan.constant.LegName;
import co.dhan.constant.OrderType;
import co.dhan.constant.Validity;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyOrderRequest {

  private String orderId;
  private OrderType orderType;
  private LegName legName;
  private Validity validity = Validity.DAY;

  private BigDecimal price;
  private BigDecimal triggerPrice;
  private int quantity;

  /** Number shares visible in the market depth */
  private int disclosedQuantity;
}
