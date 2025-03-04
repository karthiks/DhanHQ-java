package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.dto.ForeverOrder;
import co.dhan.dto.ForeverOrderResponse;
import co.dhan.dto.ModifyForeverOrderRequest;
import co.dhan.dto.NewForeverOrderRequest;
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

    public ForeverOrderResponse placeForeverOrder(NewForeverOrderRequest foRequest) throws DhanAPIException {
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.OrderFlag, foRequest.getOrderFlag().toString());
        payload.put(APIParam.TransactionType, foRequest.getTransactionType().toString());
        payload.put(APIParam.ExchangeSegment, foRequest.getExchangeSegment().toString());
        payload.put(APIParam.ProductType, foRequest.getProductType().toString());
        payload.put(APIParam.OrderType, foRequest.getOrderType().toString());
        payload.put(APIParam.Validity, foRequest.getValidity().toString());
        payload.put(APIParam.SecurityID, foRequest.getSecurityId());
        payload.put(APIParam.Quantity, String.valueOf(foRequest.getQuantity()));
        payload.put(APIParam.DisclosedQuantity, String.valueOf(foRequest.getDisclosedQuantity()));
        payload.put(APIParam.Price, String.valueOf(foRequest.getPrice()));
        payload.put(APIParam.TriggerPrice, String.valueOf(foRequest.getTriggerPrice()));
        payload.put(APIParam.Price1, String.valueOf(foRequest.getPrice1()));
        payload.put(APIParam.TriggerPrice1, String.valueOf(foRequest.getTriggerPrice1()));
        payload.put(APIParam.Quantity1, String.valueOf(foRequest.getQuantity1()));
        if (foRequest.getCorrelationId() != null && !foRequest.getCorrelationId().isBlank()){
            payload.put(APIParam.CorrelationID,foRequest.getCorrelationId());
        }

        return dhanConnection
                .getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.CreateForeverOrder, payload)
                .convertToType(ForeverOrderResponse.class);
    }


    public ForeverOrderResponse modifyForeverOrder(ModifyForeverOrderRequest mfoRequest) throws DhanAPIException {
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.OrderID, mfoRequest.getOrderId());
        payload.put(APIParam.OrderFlag, mfoRequest.getOrderFlag().toString());
        payload.put(APIParam.OrderType, mfoRequest.getOrderType().toString());
        payload.put(APIParam.LegName, mfoRequest.getLegName().toString());
        payload.put(APIParam.Quantity, String.valueOf(mfoRequest.getQuantity()));
        payload.put(APIParam.DisclosedQuantity, String.valueOf(mfoRequest.getDisclosedQuantity()));
        payload.put(APIParam.Price, String.valueOf(mfoRequest.getPrice()));
        payload.put(APIParam.TriggerPrice, String.valueOf(mfoRequest.getTriggerPrice()));
        payload.put(APIParam.Validity, mfoRequest.getValidity().toString());

        String endpoint = String.format(APIEndpoint.ModifyForeverOrder,mfoRequest.getOrderId());
        return dhanConnection
                .getDhanHTTP()
                .doHttpPutRequest(endpoint, payload)
                .convertToType(ForeverOrderResponse.class);
    }

    public ForeverOrderResponse cancelForeverOrder(String orderID) throws DhanAPIException {
        String endpoint = String.format(APIEndpoint.CancelForeverOrder,orderID);
        return dhanConnection
                .getDhanHTTP()
                .doHttpDeleteRequest(endpoint)
                .convertToType(ForeverOrderResponse.class);
    }

    public List<ForeverOrder> getAllForeverOrders() throws DhanAPIException {
        return dhanConnection
                .getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetAllExistingForeverOrders)
                .convertToType(new TypeReference<List<ForeverOrder>>() {});
    }
}
