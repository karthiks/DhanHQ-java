package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@Data
public class LiveDepth {
    // 8 + 4 + 4 bytes
    public static final int TUPLE_SIZE = 16;
    private String securityID;
    private ExchangeSegment exchangeSegment;

    private List<Bid> buy;
    private List<Ask> sell;

    public LiveDepth(String securityID, ExchangeSegment exchangeSegment) {
        this.securityID = securityID;
        this.exchangeSegment = exchangeSegment;
    }

    public static List<Bid> parseBids(ByteBuffer buffer) {
        log.debug("ByteBuffer's current position is " + buffer.position() + " and buffer size is " + buffer.remaining());
//        buffer.position(12);
        List<Bid> bids = new ArrayList<>();
        while (buffer.remaining() >= TUPLE_SIZE) {
            Bid b = new Bid();
            b.setPrice(getDoubleAsStringFrom(buffer));
            b.setQuantity(getIntAsStringFrom(buffer));
            b.setOrders(getIntAsStringFrom(buffer));
            bids.add(b);
        }
        return bids;
    }

    public static List<Ask> parseAsks(ByteBuffer buffer) {
        log.debug("ByteBuffer's current position is " + buffer.position() + " and buffer size is " + buffer.remaining());
        List<Ask> asks = new ArrayList<>();
        while (buffer.remaining() >= TUPLE_SIZE) {
            Ask a = new Ask();
            a.setPrice(getDoubleAsStringFrom(buffer));
            a.setQuantity(getIntAsStringFrom(buffer));
            a.setOrders(getIntAsStringFrom(buffer));
            asks.add(a);
        }
        return asks;
    }

    @NotNull
    private static String getIntAsStringFrom(ByteBuffer buffer) {
        return String.valueOf(buffer.getInt());
    }

    @NotNull
    private static String getDoubleAsStringFrom(ByteBuffer buffer) {
        return String.valueOf(buffer.getDouble());
    }

}
