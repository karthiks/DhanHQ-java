package co.dhan.dto;

import co.dhan.constant.LegName;
import co.dhan.constant.OrderType;
import co.dhan.constant.Validity;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * Request object for modifying a pending super order. Contains the modification details for a
 * single super order leg.
 */
@Data
@Builder
public class SuperOrderModificationRequest {

  /** The super order ID to modify */
  private String orderId;

  /** Order type (LIMIT, MARKET, etc.) */
  private OrderType orderType;

  /** Identifies which leg this is (LEG_1, LEG_2, etc.) */
  private LegName legName;

  /** Validity (DAY, IOC, GTT, etc.) */
  private Validity validity = Validity.DAY;

  /** Quantity for this leg */
  private int quantity;

  /** Order price */
  private BigDecimal price;

  /** Trigger price */
  private BigDecimal triggerPrice;

  /** Number shares visible in the market depth */
  private int disclosedQuantity;
}
