package co.dhan.dto;

import lombok.Data;

@Data
public class Quote {
    public static String JSONPropertyLastPrice = "last_price";
    public static String JSONPropertyLastQuantity = "last_quantity";
    public static String JSONPropertyLastTradeTime = "last_trade_time";
    public static String JSONPropertyAveragePrice = "average_price";
    public static String JSONPropertyBuyQuantity = "buy_quantity";
    public static String JSONPropertySellQuantity = "sell_quantity";
    public static String JSONPropertyDepth = "depth";
    public static String JSONPropertyLowerCircuitLimit = "lower_circuit_limit";
    public static String JSONPropertyUpperCircuitLimit = "upper_circuit_limit";
    public static String JSONPropertyNetChange = "net_change";
    public static String JSONPropertyOHLC = "ohlc";
    public static String JSONPropertyOpen = "open";
    public static String JSONPropertyClose = "close";
    public static String JSONPropertyHigh = "high";
    public static String JSONPropertyLow = "low";
    public static String JSONPropertyOI = "oi";
    public static String JSONPropertyOIDayHigh = "oi_day_high";
    public static String JSONPropertyOIDayLow = "oi_day_low";
    public static String JSONPropertyVolume = "volume";

    private String security;
    private String lastPrice;
    private String lastQuantity;
    private String lastTradeTime;
    /**
     * Average Trade Price (ATP)
     */
    private String averagePrice;
    private String volume;
    /**
     * Total Buy Quantity
     */
    private String buyQuantity;
    /**
     * Total Sell Quantity
     */
    private String sellQuantity;
    private String open;
    private String close;
    private String high;
    private String low;
    private String lowerCircuitLimit;
    private String upperCircuitLimit;
    private String netChange;
    /**
     * Open Interest in the contract (for Derivatives)
     */
    private String oi;
    /**
     * Highest Open Interest for the day (only for NSE_FNO)
     */
    private String oiDayHigh;
    /**
     * Lowest Open Interest for the day (only for NSE_FNO)
     */
    private String oiDayLow;
    private Depth depth;
}
