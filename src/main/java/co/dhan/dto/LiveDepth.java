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
//        System.out.println("ByteBuffer's current position is " + buffer.position());
//        buffer.position(12);
        int tuple_size = 16; // 8 + 4 + 4 bytes
        int size = 0;
        List<Bid> bids = new ArrayList<>();
        while (buffer.remaining() > tuple_size) {
//            System.out.print("buffer.remaining() = " + buffer.remaining() + ": ");
            double price = buffer.getDouble();
            int qty = buffer.getInt();
            int orders = buffer.getInt();
//            System.out.println(String.format("(price=%.2f, qty=%d, orders=%d)",price,qty,orders));
            if(price>0 && qty>0 && orders>0) {
                Bid b = new Bid();
                b.setPrice(String.valueOf(price));
                b.setQuantity(String.valueOf(qty));
                b.setOrders(String.valueOf(orders));
                bids.add(b);
            }
            size += 1;
        }
//        System.out.println("Loop Iteration Count for Bids = " + size);
        return bids;
    }

    public static List<Ask> parseAsks(ByteBuffer buffer) {
        System.out.println("ByteBuffer's current position is " + buffer.position());
        int tuple_size = 12;
        List<Ask> asks = new ArrayList<>();
        int size = 0;
        while (buffer.remaining() > tuple_size) {
//            System.out.print("buffer.remaining() = " + buffer.remaining() + ": ");
            float price = buffer.getFloat();
            int qty = buffer.getInt();
            int orders = buffer.getInt();
//            System.out.println(String.format("(price=%.2f, qty=%d, orders=%d)",price,qty,orders));
            if(price>0 && qty>0 && orders>0) {
                Ask a = new Ask();
                a.setPrice(String.valueOf(price));
                a.setQuantity(String.valueOf(qty));
                a.setOrders(String.valueOf(orders));
                asks.add(a);
            }
            size += 1;
        }
//        System.out.println("Loop Iteration Count for Asks = " + size);
        return asks;
    }
}
