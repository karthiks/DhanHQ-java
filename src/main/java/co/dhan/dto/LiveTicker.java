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
public class LiveTicker {

//    @Getter(AccessLevel.NONE)
    private ExchangeSegment exchangeSegment;
    private String securityID; //ISIN = 12-char alpha-numeric
    private BigDecimal lastTradingPrice;
    private Instant lastTradingTime;

    public LiveTicker(ExchangeSegment es, String securityID, BigDecimal ltp, Instant ltt) {
        this.exchangeSegment = es;
        this.securityID = securityID;
        this.lastTradingPrice = ltp;
        this.lastTradingTime = ltt;
    }

    public LiveTicker(ByteBuffer buffer) {
//        if (buffer.remaining() < 16) {
//            System.out.println("Incomplete ticker data received.");
//            return;
//        }
        /*
        buffer.position(0); // Reset buffer position
        buffer.get(); // Skip the first byte (message type
        short messageLength = buffer.getShort();
        byte exchangeSegmentCode = buffer.get();
        int securityid = buffer.getInt();
        float ltp = buffer.getFloat();
        int ltt = buffer.getInt(); */
        //Extracting values based on format param to struct.unpack(format, bytestream) in python SDK - marketfeed.process_ticker()
        byte exchangeSegmentCode = buffer.get(3);
        int securityid = buffer.getInt(4);
        float ltp = buffer.getFloat(8);
        int ltt = buffer.getInt(12);
        exchangeSegment = ExchangeSegment.findByCode(exchangeSegmentCode);
        securityID = String.valueOf(securityid); //?? Or Join getChar() N times based on length of security
        lastTradingPrice = toBigDecimal(ltp);
        lastTradingTime = Instant.ofEpochSecond(ltt);
    }

    public static int sizeInBytes() {
        int sizeOfExchangeSegment = Byte.SIZE,
                sizeOfSecurityId = Integer.SIZE, // Should it be 2 time number of chars + 38 for its metadata??
                sizeOfLastTradingPrice = Float.SIZE,
                sizeOfTradingTime = Integer.SIZE;
        return sizeOfExchangeSegment + sizeOfSecurityId + sizeOfLastTradingPrice + sizeOfTradingTime;
    }
}
