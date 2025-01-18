package co.dhan.api.ondemand;

import co.dhan.api.E2EDhanTestRoot;
import co.dhan.constant.ExchangeSegment;
import co.dhan.dto.ExchangeSegmentSecurities;
import co.dhan.dto.Candlestick;
import co.dhan.http.DhanAPIException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class E2EMarketFeedEndpointTest extends E2EDhanTestRoot {

    @Test
    public void getLTPForSecurities() throws DhanAPIException {
        ExchangeSegmentSecurities exchangeSegmentSecurities = new ExchangeSegmentSecurities();
        exchangeSegmentSecurities.add(ExchangeSegment.BSE_EQ, Set.of("1026077", "1026078"));
        Map<ExchangeSegment, List<Candlestick>> map = dhanCore
                .getMarketFeedEndpoint()
                .getLTPFor(exchangeSegmentSecurities)
                .getExchangeSegmentCandlesticksMap();
        System.out.println("--Map<ExchangeSegment, List<SecurityLTP>> map--");
        map.forEach((exchangeSegment, securityLTPS) -> {
            System.out.println("exchangeSegment: " + exchangeSegment + ", securities :");
            securityLTPS.forEach(System.out::println);
        });
        assertThat(map).isNotNull();
    }
}
