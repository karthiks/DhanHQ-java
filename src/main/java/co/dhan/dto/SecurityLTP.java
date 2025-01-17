package co.dhan.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SecurityLTP {

    public static String JSONPropertyLastPrice = "last_price";
    public static String JSONPropertyOHLC = "ohlc";
    public static String JSONPropertyOpen = "open";
    public static String JSONPropertyClose = "close";
    public static String JSONPropertyHigh = "high";
    public static String JSONPropertyLow = "low";

    private String security;
    private String ltp;
    private String open;
    private String close;
    private String high;
    private String low;

    public SecurityLTP(String security, String ltp) {
        this.security = security;
        this.ltp = ltp;
    }

    public SecurityLTP(String security, String ltp, String open, String close, String high, String low) {
        this.security = security;
        this.ltp = ltp;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }
}
