package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@ToString
@EqualsAndHashCode
public class LiveMarketDataTick {


    @Getter(AccessLevel.NONE)
    private ExchangeSegment exchangeSegment;

    private int instrumentToken;
    private double lastTradedPrice;
    private long lastTradedQuantity;
    private double averageTradedPrice;
    private double volume;
    private double buyQuantity;
    private double sellQuantity;
    private double openInterest;

    public LiveMarketDataTick(ByteBuffer buffer) {
        ExchangeSegment exchangeSegment = ExchangeSegment.findByCode(buffer.get());
        int instrumentToken = buffer.getInt();
        double lastTradedPrice = buffer.getDouble();
        long lastTradedQuantity = buffer.getInt();
        double averageTradedPrice = buffer.getDouble();
        double volume = buffer.getDouble();
        double buyQuantity = buffer.getDouble();
        double sellQuantity = buffer.getDouble();
        double openInterest = buffer.getDouble();
    }

}
