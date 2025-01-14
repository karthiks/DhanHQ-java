package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {
    LIMIT("For Limit Order types"),
    MARKET("For market Order types"),
    STOP_LOSS("For Stop Loss Limit orders"),
    STOP_LOSS_MARKET("For Stop Loss Market orders");

    private String description;
}
