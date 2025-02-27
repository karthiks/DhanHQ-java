package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.constant.AMOTime;
import co.dhan.dto.Order;
import co.dhan.dto.OrderResponse;
import co.dhan.http.DhanAPIException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderEndpoint {

    interface APIParam {
        String OrderID = "orderId";
        String CorrelationID = "correlationId";
        String OrderType = "orderType";
        String LegName = "legName";
        String Validity = "validity";
        String Quantity = "quantity";
        String Price = "price";
        String DisclosedQuantity = "disclosedQuantity";
        String TriggerPrice = "triggerPrice";
        String SecurityID = "securityId";
        String TransactionType = "transactionType";
        String ExchangeSegment = "exchangeSegment";
        String ProductType = "productType";
        String AfterMarketOrder = "afterMarketOrder";
        String BOProfitValue = "boProfitValue";
        String BOStopLossValue = "boStopLossValue";
        String AMOTime = "amoTime";
        String OrderFlag = "orderFlag";
    }

    interface APIEndpoint {
        String GetOrders = "/orders";
        String GetOrderByID = "/orders/%s";
        String GetOrderByCorrelationID = "/orders/external/%s";
        String CancelOrder ="/orders/%s";
        String ModifyOrder = "/orders/%s";
        String PlaceOrder = "/orders";
        String PlaceSliceOrder = "/orders/slicing";
    }

    private final DhanConnection dhanConnection;

    public OrderEndpoint(DhanConnection dhanConnection) {
        this.dhanConnection = dhanConnection;
    }

    /**
     * The order request API allows you to place new order.
     * @param order: order that is to be placed
     * @param slice: boolean value true makes this a slice-order
     * @return OrderResponse
     * @throws DhanAPIException
     */
    public OrderResponse placeOrder(Order order, String tag, boolean slice) throws DhanAPIException {

        EnumSet<AMOTime> afterMarketOrderTimes = EnumSet.of(AMOTime.OPEN, AMOTime.OPEN_30, AMOTime.OPEN_60);
        if(order.isAfterMarketOrder()
                && !afterMarketOrderTimes.contains(order.getAmoTime()) ) {
            throw new DhanAPIException("Input Error", "After Market Order has to be one of the values OPEN, OPEN_30, OPEN_60");
        }

        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.SecurityID, order.getSecurityId());
        payload.put(APIParam.TransactionType, order.getTransactionType().toString());
        payload.put(APIParam.ExchangeSegment, order.getExchangeSegment().toString());
        payload.put(APIParam.ProductType, order.getProductType().toString());
        payload.put(APIParam.OrderType, order.getOrderType().toString());
        payload.put(APIParam.Validity, order.getValidity().toString());
        payload.put(APIParam.Quantity, String.valueOf(order.getQuantity()));
        payload.put(APIParam.DisclosedQuantity, String.valueOf(order.getDisclosedQuantity()));
        payload.put(APIParam.Price, String.valueOf(order.getPrice()));
        payload.put(APIParam.AfterMarketOrder, String.valueOf(order.isAfterMarketOrder()));
        payload.put(APIParam.AMOTime, String.valueOf(order.getAmoTime()));
        payload.put(APIParam.BOProfitValue, String.valueOf(order.getBoProfitValue()));
        payload.put(APIParam.BOStopLossValue, String.valueOf(order.getBoStopLossValue()));
        if(tag!=null && !tag.isEmpty()) {
            payload.put(APIParam.CorrelationID,tag);
        }

        String endpoint = slice? APIEndpoint.PlaceSliceOrder : APIEndpoint.PlaceOrder;
        return dhanConnection
                .getDhanHTTP()
                .doHttpPostRequest(endpoint, payload)
                .convertToType(OrderResponse.class);
    }

    public OrderResponse placeOrder(Order order, String tag) throws DhanAPIException {
        return placeOrder(order, tag,false);
    }

    public OrderResponse placeOrder(Order order) throws DhanAPIException {
        return placeOrder(order, null,false);
    }

    public OrderResponse placeSliceOrder(Order order, String tag) throws DhanAPIException {
        return placeOrder(order, tag, true);
    }

    public OrderResponse placeSliceOrder(Order order) throws DhanAPIException {
        return placeOrder(order, null, true);
    }

    /**
     * The api allows you to modify pending order in orderbook.
     * The fields that can be modified are price, quantity, order type & validity.
     * @param order
     * @return
     * @throws DhanAPIException
     */
    public OrderResponse modifyOrder(Order order)
            throws DhanAPIException {
        if (order==null || order.getOrderId()==null || order.getOrderId().isEmpty()
                || order.getOrderType()==null || order.getLegName()==null || order.getValidity()==null
                || order.getQuantity()==0) {
            throw new DhanAPIException("Input Error", "One of the values are invalid -> OrderID, OrderType, OrderLegName, OrderValidity, OrderQuantity.");
        }
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.OrderID, order.getOrderId());
        payload.put(APIParam.OrderType, order.getOrderType().toString());
        payload.put(APIParam.LegName, order.getLegName().toString());
        payload.put(APIParam.Validity, order.getValidity().toString());
        payload.put(APIParam.Quantity, String.valueOf(order.getQuantity()));
        payload.put(APIParam.DisclosedQuantity, String.valueOf(order.getDisclosedQuantity()));
        payload.put(APIParam.Price, String.valueOf(order.getPrice()));
        payload.put(APIParam.TriggerPrice, String.valueOf(order.getTriggerPrice()));

        String endpoint = String.format(APIEndpoint.ModifyOrder,order.getOrderId());
        return dhanConnection
                .getDhanHTTP()
                .doHttpPutRequest(endpoint, payload)
                .convertToType(OrderResponse.class);
    }

    public OrderResponse cancelOrder(String orderID) throws DhanAPIException {
        String endpoint = String.format(APIEndpoint.CancelOrder,orderID);
        return dhanConnection
                .getDhanHTTP()
                .doHttpDeleteRequest(endpoint)
                .convertToType(OrderResponse.class);
    }

    public List<Order> getCurrentOrders() throws DhanAPIException {
        return dhanConnection
                .getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetOrders)
                .convertToType(new TypeReference<List<Order>>() {});
    }

    public Order getOrderByID(String orderID) throws DhanAPIException {
        String endpoint = String.format(APIEndpoint.GetOrderByID,orderID);
        return dhanConnection
                .getDhanHTTP()
                .doHttpGetRequest(endpoint)
                .convertToType(Order.class);
    }

    public Order getOrderByCorrelationID(String correlationID) throws DhanAPIException {
        String endpoint = String.format(APIEndpoint.GetOrderByCorrelationID,correlationID);
        return dhanConnection
                .getDhanHTTP()
                .doHttpGetRequest(endpoint)
                .convertToType(Order.class);
    }
}
