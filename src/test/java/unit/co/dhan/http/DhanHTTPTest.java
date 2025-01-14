package co.dhan.http;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DhanHTTPTest {

    @Mock private OkHttpClient mockHttpClient;
    @Mock private Response mockResponse;
    @Mock private Call mockCall;
    @Mock private ResponseBody mockResponseBody;
    private DhanHTTP dhanHTTP;
    private String clientId = "clientId";
    private String accessToken = "accessToken";

    @BeforeEach
    void setUp() {
        dhanHTTP = new DhanHTTP(clientId, accessToken, mockHttpClient);
    }

    @Test
    public void testAllRequestHeadersToHaveClientIdAndAccessTokenForSuccessfulAuthentication() {
        Request request = dhanHTTP.makeRequestBuilderWithURL(DhanHTTP.API_BASE_URL).build();
        assertThat(request.headers().get("client-id")).isEqualTo(clientId);
        assertThat(request.headers().get("access-token")).isEqualTo(accessToken);
    }

    @Test
    public void testDoHttpGetRequestWithoutParamsSuccess() throws DhanAPIException, IOException {
        String endpoint = "/api/instruments";
        String expectedResponse = """
        {
            "data": []
        }
        """;
        when(mockResponseBody.string()).thenReturn(expectedResponse);

        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        dhanHTTP = spy(dhanHTTP);
        DhanResponse response = dhanHTTP.doHttpGetRequest(endpoint);
        verify(dhanHTTP).makeRequestBuilderWithURL(DhanHTTP.API_BASE_URL + endpoint);
        assertThat(response.toString()).isEqualTo(expectedResponse);
    }

    @Test
    public void testDoHttpGetRequestWithParamsSuccess() throws DhanAPIException, IOException {
        String endpoint = "/api/instruments";
        Map<String, String> requestParams = new HashMap<>();
        String key1 = "key1";
        String value1 = "value1";
        requestParams.put(key1, value1);

        String expectedResponse = """
        {
            "data": []
        }
        """;
        when(mockResponseBody.string()).thenReturn(expectedResponse);

        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        dhanHTTP = spy(dhanHTTP);
        DhanResponse response = dhanHTTP.doHttpGetRequest(endpoint, requestParams);
        verify(dhanHTTP).makeRequestBuilderWithURL(DhanHTTP.API_BASE_URL + endpoint + "?"+key1+"="+value1);
        assertThat(response.toString()).isEqualTo(expectedResponse);
    }

    @Test
    public  void testDoHttpGetRequestThrowsExceptionInCaseOfFailure() throws IOException {
        String endpoint = "/api/instruments";
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockResponse.message()).thenReturn("Server-side Error");
        when(mockResponse.code()).thenReturn(500);

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        assertThatThrownBy(() -> dhanHTTP.doHttpGetRequest(endpoint)).isInstanceOf(DhanAPIException.class);
    }

}