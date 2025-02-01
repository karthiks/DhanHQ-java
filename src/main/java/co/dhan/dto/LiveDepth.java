package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
public class LiveDepth {
    private String securityID;
    private ExchangeSegment exchangeSegment;

    private List<Bid> buy;
    private List<Ask> sell;

    public LiveDepth(String securityID, ExchangeSegment exchangeSegment) {
        this.securityID = securityID;
        this.exchangeSegment = exchangeSegment;
    }

    public static List<Bid> parseBids(ByteBuffer buffer) {
        buffer.position(13);
        int tuple_size = 16; // 8 + 4 + 4 bytes
        List<Bid> bids = new ArrayList<>();
        while (buffer.remaining() > tuple_size) {
            double price = buffer.getDouble();
            int qty = buffer.getInt();
            int orders = buffer.getInt();
            Bid b = new Bid();
            b.setPrice(String.valueOf(price));
            b.setQuantity(String.valueOf(qty));
            b.setOrders(String.valueOf(orders));
            bids.add(b);
        }
        return bids;
    }

    public static List<Ask> parseAsks(ByteBuffer buffer) {
        int tuple_size = 12;
        List<Ask> asks = new ArrayList<>();
        while (buffer.remaining() > tuple_size) {
            float price = buffer.getFloat();
            int qty = buffer.getInt();
            int orders = buffer.getInt();
            Ask a = new Ask();
            a.setPrice(String.valueOf(price));
            a.setQuantity(String.valueOf(qty));
            a.setOrders(String.valueOf(orders));
            asks.add(a);
        }
        return asks;
    }
}
