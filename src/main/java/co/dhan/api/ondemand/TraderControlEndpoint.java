package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.constant.KillSwitchStatus;
import co.dhan.dto.KillSwitchStatusResponse;
import co.dhan.helper.HTTPUtils;
import co.dhan.http.DhanAPIException;
import co.dhan.http.DhanResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TraderControlEndpoint {

  interface APIParam {
    String KillSwitchStatus = "killSwitchStatus";
  }

  interface APIEndopint {
    String ManageKillSwitch = "/killswitch?killSwitchStatus=%s";
  }

  private final DhanConnection dhanConnection;

  public TraderControlEndpoint(DhanConnection dhanConnection) {
    this.dhanConnection = dhanConnection;
  }

  public String manageKillSwitch(KillSwitchStatus killSwitchStatus) throws DhanAPIException {
    String endpoint = String.format(APIEndopint.ManageKillSwitch, killSwitchStatus.toString());
    DhanResponse dhanResponse =
        dhanConnection.getDhanHTTP().doHttpPostRequest(endpoint, new HashMap<>());
    Map<String, String> map = null;
    try {
      map = HTTPUtils.DhanObjectMapper.readValue(dhanResponse.toString(), Map.class);
      return map.get(APIParam.KillSwitchStatus);
    } catch (JsonProcessingException e) {
      String msg = String.format("Error parsting HTTP Response: %s", dhanResponse.toString());
      log.error(msg);
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves the current status of the Kill Switch (Trader's Control).
   *
   * <p>Endpoint: GET https://api.dhan.co/v2/killswitch
   *
   * <p>Response contains: Kill switch status details including client ID, status, enabled state,
   * reason, and timestamps
   *
   * @return KillSwitchStatusResponse object containing the kill switch status information
   * @throws DhanAPIException if the API request fails
   */
  public KillSwitchStatusResponse getKillSwitchStatus() throws DhanAPIException {
    DhanResponse dhanResponse = dhanConnection.getDhanHTTP().doHttpGetRequest("/killswitch");
    return dhanResponse.convertToType(KillSwitchStatusResponse.class);
  }
}
