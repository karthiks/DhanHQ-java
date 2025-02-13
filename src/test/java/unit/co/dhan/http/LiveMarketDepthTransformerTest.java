package co.dhan.http;

import co.dhan.UnitTestRoot;
import co.dhan.api.stream.LiveMarketDepthListener;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.FeedResponseCode;
import co.dhan.dto.Ask;
import co.dhan.dto.Bid;
import co.dhan.dto.LiveDepth;
import co.dhan.helper.BigDecimalUtils;
import okio.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class LiveMarketDepthTransformerTest extends UnitTestRoot {

    @Mock private LiveMarketDepthListener mockDepthListener;
    private LiveDepth liveDepth;
    private List<Bid> bids;
    private List<Ask> asks;

    @BeforeEach
    public void setup() {
        bids = new ArrayList<>();
        for (int i=1;i<=20;i++) {
            bids.add(new Bid(BigDecimalUtils.toBigDecimal(String.valueOf(i)), i, i));
        }

        asks = new ArrayList<>();
        for (int i=1;i<=20;i++) {
            String s = Integer.toString(i);
            asks.add(new Ask(BigDecimalUtils.toBigDecimal(String.valueOf(i)), i, i));
        }

        String securityID = "1";
        liveDepth = new LiveDepth(securityID, ExchangeSegment.NSE_EQ);
        liveDepth.setBuy(bids);
        liveDepth.setSell(asks);
    }

    @Test
    public void testSplitByteArray_EmptyArray() {
        byte[] originalArray = new byte[0];
        int chunkSize = 3;
        byte[][] result = LiveMarketDepthTransformer.splitByteArray(originalArray, chunkSize);

        assertThat(result.length)
                .withFailMessage("Empty array should return an empty result")
                .isEqualTo(0);
    }

    @Test
    public void testSplitByteArray_SmallerThanChunkSize() {
        byte[] originalArray = {1, 2};
        int chunkSize = 3;
        byte[][] result = LiveMarketDepthTransformer.splitByteArray(originalArray, chunkSize);

        assertThat(result.length)
                .withFailMessage("Array smaller than chunk size should return EMPTY array")
                .isEqualTo(0);
    }

    @Test
    public void testSplitByteArray_ExactlyDivisible() {
        byte[] originalArray = {1, 2, 3, 4, 5, 6};
        int chunkSize = 3;
        byte[][] result = LiveMarketDepthTransformer.splitByteArray(originalArray, chunkSize);

        assertThat(result.length).isEqualTo(2);
        assertThat(result[0]).containsExactly(1, 2, 3);
        assertThat(result[1]).containsExactly(4, 5, 6);
    }

    @Test
    public void testSplitByteArray_NotExactlyDivisible() {
        byte[] originalArray = {1, 2, 3, 4, 5};
        int chunkSize = 2;
        byte[][] result = LiveMarketDepthTransformer.splitByteArray(originalArray, chunkSize);

        assertThat(result.length).isEqualTo(2);
        assertThat(result[0]).containsExactly(1, 2);
        assertThat(result[1]).containsExactly(3, 4);
    }

    @Test
    void onMessageSuccess_ReceivedBinaryDataIsTransformedToBidsListAndAsksList() {
        String expectedSecurityID = "1";
        LiveDepth expectedLiveDepthForBids = new LiveDepth(expectedSecurityID, ExchangeSegment.NSE_EQ);
        expectedLiveDepthForBids.setBuy(bids);
        LiveDepth expectedLiveDepthForAsks = new LiveDepth(expectedSecurityID, ExchangeSegment.NSE_EQ);
        expectedLiveDepthForAsks.setSell(asks);
        LiveMarketDepthTransformer transformer = new LiveMarketDepthTransformer(mockDepthListener);
        byte[] littleEndianByteArray = liveDepthToByteArray(liveDepth);
        ByteString bytes = ByteString.of(littleEndianByteArray);
        transformer.onMessage(null, bytes);
        verify(mockDepthListener).onBidsArrival(expectedLiveDepthForBids);
        verify(mockDepthListener).onAsksArrival(expectedLiveDepthForAsks);
    }

    private byte[] reverseBytes(byte[] bytes) {
        byte[] reversedBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversedBytes[i] = bytes[bytes.length - 1 - i];
        }
        return reversedBytes;
    }

    private byte[] liveDepthToByteArray(LiveDepth ld) {
        // headerByteSize = 2b(messageLength=332) + 1b(FeedResponseCode) + 1b(ExchangeSegmentCode) + 4b(SecurityID) + 4b(IgnoreMsg)
        Short messageLength = Short.valueOf("332"); // = headerByteSize(12) + listByteSize(320)
        int randomNumberToIgnore = 100;

        byte twentyDepthBidsPacketCode = (byte) FeedResponseCode.TWENTY_DEPTH_BIDS_PACKET.getCode();
        byte twentyDepthAsksPacketCode = (byte) FeedResponseCode.TWENTY_DEPTH_ASKS_PACKET.getCode();
        byte exchangeSegmentCode = (byte) ld.getExchangeSegment().getCode();
        int securityID = Integer.parseInt(ld.getSecurityID());

        byte[] twenty_depth_bids_packet = buildBidsPacketFrom(ld,messageLength,
                twentyDepthBidsPacketCode,exchangeSegmentCode,
                securityID,randomNumberToIgnore);

        byte[] twenty_depth_asks_packet = buildAsksPacketFrom(ld,messageLength,
                twentyDepthAsksPacketCode,exchangeSegmentCode,
                securityID, randomNumberToIgnore);

        return concatenateByteArrays(twenty_depth_bids_packet, twenty_depth_asks_packet);
    }

    private byte[] buildBidsPacketFrom(LiveDepth ld,
                                       short messageLength,
                                       byte feedResponseCode,
                                       byte exchangeSegmentCode,
                                       int securityID,
                                       int randomNumberToIgnore) {
        ByteBuffer bidsBuffer = ByteBuffer
                .allocate(messageLength)
                .order(ByteOrder.LITTLE_ENDIAN);

        //Header
        bidsBuffer.putShort(Short.valueOf(messageLength));
        bidsBuffer.put(feedResponseCode);
        bidsBuffer.put(exchangeSegmentCode);
        bidsBuffer.putInt(securityID);
        bidsBuffer.putInt(randomNumberToIgnore);

        //Body
        for (Bid bid : liveDepth.getBuy()) {
            bidsBuffer.putDouble(bid.getPrice().doubleValue());
            bidsBuffer.putInt(bid.getQuantity());
            bidsBuffer.putInt(bid.getOrders());
        }
        return bidsBuffer.array();
    }

    private byte[] buildAsksPacketFrom(LiveDepth ld,
                                       short messageLength,
                                       byte feedResponseCode,
                                       byte exchangeSegmentCode,
                                       int securityID,
                                       int randomNumberToIgnore) {
        ByteBuffer bidsBuffer = ByteBuffer
                .allocate(messageLength)
                .order(ByteOrder.LITTLE_ENDIAN);

        //Header
        bidsBuffer.putShort(Short.valueOf(messageLength));
        bidsBuffer.put(feedResponseCode);
        bidsBuffer.put(exchangeSegmentCode);
        bidsBuffer.putInt(securityID);
        bidsBuffer.putInt(randomNumberToIgnore);

        //Body
        for (Ask ask : liveDepth.getSell()) {
            bidsBuffer.putDouble(ask.getPrice().doubleValue());
            bidsBuffer.putInt(ask.getQuantity());
            bidsBuffer.putInt(ask.getOrders());
        }
        return bidsBuffer.array();
    }

    private byte[] concatenateByteArrays(byte[] array1, byte[] array2) {
        ByteBuffer buffer = ByteBuffer.allocate(array1.length + array2.length);
        buffer.put(array1);
        buffer.put(array2);
        return buffer.array();
    }

}