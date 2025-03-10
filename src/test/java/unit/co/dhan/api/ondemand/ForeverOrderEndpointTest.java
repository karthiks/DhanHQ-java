package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.api.ondemand.ForeverOrderEndpoint.APIEndpoint;
import co.dhan.api.ondemand.ForeverOrderEndpoint.APIParam;
import co.dhan.constant.*;
import co.dhan.dto.ForeverOrder;
import co.dhan.dto.ForeverOrderResponse;
import co.dhan.dto.ModifyForeverOrderRequest;
import co.dhan.dto.NewForeverOrderRequest;
import co.dhan.helper.BigDecimalUtils;
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
        Map<String, String> expectedParams = getExpectedParams();
        List<String> expectedKeys =
                List.of(APIParam.OrderFlag, APIParam.TransactionType, APIParam.ExchangeSegment,
                        APIParam.ProductType, APIParam.OrderType, APIParam.Validity, APIParam.SecurityID,
                        APIParam.Quantity, APIParam.DisclosedQuantity, APIParam.Price, APIParam.TriggerPrice,
                        APIParam.Price1, APIParam.TriggerPrice1, APIParam.Quantity1, APIParam.CorrelationID);
        expectedParams.keySet().retainAll(expectedKeys);
        ForeverOrderResponse expectedResponse = new ForeverOrderResponse("o1", OrderStatus.PENDING);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(eq(APIEndpoint.CreateForeverOrder), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(ForeverOrderResponse.class)).thenReturn(expectedResponse);

        NewForeverOrderRequest foRequest = NewForeverOrderRequest.builder().securityId("123").correlationId("c123")
                .orderFlag(OrderFlag.OCO).orderType(OrderType.LIMIT).validity(Validity.DAY)
                .transactionType(TransactionType.BUY).productType(ProductType.CNC)
                .exchangeSegment(ExchangeSegment.NSE_EQ)
                .quantity(900).quantity1(800).disclosedQuantity(90)
                .price(BigDecimal.valueOf(99.99)).price1(BigDecimal.valueOf(88.88))
                .triggerPrice(BigDecimalUtils.toBigDecimal(99)).triggerPrice1(BigDecimalUtils.toBigDecimal(88))
                .build();
        ForeverOrderResponse actualResponse = foreverOrderEndpoint.placeForeverOrder(foRequest);
        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpPostRequest(eq(APIEndpoint.CreateForeverOrder), argThat(payload -> {
            assertThat(payload).isEqualTo(expectedParams);
            return true;
        }));
    }

    @Test
    void placeForeverOrder_ShouldThrowsDhanAPIException() throws DhanAPIException {
        when(mockDhanConnection.getDhanHTTP())
                .thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(eq(APIEndpoint.CreateForeverOrder), anyMap()))
                .thenThrow(new DhanAPIException("DH-00", "API Error"));

        NewForeverOrderRequest foRequest = NewForeverOrderRequest.builder().securityId("1")
                //.correlationId("cid1")
                .orderFlag(OrderFlag.OCO).orderType(OrderType.LIMIT).validity(Validity.DAY)
                .transactionType(TransactionType.BUY).productType(ProductType.CNC)
                .exchangeSegment(ExchangeSegment.NSE_EQ)
                .quantity(900).quantity1(800).disclosedQuantity(90)
                .price(BigDecimalUtils.toBigDecimal(99.99)).price1(BigDecimalUtils.toBigDecimal(88.88))
                .triggerPrice(BigDecimalUtils.toBigDecimal(99)).triggerPrice1(BigDecimalUtils.toBigDecimal(88))
                .build();
        assertThatThrownBy(() -> foreverOrderEndpoint.placeForeverOrder(foRequest))
                .isInstanceOf(DhanAPIException.class)
                .hasMessage("API Error");

        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpPostRequest(eq(APIEndpoint.CreateForeverOrder), anyMap());
    }

    @Test
    void modifyForeverOrder_ShouldReturnResult() throws DhanAPIException {
        Map<String, String> expectedParams = getExpectedParams();
        List<String> expectedKeys = List.of(APIParam.OrderID, APIParam.OrderFlag, APIParam.OrderType, APIParam.LegName,
                APIParam.Quantity, APIParam.DisclosedQuantity, APIParam.Price, APIParam.TriggerPrice, APIParam.Validity);
        expectedParams.keySet().retainAll(expectedKeys);

        ForeverOrderResponse expectedResponse = new ForeverOrderResponse("123", OrderStatus.PENDING);

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPutRequest(contains("123"), anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(ForeverOrderResponse.class)).thenReturn(expectedResponse);

        ModifyForeverOrderRequest mfoRequest = ModifyForeverOrderRequest.builder().orderId("123")
                .orderFlag(OrderFlag.OCO).orderType(OrderType.LIMIT).legName(LegName.ENTRY_LEG).validity(Validity.DAY)
                .quantity(900).disclosedQuantity(90)
                .price(BigDecimalUtils.toBigDecimal(99.99))
                .triggerPrice(BigDecimalUtils.toBigDecimal(99))
                .build();
        ForeverOrderResponse actualResponse = foreverOrderEndpoint.modifyForeverOrder(mfoRequest);
        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpPutRequest(contains("123"), argThat(payload -> {
            assertThat(payload).isEqualTo(expectedParams);
            return true;
        }));
    }

    @Test
    void getAllForeverOrders_ShouldReturnResult() throws DhanAPIException {
        ForeverOrder feo = ForeverOrder.builder().orderId("123").build();
        List<ForeverOrder> expectedOrders = List.of(feo);
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
        ForeverOrderResponse expectedResponse = new ForeverOrderResponse(orderID, OrderStatus.CANCELLED);
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpDeleteRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(ForeverOrderResponse.class)).thenReturn(expectedResponse);

        ForeverOrderResponse actualResponse = foreverOrderEndpoint.cancelForeverOrder(orderID);
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(mockDhanConnection).getDhanHTTP();
        verify(mockDhanHTTP).doHttpDeleteRequest(contains(orderID));
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
        expectedParams.put(APIParam.TriggerPrice, "99.00");
        expectedParams.put(APIParam.Price1, "88.88");
        expectedParams.put(APIParam.TriggerPrice1, "88.00");
        expectedParams.put(APIParam.Quantity1, "800");
        expectedParams.put(APIParam.LegName, LegName.ENTRY_LEG.toString());
        return expectedParams;
    }
}