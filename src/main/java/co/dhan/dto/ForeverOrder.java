package co.dhan.dto;

import co.dhan.constant.*;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForeverOrder {
  private String orderId;
  private OrderStatus orderStatus;
  private OrderFlag orderFlag;
  private LegName legName;

  private String exchangeOrderId;

  /** Exchange standard identification for each scrip */
  private String securityId;

  private String tradingSymbol;
  private ExchangeSegment exchangeSegment;
  private TransactionType transactionType;
  private ProductType productType;

  private int quantity;
  private BigDecimal price;
  private BigDecimal triggerPrice;

  private String createTime;
  private String updateTime;
  private String exchangeTime;

  private String drvExpiryDate;
  private OptionType drvOptionType;
  private BigDecimal drvStrikePrice;
}
