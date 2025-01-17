package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Segment {
    I("Index value"),
    EQ("Equity"),
    FNO("Futures and Options"),
    COMM("Commodity"),
    CURRENCY("Currency");

    private String description;
}
