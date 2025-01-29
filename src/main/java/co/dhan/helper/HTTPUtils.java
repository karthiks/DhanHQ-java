package co.dhan.helper;

import co.dhan.dto.ExchangeSegmentCandlesticksWrapper;
import co.dhan.dto.ExchangeSegmentQuotesWrapper;
import co.dhan.http.DhanHTTP;
import co.dhan.http.ExchangeSegmentCandlesticksDeserializer;
import co.dhan.http.ExchangeSegmentQuotesDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class HTTPUtils {

    public static ObjectMapper DhanObjectMapper;

    static {
        DhanObjectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ExchangeSegmentCandlesticksWrapper.class,new ExchangeSegmentCandlesticksDeserializer());
        module.addDeserializer(ExchangeSegmentQuotesWrapper.class, new ExchangeSegmentQuotesDeserializer());
        DhanObjectMapper.registerModule(module);
    }

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
        if(requestParameters == null || requestParameters.isEmpty())  return endpoint;
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
