package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductType {
    CNC("Cash & Carry for equity deliveries"),
    INTRADAY("Intraday for Equity, Futures & Options"),
    MARGIN("Carry Forward in Futures & Options"),
    CO("Cover Order; entry and stop loss for Intraday"),
    BO("Bracket Order; entry, stop loss & target price for Intraday"),
    MTF("Margin Traded Fund"); //aka MARGIN in https://dhanhq.co/docs/v2/annexure/#exchange-segment

    private String description;
}
