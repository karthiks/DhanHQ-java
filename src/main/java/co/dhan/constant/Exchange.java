package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Exchange {
    NSE("National Stock Exchange"),
    BSE("Bombay Stock Exchange"),
    MCX("Multi Commodity Exchange"),
    ALL("All Exhanges - NSE, BSE, MCX");

    private String description;
}
