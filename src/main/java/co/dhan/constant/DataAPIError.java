package co.dhan.constant;

import co.dhan.http.DhanAPIException;
import lombok.Getter;

import java.nio.ByteBuffer;

@Getter
public enum DataAPIError {
    INTERNAL_SERVER_ERROR(800, "Internal Server Error", 500),
    TOO_MANY_REQUESTS(804,"Requested number of instruments exceeds limit", 429),
    TOO_MANY_CONNECTIONS(805,"Too many requests or connections. Further requests may result in the user being blocked",429),
    UNAUTHORIZED(806, "Data APIs not subscribed",403),
    ACCESS_TOKEN_EXPIRED(807,"Access token is expired",401),
    AUTHENTICATION_FAILED(808,"Authentication Failed - Client ID or Access Token invalid",401),
    ACCESS_TOKEN_INVALID(809,"Access token is invalid",401),
    CLIENT_ID_INVALID(810,"Client ID is invalid",401),
    INVALID_EXPIRY_DATE(811,"Invalid Expiry Date",400),
    INVALID_DATE_FORMAT(812,"Invalid Date Format",400),
    INVALID_SECURITYID(813,"Invalid SecurityId",400),
    INVALID_REQUEST(814,"Invalid Request",400);

    private final int code;
    private final String dhanErrorMessage;
    private final int httpStatusCode;

    private DataAPIError(int code, String dhanErrorMessage, int httpStatusCode) {
        this.code = code;
        this.dhanErrorMessage = dhanErrorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public static DataAPIError findByCode(int code) {
        for (DataAPIError value : DataAPIError.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    public static DhanAPIException makeException(ByteBuffer buffer) {
        // Error code is at offset 4, as per Python code. See marketfeed.server_disconnection()
        int errorCode = buffer.getInt(4);

        DataAPIError error = findByCode(errorCode);
        if (error != null) {
            return new DhanAPIException(String.valueOf(error.code), error.dhanErrorMessage);
        }
        return new DhanAPIException(String.valueOf(error.code), "Unknown Error Code received from Dhan API Server!");
    }

    @Override
    public String toString() {
        return code + ": " + dhanErrorMessage;
    }
}
