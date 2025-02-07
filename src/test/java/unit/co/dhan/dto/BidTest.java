package co.dhan.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BidTest {

    @Test
    void setPriceShouldTrimTrailingZeros() {
        Bid bid = new Bid();
        bid.setPrice("0.00");
        Assertions.assertThat(bid.getPrice()).isEqualTo("0");
    }

    @Test
    void setPriceShouldShowPricePrecisionSetTo2DecimalPlaces() {
        Bid bid = new Bid();
        bid.setPrice("9.555");
        Assertions.assertThat(bid.getPrice()).isEqualTo("9.56");
    }
}