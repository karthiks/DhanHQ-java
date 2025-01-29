package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.Instant;

import static co.dhan.helper.BigDecimalUtils.toBigDecimal;

@Getter
@ToString
@EqualsAndHashCode
public class LiveQuote {
    private ExchangeSegment exchangeSegment;
    private String securityID; //ISIN = 12-char alpha-numeric
    private BigDecimal lastPrice;
    private int lastQuantity;
    private Instant lastTradeTime;
    /**
     * Average Trade Price (ATP)
     */
    private BigDecimal averagePrice;
    private int volume;
    /**
     * Total Sell Quantity
     */
    private int sellQuantity;
    /**
     * Total Buy Quantity
     */
    private int buyQuantity;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;

    public LiveQuote(ByteBuffer buffer) {
        //Extracting values based on format param to struct.unpack(format, bytestream) in python SDK - marketfeed.process_quote()
        /* Warning: Buffer reading follows order sequence as in '<BHBIfHIfIIIffff'. Do not change it below: */
        buffer.position(0); // Reset buffer position
        byte code = buffer.get(); // Skip the first byte (message type
        short messageLength = buffer.getShort();
        exchangeSegment = ExchangeSegment.findByCode(buffer.get());
        securityID = String.valueOf(buffer.getInt());
        lastPrice = toBigDecimal(buffer.getFloat());
        lastQuantity = buffer.getShort();
        lastTradeTime =  Instant.ofEpochSecond(buffer.getInt()); //DateUtils.convertToDateTime(buffer.getInt());
        averagePrice = toBigDecimal(buffer.getFloat());
        volume = buffer.getInt();
        sellQuantity = buffer.getInt();
        buyQuantity = buffer.getInt();
        open = toBigDecimal(buffer.getFloat());
        close = toBigDecimal(buffer.getFloat());
        high = toBigDecimal(buffer.getFloat());
        low = toBigDecimal(buffer.getFloat());
    }
}
