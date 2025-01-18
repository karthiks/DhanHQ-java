package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.constant.ExchangeSegment;
import co.dhan.dto.Candlestick;
import co.dhan.dto.ExchangeSegmentSecurities;
import co.dhan.dto.Quote;
import co.dhan.http.DhanAPIException;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static co.dhan.api.ondemand.MarketFeedEndpoint.APIEndpoint.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class MarketFeedEndpointTest extends UnitTestRoot {

    @Mock
    private DhanConnection mockDhanConnection;
    @Mock
    private DhanHTTP mockDhanHTTP;

    @InjectMocks
    private MarketFeedEndpoint marketFeedEndpoint;

    @Test
    public void testGetLTPForSecuritiesOnSuccessResponse() throws DhanAPIException, IOException, URISyntaxException {
        String expectedResponse = getExpectedResponseFromResource("/data/marketfeed-ltp.json");
        DhanResponse stubDhanResponse = new DhanResponse(expectedResponse);

        ExchangeSegmentSecurities exchangeSegmentSecurities = new ExchangeSegmentSecurities();
        exchangeSegmentSecurities.add(ExchangeSegment.NSE_EQ, Set.of("11536"));
        exchangeSegmentSecurities.add(ExchangeSegment.NSE_FNO, Set.of("49081","49082"));

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP
                .doHttpPostRequest(GetLTPForSecurities, exchangeSegmentSecurities.toMapOfStrings()))
                .thenReturn(stubDhanResponse);

        Map<ExchangeSegment, List<Candlestick>> map = marketFeedEndpoint
                .getLTPFor(exchangeSegmentSecurities)
                .getExchangeSegmentCandlesticksMap();

        assertThat(map.get(ExchangeSegment.NSE_EQ))
                .isEqualTo(List.of(new Candlestick("11536", "4520")));
        assertThat(map.get(ExchangeSegment.NSE_FNO))
                .isEqualTo(List.of(
                        new Candlestick("49081", "368.15"),
                        new Candlestick("49082","694.35")));
    }

    @Test
    public void testGetOHLCForSecuritiesOnSuccessResponse() throws DhanAPIException, URISyntaxException, IOException {
        String expectedResponse = getExpectedResponseFromResource("/data/marketfeed-ohlc.json");
        DhanResponse stubDhanResponse = new DhanResponse(expectedResponse);

        ExchangeSegmentSecurities exchangeSegmentSecurities = new ExchangeSegmentSecurities();
        exchangeSegmentSecurities.add(ExchangeSegment.NSE_EQ, Set.of("11536"));
        exchangeSegmentSecurities.add(ExchangeSegment.NSE_FNO, Set.of("49081","49082"));

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP
                .doHttpPostRequest(GetOHLCForSecurities,exchangeSegmentSecurities.toMapOfStrings()))
                .thenReturn(stubDhanResponse);

        Map<ExchangeSegment, List<Candlestick>> map = marketFeedEndpoint
                .getOHLCFor(exchangeSegmentSecurities)
                .getExchangeSegmentCandlesticksMap();

        assertThat(map.get(ExchangeSegment.NSE_EQ))
                .isEqualTo(List.of(new Candlestick("11536", "4525.55", "4521.45", "4507.85","4530","4500")));
        assertThat(map.get(ExchangeSegment.NSE_FNO))
                .isEqualTo(List.of(
                        new Candlestick("49081", "368.15","0","368.15","0","0"),
                        new Candlestick("49082","694.35","0","694.35","0","0")));

    }

    @Test
    public void testGetQuoteForSecuritiesOnSuccessResponse() throws DhanAPIException, IOException, URISyntaxException {
        String expectedResponse = getExpectedResponseFromResource("/data/marketfeed-quote.json");
        DhanResponse stubDhanResponse = new DhanResponse(expectedResponse);

        ExchangeSegmentSecurities exchangeSegmentSecurities = new ExchangeSegmentSecurities();
        exchangeSegmentSecurities.add(ExchangeSegment.NSE_FNO, Set.of("49081"));

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(eq(GetQuoteForSecurities), anyMap())).thenReturn(stubDhanResponse);
        Map<ExchangeSegment, List<Quote>> map = marketFeedEndpoint
                .getQuoteFor(exchangeSegmentSecurities)
                .getExchangeSegmentQuotesMap();
        assertThat(map.get(ExchangeSegment.NSE_FNO)).hasSize(1);
    }
}
