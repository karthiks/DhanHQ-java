package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PositionType {
    LONG("When net bought quantity is more than sold quantity"),
    SHORT("When net sold quantity is more than bought quantity"),
    CLOSED("When no open position standing");

    private String description;
}
