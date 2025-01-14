package co.dhan.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DhanAPIException extends Throwable {
    private static final long serialVersionUID = 1L;

    @Getter
    public String code;

    @Getter
    public String message;

    @Override
    public String toString() {
        return String.format("DhanAPIException (HTTP STATUS CODE %s): %s",code,message);
    }
}
