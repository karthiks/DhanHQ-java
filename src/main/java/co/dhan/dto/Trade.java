package co.dhan.dto;

import lombok.Data;

@Data
public class Trade {
    public static String JSONPropertyQuantity = "quantity";
    public static String JSONPropertyOrders = "orders";
    public static String JSONPropertyPrice = "price";

    private String quantity;
    private String orders;
    private String price;
}
