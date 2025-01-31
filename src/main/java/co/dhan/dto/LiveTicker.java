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
}
