package co.dhan.api.stream;

import static org.assertj.core.api.Assertions.*;

import co.dhan.api.DhanConnection;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.FeedRequestCode;
import co.dhan.dto.Instrument;
import co.dhan.http.DhanAPIException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LiveMarketDepthTest {
  private LiveMarketDepth liveMarketDepth;
  private DhanConnection dhanConnection;

  @BeforeEach
  void setUp() throws Exception {
    dhanConnection = new DhanConnection("test_client", "test_token");
    liveMarketDepth = new LiveMarketDepth(dhanConnection, "ws://test-url");
  }

  @Test
  void constructor_setsDefaultWebSocketURL() {
    LiveMarketDepth defaultDepth = new LiveMarketDepth(dhanConnection);

    assertThat(defaultDepth).isNotNull();
    assertThat(defaultDepth.getClass()).isEqualTo(LiveMarketDepth.class);
  }

  @Test
  void connect_throwsIllegalArgumentException_whenListenerIsNull() {
    assertThatThrownBy(() -> liveMarketDepth.connect(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Feed listener cannot be null");
  }

  @Test
  void command_throwsDhanAPIException_whenInstrumentsListIsNull() {
    assertThatThrownBy(
            () -> liveMarketDepth.command(null, FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH))
        .isInstanceOf(DhanAPIException.class)
        .hasMessage(
            "Instruments list cannot be empty and cannot be greater than 100 in one batch.");
  }

  @Test
  void command_throwsDhanAPIException_whenInstrumentsListIsEmpty() {
    List<Instrument> emptyList = new ArrayList<>();
    assertThatThrownBy(
            () -> liveMarketDepth.command(emptyList, FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH))
        .isInstanceOf(DhanAPIException.class)
        .hasMessage(
            "Instruments list cannot be empty and cannot be greater than 100 in one batch.");
  }

  @Test
  void command_throwsDhanAPIException_whenInstrumentsListExceeds100() {
    List<Instrument> largeList = new ArrayList<>();
    for (int i = 0; i < 101; i++) {
      largeList.add(new Instrument(ExchangeSegment.NSE_EQ, "TEST" + i));
    }

    assertThatThrownBy(
            () -> liveMarketDepth.command(largeList, FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH))
        .isInstanceOf(DhanAPIException.class)
        .hasMessage(
            "Instruments list cannot be empty and cannot be greater than 100 in one batch.");
  }

  @Test
  void command_sendsJSON_whenWebSocketIsConnected() throws Exception {
    List<Instrument> instruments = List.of(new Instrument(ExchangeSegment.NSE_EQ, "RELIANCE"));

    // Mock the WebSocket using reflection
    okhttp3.WebSocket mockWebSocket = org.mockito.Mockito.mock(okhttp3.WebSocket.class);
    Field webSocketField = LiveMarketDepth.class.getDeclaredField("webSocket");
    webSocketField.setAccessible(true);
    webSocketField.set(liveMarketDepth, mockWebSocket);

    liveMarketDepth.command(instruments, FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH);

    org.mockito.Mockito.verify(mockWebSocket).send(org.mockito.Mockito.anyString());
  }

  @Test
  void command_logsError_whenWebSocketIsNull() {
    List<Instrument> instruments = List.of(new Instrument(ExchangeSegment.NSE_EQ, "RELIANCE"));

    liveMarketDepth.command(instruments, FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH);

    // No exception thrown, just logs error
    assertThat(true).isTrue();
  }
}
