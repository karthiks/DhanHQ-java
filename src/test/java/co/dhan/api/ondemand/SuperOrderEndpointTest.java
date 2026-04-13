package co.dhan.api.ondemand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.api.ondemand.SuperOrderEndpoint.APIEndpoint;
import co.dhan.api.ondemand.SuperOrderEndpoint.APIParam;
import co.dhan.constant.*;
import co.dhan.constant.SuperOrderStatus;
import co.dhan.dto.SuperOrder;
import co.dhan.dto.SuperOrderModificationRequest;
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

  @Test
  void modifySuperOrder_ShouldReturnResult() throws DhanAPIException {
    SuperOrderModificationRequest request =
        SuperOrderModificationRequest.builder()
            .orderId("super123")
            .orderType(OrderType.LIMIT)
            .legName(LegName.ENTRY_LEG)
            .validity(Validity.DAY)
            .quantity(100)
            .price(BigDecimal.valueOf(99.99))
            .triggerPrice(BigDecimal.valueOf(99))
            .disclosedQuantity(10)
            .build();

    SuperOrderResponse expectedResponse =
        new SuperOrderResponse("super123", SuperOrderStatus.PENDING);

    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpPutRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
    when(mockDhanResponse.convertToType(SuperOrderResponse.class)).thenReturn(expectedResponse);

    SuperOrderResponse actualResponse = superOrderEndpoint.modifySuperOrder(request);
    assertThat(actualResponse).isEqualTo(expectedResponse);

    verify(mockDhanConnection).getDhanHTTP();
    verify(mockDhanHTTP)
        .doHttpPutRequest(
            eq(String.format(APIEndpoint.ModifySuperOrder, request.getOrderId())),
            argThat(
                payload -> {
                  // Verify that payload contains expected values
                  return payload != null
                      && payload.get(APIParam.OrderID).equals(request.getOrderId())
                      && payload.get(APIParam.OrderType).equals(request.getOrderType().toString())
                      && payload.get(APIParam.LegName).equals(request.getLegName().toString())
                      && payload.get(APIParam.Validity).equals(request.getValidity().toString())
                      && payload
                          .get(APIParam.Quantity)
                          .equals(String.valueOf(request.getQuantity()))
                      && payload
                          .get(APIParam.DisclosedQuantity)
                          .equals(String.valueOf(request.getDisclosedQuantity()))
                      && payload.get(APIParam.Price).equals(String.valueOf(request.getPrice()))
                      && payload
                          .get(APIParam.TriggerPrice)
                          .equals(String.valueOf(request.getTriggerPrice()));
                }));
  }

  @Test
  void modifySuperOrder_ShouldThrowException_WhenRequestIsNull() throws DhanAPIException {
    assertThatThrownBy(() -> superOrderEndpoint.modifySuperOrder(null))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining(
            "One of the values are invalid -> OrderID, OrderType, OrderLegName, OrderValidity, OrderQuantity.");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void modifySuperOrder_ShouldThrowException_WhenOrderIdIsNull() throws DhanAPIException {
    SuperOrderModificationRequest request =
        SuperOrderModificationRequest.builder()
            .orderType(OrderType.LIMIT)
            .legName(LegName.ENTRY_LEG)
            .validity(Validity.DAY)
            .quantity(100)
            .price(BigDecimal.valueOf(99.99))
            .triggerPrice(BigDecimal.valueOf(99))
            .disclosedQuantity(10)
            .build();

    assertThatThrownBy(() -> superOrderEndpoint.modifySuperOrder(request))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining(
            "One of the values are invalid -> OrderID, OrderType, OrderLegName, OrderValidity, OrderQuantity.");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void modifySuperOrder_ShouldThrowException_WhenOrderIdIsBlank() throws DhanAPIException {
    SuperOrderModificationRequest request =
        SuperOrderModificationRequest.builder()
            .orderType(OrderType.LIMIT)
            .legName(LegName.ENTRY_LEG)
            .validity(Validity.DAY)
            .quantity(100)
            .price(BigDecimal.valueOf(99.99))
            .triggerPrice(BigDecimal.valueOf(99))
            .disclosedQuantity(10)
            .build();

    assertThatThrownBy(() -> superOrderEndpoint.modifySuperOrder(request))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining(
            "One of the values are invalid -> OrderID, OrderType, OrderLegName, OrderValidity, OrderQuantity.");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void modifySuperOrder_ShouldThrowException_WhenOrderTypeIsNull() throws DhanAPIException {
    SuperOrderModificationRequest request =
        SuperOrderModificationRequest.builder()
            .orderId("super123")
            .legName(LegName.ENTRY_LEG)
            .validity(Validity.DAY)
            .quantity(100)
            .price(BigDecimal.valueOf(99.99))
            .triggerPrice(BigDecimal.valueOf(99))
            .disclosedQuantity(10)
            .build();

    assertThatThrownBy(() -> superOrderEndpoint.modifySuperOrder(request))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining(
            "One of the values are invalid -> OrderID, OrderType, OrderLegName, OrderValidity, OrderQuantity.");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void modifySuperOrder_ShouldThrowException_WhenLegNameIsNull() throws DhanAPIException {
    SuperOrderModificationRequest request =
        SuperOrderModificationRequest.builder()
            .orderId("super123")
            .orderType(OrderType.LIMIT)
            .validity(Validity.DAY)
            .quantity(100)
            .price(BigDecimal.valueOf(99.99))
            .triggerPrice(BigDecimal.valueOf(99))
            .disclosedQuantity(10)
            .build();

    assertThatThrownBy(() -> superOrderEndpoint.modifySuperOrder(request))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining(
            "One of the values are invalid -> OrderID, OrderType, OrderLegName, OrderValidity, OrderQuantity.");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void modifySuperOrder_ShouldThrowException_WhenValidityIsNull() throws DhanAPIException {
    SuperOrderModificationRequest request =
        SuperOrderModificationRequest.builder()
            .orderId("super123")
            .orderType(OrderType.LIMIT)
            .legName(LegName.ENTRY_LEG)
            .quantity(100)
            .price(BigDecimal.valueOf(99.99))
            .triggerPrice(BigDecimal.valueOf(99))
            .disclosedQuantity(10)
            .build();

    assertThatThrownBy(() -> superOrderEndpoint.modifySuperOrder(request))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining(
            "One of the values are invalid -> OrderID, OrderType, OrderLegName, OrderValidity, OrderQuantity.");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void modifySuperOrder_ShouldThrowException_WhenQuantityIsZero() throws DhanAPIException {
    SuperOrderModificationRequest request =
        SuperOrderModificationRequest.builder()
            .orderId("super123")
            .orderType(OrderType.LIMIT)
            .legName(LegName.ENTRY_LEG)
            .validity(Validity.DAY)
            .quantity(0)
            .price(BigDecimal.valueOf(99.99))
            .triggerPrice(BigDecimal.valueOf(99))
            .disclosedQuantity(10)
            .build();

    assertThatThrownBy(() -> superOrderEndpoint.modifySuperOrder(request))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining(
            "One of the values are invalid -> OrderID, OrderType, OrderLegName, OrderValidity, OrderQuantity.");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void cancelSuperOrderLeg_ShouldReturnResult() throws DhanAPIException {
    SuperOrderResponse expectedResponse =
        new SuperOrderResponse("super123", SuperOrderStatus.PENDING);

    when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
    when(mockDhanHTTP.doHttpDeleteRequest(anyString())).thenReturn(mockDhanResponse);
    when(mockDhanResponse.convertToType(SuperOrderResponse.class)).thenReturn(expectedResponse);

    SuperOrderResponse actualResponse = superOrderEndpoint.cancelSuperOrderLeg("super123", "leg1");
    assertThat(actualResponse).isEqualTo(expectedResponse);

    verify(mockDhanConnection).getDhanHTTP();
    verify(mockDhanHTTP).doHttpDeleteRequest(eq("/super/orders/super123/leg1"));
  }

  @Test
  void cancelSuperOrderLeg_ShouldThrowException_WhenOrderIdIsNull() throws DhanAPIException {
    assertThatThrownBy(() -> superOrderEndpoint.cancelSuperOrderLeg(null, "leg1"))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining("Order ID must not be null or blank");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void cancelSuperOrderLeg_ShouldThrowException_WhenOrderIdIsBlank() throws DhanAPIException {
    assertThatThrownBy(() -> superOrderEndpoint.cancelSuperOrderLeg("", "leg1"))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining("Order ID must not be null or blank");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void cancelSuperOrderLeg_ShouldThrowException_WhenOrderLegIsNull() throws DhanAPIException {
    assertThatThrownBy(() -> superOrderEndpoint.cancelSuperOrderLeg("super123", null))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining("Order leg must not be null or blank");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }

  @Test
  void cancelSuperOrderLeg_ShouldThrowException_WhenOrderLegIsBlank() throws DhanAPIException {
    assertThatThrownBy(() -> superOrderEndpoint.cancelSuperOrderLeg("super123", ""))
        .isInstanceOf(DhanAPIException.class)
        .hasMessageContaining("Order leg must not be null or blank");

    verify(mockDhanConnection, never()).getDhanHTTP();
  }
}
