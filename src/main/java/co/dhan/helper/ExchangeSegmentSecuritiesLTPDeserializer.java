package co.dhan.helper;

import co.dhan.constant.ExchangeSegment;
import co.dhan.dto.ExchangeSegmentSecuritiesLTPWrapper;
import co.dhan.dto.SecurityLTP;
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

import static co.dhan.dto.SecurityLTP.*;

public class ExchangeSegmentSecuritiesLTPDeserializer
        extends JsonDeserializer<ExchangeSegmentSecuritiesLTPWrapper> {
    @Override
    public ExchangeSegmentSecuritiesLTPWrapper deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode dataNode = jsonNode.get(ExchangeSegmentSecuritiesLTPWrapper.JSONPropertyData);

        Map<ExchangeSegment, List<SecurityLTP>> map = new HashMap<>();
        dataNode.fields().forEachRemaining(field -> {
            ExchangeSegment segment = ExchangeSegment.valueOf(field.getKey());
            List<SecurityLTP> list = buildListOfSecurityLTP(field);
            map.put(segment, list);
        });

        return new ExchangeSegmentSecuritiesLTPWrapper(map);
    }

    @NotNull
    private static List<SecurityLTP> buildListOfSecurityLTP(Map.Entry<String, JsonNode> field) {
        List<SecurityLTP> list = new ArrayList<>();
        field.getValue().fields().forEachRemaining(securityField -> {
            String securityid = securityField.getKey();
            String ltp = securityField.getValue().get(JSONPropertyLastPrice).asText();
            SecurityLTP securityLTP = new SecurityLTP(securityid,ltp);
            JsonNode ohlcJsonNode = securityField.getValue().get(JSONPropertyOHLC);
            if (ohlcJsonNode != null) {
                String open = ohlcJsonNode.get(JSONPropertyOpen).asText();
                String close = ohlcJsonNode.get(JSONPropertyClose).asText();
                String high = ohlcJsonNode.get(JSONPropertyHigh).asText();
                String low = ohlcJsonNode.get(JSONPropertyLow).asText();
                securityLTP.setOpen(open);
                securityLTP.setClose(close);
                securityLTP.setHigh(high);
                securityLTP.setLow(low);
            }
            list.add(securityLTP);
        });
        return list;
    }
}
