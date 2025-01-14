package co.dhan.http;

import co.dhan.helper.HTTPUtils;
import lombok.Getter;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DhanHTTP {
    public static final String API_BASE_URL = "https://api.dhan.co/v2";
    public static final int DEFAULT_HTTP_CONNECTION_TIMEOUT = 10;
    private static boolean ENABLE_LOGGING = true;
    private final String clientID;
    private final String accessToken;

    @Getter
    private OkHttpClient httpClient;

    public DhanHTTP(String clientID, String accessToken) {
        this.clientID = clientID;
        this.accessToken = accessToken;
        httpClient = buildHTTPClient();
    }

    DhanHTTP(String clientID, String accessToken, OkHttpClient httpClient) {
        this.clientID = clientID;
        this.accessToken = accessToken;
        this.httpClient = httpClient;
    }

    @NotNull
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public DhanResponse doHttpGetRequest(@NotNull String endpoint)
            throws DhanAPIException {
        return doHttpGetRequest(endpoint, null);
    }

    public DhanResponse doHttpGetRequest(@NotNull String endpoint, Map<String,String> requestParameters)
            throws DhanAPIException {
        String urlWithParams = HTTPUtils.endpointWithURLParameters(endpoint,requestParameters);
        String fullURL = HTTPUtils.fullURL(urlWithParams);
        Request request = makeRequestBuilderWithURL(fullURL).get().build();
        return execute(request);
    }

    public DhanResponse doHttpDeleteRequest(String endpoint)
            throws DhanAPIException {
        String fullURL = HTTPUtils.fullURL(endpoint);
        Request request = makeRequestBuilderWithURL(fullURL).delete().build();
        return execute(request);
    }

    public DhanResponse doHttpPostRequest(String endpoint, Map<String, String> payload)
            throws DhanAPIException {
        String fullURL = HTTPUtils.fullURL(endpoint);
        FormBody.Builder builder = new FormBody.Builder();
        if (payload == null) {
            payload = new HashMap<>(0);
        }
        for (Map.Entry<String, String> entry : payload.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = makeRequestBuilderWithURL(fullURL).post(requestBody).build();
        return execute(request);
    }

    public DhanResponse doHttpPutRequest(String endpoint, Map<String, String> payload)
            throws DhanAPIException {
        String fullURL = HTTPUtils.fullURL(endpoint);
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : payload.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = makeRequestBuilderWithURL(fullURL).put(requestBody).build();
        return execute(request);
    }

    /**
     * Returns DhanResponse on success and throws DhanAPIException on failure
     * @param request
     * @return DhanResponse for successful HTTP Response
     * @throws DhanAPIException for non-successful HTTP Response
     */
    @NotNull
    private DhanResponse execute(Request request) throws DhanAPIException {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String message = response.message();
                if(message.isEmpty()) {
                    message = response.body().string();
                }
                throw new DhanAPIException(String.valueOf(response.code()), message);
            }
            return new DhanResponse(response.body().string());
        } catch (IOException e) {
            throw new DhanAPIException("IOException", e.getMessage());
        }
    }

    @NotNull
    Request.Builder makeRequestBuilderWithURL(String fullURL) {
        return new Request.Builder()
                .addHeader("client-id", clientID)
                .addHeader("access-token", accessToken)
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Content-type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "DhanHQ-Java SDK v1.0.0")
                .url(fullURL);
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

}
