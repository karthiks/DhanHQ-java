package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeSegment {
    IDX_I(Exchange.IDX, Segment.I),
    NSE_EQ(Exchange.NSE, Segment.EQ),
    NSE_FNO(Exchange.NSE,Segment.FNO),
    NSE_CURRENCY(Exchange.NSE,Segment.CURRENCY),
    BSE_EQ(Exchange.BSE,Segment.EQ),
    BSE_FNO(Exchange.BSE,Segment.FNO),
    BSE_CURRENCY(Exchange.BSE,Segment.CURRENCY),
    MCX_COMM(Exchange.MCX,Segment.COMM);

    private Exchange exchange;
    private Segment segment;
}
