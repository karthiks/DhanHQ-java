package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.dto.ExchangeSegmentCandlesticksWrapper;
import co.dhan.dto.ExchangeSegmentQuotesWrapper;
import co.dhan.dto.ExchangeSegmentSecurities;
import co.dhan.http.DhanAPIException;

import java.util.Map;

public class MarketQuotesEndpoint {

    interface APIEndpoint {
        String GetLTPForSecurities = "/marketfeed/ltp";
        String GetOHLCForSecurities = "/marketfeed/ohlc";
        String GetQuoteForSecurities = "/marketfeed/quote";
    }

    private final DhanConnection dhanConnection;

    public MarketQuotesEndpoint(DhanConnection dhanConnection) {
        this.dhanConnection = dhanConnection;
    }

    public ExchangeSegmentCandlesticksWrapper getLTPFor(ExchangeSegmentSecurities exchangeSegmentSecurities)
            throws DhanAPIException {
        Map<String,String> payload = exchangeSegmentSecurities.toMapOfStrings();
        return dhanConnection.getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.GetLTPForSecurities, payload)
                .convertToType(ExchangeSegmentCandlesticksWrapper.class);
    }

    public ExchangeSegmentCandlesticksWrapper getOHLCFor(ExchangeSegmentSecurities exchangeSegmentSecurities)
            throws DhanAPIException {
        Map<String,String> payload = exchangeSegmentSecurities.toMapOfStrings();
        return dhanConnection.getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.GetOHLCForSecurities, payload)
                .convertToType(ExchangeSegmentCandlesticksWrapper.class);
    }

    public ExchangeSegmentQuotesWrapper getQuoteFor(ExchangeSegmentSecurities exchangeSegmentSecurities) throws DhanAPIException {
        Map<String,String> payload = exchangeSegmentSecurities.toMapOfStrings();
        return dhanConnection.getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.GetQuoteForSecurities, payload)
                .convertToType(ExchangeSegmentQuotesWrapper.class);
    }
}