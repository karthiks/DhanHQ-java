package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedRequestCode {
    CONNECT_FEED(11),
    DISCONNECT_FEED(12),
    SUBSCRIBE_TICK(15),
    UNSUBSCRIBE_TICK(16),
    SUBSCRIBE_QUOTE(17),
    UNSUBSCRIBE_QUOTE(18),
    SUBSCRIBE_FULL_QUOTE(21),
    UNSUBSCRIBE_FULL_QUOTE(22),
    SUBSCRIBE_20_LEVEL_DEPTH(23),
    UNSUBSCRIBE_20_LEVEL_DEPTH(24);

    private final int code;

    public static FeedRequestCode findByCode(int code) {
        for (FeedRequestCode value : FeedRequestCode.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
