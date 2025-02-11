package co.dhan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The highest price a buyer is willing to pay for a security.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bid {
    public static String JSONPropertyQuantity = "quantity";
    public static String JSONPropertyOrders = "orders";
    public static String JSONPropertyPrice = "price";

    private BigDecimal price;
    private int quantity;
    private int orders;
}
