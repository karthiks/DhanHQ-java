package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeSegment {
    NSE_EQ(Exchange.NSE, Segment.EQ),
    NSE_FNO(Exchange.NSE,Segment.FNO),
    BSE_EQ(Exchange.BSE,Segment.EQ),
    BSE_FNO(Exchange.BSE,Segment.FNO),
    MCX_COMM(Exchange.MCX,Segment.COMM);

    private Exchange exchange;
    private Segment segment;
}
