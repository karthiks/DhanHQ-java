package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.ProductType;
import co.dhan.constant.TransactionType;
import java.math.BigDecimal;
import lombok.Data;

/**
 * Request object for margin calculation for a single order. Used in both single and multi-order
 * margin calculations.
 */
@Data
public class MarginRequest {

  /** Security ID of the instrument */
  private String securityID;

  /** Exchange segment (e.g., NSE_EQ, NSE_FO) */
  private ExchangeSegment exchangeSegment;

  /** Transaction type (BUY or SELL) */
  private TransactionType transactionType;

  /** Quantity of the instrument */
  private int quantity;

  /** Product type (e.g., CNC, INTRADAY) */
  private ProductType productType;

  /** Price of the instrument */
  private BigDecimal price;

  /** Trigger price (for stop-loss orders) */
  private BigDecimal triggerPrice;
}
