package co.dhan.dto;

import co.dhan.constant.FeedRequestCode;
import co.dhan.helper.HTTPUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
public class InstrumentsFeedRequestWrapper {

    @JsonProperty("RequestCode")
    private final int feedRequestCode;

    @JsonProperty("InstrumentCount")
    private final int instrumentCount;

    @JsonProperty("InstrumentList")
    private final List<Instrument> instrumentList;

    public InstrumentsFeedRequestWrapper(List<Instrument> instrumentList, FeedRequestCode feedRequestCode) {
        this.feedRequestCode = feedRequestCode.getCode();
        this.instrumentList = instrumentList;
        this.instrumentCount = instrumentList.size();
    }

    public String toJSON() {
        try {
            String json = HTTPUtils.DhanObjectMapper.writeValueAsString(this);
            System.out.println("Subscription request: " + json);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Error converting object of type " + getClass().getName() + " to JSON string.", e);
            return null;
        }
    }
}
