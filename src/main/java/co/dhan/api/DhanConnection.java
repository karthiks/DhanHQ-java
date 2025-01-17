package co.dhan.api;

import co.dhan.http.DhanHTTP;
import lombok.Getter;

public class DhanConnection {
    private final String clientID;
    private final String accessToken;

    @Getter
    private final DhanHTTP dhanHTTP;

    public DhanConnection(String clientID, String accessToken) {
        this.clientID = clientID;
        this.accessToken = accessToken;
        this.dhanHTTP = new DhanHTTP(clientID, accessToken);
    }

    public DhanConnection(String clientID, String accessToken, DhanHTTP dhanHTTP) {
        this.clientID = clientID;
        this.accessToken = accessToken;
        this.dhanHTTP = dhanHTTP;
    }
}
