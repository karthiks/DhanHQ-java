package co.dhan.helper;

import co.dhan.http.DhanHTTP;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class HTTPUtils {

    @NotNull
    public static String fullURL(@NotNull String endpoint) {
        String url = String.format("%s%s", DhanHTTP.API_BASE_URL, slashedEndpoint(endpoint));
        return url;
    }

    @NotNull
    private static String slashedEndpoint(@NotNull String endpoint) {
        if(endpoint.startsWith("/")) return endpoint;
        return String.format("/%s",endpoint);
    }

    @NotNull
    public static String endpointWithURLParameters(@NotNull String endpoint, Map<String,String> requestParameters){
        if(requestParameters == null)  return endpoint;
        String urlParams = requestParameters.entrySet().stream()
                .map(entry -> String.format("%s=%s",entry.getKey(),entry.getValue()))
                .collect(Collectors.joining("&"));
        return String.format("%s?%s",endpoint,urlParams);
    }

    private static HttpUrl encodeURL(String url) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        return httpBuilder.build();
    }
}
