package co.dhan.dto;

import java.util.List;
import lombok.Data;

@Data
public class Depth {
  public static String JSONPropertyBuy = "buy";
  public static String JSONPropertySell = "sell";

  private List<Bid> buy;
  private List<Ask> sell;
}
