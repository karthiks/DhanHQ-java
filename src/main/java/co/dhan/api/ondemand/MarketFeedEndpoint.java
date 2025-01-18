package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.dto.ExchangeSegmentSecurities;
import co.dhan.dto.ExchangeSegmentCandlestickWrapper;
import co.dhan.http.DhanAPIException;

import java.util.Map;

public class MarketFeedEndpoint {

    interface APIParam {
        String ExchangeSegment = "exchange_segment";
        String SecurityID = "security_id";
    }

    interface APIEndpoint {
        String GetLTPForSecurities = "/marketfeed/ltp";
        String GetOHLCForSecurities = "/marketfeed/ohlc";
        String GetQuoteForSecurities = "/marketfeed/quote";
    }

    private final DhanConnection dhanConnection;

    public MarketFeedEndpoint(DhanConnection dhanConnection) {
        this.dhanConnection = dhanConnection;
    }

    public ExchangeSegmentCandlestickWrapper getLTPFor(ExchangeSegmentSecurities exchangeSegmentSecurities)
            throws DhanAPIException {
        Map<String,String> payload = exchangeSegmentSecurities.toMapOfStrings();
        return dhanConnection.getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.GetLTPForSecurities, payload)
                .convertToType(ExchangeSegmentCandlestickWrapper.class);
    }

    public ExchangeSegmentCandlestickWrapper getOHLCFor(ExchangeSegmentSecurities exchangeSegmentSecurities)
            throws DhanAPIException {
        Map<String,String> payload = exchangeSegmentSecurities.toMapOfStrings();
        return dhanConnection.getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.GetOHLCForSecurities, payload)
                .convertToType(ExchangeSegmentCandlestickWrapper.class);
    }
}
