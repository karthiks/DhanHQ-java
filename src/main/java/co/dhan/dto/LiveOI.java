package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.nio.ByteBuffer;

@Getter
@ToString
@EqualsAndHashCode
public class LiveOI {
    private ExchangeSegment exchangeSegment;
    private String securityID; //ISIN = 12-char alpha-numeric
    private int oi;

    public LiveOI(ByteBuffer buffer) {
        //Extracting values based on format param to struct.unpack(format, bytestream) in python SDK - marketfeed.process_oi()
        /* Warning: Buffer reading follows order sequence as in '<BHBII'. Do not change it below: */
        buffer.position(0); // Reset buffer position
        byte code = buffer.get(); // Skip the first byte (message type
        short messageLength = buffer.getShort();
        exchangeSegment = ExchangeSegment.findByCode(buffer.get());
        securityID = String.valueOf(buffer.getInt());
        oi = buffer.getInt();
    }
}
