package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.api.ondemand.ForeverOrderEndpoint.APIEndpoint;
import co.dhan.api.ondemand.ForeverOrderEndpoint.APIParam;
import co.dhan.constant.*;
import co.dhan.dto.Order;
import co.dhan.dto.OrderResponse;
import co.dhan.http.DhanAPIException;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ForeverOrderEndpointTest extends UnitTestRoot {

    @Mock
    private DhanConnection mockDhanConnection;

    @Mock
    private DhanHTTP mockDhanHTTP;

    @Mock
    private DhanResponse mockDhanResponse;

    @InjectMocks
    private ForeverOrderEndpoint foreverOrderEndpoint;

    @Test
    void placeForeverOrder_ShouldReturnResult() throws DhanAPIException {
        Order order = getSampleOrder();
        Map<String, String> expectedParams = getExpectedParams();
        List<String> expectedKeys =
                List.of(APIParam.OrderFlag, APIParam.TransactionType, APIParam.ExchangeSegment,
                        APIParam.ProductType, APIParam.OrderType, APIParam.Validity, APIParam.SecurityID,
                        APIParam.Quantity, APIParam.DisclosedQuantity, APIParam.Price, APIParam.TriggerPrice,
                        APIParam.Price1, APIParam.TriggerPrice1, APIParam.Quantity1, APIParam.CorrelationID);
        expectedParams.keySet().retainAll(expectedKeys);
        OrderResponse expectedOrderStatus = new OrderResponse(order.getOrderId(), OrderStatus.PENDING);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(eq(APIEndpoint.CreateForeverOrder), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderResponse.class)).thenReturn(expectedOrderStatus);

        OrderResponse orderStatus = foreverOrderEndpoint.placeForeverOrder(order, expectedParams.get(APIParam.CorrelationID));
        assertThat(orderStatus).isEqualTo(expectedOrderStatus);

        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpPostRequest(eq(APIEndpoint.CreateForeverOrder), argThat(payload -> {
            assertThat(payload).isEqualTo(expectedParams);
            return true;
        }));
    }

    @Test
    void placeForeverOrder_ShouldThrowsDhanAPIException() throws DhanAPIException {
        Order order = getSampleOrder();

        when(mockDhanConnection.getDhanHTTP())
                .thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(eq(APIEndpoint.CreateForeverOrder), anyMap()))
                .thenThrow(new DhanAPIException("DH-00", "API Error"));

        assertThatThrownBy(() -> foreverOrderEndpoint.placeForeverOrder(order, "test-tag"))
                .isInstanceOf(DhanAPIException.class)
                .hasMessage("API Error");

        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpPostRequest(eq(APIEndpoint.CreateForeverOrder), anyMap());
    }

    @Test
    void modifyForeverOrder_ShouldReturnResult() throws DhanAPIException {
        Order order = getSampleOrder();
        order.setOrderId("123");

        Map<String, String> expectedParams = getExpectedParams();
        List<String> expectedKeys = List.of(APIParam.OrderID, APIParam.OrderFlag, APIParam.OrderType, APIParam.LegName,
                APIParam.Quantity, APIParam.DisclosedQuantity, APIParam.Price, APIParam.TriggerPrice, APIParam.Validity);
        expectedParams.keySet().retainAll(expectedKeys);

        OrderResponse expectedOrderStatus = new OrderResponse(order.getOrderId(), OrderStatus.PENDING);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPutRequest(contains(order.getOrderId()), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderResponse.class)).thenReturn(expectedOrderStatus);

        OrderResponse orderStatus = foreverOrderEndpoint.modifyForeverOrder(order);
        assertThat(orderStatus).isEqualTo(expectedOrderStatus);

        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpPutRequest(contains(order.getOrderId()), argThat(payload -> {
            assertThat(payload).isEqualTo(expectedParams);
            return true;
        }));
    }

    @Test
    void getAllForeverOrders_ShouldReturnResult() throws DhanAPIException {
        List<Order> expectedOrders = List.of(getSampleOrder());
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpGetRequest(eq(APIEndpoint.GetAllExistingForeverOrders))).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType((TypeReference<Object>) any())).thenReturn(expectedOrders);

        assertThat(foreverOrderEndpoint.getAllForeverOrders()).isEqualTo(expectedOrders);
        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpGetRequest(eq(APIEndpoint.GetAllExistingForeverOrders));
    }

    @Test
    void cancelForeverOrder_ShouldReturnResult() throws DhanAPIException {
        String orderID = "123";
        OrderResponse expectedOrderStatus = new OrderResponse(orderID, OrderStatus.CANCELLED);
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpDeleteRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(OrderResponse.class)).thenReturn(expectedOrderStatus);

        OrderResponse orderResponse = foreverOrderEndpoint.cancelForeverOrder(orderID);
        assertThat(orderResponse).isEqualTo(expectedOrderStatus);
        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpDeleteRequest(contains(orderID));
    }

    @NotNull
    private static Order getSampleOrder() {
        Order order = new Order();
        order.setOrderId("123");
        order.setOrderFlag(OrderFlag.OCO);
        order.setTransactionType(TransactionType.BUY);
        order.setExchangeSegment(ExchangeSegment.NSE_EQ);
        order.setProductType(ProductType.CNC);
        order.setOrderType(OrderType.LIMIT);
        order.setValidity(Validity.DAY);
        order.setSecurityId("123");
        order.setQuantity(900);
        order.setDisclosedQuantity(90);
        order.setPrice(BigDecimal.valueOf(99.99));
        order.setTriggerPrice(BigDecimal.valueOf(99.0));
        order.setPrice1(BigDecimal.valueOf(88.88));
        order.setTriggerPrice1(BigDecimal.valueOf(88.0));
        order.setQuantity1(800);
        order.setLegName(LegName.ENTRY_LEG); // Used in ModifyOrder operation
        return order;
    }

    @NotNull
    private static Map<String, String> getExpectedParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put(APIParam.OrderID, "123");
        expectedParams.put(APIParam.CorrelationID, "c123");
        expectedParams.put(APIParam.OrderFlag, OrderFlag.OCO.toString());
        expectedParams.put(APIParam.TransactionType, TransactionType.BUY.toString());
        expectedParams.put(APIParam.ExchangeSegment, ExchangeSegment.NSE_EQ.toString());
        expectedParams.put(APIParam.ProductType, ProductType.CNC.toString());
        expectedParams.put(APIParam.OrderType, OrderType.LIMIT.toString());
        expectedParams.put(APIParam.Validity, Validity.DAY.toString());
        expectedParams.put(APIParam.SecurityID, "123");
        expectedParams.put(APIParam.Quantity, "900");
        expectedParams.put(APIParam.DisclosedQuantity, "90");
        expectedParams.put(APIParam.Price, "99.99");
        expectedParams.put(APIParam.TriggerPrice, "99.0");
        expectedParams.put(APIParam.Price1, "88.88");
        expectedParams.put(APIParam.TriggerPrice1, "88.0");
        expectedParams.put(APIParam.Quantity1, "800");
        expectedParams.put(APIParam.LegName, LegName.ENTRY_LEG.toString());
        return expectedParams;
    }
}