package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Instrument {
    @JsonProperty("ExchangeSegment")
    private final ExchangeSegment exchangeSegment;

    @JsonProperty("SecurityId")
    private final String securityId;
}
