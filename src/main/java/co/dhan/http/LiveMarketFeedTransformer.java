package co.dhan.http;

import co.dhan.api.stream.listener.LiveMarketFeedListener;
import co.dhan.constant.DataAPIError;
import co.dhan.constant.FeedResponseCode;
import co.dhan.dto.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Slf4j
public class LiveMarketFeedTransformer extends WebSocketListener {

    private final LiveMarketFeedListener feedListener;

    public LiveMarketFeedTransformer(LiveMarketFeedListener feedListener) {
        this.feedListener = feedListener;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log.info("WebSocket connection opened");
        feedListener.onConnection();
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        log.info("WebSocket connection closed: " + reason);
        feedListener.onTermination(new DhanAPIException(String.valueOf(code), "Client terminated the connection. " + reason));
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (response != null) {
            try (ResponseBody body = response.body()) {
                DhanAPIException e = new DhanAPIException(String.valueOf(response.code()), response.body().string());
                feedListener.onError(e);
                log.error("WebSocket connection failure: ", e);
            } catch (IOException ioException) {
                DhanAPIException e = new DhanAPIException("", response.toString());
                feedListener.onError(e);
                log.error("WebSocket connection failure: ", e);
            }
        } else {
            log.error("WebSocket connection for LiveMarketFeed Failed. Throwable: ",t);
            feedListener.onError(new DhanAPIException("",t.getMessage()));
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        // Based on marketfeed.process_data() of Python SDK.
        try {
            byte[] byteArray = bytes.toByteArray();
            ByteBuffer buffer = ByteBuffer
                    .wrap(byteArray)
                    .order(ByteOrder.LITTLE_ENDIAN);

            byte firstByte = byteArray[0]; //buffer.get();
            FeedResponseCode code = FeedResponseCode.findByCode(firstByte);
            if(code == null) {
                String message = String.format("Undefined FeedResponseCode %s sent by API Server!", firstByte);
                log.error(message);
                feedListener.onError(new Exception(message));
            }
            switch (code) {
                case TICKER_PACKET -> feedListener.onTickerArrival(new LiveTicker(buffer));
                case PREV_CLOSE_PACKET -> feedListener.onPrevCloseArrival(new LivePrevClose(buffer));
                case QUOTE_PACKET -> feedListener.onQuoteArrival(new LiveQuote(buffer));
                case OI_PACKET -> feedListener.onOIArrival(new LiveOI(buffer));
                case FULL_PACKET -> feedListener.onFullPacketArrival(new LiveQuoteMax(buffer));
                case FEED_DISCONNECT -> feedListener.onTermination(DataAPIError.makeException(buffer));
                default -> {
                    String message = String.format("Undefined FeedResponseCode %s sent by API Server!", firstByte);
                    log.error(message);
                    feedListener.onError(new Exception(message));
                }
            }
        } catch (Exception e) {
            log.error("Error processing binary message: ", e);
        }
    }

}
