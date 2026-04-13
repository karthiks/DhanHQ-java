package co.dhan.api.ondemand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.constant.KillSwitchStatus;
import co.dhan.constant.PnLExitStatus;
import co.dhan.constant.ProductType;
import co.dhan.dto.KillSwitchStatusResponse;
import co.dhan.dto.PnlExitRequest;
import co.dhan.dto.PnlExitResponse;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

class TraderControlEndpointTest extends UnitTestRoot {

  @Mock DhanConnection mockDhanConnection;

  @Mock DhanHTTP mockDhanHTTP;

  @Mock DhanResponse mockDhanResponse;

  @Spy @InjectMocks TraderControlEndpoint traderControlEndpoint;

  @Test
  void manageKillSwitch_ReturnResultSuccessfully() {
    String expectedResponse =
        """
        {
            "dhanClientId":"123",
            "killSwitchStatus": "Kill Switch has been successfully activated"
        }""";
    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
    when(mockDhanResponse.toString()).thenReturn(expectedResponse);
    assertThat(traderControlEndpoint.manageKillSwitch(KillSwitchStatus.ACTIVATE))
        .contains("activated");
    verify(mockDhanHTTP).doHttpPostRequest(eq("/killswitch?killSwitchStatus=ACTIVATE"), anyMap());
  }

  @Test
  void getKillSwitchStatus_ReturnResultSuccessfully() throws IOException {
    KillSwitchStatusResponse expectedStatus =
        new KillSwitchStatusResponse("123", KillSwitchStatus.ACTIVATE);
    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpGetRequest(anyString())).thenReturn(mockDhanResponse);
    when(mockDhanResponse.convertToType(KillSwitchStatusResponse.class)).thenReturn(expectedStatus);

    assertThat(traderControlEndpoint.getKillSwitchStatus())
        .usingRecursiveComparison()
        .isEqualTo(expectedStatus);
    verify(mockDhanHTTP).doHttpGetRequest(eq("/killswitch"));
  }

  @Test
  void configurePnlExit_ReturnResultSuccessfully() {
    PnlExitRequest request =
        PnlExitRequest.builder()
            .profitValue(new BigDecimal("1000"))
            .lossValue(new BigDecimal("500"))
            .enableKillSwitch(true)
            .productType(ProductType.INTRADAY)
            .build();

    PnlExitResponse expectedResponse =
        new PnlExitResponse(PnLExitStatus.ACTIVE, "P&L exit configured successfully");
    Map<String, String> expectedPayload = new HashMap<>();
    expectedPayload.put("profitValue", "1000");
    expectedPayload.put("lossValue", "500");
    expectedPayload.put("enableKillSwitch", "true");
    expectedPayload.put("productType", "INTRADAY");

    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpPostRequest(
            eq("/pnlExit"), argThat(payload -> expectedPayload.equals(payload))))
        .thenReturn(mockDhanResponse);
    when(mockDhanResponse.convertToType(PnlExitResponse.class)).thenReturn(expectedResponse);

    assertThat(traderControlEndpoint.configurePnlExit(request))
        .usingRecursiveComparison()
        .isEqualTo(expectedResponse);
    verify(mockDhanHTTP)
        .doHttpPostRequest(eq("/pnlExit"), argThat(payload -> expectedPayload.equals(payload)));
  }

  @Test
  void disablePnlExit_ReturnResultSuccessfully() {
    PnlExitResponse expectedResponse =
        new PnlExitResponse(PnLExitStatus.INACTIVE, "P&L exit disabled successfully");

    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpDeleteRequest(eq("/pnlExit"))).thenReturn(mockDhanResponse);
    when(mockDhanResponse.convertToType(PnlExitResponse.class)).thenReturn(expectedResponse);

    assertThat(traderControlEndpoint.disablePnlExit())
        .usingRecursiveComparison()
        .isEqualTo(expectedResponse);
    verify(mockDhanHTTP).doHttpDeleteRequest(eq("/pnlExit"));
  }

  @Test
  void getPnlExitConfig_ReturnResultSuccessfully() {
    PnlExitResponse expectedResponse =
        new PnlExitResponse(PnLExitStatus.ACTIVE, "P&L exit configured successfully");

    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpGetRequest(eq("/pnlExit"))).thenReturn(mockDhanResponse);
    when(mockDhanResponse.convertToType(PnlExitResponse.class)).thenReturn(expectedResponse);

    assertThat(traderControlEndpoint.getPnlExitConfig())
        .usingRecursiveComparison()
        .isEqualTo(expectedResponse);
    verify(mockDhanHTTP).doHttpGetRequest(eq("/pnlExit"));
  }
}
