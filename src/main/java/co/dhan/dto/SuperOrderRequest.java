package co.dhan.dto;

import co.dhan.constant.*;
import co.dhan.constant.TriggerOperator;
import co.dhan.constant.TriggerType;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Request object for placing a new super order. Contains the configuration for multiple order legs
 * and their trigger conditions.
 */
@Data
@Builder
public class SuperOrderRequest {
  /** List of individual order leg requests */
  private List<SuperOrderLegRequest> legs;

  /** List of trigger conditions between legs */
  private List<TriggerCondition> conditions;

  /** Order type (LIMIT, MARKET, etc.) */
  private OrderType orderType;

  /** Validity (DAY, IOC, GTT, etc.) */
  private Validity validity;

  /** Product type (CNC, NRML, MARGIN, etc.) */
  private ProductType productType;

  /** User/partner generated id for tracking back (length >=0 and <=25 characters) */
  private String correlationId;

  /** Request object for an individual leg of a super order */
  @Data
  @Builder
  public static class SuperOrderLegRequest {
    /** Exchange standard identification for each scrip */
    private String securityId;

    /** Exchange segment */
    private ExchangeSegment exchangeSegment;

    /** Transaction type (BUY or SELL) */
    private TransactionType transactionType;

    /** Order type (LIMIT, MARKET, etc.) */
    private OrderType orderType;

    /** Quantity for this leg */
    private int quantity;

    /** Order price */
    private BigDecimal price;

    /** Trigger price */
    private BigDecimal triggerPrice;

    /** Identifies which leg this is (LEG_1, LEG_2, etc.) */
    private LegName legName;

    /** Number shares visible in the market depth */
    private int disclosedQuantity;

    /** After market order flag */
    private boolean afterMarketOrder;

    /** AMO time (OPEN, OPEN_30, OPEN_60) */
    private AMOTime amoTime;

    /** Profit value for bracket orders */
    private BigDecimal boProfitValue;

    /** Stop loss value for bracket orders */
    private BigDecimal boStopLossValue;

    /** Derivative expiry date */
    private String drvExpiryDate;

    /** Derivative option type */
    private OptionType drvOptionType;

    /** Derivative strike price */
    private BigDecimal drvStrikePrice;

    /** Algorithm ID */
    private String algoId;

    /** OMS error code */
    private String omsErrorCode;

    /** OMS error description */
    private String omsErrorDescription;

    /** Filled quantity */
    private int filledQty;

    /** Remaining quantity */
    private int remainingQuantity;

    /** Average traded price */
    private BigDecimal averageTradedPrice;
  }

  /** Trigger condition that controls when legs execute based on price/quantity of another leg */
  @Data
  @Builder
  public static class TriggerCondition {
    /** Type of trigger (PRICE_TRIGGER, QUANTITY_TRIGGER) */
    private TriggerType type;

    /** Which leg triggers this condition (fromLeg) */
    private String fromLeg;

    /** Which leg gets triggered (toLeg) */
    private String toLeg;

    /** Price/quantity threshold value */
    private BigDecimal triggerValue;

    /** Comparison operator (GREATER_THAN, LESS_THAN, EQUALS) */
    private TriggerOperator operator;

    /** Whether the condition has been met */
    private boolean isMet;
  }
}
