package co.dhan.dto;

import co.dhan.constant.ExchangeSegment;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LiveDepthTest {

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
        b.setPrice("99.99");
        b.setQuantity("100");
        b.setOrders("1");

        ByteBuffer bb = ByteBuffer.allocate(20); // 8 (Double) + 3 times 4Bytes (Int)
        bb.putDouble(Double.parseDouble(b.getPrice()));
        bb.putInt(Integer.parseInt(b.getQuantity()));
        bb.putInt(Integer.parseInt(b.getOrders()));
        bb.putInt(-99);
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
        a.setPrice("99.99");
        a.setQuantity("100");
        a.setOrders("1");

        ByteBuffer bb = ByteBuffer.allocate(20); // 8 (Double) + 3 times 4Bytes (Int)
        bb.putDouble(Double.parseDouble(a.getPrice()));
        bb.putInt(Integer.parseInt(a.getQuantity()));
        bb.putInt(Integer.parseInt(a.getOrders()));
        bb.putInt(-99);
        bb.flip(); // Prepare for reading

        LiveDepth liveDepth = new LiveDepth("1", ExchangeSegment.NSE_EQ);
        List<Ask> asks = liveDepth.parseAsks(bb);

        assertThat(asks.size()).isEqualTo(1);
        assertThat(asks).isEqualTo(List.of(a));
    }

}