package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ExchangeSegmentCandlestickWrapper {
    public static String JSONPropertyData = "data";

    private Map<ExchangeSegment, List<Candlestick>> exchangeSegmentSecuritiesLTPMap;
}
