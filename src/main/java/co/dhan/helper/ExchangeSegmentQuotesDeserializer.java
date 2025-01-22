package co.dhan.helper;

import co.dhan.constant.ExchangeSegment;
import co.dhan.dto.*;
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

import static co.dhan.dto.Depth.JSONPropertyBuy;
import static co.dhan.dto.Depth.JSONPropertySell;
import static co.dhan.dto.ExchangeSegmentQuotesWrapper.JSONPropertyData;
import static co.dhan.dto.Quote.*;

public class ExchangeSegmentQuotesDeserializer
        extends JsonDeserializer<ExchangeSegmentQuotesWrapper> {
    @Override
    public ExchangeSegmentQuotesWrapper deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode dataNode = jsonNode.get(JSONPropertyData);

        Map<ExchangeSegment, List<Quote>> map = new HashMap<>();
        dataNode.fields().forEachRemaining(field -> {
            ExchangeSegment segment = ExchangeSegment.valueOf(field.getKey());
            List<Quote> list = buildListOfQuotes(field);
            map.put(segment, list);
        });

        return new ExchangeSegmentQuotesWrapper(map);
    }

    @NotNull
    private static List<Quote> buildListOfQuotes(Map.Entry<String, JsonNode> field) {
        List<Quote> list = new ArrayList<>();
        field.getValue().fields().forEachRemaining(securityField -> {
            Quote quote = new Quote();
            quote.setSecurity(securityField.getKey());
            JsonNode securityJsonNode = securityField.getValue();
            quote.setAveragePrice(securityJsonNode.get(JSONPropertyAveragePrice).asText());
            quote.setBuyQuantity(securityJsonNode.get(JSONPropertyBuyQuantity).asText());
            quote.setLastPrice(securityJsonNode.get(JSONPropertyLastPrice).asText());
            quote.setLastQuantity(securityJsonNode.get(JSONPropertyLastQuantity).asText());
            quote.setLastTradeTime(securityJsonNode.get(JSONPropertyLastTradeTime).asText());
            quote.setLowerCircuitLimit(securityJsonNode.get(JSONPropertyLowerCircuitLimit).asText());
            quote.setNetChange(securityJsonNode.get(JSONPropertyNetChange).asText());
            quote.setOi(securityJsonNode.get(JSONPropertyOI).asText());
            quote.setOiDayHigh(securityJsonNode.get(JSONPropertyOIDayHigh).asText());
            quote.setOiDayLow(securityJsonNode.get(JSONPropertyOIDayLow).asText());
            quote.setSellQuantity(securityJsonNode.get(JSONPropertySellQuantity).asText());
            quote.setUpperCircuitLimit(securityJsonNode.get(JSONPropertyUpperCircuitLimit).asText());
            quote.setVolume(securityJsonNode.get(JSONPropertyVolume).asText());

            JsonNode ohlcJsonNode = securityJsonNode.get(JSONPropertyOHLC);
            quote.setOpen(ohlcJsonNode.get(JSONPropertyOpen).asText());
            quote.setClose(ohlcJsonNode.get(JSONPropertyClose).asText());
            quote.setHigh(ohlcJsonNode.get(JSONPropertyHigh).asText());
            quote.setLow(ohlcJsonNode.get(JSONPropertyLow).asText());

            JsonNode depthJsonNode = securityJsonNode.get(JSONPropertyDepth);
            Depth depth = new Depth();

            JsonNode buyJsonNode = depthJsonNode.get(JSONPropertyBuy);
            List<Bid> bids = new ArrayList<>();
            buyJsonNode.forEach( buy -> {
                Bid bid = new Bid();
                bid.setOrders(buy.get(Bid.JSONPropertyOrders).asText());
                bid.setQuantity(buy.get(Bid.JSONPropertyQuantity).asText());
                bid.setPrice(buy.get(Bid.JSONPropertyPrice).asText());
                bids.add(bid);
            });
            depth.setBuy(bids);

            JsonNode sellJsonNode = depthJsonNode.get(JSONPropertySell);
            List<Ask> asks = new ArrayList<>();
            sellJsonNode.forEach( sell -> {
                Ask ask = new Ask();
                ask.setOrders(sell.get(Ask.JSONPropertyOrders).asText());
                ask.setQuantity(sell.get(Ask.JSONPropertyQuantity).asText());
                ask.setPrice(sell.get(Ask.JSONPropertyPrice).asText());
                asks.add(ask);
            });
            depth.setSell(asks);

            quote.setDepth(depth);
            list.add(quote);
        });
        return list;
    }
}
