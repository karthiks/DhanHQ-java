package co.dhan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
  private String dhanClientId;
  private String orderId;
  private String exchangeOrderId;
  private String exchangeTradeId;
  private String transactionType;
  private String exchangeSegment;
  private String productType;
  private String orderType;
  private String tradingSymbol;
  private String securityId;
  private Integer tradedQuantity;
  private Double tradedPrice;
  private String createTime;
  private String updateTime;
  private String exchangeTime;
  private String drvExpiryDate;
  private String drvOptionType;
  private Double drvStrikePrice;
}
