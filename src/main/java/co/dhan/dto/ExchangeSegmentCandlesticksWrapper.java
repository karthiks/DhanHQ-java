package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeSegmentCandlesticksWrapper {
  public static String JSONPropertyData = "data";

  private Map<ExchangeSegment, List<Candlestick>> exchangeSegmentCandlesticksMap;
}
