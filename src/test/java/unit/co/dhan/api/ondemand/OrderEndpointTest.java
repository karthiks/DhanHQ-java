package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.api.ondemand.OrderEndpoint.APIEndpoint;
import co.dhan.api.ondemand.OrderEndpoint.APIParam;
import co.dhan.constant.*;
import co.dhan.dto.Order;
import co.dhan.dto.OrderStatusDTO;
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
        OrderStatusDTO expectedOrderStatus = new OrderStatusDTO(orderId, OrderStatus.PENDING);

        Order order = getSampleOrder();
        Set<String> expectedParamKeys = Set.of(APIParam.SecurityID, APIParam.TransactionType, APIParam.ExchangeSegment,
                APIParam.ProductType, APIParam.OrderType, APIParam.Validity, APIParam.Quantity, APIParam.DisclosedQuantity,
                APIParam.Price, APIParam.AfterMarketOrder, APIParam.AMOTime, APIParam.BOProfitValue, APIParam.BOStopLossValue,
                APIParam.CorrelationID);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderStatusDTO.class)).thenReturn(expectedOrderStatus);

        assertThat(orderEndpoint.placeOrder(order, order.getCorrelationId(), false)).isEqualTo(expectedOrderStatus);
        verify(mockDhanHTTP).doHttpPostRequest(eq(APIEndpoint.PlaceOrder), argThat(payoad -> {
            assertThat(payoad.keySet()).isEqualTo(expectedParamKeys);
            return true;
        }));
    }

    @Test
    void placeOrder_WithoutTag_ShouldExcludeCorrelationIdParam() {
        String orderId = "1";
        OrderStatusDTO expectedOrderStatus = new OrderStatusDTO(orderId, OrderStatus.PENDING);
        Order order = getSampleOrder();
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderStatusDTO.class)).thenReturn(expectedOrderStatus);

        orderEndpoint.placeOrder(order);
        verify(mockDhanHTTP).doHttpPostRequest(eq(APIEndpoint.PlaceOrder), argThat(payoad -> {
            assertThat(payoad.keySet()).doesNotContain(APIParam.CorrelationID);
            return true;
        }));
    }

    @Test
    void placeOrder_WithoutTagAndSlice_ShouldPassDefaultValuesToOverloadedMethod() {
        String orderId = "1";
        OrderStatusDTO expectedOrderStatus = new OrderStatusDTO(orderId, OrderStatus.PENDING);
        Order order = getSampleOrder();
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderStatusDTO.class)).thenReturn(expectedOrderStatus);

        orderEndpoint.placeOrder(order);
        verify(orderEndpoint, times(1)).placeOrder(order,null,false);
    }

    @Test
    void placeSliceOrder_calls_placeOrder() {
        String orderId = "1";
        OrderStatusDTO expectedOrderStatus = new OrderStatusDTO(orderId, OrderStatus.PENDING);
        Order order = getSampleOrder();
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderStatusDTO.class)).thenReturn(expectedOrderStatus);

        orderEndpoint.placeSliceOrder(order);
        verify(orderEndpoint, times(1)).placeOrder(order,null,true);
    }

    @Test
    void modifyOrder_ReturnsStatus() {
        String orderId = "1";
        OrderStatusDTO expectedOrderStatus = new OrderStatusDTO(orderId, OrderStatus.PENDING);

        Order order = getSampleOrder();
        order.setOrderId(orderId);
        order.setLegName(LegName.ENTRY_LEG);
        Set<String> expectedParamKeys = Set.of(APIParam.OrderID, APIParam.OrderType, APIParam.LegName,
                APIParam.Validity, APIParam.Quantity, APIParam.DisclosedQuantity,
                APIParam.Price, APIParam.TriggerPrice);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPutRequest(anyString(), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderStatusDTO.class)).thenReturn(expectedOrderStatus);

        assertThat(orderEndpoint.modifyOrder(order)).isEqualTo(expectedOrderStatus);
        verify(mockDhanHTTP).doHttpPutRequest(eq("/orders/"+orderId), argThat(payoad -> {
            assertThat(payoad.keySet()).isEqualTo(expectedParamKeys);
            return true;
        }));
    }

    @Test
    void cancelOrder_ReturnStatus() {
        String orderID = "123";
        OrderStatusDTO expectedOrderStatus = new OrderStatusDTO(orderID, OrderStatus.CANCELLED);
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpDeleteRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderStatusDTO.class)).thenReturn(expectedOrderStatus);

        OrderStatusDTO orderStatusDTO = orderEndpoint.cancelOrder(orderID);
        assertThat(orderStatusDTO).isEqualTo(expectedOrderStatus);
        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpDeleteRequest(eq("/orders/" +orderID));
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
        order.setAmoTime(AMOTime.OPEN);
        order.setBoProfitValue(BigDecimal.valueOf(111.0));
        order.setBoStopLossValue(BigDecimal.valueOf(115.0));
        order.setCorrelationId("c1");
        return order;
    }
}