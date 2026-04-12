package co.dhan.http;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import co.dhan.UnitTestRoot;
import co.dhan.api.stream.listener.LiveMarketFeedListener;
import co.dhan.constant.FeedResponseCode;
import co.dhan.dto.LiveOI;
import co.dhan.dto.LivePrevClose;
import co.dhan.dto.LiveQuote;
import co.dhan.dto.LiveQuoteMax;
import co.dhan.dto.LiveTicker;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class LiveMarketFeedTransformerTest extends UnitTestRoot {

  @Mock private LiveMarketFeedListener mockFeedListener;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void onOpen_Success_CallsOnConnection() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    Response mockResponse = Mockito.mock(Response.class);

    transformer.onOpen(null, mockResponse);

    verify(mockFeedListener).onConnection();
  }

  @Test
  void onClosed_Success_CallsOnTerminationWithDhanAPIException() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    String reason = "Connection closed";

    transformer.onClosed(null, 1001, reason);

    verify(mockFeedListener)
        .onTermination(
            Mockito.argThat(
                e ->
                    e.getMessage().contains("Client terminated the connection")
                        && e.getCode().equals("1001")
                        && e.getMessage().contains(reason)));
  }

  @Test
  void onFailure_WithResponse_CallsOnErrorWithResponseBody() throws IOException {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    Response mockResponse = Mockito.mock(Response.class);
    ResponseBody mockBody = Mockito.mock(ResponseBody.class);

    Mockito.when(mockResponse.code()).thenReturn(500);
    Mockito.when(mockResponse.body()).thenReturn(mockBody);
    Mockito.when(mockBody.string()).thenReturn("Internal Server Error");

    transformer.onFailure(null, new RuntimeException("Test failure"), mockResponse);

    verify(mockFeedListener)
        .onError(
            Mockito.argThat(
                e ->
                    e instanceof DhanAPIException
                        && ((DhanAPIException) e).getCode().equals("500")
                        && ((DhanAPIException) e).getMessage().equals("Internal Server Error")));
  }

  @Test
  void onFailure_WithResponse_IOException_CallsOnErrorWithResponseToString() throws IOException {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    Response mockResponse = Mockito.mock(Response.class);
    ResponseBody mockBody = Mockito.mock(ResponseBody.class);

    Mockito.when(mockResponse.code()).thenReturn(500);
    Mockito.when(mockResponse.body()).thenReturn(mockBody);
    Mockito.when(mockBody.string()).thenThrow(new IOException("Read failed"));
    Mockito.when(mockResponse.toString()).thenReturn("HTTP 500 - Internal Server Error");

    transformer.onFailure(null, new RuntimeException("Test failure"), mockResponse);

    verify(mockFeedListener)
        .onError(
            Mockito.argThat(
                e ->
                    e instanceof DhanAPIException
                        && ((DhanAPIException) e).getCode().isEmpty()
                        && ((DhanAPIException) e).getMessage().contains("500")));
  }

  @Test
  void onFailure_WithoutResponse_CallsOnErrorWithThrowableMessage() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    RuntimeException testException = new RuntimeException("Network error");

    transformer.onFailure(null, testException, null);

    verify(mockFeedListener).onError(Mockito.argThat(e -> e.getMessage().equals("Network error")));
  }

  @Test
  void onMessage_TickerPacket_Success_CallsOnTickerArrival() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    byte[] tickerData = createMinimalTickerPacket();
    ByteString bytes = ByteString.of(tickerData);

    transformer.onMessage(null, bytes);

    verify(mockFeedListener).onTickerArrival(Mockito.any(LiveTicker.class));
  }

  @Test
  void onMessage_PrevClosePacket_Success_CallsOnPrevCloseArrival() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    byte[] prevCloseData = createMinimalPrevClosePacket();
    ByteString bytes = ByteString.of(prevCloseData);

    transformer.onMessage(null, bytes);

    verify(mockFeedListener).onPrevCloseArrival(Mockito.any(LivePrevClose.class));
  }

  @Test
  void onMessage_QuotePacket_Success_CallsOnQuoteArrival() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    byte[] quoteData = createMinimalQuotePacket();
    ByteString bytes = ByteString.of(quoteData);

    transformer.onMessage(null, bytes);

    verify(mockFeedListener).onQuoteArrival(Mockito.any(LiveQuote.class));
  }

  @Test
  void onMessage_OIPacket_Success_CallsOnOIArrival() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    byte[] oiData = createMinimalOIPacket();
    ByteString bytes = ByteString.of(oiData);

    transformer.onMessage(null, bytes);

    verify(mockFeedListener).onOIArrival(Mockito.any(LiveOI.class));
  }

  @Test
  void onMessage_FullPacket_Success_CallsOnFullPacketArrival() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    byte[] fullData = createMinimalFullPacket();
    ByteString bytes = ByteString.of(fullData);

    transformer.onMessage(null, bytes);

    verify(mockFeedListener).onFullPacketArrival(Mockito.any(LiveQuoteMax.class));
  }

  @Test
  void onMessage_FeedDisconnect_Success_CallsOnTerminationWithDataAPIError() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    byte[] disconnectData = createMinimalFeedDisconnectPacket();
    ByteString bytes = ByteString.of(disconnectData);

    transformer.onMessage(null, bytes);

    verify(mockFeedListener).onTermination(Mockito.any(DhanAPIException.class));
  }

  @Test
  void onMessage_UndefinedResponseCode_Success_CallsOnError() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    byte[] undefinedData = new byte[] {(byte) 0xFF, 0x00, 0x00, 0x00};
    ByteString bytes = ByteString.of(undefinedData);

    transformer.onMessage(null, bytes);

    verify(mockFeedListener).onError(Mockito.any(Exception.class));
  }

  @Test
  void onMessage_ExceptionDuringProcessing_Success_LogsErrorWithoutPropagating() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);
    byte[] invalidData = new byte[] {0x02}; // Ticker packet with insufficient data
    ByteString bytes = ByteString.of(invalidData);

    transformer.onMessage(null, bytes);

    verifyNoInteractions(mockFeedListener);
  }

  @Test
  void onMessage_MultipleMessages_Success_ProcessesEachMessage() {
    LiveMarketFeedTransformer transformer = new LiveMarketFeedTransformer(mockFeedListener);

    // Simulate two messages - first ticker, then quote
    transformer.onMessage(null, ByteString.of(createMinimalTickerPacket()));
    transformer.onMessage(null, ByteString.of(createMinimalQuotePacket()));

    verify(mockFeedListener).onTickerArrival(Mockito.any(LiveTicker.class));
    verify(mockFeedListener).onQuoteArrival(Mockito.any(LiveQuote.class));
  }

  // Helper methods to create minimal valid packets

  private byte[] createMinimalTickerPacket() {
    // LiveTicker expects: code(0), length(1-2), exchange(3), securityID(4-7), ltp
    // float(8-11), ltt int(12-15)
    ByteBuffer buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
    buffer.put((byte) FeedResponseCode.TICKER_PACKET.getCode()); // 0
    buffer.putShort((short) 16); // message length at 1-2
    buffer.put((byte) 0x00); // exchangeSegment (NSE_EQ) at 3
    buffer.putInt(1234); // securityID at 4-7
    buffer.putFloat(123.45f); // ltp at 8-11
    buffer.putInt(123456789); // ltt at 12-15
    return buffer.array();
  }

  private byte[] createMinimalPrevClosePacket() {
    // LivePrevClose structure:
    // Offset 0: code (1 byte)
    // Offset 1-2: length (2 bytes)
    // Offset 3: exchangeSegment (1 byte)
    // Offset 4-7: securityID (4 bytes)
    // Offset 8-11: prevClose (4 bytes)
    // Offset 12-15: prevOI (4 bytes)
    ByteBuffer buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
    buffer.put((byte) FeedResponseCode.PREV_CLOSE_PACKET.getCode()); // offset 0
    buffer.putShort((short) 16); // total message length at offset 1-2
    buffer.put((byte) 0x00); // exchangeSegment at offset 3
    buffer.putInt(1234); // securityID at offset 4-7
    buffer.putFloat(100.0f); // prev_close at offset 8-11
    buffer.putInt(5000); // prev_OI at offset 12-15
    return buffer.array();
  }

  private byte[] createMinimalQuotePacket() {
    // LiveQuote sequential: code, length, exchange, securityID(int),
    // lastPrice(float), lastQuantity(short),
    // lastTradeTime(int), averagePrice(float), volume(int), sellQuantity(int),
    // buyQuantity(int),
    // open(float), close(float), high(float), low(float)
    ByteBuffer buffer = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
    buffer.put((byte) FeedResponseCode.QUOTE_PACKET.getCode());
    buffer.putShort((short) 50); // message length
    buffer.put((byte) 0x00); // exchangeSegment
    buffer.putInt(1234); // securityID
    buffer.putFloat(105.0f); // lastPrice
    buffer.putShort((short) 100); // lastQuantity
    buffer.putInt(123456789); // lastTradeTime (epoch seconds)
    buffer.putFloat(104.5f); // averagePrice
    buffer.putInt(1000); // volume
    buffer.putInt(50); // sellQuantity
    buffer.putInt(60); // buyQuantity
    buffer.putFloat(103.0f); // open
    buffer.putFloat(107.0f); // high
    buffer.putFloat(102.5f); // low
    buffer.putFloat(106.0f); // close
    return buffer.array();
  }

  private byte[] createMinimalOIPacket() {
    // LiveOI sequential: code, length, exchange, securityID(int), oi(int)
    ByteBuffer buffer = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);
    buffer.put((byte) FeedResponseCode.OI_PACKET.getCode());
    buffer.putShort((short) 12);
    buffer.put((byte) 0x00); // exchangeSegment
    buffer.putInt(1234); // securityID
    buffer.putInt(5000); // oi
    return buffer.array();
  }

  private byte[] createMinimalFullPacket() {
    // LiveQuoteMax sequential: code, length, exchange, securityID,
    // lastPrice(float), lastQuantity(short),
    // lastTradeTime(int), averagePrice(float), volume(int), sellQuantity(int),
    // buyQuantity(int),
    // oi(int), oi_highest(int), oi_lowest(int), open(float), close(float),
    // high(float), low(float)
    ByteBuffer buffer = ByteBuffer.allocate(62).order(ByteOrder.LITTLE_ENDIAN);
    buffer.put((byte) FeedResponseCode.FULL_PACKET.getCode());
    buffer.putShort((short) 62);
    buffer.put((byte) 0x00); // exchangeSegment
    buffer.putInt(1234); // securityID
    buffer.putFloat(105.0f); // lastPrice
    buffer.putShort((short) 100); // lastQuantity
    buffer.putInt(123456789); // lastTradeTime
    buffer.putFloat(104.5f); // averagePrice
    buffer.putInt(1000); // volume
    buffer.putInt(50); // sellQuantity
    buffer.putInt(60); // buyQuantity
    buffer.putInt(5000); // oi
    buffer.putInt(5000); // oi_highest
    buffer.putInt(5000); // oi_lowest
    buffer.putFloat(103.0f); // open
    buffer.putFloat(107.0f); // high
    buffer.putFloat(102.5f); // low
    buffer.putFloat(106.0f); // close
    return buffer.array();
  }

  private byte[] createMinimalFeedDisconnectPacket() {
    // DataAPIError.makeException reads int at offset 4. We need: code(0),
    // length(1-2), exchange(3), errorCode int(4-7)
    ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
    buffer.put((byte) FeedResponseCode.FEED_DISCONNECT.getCode());
    buffer.putShort((short) 8); // total length
    buffer.put((byte) 0x00); // exchangeSegment (any)
    buffer.putInt(805); // error code (TOO_MANY_CONNECTIONS)
    return buffer.array();
  }
}
