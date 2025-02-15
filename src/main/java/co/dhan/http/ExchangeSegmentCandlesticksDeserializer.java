package co.dhan.http;

import co.dhan.constant.ExchangeSegment;
import co.dhan.dto.ExchangeSegmentCandlesticksWrapper;
import co.dhan.dto.Candlestick;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.dhan.dto.Candlestick.*;

public class ExchangeSegmentCandlesticksDeserializer
        extends JsonDeserializer<ExchangeSegmentCandlesticksWrapper> {
    @Override
    public ExchangeSegmentCandlesticksWrapper deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode dataNode = jsonNode.get(ExchangeSegmentCandlesticksWrapper.JSONPropertyData);

        Map<ExchangeSegment, List<Candlestick>> map = new HashMap<>();
        dataNode.fields().forEachRemaining(field -> {
            ExchangeSegment segment = ExchangeSegment.valueOf(field.getKey());
            List<Candlestick> list = buildListOfCandlesticks(field);
            map.put(segment, list);
        });

        return new ExchangeSegmentCandlesticksWrapper(map);
    }

    @NotNull
    private static List<Candlestick> buildListOfCandlesticks(Map.Entry<String, JsonNode> field) {
        List<Candlestick> list = new ArrayList<>();
        field.getValue().fields().forEachRemaining(securityField -> {
            String securityid = securityField.getKey();
            String ltp = securityField.getValue().get(JSONPropertyLastPrice).asText();
            Candlestick candleStick = new Candlestick(securityid,ltp);
            JsonNode ohlcJsonNode = securityField.getValue().get(JSONPropertyOHLC);
            if (ohlcJsonNode != null) {
                String open = ohlcJsonNode.get(JSONPropertyOpen).asText();
                String close = ohlcJsonNode.get(JSONPropertyClose).asText();
                String high = ohlcJsonNode.get(JSONPropertyHigh).asText();
                String low = ohlcJsonNode.get(JSONPropertyLow).asText();
                candleStick.setOpen(open);
                candleStick.setClose(close);
                candleStick.setHigh(high);
                candleStick.setLow(low);
            }
            list.add(candleStick);
        });
        return list;
    }
}
