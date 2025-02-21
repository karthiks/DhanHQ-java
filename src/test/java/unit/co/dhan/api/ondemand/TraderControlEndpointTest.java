package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.constant.KillSwitchStatus;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraderControlEndpointTest extends UnitTestRoot {

    @Mock
    DhanConnection mockDhanConnection;

    @Mock
    DhanHTTP mockDhanHTTP;

    @Mock
    DhanResponse mockDhanResponse;

    @Spy
    @InjectMocks
    TraderControlEndpoint traderControlEndpoint;

    @Test
    void manageKillSwitch_ReturnResultSuccessfully() {
        String expectedResponse = """
                {
                    "dhanClientId":"123",
                    "killSwitchStatus": "Kill Switch has been successfully activated"
                }""";
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.toString()).thenReturn(expectedResponse);
        assertThat(traderControlEndpoint.manageKillSwitch(KillSwitchStatus.ACTIVATE)).contains("activated");
        verify(mockDhanHTTP).doHttpPostRequest(eq("/killswitch?killSwitchStatus=ACTIVATE"), anyMap());
    }
}