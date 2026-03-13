package co.dhan.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** The lowest price a seller is willing to accept for a security. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ask {
  public static String JSONPropertyQuantity = "quantity";
  public static String JSONPropertyOrders = "orders";
  public static String JSONPropertyPrice = "price";

  private BigDecimal price;
  private int quantity;
  private int orders;
}
