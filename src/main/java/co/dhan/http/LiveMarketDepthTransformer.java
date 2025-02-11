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

    // Message Length (320b depth + 12b header) as per API Doc: https://dhanhq.co/docs/v2/20-market-depth/
    public static final int CHUNK_SIZE = 332;
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
        log.debug("LiveMarketDepthTransformer.onMessage(..)");
        log.debug("bytes size = " + bytes.size());
//        log.debug(bytes.hex());
//        log.debug("---hex-ed---");
        byte[] byteArray = bytes.toByteArray();
        byte[][] subArrays = splitByteArray(byteArray, CHUNK_SIZE);
        for (byte[] subArray : subArrays) {
            processMessage(subArray);
        }
    }

    void processMessage(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer
                .wrap(byteArray)
                .order(ByteOrder.LITTLE_ENDIAN);

        // https://dhanhq.co/docs/v2/20-market-depth/#response-header
        short messageLength = buffer.getShort(); //1 short = 16 bits = 2 bytes
        byte byteResponseCode = buffer.get();//byteArray[2]; //buffer.get();

        byte exchangeSegmentCode = buffer.get();//byteArray[3];
        int securityID = buffer.getInt();
        var ignoreSegment = buffer.getInt();
        log.debug("messageLength = " + messageLength
                + ", byteResponseCode = " + byteResponseCode
                + ", exchangeSegmentCode = " + exchangeSegmentCode
                + ", securityID = " + securityID
                + ", ignoreSegment = " + ignoreSegment);
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

    static byte[][] splitByteArray(byte[] originalArray, int chunkSize) {
        int totalLength = originalArray.length;
//        int numOfSubArrays = (totalLength + chunkSize - 1) / chunkSize; // To return sub-array less than chunk-size
        int numOfSubArrays = totalLength / chunkSize; // reject sub-array less than chunk-size

        byte[][] subArrays = new byte[numOfSubArrays][];

        for (int i = 0; i < numOfSubArrays; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, totalLength);
            int length = end - start;

            subArrays[i] = new byte[length];
            System.arraycopy(originalArray, start, subArrays[i], 0, length);
        }

        return subArrays;
    }

}