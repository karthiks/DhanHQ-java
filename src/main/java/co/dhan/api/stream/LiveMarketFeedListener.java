package co.dhan.api.stream;

import co.dhan.dto.*;
import co.dhan.http.DhanAPIException;

public interface LiveMarketFeedListener {
    void onConnection();

    void onTermination(DhanAPIException e);

    void onError(Exception e);

    void onTickerArrival(LiveTicker liveTicker);

    void onPrevCloseArrival(LivePrevClose livePrevClose);

    void onQuoteArrival(LiveQuote liveQuote);

    void onOIArrival(LiveOI liveOI);

    void onQuoteMaxArrival(LiveQuoteMax liveQuoteMax);

    default void onFullPacketArrival(LiveQuoteMax liveQuoteMax) {
        onQuoteMaxArrival(liveQuoteMax);
    }
}
