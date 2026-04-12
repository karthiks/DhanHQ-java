package co.dhan.dto;

import co.dhan.constant.*;
import co.dhan.constant.SuperOrderStatus;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * Represents a super order in the Dhan trading system. A super order consists of multiple order
 * legs that can be executed based on trigger conditions.
 */
@Data
public class SuperOrder {
  /** Unique identifier for the super order */
  private String superOrderId;

  /** User/partner generated id for tracking back (length >=0 and <=25 characters) */
  private String correlationId;

  /** Overall status of the super order */
  private SuperOrderStatus orderStatus;

  /** List of individual order legs in this super order */
  private List<OrderLeg> orderLegs;

  /** Timestamp when the super order was created */
  private String createTime;

  /** Timestamp when the super order was last updated */
  private String updateTime;

  /** Timestamp from the exchange */
  private String exchangeTime;

  /** Algorithm ID if this is an algorithmic order */
  private String algoId;

  /** Product type (CNC, NRML, MARGIN, etc.) */
  private ProductType productType;

  /** Order type (LIMIT, MARKET, etc.) */
  private OrderType orderType;

  /** Validity (DAY, IOC, GTT, etc.) */
  private Validity validity;

  /** Exchange segment (NSE_EQ, NSE_FNO, BSE_EQ, etc.) */
  private ExchangeSegment exchangeSegment;

  /** Transaction type (BUY or SELL) */
  private TransactionType transactionType;

  /** Total quantity across all legs */
  private int totalQuantity;

  /** Represents an individual leg of a super order */
  @Data
  public static class OrderLeg {
    /** Unique identifier for this order leg */
    private String orderId;

    /** Identifies which leg this is (LEG_1, LEG_2, etc.) */
    private LegName legName;

    /** Status of this order leg */
    private OrderStatus orderStatus;

    /** Transaction type for this leg */
    private TransactionType transactionType;

    /** Exchange standard identification for each scrip */
    private String securityId;

    /** Trading symbol */
    private String tradingSymbol;

    /** Exchange segment */
    private ExchangeSegment exchangeSegment;

    /** Product type */
    private ProductType productType;

    /** Order type */
    private OrderType orderType;

    /** Validity */
    private Validity validity;

    /** Quantity for this leg */
    private int quantity;

    /** Number shares visible in the market depth */
    private int disclosedQuantity;

    /** Order price */
    private BigDecimal price;

    /** Trigger price */
    private BigDecimal triggerPrice;

    /** Number of shares filled */
    private int filledQty;

    /** Remaining quantity to be filled */
    private int remainingQuantity;

    /** Average traded price */
    private BigDecimal averageTradedPrice;

    /** After market order flag */
    private boolean afterMarketOrder;

    /** Option type for derivatives */
    private OptionType drvOptionType;

    /** Derivative strike price */
    private BigDecimal drvStrikePrice;

    /** Derivative expiry date */
    private String drvExpiryDate;

    /** Algorithm ID */
    private String algoId;

    /** OMS error code */
    private String omsErrorCode;

    /** OMS error description */
    private String omsErrorDescription;
  }
}
