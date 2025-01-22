package co.dhan.dto;

import lombok.Data;

import java.util.List;

@Data
public class Depth {
    public static String JSONPropertyBuy = "buy";
    public static String JSONPropertySell = "sell";

    private List<Bid> buy;
    private List<Ask> sell;
}
