package co.dhan.dto;

import co.dhan.helper.BigDecimalUtils;
import lombok.Data;

/**
 * The highest price a buyer is willing to pay for a security.
 */
@Data
public class Bid {
    public static String JSONPropertyQuantity = "quantity";
    public static String JSONPropertyOrders = "orders";
    public static String JSONPropertyPrice = "price";

    private String price;
    private String quantity;
    private String orders;

    public void setPrice(String price) {
        this.price = BigDecimalUtils.toBigDecimal(price).stripTrailingZeros().toString();
    }
}
