package co.dhan.dto;

import co.dhan.UnitTestRoot;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.FeedRequestCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentsFeedRequestWrapperTest extends UnitTestRoot {

    @Test
    public void test_toJSON_successful_conversion() {
        String expectedJSONString = """
                {"RequestCode":15,"InstrumentCount":2,"InstrumentList":[{"ExchangeSegment":"NSE_EQ","SecurityId":"securityId1"},{"ExchangeSegment":"NSE_EQ","SecurityId":"securityId2"}]}""";
        List<Instrument> list = List.of(new Instrument(ExchangeSegment.NSE_EQ, "securityId1"),
                new Instrument(ExchangeSegment.NSE_EQ, "securityId2"));
        assertThat(new InstrumentsFeedRequestWrapper(list, FeedRequestCode.SUBSCRIBE_TICK).toJSON())
                .isEqualTo(expectedJSONString);
    }
}
