package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.constant.ExchangeSegment;
import co.dhan.dto.ExchangeSegmentSecurities;
import co.dhan.dto.SecurityLTP;
import co.dhan.http.DhanAPIException;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MarketFeedEndpointTest extends UnitTestRoot {

    @Mock
    private DhanConnection mockDhanConnection;
    @Mock
    private DhanHTTP mockDhanHTTP;

    @InjectMocks
    private MarketFeedEndpoint marketFeedEndpoint;

    @Test
    public void testGetLTPForSecuritiesOnSuccessResponse() throws DhanAPIException {
        String expectedResponse = """
            {
                "data": {
                    "NSE_EQ": {
                        "11536": { "last_price": 4520 }
                    },
                    "NSE_FNO": {
                        "49081": { "last_price": 368.15 },
                        "49082": { "last_price": 694.35 }
                    }
                },
                "status": "success"
            }
        """;
        ExchangeSegmentSecurities exchangeSegmentSecurities = new ExchangeSegmentSecurities();
        exchangeSegmentSecurities.add(ExchangeSegment.NSE_EQ, Set.of("11536"));
        exchangeSegmentSecurities.add(ExchangeSegment.NSE_FNO, Set.of("49081","49082"));

        DhanResponse stubDhanResponse = new DhanResponse(expectedResponse);
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP
                .doHttpPostRequest(MarketFeedEndpoint.APIEndpoint.GetLTPForSecurities,
                        exchangeSegmentSecurities.toMapOfStrings()))
                .thenReturn(stubDhanResponse);

        Map<ExchangeSegment, List<SecurityLTP>> map = marketFeedEndpoint
                .getLTPFor(exchangeSegmentSecurities)
                .getExchangeSegmentSecuritiesLTPMap();

        assertThat(map.get(ExchangeSegment.NSE_EQ))
                .isEqualTo(List.of(new SecurityLTP("11536", "4520")));
        assertThat(map.get(ExchangeSegment.NSE_FNO))
                .isEqualTo(List.of(
                        new SecurityLTP("49081", "368.15"),
                        new SecurityLTP("49082","694.35")));
    }

    @Test
    public void getOHLCForSecuritiesOnSuccessResponse() throws DhanAPIException {
        String expectedResponse = """
        {
             "data": {
                 "NSE_EQ": {
                     "11536": {
                         "last_price": 4525.55,
                         "ohlc": { "open": 4521.45, "close": 4507.85, "high": 4530, "low": 4500 }
                     }
                 },
                 "NSE_FNO": {
                     "49081": {
                         "last_price": 368.15,
                         "ohlc": { "open": 0, "close": 368.15, "high": 0, "low": 0 }
                     },
                     "49082": {
                         "last_price": 694.35,
                         "ohlc": { "open": 0, "close": 694.35, "high": 0, "low": 0 }
                     }
                 }
             },
             "status": "success"
        }
        """;
        ExchangeSegmentSecurities exchangeSegmentSecurities = new ExchangeSegmentSecurities();
        exchangeSegmentSecurities.add(ExchangeSegment.NSE_EQ, Set.of("11536"));
        exchangeSegmentSecurities.add(ExchangeSegment.NSE_FNO, Set.of("49081","49082"));

        DhanResponse stubDhanResponse = new DhanResponse(expectedResponse);
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP
                .doHttpPostRequest(MarketFeedEndpoint.APIEndpoint.GetOHLCForSecurities,
                        exchangeSegmentSecurities.toMapOfStrings()))
                .thenReturn(stubDhanResponse);

        Map<ExchangeSegment, List<SecurityLTP>> map = marketFeedEndpoint
                .getOHLCFor(exchangeSegmentSecurities)
                .getExchangeSegmentSecuritiesLTPMap();

        assertThat(map.get(ExchangeSegment.NSE_EQ))
                .isEqualTo(List.of(new SecurityLTP("11536", "4525.55", "4521.45", "4507.85","4530","4500")));
        assertThat(map.get(ExchangeSegment.NSE_FNO))
                .isEqualTo(List.of(
                        new SecurityLTP("49081", "368.15","0","368.15","0","0"),
                        new SecurityLTP("49082","694.35","0","694.35","0","0")));

    }
}
