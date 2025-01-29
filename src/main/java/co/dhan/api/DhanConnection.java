package co.dhan.api;

import co.dhan.http.DhanHTTP;
import lombok.Getter;

public class DhanConnection {
    @Getter
    private final String clientID;
    @Getter
    private final String accessToken;
    @Getter
    private final DhanHTTP dhanHTTP;

//    @Getter
//    private final DhanWebSocketClient dhanWebSocketClient;

    public DhanConnection(String clientID, String accessToken) {
        this(clientID, accessToken, null);
    }

    public DhanConnection(String clientID, String accessToken, DhanHTTP dhanHTTP) {
        this.clientID = clientID;
        this.accessToken = accessToken;

        DhanHTTP defaultDhanHTTP = new DhanHTTP(clientID, accessToken);
        this.dhanHTTP = (dhanHTTP!=null) ? dhanHTTP : defaultDhanHTTP;
    }
}
