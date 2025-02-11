package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import co.dhan.helper.BigDecimalUtils;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LiveDepthTest {

    public static final int RANDOM_VALUE = -99;

    @Test
    void parseBidsReturnEmptyListWhenByteSizeIsLessThan16Bytes() {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.flip(); // Prepare for reading

        LiveDepth liveDepth = new LiveDepth("1", ExchangeSegment.NSE_EQ);
        List<Bid> bids = liveDepth.parseBids(bb);

        assertThat(bids.isEmpty()).isTrue();
    }

    @Test
    void parseBidsReturnNonEmptyListWhenByteSizeIsGreaterThanOrEqualTo16Bytes() {
        Bid b = new Bid();
        b.setPrice(BigDecimalUtils.toBigDecimal("99.99"));
        b.setQuantity(100);
        b.setOrders(1);

        ByteBuffer bb = ByteBuffer.allocate(20); // 8 (Double) + 3 times 4Bytes (Int)
        bb.putDouble(b.getPrice().doubleValue());
        bb.putInt(b.getQuantity());
        bb.putInt(b.getOrders());
        bb.putInt(RANDOM_VALUE);
        bb.flip(); // Prepare for reading

        LiveDepth liveDepth = new LiveDepth("1", ExchangeSegment.NSE_EQ);
        List<Bid> bids = liveDepth.parseBids(bb);

        assertThat(bids.size()).isEqualTo(1);
        assertThat(bids).isEqualTo(List.of(b));
    }

    @Test
    void parseAsksReturnEmptyListWhenByteSizeIsLessThan16Bytes() {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.flip(); // Prepare for reading

        LiveDepth liveDepth = new LiveDepth("1", ExchangeSegment.NSE_EQ);
        List<Ask> asks = liveDepth.parseAsks(bb);

        assertThat(asks.isEmpty()).isTrue();
    }

    @Test
    void parseAsksReturnNonEmptyListWhenByteSizeIsGreaterThanOrEqualTo16Bytes() {
        Ask a = new Ask();
        a.setPrice(BigDecimalUtils.toBigDecimal("99.99"));
        a.setQuantity(100);
        a.setOrders(1);

        ByteBuffer bb = ByteBuffer.allocate(20); // 8 (Double) + 3 times 4Bytes (Int)
        bb.putDouble(a.getPrice().doubleValue());
        bb.putInt(a.getQuantity());
        bb.putInt(a.getOrders());
        bb.putInt(RANDOM_VALUE);
        bb.flip(); // Prepare for reading

        LiveDepth liveDepth = new LiveDepth("1", ExchangeSegment.NSE_EQ);
        List<Ask> asks = liveDepth.parseAsks(bb);

        assertThat(asks.size()).isEqualTo(1);
        assertThat(asks).isEqualTo(List.of(a));
    }

}