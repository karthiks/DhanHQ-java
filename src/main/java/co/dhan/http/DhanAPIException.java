package co.dhan.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DhanAPIException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    public String code;

    @Getter
    public String message;

    @Override
    public String toString() {
        return String.format("DhanAPIException (CODE %s): %s",code,message);
    }
}
