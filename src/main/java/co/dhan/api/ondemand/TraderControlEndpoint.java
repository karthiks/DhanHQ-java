package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.constant.KillSwitchStatus;
import co.dhan.http.DhanAPIException;
import co.dhan.http.DhanResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

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
        DhanResponse dhanResponse = dhanConnection.getDhanHTTP()
                .doHttpPostRequest(endpoint, new HashMap<>());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = null;
        try {
            map = objectMapper.readValue(dhanResponse.toString(), Map.class);
            return map.get(APIParam.KillSwitchStatus);
        } catch (JsonProcessingException e) {
            String msg = String.format("Error parsting HTTP Response: %s",dhanResponse.toString());
            log.error(msg);
            throw new RuntimeException(e);
        }
    }
}
