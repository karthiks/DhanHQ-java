package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Segment {
    EQ("Equity"),
    FNO("Futures and Options"),
    COMM("Commodity");

    private String description;
}
