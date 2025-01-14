package co.dhan.helper;

import co.dhan.http.DhanHTTP;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HTTPUtilsTest {

    @Test
    public void testFullURLWithSlashPrefix() {
        String endpoint = "/api/instruments";
        String expectedURL = DhanHTTP.API_BASE_URL + endpoint;
        assertThat(HTTPUtils.fullURL(endpoint))
                .isEqualTo(expectedURL);
    }

    @Test
    public void testFullURLWithoutSlashPrefix() {
        String endpoint = "api/instruments";
        String expectedURL = DhanHTTP.API_BASE_URL + "/" + endpoint;
        assertThat(HTTPUtils.fullURL(endpoint))
                .isEqualTo(expectedURL);
    }

    @Test
    public void testEndpointWithURLParametersEmptyParameters() {
        String endpoint = "/api/instruments";
        String expectedURL = endpoint;
        assertThat(HTTPUtils.endpointWithURLParameters(endpoint, new HashMap<>()))
                .isEqualTo(expectedURL);
    }

    @Test
    public void testEndpointWithURLParametersSingleParam() {
        String endpoint = "/api/instruments";
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "RELIANCE");
        String expectedURL = endpoint + "?symbol=RELIANCE";
        assertThat(HTTPUtils.endpointWithURLParameters(endpoint, params))
                .isEqualTo(expectedURL);
    }

    @Test
    public void testEndpointWithURLParametersMultipleParams() {
        String endpoint = "/api/instruments";
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "RELIANCE");
        params.put("expiry", "23MAR2023");
        String expectedURL = endpoint + "?symbol=RELIANCE&expiry=23MAR2023";
        assertThat(HTTPUtils.endpointWithURLParameters(endpoint, params))
                .isEqualTo(expectedURL);
    }
}