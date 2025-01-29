package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedResponseCode {
    INDEX_PACKET(1),
    TICKER_PACKET(2),
    QUOTE_PACKET(4),
    OI_PACKET(5),
    PREV_CLOSE_PACKET(6),
    MARKET_STATUS_PACKET(7),
    FULL_PACKET(8),
    FEED_DISCONNECT(50);

    private final int code;

    public static FeedResponseCode findByCode(int code) {
        for (FeedResponseCode value : FeedResponseCode.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
