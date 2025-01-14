package co.dhan.api.ondemand;

import co.dhan.api.DhanContext;
import co.dhan.constant.AMOTime;
import co.dhan.dto.OrderStatusDTO;
import co.dhan.http.DhanAPIException;
import co.dhan.dto.Order;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
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

    private final DhanContext dhanContext;

    public OrderEndpoint(DhanContext dhanContext) {
        this.dhanContext = dhanContext;
    }

    /**
     * The order request API allows you to place new order.
     * @param order: order that is to be placed
     * @param slice: boolean value true makes this a slice-order
     * @return OrderStatusDTO
     * @throws DhanAPIException
     */
    public OrderStatusDTO placeOrder(Order order, String tag, boolean slice) throws DhanAPIException {

        EnumSet<AMOTime> afterMarketOrderTimes = EnumSet.of(AMOTime.OPEN, AMOTime.OPEN_30, AMOTime.OPEN_60);
        if(order.isAfterMarketOrder()
                && !afterMarketOrderTimes.contains(order.getAmoTime()) ) {
            throw new DhanAPIException("Input Error", "After Market Order has to be one of the values OPEN, OPEN_30, OPEN_60");
        }

        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.SecurityID, order.getSecurityId().toString());
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
        OrderStatusDTO orderStatus = dhanContext
                .getDhanHTTP()
                .doHttpPostRequest(endpoint, payload)
                .convertToType(OrderStatusDTO.class);
        return orderStatus;
    }

    public OrderStatusDTO placeOrder(Order order, String tag) throws DhanAPIException {
        return placeOrder(order, tag,false);
    }

    public OrderStatusDTO placeOrder(Order order) throws DhanAPIException {
        return placeOrder(order, null,false);
    }

    public OrderStatusDTO placeSliceOrder(Order order, String tag) throws DhanAPIException {
        return placeOrder(order, tag, true);
    }

    public OrderStatusDTO placeSliceOrder(Order order) throws DhanAPIException {
        return placeOrder(order, null, true);
    }

    /**
     * The api allows you modify pending order in orderbook.
     * The fields that can be modified are price, quantity, order type & validity.
     * @param order
     * @return
     * @throws IOException
     * @throws DhanAPIException
     */
    public OrderStatusDTO modifyOrder(Order order)
            throws IOException, DhanAPIException {
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
        OrderStatusDTO orderStatus = dhanContext
                .getDhanHTTP()
                .doHttpPutRequest(endpoint, payload)
                .convertToType(OrderStatusDTO.class);
        return orderStatus;
    }

    public OrderStatusDTO cancelOrder(String orderID) throws IOException, DhanAPIException {
        String endpoint = String.format(APIEndpoint.CancelOrder,orderID);
        OrderStatusDTO orderStatus = dhanContext
                .getDhanHTTP()
                .doHttpDeleteRequest(endpoint)
                .convertToType(OrderStatusDTO.class);
        return orderStatus;
    }

    public List<Order> getCurrentOrders() throws IOException, DhanAPIException {
        List<Order> orders = dhanContext
                .getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetOrders)
                .convertToType(new TypeReference<List<Order>>() {});
        return orders;
    }

    public Order getOrderByID(String orderID) throws IOException, DhanAPIException {
        String endpoint = String.format(APIEndpoint.GetOrderByID,orderID);
        Order order = dhanContext
                .getDhanHTTP()
                .doHttpGetRequest(endpoint)
                .convertToType(Order.class);
        return order;
    }

    public Order getOrderByCorrelationID(String correlationID) throws IOException, DhanAPIException {
        String endpoint = String.format(APIEndpoint.GetOrderByCorrelationID,correlationID);
        Order order = dhanContext
                .getDhanHTTP()
                .doHttpGetRequest(endpoint)
                .convertToType(Order.class);
        return order;
    }
}
