package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ExchangeSegmentSecuritiesLTPWrapper {
    public static String JSONPropertyData = "data";

    private Map<ExchangeSegment, List<SecurityLTP>> exchangeSegmentSecuritiesLTPMap;
}
