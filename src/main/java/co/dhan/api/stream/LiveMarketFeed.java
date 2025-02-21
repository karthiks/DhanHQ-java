package co.dhan.api.stream;

import co.dhan.api.DhanConnection;
import co.dhan.api.stream.listener.LiveMarketFeedListener;
import co.dhan.constant.FeedRequestCode;
import co.dhan.dto.Instrument;
import co.dhan.dto.InstrumentsFeedRequestWrapper;
import co.dhan.http.DhanAPIException;
import co.dhan.http.LiveMarketFeedTransformer;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LiveMarketFeed {
    public static final int DEFAULT_HTTP_CONNECTION_TIMEOUT = 10;
    private static boolean ENABLE_LOGGING = true;
    private final String WebSocketURL;
    private WebSocket webSocket;
    private final OkHttpClient okHttpClient;

    private final ExecutorService executor;

    public LiveMarketFeed(DhanConnection dhanConnection) {
        this(dhanConnection, null);
    }

    public LiveMarketFeed(DhanConnection dhanConnection, String webSocketURL) {
        String defaultWebsocketURL = "wss://api-feed.dhan.co?version=2&authType=2"
                + "&token=" + dhanConnection.getAccessToken()
                + "&clientId=" + dhanConnection.getClientID();
        WebSocketURL = (webSocketURL != null) ? webSocketURL : defaultWebsocketURL;
        this.executor = Executors.newSingleThreadExecutor();
        this.okHttpClient = buildHTTPClient();
    }

    @NotNull
    private OkHttpClient buildHTTPClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_HTTP_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        if(ENABLE_LOGGING) {
            builder.addInterceptor(getHttpLoggingInterceptor());
        }
        return builder.build();
    }

    @NotNull
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public void connect(LiveMarketFeedListener feedListener) {
        log.debug("LiveMarketFeed: connecting to websocket");
        if (feedListener == null) {
            throw new IllegalArgumentException("Feed listener cannot be null");
        }
        executor.submit(() -> { // Run connection in a separate thread
            log.debug("Executor running now..");
            Request request = new Request.Builder().url(WebSocketURL).build();
            webSocket = okHttpClient.newWebSocket(request, new LiveMarketFeedTransformer(feedListener));
        });
    }

    public void disconnect(){
        if(webSocket!=null) {
            webSocket.close(1000, "Disconnecting " + getClass().getName() + ".");
            webSocket = null;
            executor.shutdownNow();
        }
        log.info("Disconnected " + getClass().getName() + ".");
    }

    public void command(List<Instrument> instruments, FeedRequestCode code) {
        if (instruments == null || instruments.isEmpty() || instruments.size()>100) {
            throw new DhanAPIException("Invalid Argument Error:",
                    "Instruments list cannot be empty and cannot be greater than 100 in one batch.");
        }
        InstrumentsFeedRequestWrapper wrapper = new InstrumentsFeedRequestWrapper(instruments, code);
        if (webSocket != null)
            webSocket.send(wrapper.toJSON());
        else {
            log.error("Websocket is null, cannot send message");
        }
    }

}