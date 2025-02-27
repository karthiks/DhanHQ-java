package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.dto.Order;
import co.dhan.dto.OrderResponse;
import co.dhan.http.DhanAPIException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForeverOrderEndpoint {

    public interface APIParam {
        String OrderID = "orderId";
        String CorrelationID = "correlationId";
        String OrderFlag = "orderFlag";
        String LegName = "legName";
        String TransactionType = "transactionType";
        String ExchangeSegment = "exchangeSegment";
        String ProductType = "productType";
        String OrderType = "orderType";
        String Validity = "validity";
        String SecurityID = "securityId";
        String Quantity = "quantity";
        String DisclosedQuantity = "disclosedQuantity";
        String Price = "price";
        String TriggerPrice = "triggerPrice";
        String Price1 = "price1";
        String TriggerPrice1 = "triggerPrice1";
        String Quantity1 = "Quantity1";
    }

    public interface APIEndpoint {
        String GetAllExistingForeverOrders = "/forever/orders";
        String CreateForeverOrder = "/forever/orders";
        String ModifyForeverOrder = "/forever/orders/%s";
        String CancelForeverOrder = "/forever/orders/%s";
    }

    private final DhanConnection dhanConnection;

    public ForeverOrderEndpoint(DhanConnection dhanConnection) {
        this.dhanConnection = dhanConnection;
    }

    public OrderResponse placeForeverOrder(Order order, String tag) throws DhanAPIException {
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.OrderFlag, order.getOrderFlag().toString());
        payload.put(APIParam.TransactionType, order.getTransactionType().toString());
        payload.put(APIParam.ExchangeSegment, order.getExchangeSegment().toString());
        payload.put(APIParam.ProductType, order.getProductType().toString());
        payload.put(APIParam.OrderType, order.getOrderType().toString());
        payload.put(APIParam.Validity, order.getValidity().toString());
        payload.put(APIParam.SecurityID, order.getSecurityId());
        payload.put(APIParam.Quantity, String.valueOf(order.getQuantity()));
        payload.put(APIParam.DisclosedQuantity, String.valueOf(order.getDisclosedQuantity()));
        payload.put(APIParam.Price, String.valueOf(order.getPrice()));
        payload.put(APIParam.TriggerPrice, String.valueOf(order.getTriggerPrice()));
        payload.put(APIParam.Price1, String.valueOf(order.getPrice1()));
        payload.put(APIParam.TriggerPrice1, String.valueOf(order.getTriggerPrice1()));
        payload.put(APIParam.Quantity1, String.valueOf(order.getQuantity1()));
        if(tag!=null && !tag.trim().isEmpty()) {
            payload.put(APIParam.CorrelationID,tag.trim());
        }

        return dhanConnection
                .getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.CreateForeverOrder, payload)
                .convertToType(OrderResponse.class);
    }

    public OrderResponse placeForeverOrder(Order order) throws DhanAPIException {
        return placeForeverOrder(order, null);
    }

    public OrderResponse modifyForeverOrder(Order order) throws DhanAPIException {
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.OrderID, order.getOrderId());
        payload.put(APIParam.OrderFlag, order.getOrderFlag().toString());
        payload.put(APIParam.OrderType, order.getOrderType().toString());
        payload.put(APIParam.LegName, order.getLegName().toString());
        payload.put(APIParam.Quantity, String.valueOf(order.getQuantity()));
        payload.put(APIParam.DisclosedQuantity, String.valueOf(order.getDisclosedQuantity()));
        payload.put(APIParam.Price, String.valueOf(order.getPrice()));
        payload.put(APIParam.TriggerPrice, String.valueOf(order.getTriggerPrice()));
        payload.put(APIParam.Validity, order.getValidity().toString());

        String endpoint = String.format(APIEndpoint.ModifyForeverOrder,order.getOrderId());
        return dhanConnection
                .getDhanHTTP()
                .doHttpPutRequest(endpoint, payload)
                .convertToType(OrderResponse.class);
    }

    public OrderResponse cancelForeverOrder(String orderID) throws DhanAPIException {
        String endpoint = String.format(APIEndpoint.CancelForeverOrder,orderID);
        return dhanConnection
                .getDhanHTTP()
                .doHttpDeleteRequest(endpoint)
                .convertToType(OrderResponse.class);
    }

    public List<Order> getAllForeverOrders() throws DhanAPIException {
        return dhanConnection
                .getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetAllExistingForeverOrders)
                .convertToType(new TypeReference<List<Order>>() {});
    }
}
