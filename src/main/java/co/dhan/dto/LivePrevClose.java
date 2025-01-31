package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import static co.dhan.helper.BigDecimalUtils.toBigDecimal;

@Getter
@ToString
@EqualsAndHashCode
public class LivePrevClose {
    private ExchangeSegment exchangeSegment;
    private String securityID;
    private BigDecimal prev_close;
    private int prev_OI;

    public LivePrevClose(ByteBuffer buffer) {
        //Extracting values based on format param to struct.unpack(format, bytestream) in python SDK - marketfeed.process_prev_close()
        byte exchangeSegmentCode = buffer.get(3);
        int securityid = buffer.getInt(4);
        float prevClose = buffer.getFloat(5);
        int prevoi = buffer.getInt(6);
        exchangeSegment = ExchangeSegment.findByCode(exchangeSegmentCode);
        securityID = String.valueOf(securityid);
        prev_close = toBigDecimal(prevClose);
        prev_OI = prevoi;
    }
}
