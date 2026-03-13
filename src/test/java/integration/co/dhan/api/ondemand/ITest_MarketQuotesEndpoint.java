package co.dhan.api.ondemand;

import static org.assertj.core.api.Assertions.assertThat;

import co.dhan.api.ITest_DhanTestRoot;
import co.dhan.constant.ExchangeSegment;
import co.dhan.dto.Candlestick;
import co.dhan.dto.ExchangeSegmentSecurities;
import co.dhan.http.DhanAPIException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ITest_MarketQuotesEndpoint extends ITest_DhanTestRoot {

  @Test
  public void getLTPForSecurities() throws DhanAPIException {
    ExchangeSegmentSecurities exchangeSegmentSecurities = new ExchangeSegmentSecurities();
    exchangeSegmentSecurities.add(ExchangeSegment.BSE_EQ, Set.of("1026077", "1026078"));
    Map<ExchangeSegment, List<Candlestick>> map =
        dhanCore
            .getMarketQuotesEndpoint()
            .getLTPFor(exchangeSegmentSecurities)
            .getExchangeSegmentCandlesticksMap();
    assertThat(map).isNotNull();
  }
}
