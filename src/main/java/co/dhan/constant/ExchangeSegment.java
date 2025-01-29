package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @code : Used in WebSocket Communication. See API Docs: https://dhanhq.co/docs/v2/annexure/#exchange-segment
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum ExchangeSegment {
    IDX_I(Exchange.IDX, Segment.I,0),
    NSE_EQ(Exchange.NSE, Segment.EQ,1),
    NSE_FNO(Exchange.NSE,Segment.FNO,2),
    NSE_CURRENCY(Exchange.NSE,Segment.CURRENCY,3),
    BSE_EQ(Exchange.BSE,Segment.EQ,4),
    BSE_FNO(Exchange.BSE,Segment.FNO,8),
    BSE_CURRENCY(Exchange.BSE,Segment.CURRENCY,7),
    MCX_COMM(Exchange.MCX,Segment.COMM,5);

    private Exchange exchange;
    private Segment segment;
    private int code;

    public static ExchangeSegment findByCode(int code) {
        for (ExchangeSegment value : ExchangeSegment.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        log.error("Unknown Exchange Segment Code : " + code);
        return null;
    }
}
