package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class ExchangeSegmentSecurities { //Rename as MapOfExchangeSegmentToSecurities
    private Map<ExchangeSegment, Set<String>> data = new HashMap<>();

    public ExchangeSegmentSecurities add(ExchangeSegment exchangeSegment,Set<String> securities) {
        if (exchangeSegment == null || securities == null || securities.isEmpty()) {
            throw new IllegalArgumentException("Input values cannot be null or empty!");
        }
        if (data.containsKey(exchangeSegment)) {
            data.get(exchangeSegment).addAll(securities);
        } else {
            data.put(exchangeSegment, securities);
        }
        return this;
    }

    public Map<String,String> toMapOfStrings() {
        Map<String, String> map = new HashMap<>();
        data.forEach( (k,v) -> {
            String concatinatedValue = "[" + String.join(",", v) + "]";
            map.put(k.toString(), concatinatedValue);
        });
        return map;
    }
}
