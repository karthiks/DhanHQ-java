package co.dhan.api.stream.listener;

import co.dhan.dto.LiveDepth;
import co.dhan.http.DhanAPIException;

public interface LiveMarketDepthListener {
    void onConnection();

    void onTermination(DhanAPIException e);

    void onError(Exception e);

    void onBidsArrival(LiveDepth liveDepth);
    void onAsksArrival(LiveDepth liveDepth);
}
