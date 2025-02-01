package co.dhan.http;

import co.dhan.api.stream.LiveMarketDepthListener;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.FeedResponseCode;
import co.dhan.dto.LiveDepth;
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
public class LiveMarketDepthTransformer extends WebSocketListener {

    private LiveMarketDepthListener depthListener;

    public LiveMarketDepthTransformer(LiveMarketDepthListener depthListener) {
        this.depthListener = depthListener;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log.info("WebSocket connection opened");
        depthListener.onConnection();
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        log.info("WebSocket connection closed: " + reason);
        depthListener.onTermination(new DhanAPIException(String.valueOf(code), "Client terminated the connection. " + reason));
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (response != null) {
            try (ResponseBody body = response.body()) {
                DhanAPIException e = new DhanAPIException(String.valueOf(response.code()), response.body().string());
                depthListener.onError(e);
                log.error("WebSocket connection failure: ", e);
            } catch (IOException ioException) {
                DhanAPIException e = new DhanAPIException("", response.toString());
                depthListener.onError(e);
                log.error("WebSocket connection failure: ", e);
            }
        } else {
            log.error("WebSocket connection for LiveMarketFeed Failed. Throwable: ", t);
            depthListener.onError(new DhanAPIException("", t.getMessage()));
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println("LiveMarketDepthTransformer.onMessage(..)");
        byte[] byteArray = bytes.toByteArray();
        ByteBuffer buffer = ByteBuffer
                .wrap(byteArray)
                .order(ByteOrder.LITTLE_ENDIAN);

        // https://dhanhq.co/docs/v2/20-market-depth/#response-header
        short messageLength = buffer.getShort(); //1 short = 16 bits = 2 bytes
        byte byteResponseCode = buffer.get();//byteArray[2]; //buffer.get();
        byte exchangeSegmentCode = buffer.get();//byteArray[3];
        int securityID = buffer.getInt();
        var ignoreSegment = buffer.getInt();
        System.out.println("byteResponseCode = " + byteResponseCode
                + ", exchangeSegmentCode = " + exchangeSegmentCode
                + ", securityID = " + securityID
                + ", messageLength = " + messageLength);
        FeedResponseCode feedResponseCode = FeedResponseCode.findByCode(byteResponseCode);
        ExchangeSegment exchangeSegment = ExchangeSegment.findByCode(exchangeSegmentCode);
        switch (feedResponseCode) { //FeedResponseCode.findByCode(41) == ??
            case TWENTY_DEPTH_BIDS_PACKET -> {
                LiveDepth liveDepth = new LiveDepth(String.valueOf(securityID), exchangeSegment);
                liveDepth.setBuy(LiveDepth.parseBids(buffer));
                depthListener.onBidsArrival(liveDepth);
            }
            case TWENTY_DEPTH_ASKS_PACKET -> {
                LiveDepth liveDepth = new LiveDepth(String.valueOf(securityID), exchangeSegment);
                liveDepth.setSell(LiveDepth.parseAsks(buffer));
                depthListener.onAsksArrival(liveDepth);
            }
            default -> {
                depthListener.onError(new Exception("Unknown packet received with feedResponseCode " + byteResponseCode));
            }
        }
    }

}