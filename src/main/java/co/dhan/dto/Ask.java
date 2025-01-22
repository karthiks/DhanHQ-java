package co.dhan.dto;

import lombok.Data;

/**
 * The lowest price a seller is willing to accept for a security.
 */
@Data
public class Ask {
    public static String JSONPropertyQuantity = "quantity";
    public static String JSONPropertyOrders = "orders";
    public static String JSONPropertyPrice = "price";

    private String quantity;
    private String orders;
    private String price;
}
