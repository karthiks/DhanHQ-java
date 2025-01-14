package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderFlag {
    SINGLE("Single Good Till Triggered (GTT) Order"),
    OCO("One Cancels the Other GTT Order");

    private String description;
}
