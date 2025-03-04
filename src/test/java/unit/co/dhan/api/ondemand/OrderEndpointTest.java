package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.api.ondemand.OrderEndpoint.APIEndpoint;
import co.dhan.api.ondemand.OrderEndpoint.APIParam;
import co.dhan.constant.*;
import co.dhan.dto.ModifyOrderRequest;
import co.dhan.dto.NewOrderRequest;
import co.dhan.dto.Order;
import co.dhan.dto.OrderResponse;
import co.dhan.helper.BigDecimalUtils;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderEndpointTest extends UnitTestRoot {

    @Mock
    private DhanConnection mockDhanConnection;

    @Mock
    private DhanHTTP mockDhanHTTP;

    @Mock
    private DhanResponse mockDhanResponse;

    @Spy
    @InjectMocks
    private OrderEndpoint orderEndpoint;

    @Test
    void getOrderByID_ShouldReturnResult() throws IOException {
        Order expectedOrder = new Order();
        String orderId = "1";
        expectedOrder.setOrderId(orderId);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpGetRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(Order.class)).thenReturn(expectedOrder);

        assertThat(orderEndpoint.getOrderByID(orderId)).isEqualTo(expectedOrder);
        verify(mockDhanHTTP).doHttpGetRequest(eq("/orders/" + orderId));
        ;
    }

    @Test
    void getOrderByCorrelationID_ShouldReturnResult() throws IOException {
        Order expectedOrder = new Order();
        String orderId = "1";
        String correlationId = "c1";
        expectedOrder.setOrderId(orderId);
        expectedOrder.setCorrelationId(correlationId);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpGetRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(Order.class)).thenReturn(expectedOrder);

        assertThat(orderEndpoint.getOrderByCorrelationID(correlationId)).isEqualTo(expectedOrder);
        verify(mockDhanHTTP).doHttpGetRequest(eq("/orders/external/" + correlationId));
    }

    @Test
    void getCurrentOrders_ShouldReturnResult() throws IOException {
        Order expectedOrder = new Order();
        String orderId = "1";
        expectedOrder.setOrderId(orderId);
        List<Order> expectedOrders = List.of(expectedOrder);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpGetRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType((TypeReference<List<Order>>) any())).thenReturn(expectedOrders);

        assertThat(orderEndpoint.getCurrentOrders()).isEqualTo(expectedOrders);
        verify(mockDhanHTTP).doHttpGetRequest(eq("/orders"));
    }

    @Test
    void placeOrder_WithTagAndSliceValues_ShouldReturnResult() {
        String orderId = "1";
        OrderResponse expectedOrderStatus = new OrderResponse(orderId, OrderStatus.PENDING);

        Set<String> expectedParamKeys = Set.of(APIParam.SecurityID, APIParam.TransactionType, APIParam.ExchangeSegment,
                APIParam.ProductType, APIParam.OrderType, APIParam.Validity, APIParam.Quantity, APIParam.DisclosedQuantity,
                APIParam.Price, APIParam.AfterMarketOrder, APIParam.AMOTime, APIParam.BOProfitValue, APIParam.BOStopLossValue,
                APIParam.CorrelationID);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderResponse.class)).thenReturn(expectedOrderStatus);

        NewOrderRequest nordRequest = NewOrderRequest.builder()
                .securityId("1").exchangeSegment(ExchangeSegment.NSE_EQ).transactionType(TransactionType.BUY)
                .orderType(OrderType.LIMIT).productType(ProductType.CNC).validity(Validity.DAY)
                .price(BigDecimalUtils.toBigDecimal(99.99)).triggerPrice(BigDecimalUtils.toBigDecimal(99))
                .quantity(100).disclosedQuantity(10)
                .afterMarketOrder(true).amoTime(AMOTime.OPEN)
//                .boProfitValue(BigDecimalUtils.toBigDecimal(0)).boStopLossValue(BigDecimalUtils.toBigDecimal(0))
                .correlationId("tag1")
                .build();
        assertThat(orderEndpoint.placeOrder(nordRequest, "tag1", false)).isEqualTo(expectedOrderStatus);
        verify(mockDhanHTTP).doHttpPostRequest(eq(APIEndpoint.PlaceOrder), argThat(payoad -> {
            assertThat(payoad.keySet()).isEqualTo(expectedParamKeys);
            return true;
        }));
    }

    @Test
    void placeOrder_WithoutTag_ShouldExcludeCorrelationIdParam() {
        String orderId = "1";
        OrderResponse expectedOrderStatus = new OrderResponse(orderId, OrderStatus.PENDING);
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderResponse.class)).thenReturn(expectedOrderStatus);

        NewOrderRequest nordRequest = NewOrderRequest.builder()
                .securityId("1").exchangeSegment(ExchangeSegment.NSE_EQ).transactionType(TransactionType.BUY)
                .orderType(OrderType.LIMIT).productType(ProductType.CNC).validity(Validity.DAY)
                .price(BigDecimalUtils.toBigDecimal(99.99)).triggerPrice(BigDecimalUtils.toBigDecimal(99))
                .quantity(100).disclosedQuantity(10)
                .afterMarketOrder(true).amoTime(AMOTime.OPEN)
//                .boProfitValue(BigDecimalUtils.toBigDecimal(0)).boStopLossValue(BigDecimalUtils.toBigDecimal(0))
                .correlationId("tag1")
                .build();
        orderEndpoint.placeOrder(nordRequest);
        verify(mockDhanHTTP).doHttpPostRequest(eq(APIEndpoint.PlaceOrder), argThat(payoad -> {
            assertThat(payoad.keySet()).doesNotContain(APIParam.CorrelationID);
            return true;
        }));
    }

    @Test
    void placeOrder_WithoutTagAndSlice_ShouldPassDefaultValuesToOverloadedMethod() {
        String orderId = "1";
        OrderResponse expectedOrderStatus = new OrderResponse(orderId, OrderStatus.PENDING);
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderResponse.class)).thenReturn(expectedOrderStatus);

        NewOrderRequest nordRequest = NewOrderRequest.builder()
                .securityId("1").exchangeSegment(ExchangeSegment.NSE_EQ).transactionType(TransactionType.BUY)
                .orderType(OrderType.LIMIT).productType(ProductType.CNC).validity(Validity.DAY)
                .price(BigDecimalUtils.toBigDecimal(99.99)).triggerPrice(BigDecimalUtils.toBigDecimal(99))
                .quantity(100).disclosedQuantity(10)
                .afterMarketOrder(true).amoTime(AMOTime.OPEN)
//                .boProfitValue(BigDecimalUtils.toBigDecimal(0)).boStopLossValue(BigDecimalUtils.toBigDecimal(0))
                .correlationId("tag1")
                .build();
        orderEndpoint.placeOrder(nordRequest);
        verify(orderEndpoint, times(1)).placeOrder(nordRequest, null, false);
    }

    @Test
    void placeSliceOrder_calls_placeOrder() {
        String orderId = "1";
        OrderResponse expectedOrderStatus = new OrderResponse(orderId, OrderStatus.PENDING);
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderResponse.class)).thenReturn(expectedOrderStatus);

        NewOrderRequest nordRequest = NewOrderRequest.builder()
                .securityId("1").exchangeSegment(ExchangeSegment.NSE_EQ).transactionType(TransactionType.BUY)
                .orderType(OrderType.LIMIT).productType(ProductType.CNC).validity(Validity.DAY)
                .price(BigDecimalUtils.toBigDecimal(99.99)).triggerPrice(BigDecimalUtils.toBigDecimal(99))
                .quantity(100).disclosedQuantity(10)
                .afterMarketOrder(true).amoTime(AMOTime.OPEN)
//                .boProfitValue(BigDecimalUtils.toBigDecimal(0)).boStopLossValue(BigDecimalUtils.toBigDecimal(0))
                .correlationId("tag1")
                .build();
        orderEndpoint.placeSliceOrder(nordRequest);
        verify(orderEndpoint, times(1)).placeOrder(nordRequest, null, true);
    }

    @Test
    void modifyOrder_ReturnsStatus() {
        String orderId = "1";
        OrderResponse expectedOrderStatus = new OrderResponse(orderId, OrderStatus.PENDING);

        Set<String> expectedParamKeys = Set.of(APIParam.OrderID, APIParam.OrderType, APIParam.LegName,
                APIParam.Validity, APIParam.Quantity, APIParam.DisclosedQuantity,
                APIParam.Price, APIParam.TriggerPrice);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPutRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderResponse.class)).thenReturn(expectedOrderStatus);

        ModifyOrderRequest mor = ModifyOrderRequest.builder()
                .orderId("1").orderType(OrderType.LIMIT)
                .legName(LegName.ENTRY_LEG).validity(Validity.DAY)
                .price(BigDecimalUtils.toBigDecimal(99.99)).triggerPrice(BigDecimalUtils.toBigDecimal(99))
                .quantity(100).disclosedQuantity(10)
                .build();
        assertThat(orderEndpoint.modifyOrder(mor)).isEqualTo(expectedOrderStatus);
        verify(mockDhanHTTP).doHttpPutRequest(eq("/orders/" + orderId), argThat(payoad -> {
            assertThat(payoad.keySet()).isEqualTo(expectedParamKeys);
            return true;
        }));
    }

    @Test
    void cancelOrder_ReturnStatus() {
        String orderID = "123";
        OrderResponse expectedOrderStatus = new OrderResponse(orderID, OrderStatus.CANCELLED);
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpDeleteRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderResponse.class)).thenReturn(expectedOrderStatus);

        OrderResponse orderResponse = orderEndpoint.cancelOrder(orderID);
        assertThat(orderResponse).isEqualTo(expectedOrderStatus);
        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpDeleteRequest(eq("/orders/" + orderID));
    }

    @NotNull
    private static Order getSampleOrder() {
        Order order = new Order();
        order.setSecurityId("11");
        order.setTransactionType(TransactionType.BUY);
        order.setExchangeSegment(ExchangeSegment.NSE_EQ);
        order.setProductType(ProductType.CNC);
        order.setOrderType(OrderType.MARKET);
        order.setValidity(Validity.DAY);
        order.setQuantity(100);
        order.setDisclosedQuantity(10);
        order.setPrice(BigDecimal.valueOf(99.99));
        order.setAfterMarketOrder(false);
        order.setBoProfitValue(BigDecimal.valueOf(111.0));
        order.setBoStopLossValue(BigDecimal.valueOf(115.0));
        order.setCorrelationId("c1");
        return order;
    }
}