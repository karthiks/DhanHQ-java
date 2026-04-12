package co.dhan.api.ondemand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.api.ondemand.SuperOrderEndpoint.APIEndpoint;
import co.dhan.constant.*;
import co.dhan.constant.SuperOrderStatus;
import co.dhan.dto.SuperOrder;
import co.dhan.dto.SuperOrderRequest;
import co.dhan.dto.SuperOrderResponse;
import co.dhan.http.DhanAPIException;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

class SuperOrderEndpointTest extends UnitTestRoot {

  @Mock private DhanConnection mockDhanConnection;

  @Mock private DhanHTTP mockDhanHTTP;

  @Mock private DhanResponse mockDhanResponse;

  @Spy @InjectMocks private SuperOrderEndpoint superOrderEndpoint;

  @Test
  void getAllSuperOrders_ShouldReturnResult() throws IOException {
    SuperOrder.OrderLeg leg = new SuperOrder.OrderLeg();
    leg.setOrderId("leg1");
    leg.setLegName(LegName.ENTRY_LEG);
    leg.setTransactionType(TransactionType.BUY);
    leg.setOrderStatus(OrderStatus.PENDING);
    leg.setSecurityId("123");
    leg.setExchangeSegment(ExchangeSegment.NSE_EQ);
    leg.setProductType(ProductType.CNC);
    leg.setOrderType(OrderType.LIMIT);
    leg.setQuantity(100);
    leg.setPrice(BigDecimal.valueOf(99.99));

    SuperOrder expectedOrder = new SuperOrder();
    expectedOrder.setSuperOrderId("super1");
    expectedOrder.setOrderStatus(SuperOrderStatus.PENDING);
    expectedOrder.setOrderLegs(List.of(leg));
    expectedOrder.setCreateTime("2026-04-12T10:00:00Z");
    expectedOrder.setUpdateTime("2026-04-12T10:00:00Z");

    List<SuperOrder> expectedOrders = List.of(expectedOrder);

    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpGetRequest(anyString())).thenReturn(mockDhanResponse);
    when(mockDhanResponse.convertToType((TypeReference<List<SuperOrder>>) any()))
        .thenReturn(expectedOrders);

    assertThat(superOrderEndpoint.getAllSuperOrders()).isEqualTo(expectedOrders);
    verify(mockDhanHTTP).doHttpGetRequest(eq("/super/orders"));
  }

  @Test
  void getAllSuperOrders_ShouldThrowException_WhenAPIError() throws IOException {
    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpGetRequest(anyString()))
        .thenThrow(new DhanAPIException("DH-00", "API Error"));

    assertThatThrownBy(() -> superOrderEndpoint.getAllSuperOrders())
        .isInstanceOf(DhanAPIException.class)
        .hasMessage("API Error");

    verify(mockDhanConnection).getDhanHTTP();
    verify(mockDhanHTTP).doHttpGetRequest(eq("/super/orders"));
  }

  @NotNull
  private static SuperOrder.OrderLeg getSampleLeg() {
    SuperOrder.OrderLeg leg = new SuperOrder.OrderLeg();
    leg.setOrderId("leg1");
    leg.setLegName(LegName.ENTRY_LEG);
    leg.setTransactionType(TransactionType.BUY);
    leg.setOrderStatus(OrderStatus.PENDING);
    leg.setSecurityId("123");
    leg.setTradingSymbol("TEST");
    leg.setExchangeSegment(ExchangeSegment.NSE_EQ);
    leg.setProductType(ProductType.CNC);
    leg.setOrderType(OrderType.LIMIT);
    leg.setValidity(Validity.DAY);
    leg.setQuantity(100);
    leg.setDisclosedQuantity(10);
    leg.setPrice(BigDecimal.valueOf(99.99));
    leg.setTriggerPrice(BigDecimal.valueOf(99));
    leg.setFilledQty(0);
    leg.setRemainingQuantity(100);
    leg.setAverageTradedPrice(BigDecimal.ZERO);
    leg.setAfterMarketOrder(false);
    return leg;
  }

  @Test
  void placeSuperOrder_ShouldReturnResult() throws DhanAPIException {
    SuperOrderRequest.SuperOrderLegRequest leg1 =
        SuperOrderRequest.SuperOrderLegRequest.builder()
            .securityId("123")
            .exchangeSegment(ExchangeSegment.NSE_EQ)
            .transactionType(TransactionType.BUY)
            .orderType(OrderType.LIMIT)
            .quantity(100)
            .price(BigDecimal.valueOf(99.99))
            .triggerPrice(BigDecimal.valueOf(99))
            .legName(LegName.ENTRY_LEG)
            .disclosedQuantity(10)
            .afterMarketOrder(false)
            .amoTime(AMOTime.OPEN)
            .build();

    SuperOrderRequest.SuperOrderLegRequest leg2 =
        SuperOrderRequest.SuperOrderLegRequest.builder()
            .securityId("456")
            .exchangeSegment(ExchangeSegment.NSE_EQ)
            .transactionType(TransactionType.BUY)
            .orderType(OrderType.LIMIT)
            .quantity(50)
            .price(BigDecimal.valueOf(199.99))
            .triggerPrice(BigDecimal.valueOf(199))
            .legName(LegName.STOP_LOSS_LEG)
            .disclosedQuantity(5)
            .afterMarketOrder(false)
            .amoTime(AMOTime.OPEN)
            .build();

    SuperOrderRequest.TriggerCondition condition =
        SuperOrderRequest.TriggerCondition.builder()
            .type(TriggerType.PRICE_TRIGGER)
            .fromLeg("leg1")
            .toLeg("leg2")
            .triggerValue(BigDecimal.valueOf(105))
            .operator(TriggerOperator.GREATER_THAN)
            .isMet(false)
            .build();

    SuperOrderRequest request =
        SuperOrderRequest.builder()
            .legs(List.of(leg1, leg2))
            .conditions(List.of(condition))
            .orderType(OrderType.LIMIT)
            .validity(Validity.DAY)
            .productType(ProductType.CNC)
            .correlationId("super-order-123")
            .build();

    SuperOrderResponse expectedResponse =
        new SuperOrderResponse("super123", SuperOrderStatus.PENDING);

    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpPostRequest(eq(APIEndpoint.CreateSuperOrder), anyMap()))
        .thenReturn(mockDhanResponse);
    when(mockDhanResponse.convertToType(SuperOrderResponse.class)).thenReturn(expectedResponse);

    SuperOrderResponse actualResponse = superOrderEndpoint.placeSuperOrder(request);
    assertThat(actualResponse).isEqualTo(expectedResponse);

    verify(mockDhanConnection).getDhanHTTP();
    verify(mockDhanHTTP)
        .doHttpPostRequest(
            eq(APIEndpoint.CreateSuperOrder),
            argThat(
                payload -> {
                  // Verify that payload is not null and not empty
                  return payload != null && !payload.isEmpty();
                }));
  }

  @Test
  void placeSuperOrder_ShouldThrowException_WhenRequestIsNull() throws DhanAPIException {
    assertThatThrownBy(() -> superOrderEndpoint.placeSuperOrder(null))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining("SuperOrderRequest cannot be null");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void placeSuperOrder_ShouldThrowException_WhenNoLegs() throws DhanAPIException {
    SuperOrderRequest request =
        SuperOrderRequest.builder()
            .orderType(OrderType.LIMIT)
            .validity(Validity.DAY)
            .productType(ProductType.CNC)
            .build();

    assertThatThrownBy(() -> superOrderEndpoint.placeSuperOrder(request))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining("Super order must have at least one leg");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }
}
