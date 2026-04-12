package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.dto.SuperOrder;
import co.dhan.dto.SuperOrderModificationRequest;
import co.dhan.dto.SuperOrderRequest;
import co.dhan.dto.SuperOrderResponse;
import co.dhan.http.DhanAPIException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperOrderEndpoint {

  interface APIParam {
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
    String OrderLeg = "orderLeg";
    String FromLeg = "fromLeg";
    String ToLeg = "toLeg";
    String TriggerType = "triggerType";
    String TriggerValue = "triggerValue";
    String TriggerOperator = "triggerOperator";
    String IsMet = "isMet";
    String AMOTime = "amoTime";
    String AfterMarketOrder = "afterMarketOrder";
    String BOProfitValue = "boProfitValue";
    String BOStopLossValue = "boStopLossValue";
    String DrvExpiryDate = "drvExpiryDate";
    String DrvOptionType = "drvOptionType";
    String DrvStrikePrice = "drvStrikePrice";
    String AlgoId = "algoId";
    String OmsErrorCode = "omsErrorCode";
    String OmsErrorDescription = "omsErrorDescription";
    String FilledQty = "filledQty";
    String RemainingQuantity = "remainingQuantity";
    String AverageTradedPrice = "averageTradedPrice";
  }

  interface APIEndpoint {
    String GetAllSuperOrders = "/super/orders";
    String CreateSuperOrder = "/super/orders";
    String ModifySuperOrder = "/super/orders/%s";
    String CancelSuperOrderLeg = "/super/orders/%s/%s";
  }

  private final DhanConnection dhanConnection;

  public SuperOrderEndpoint(DhanConnection dhanConnection) {
    this.dhanConnection = dhanConnection;
  }

  /**
   * Retrieves the list of all super orders for the authenticated user.
   *
   * <p>Endpoint: GET https://api.dhan.co/v2/super/orders
   *
   * <p>Response contains: Complete list of super orders with all details including order legs,
   * conditions, and status.
   *
   * <p>Throws: - DhanAPIException: For API authentication/authorization failure, invalid response
   * format, or non-200 status code
   *
   * @return List of super orders with all required fields populated
   */
  public List<SuperOrder> getAllSuperOrders() throws DhanAPIException {
    return dhanConnection
        .getDhanHTTP()
        .doHttpGetRequest(APIEndpoint.GetAllSuperOrders)
        .convertToType(new TypeReference<List<SuperOrder>>() {});
  }

  /**
   * Places a new super order with multiple legs and trigger conditions.
   *
   * <p>Endpoint: POST https://api.dhan.co/v2/super/orders
   *
   * <p>Request contains: Super order configuration including order legs, trigger conditions, and
   * trading parameters.
   *
   * <p>Response contains: Super order confirmation with order ID and status.
   *
   * <p>Throws: - DhanAPIException: For API authentication/authorization failure, invalid request
   * format, or non-200 status code from the API
   *
   * @param request Super order request containing legs, conditions, and trading parameters
   * @return Super order confirmation with order ID and status
   */
  public SuperOrderResponse placeSuperOrder(SuperOrderRequest request) throws DhanAPIException {
    // Input validation
    if (request == null) {
      throw new DhanAPIException("Input Error", "SuperOrderRequest cannot be null");
    }

    if (request.getLegs() == null || request.getLegs().isEmpty()) {
      throw new DhanAPIException("Input Error", "Super order must have at least one leg");
    }

    Map<String, String> payload = new HashMap<>();

    // Process each leg and add to payload with indexed parameters
    for (int i = 0; i < request.getLegs().size(); i++) {
      SuperOrderRequest.SuperOrderLegRequest leg = request.getLegs().get(i);
      int legIndex = i + 1; // 1-based indexing for API

      payload.put(APIParam.SecurityID + legIndex, leg.getSecurityId());
      payload.put(APIParam.ExchangeSegment + legIndex, leg.getExchangeSegment().toString());
      payload.put(APIParam.TransactionType + legIndex, leg.getTransactionType().toString());
      payload.put(APIParam.OrderType + legIndex, leg.getOrderType().toString());
      payload.put(APIParam.Quantity + legIndex, String.valueOf(leg.getQuantity()));
      payload.put(APIParam.Price + legIndex, String.valueOf(leg.getPrice()));
      payload.put(APIParam.TriggerPrice + legIndex, String.valueOf(leg.getTriggerPrice()));
      payload.put(APIParam.LegName + legIndex, leg.getLegName().toString());
      payload.put(
          APIParam.DisclosedQuantity + legIndex, String.valueOf(leg.getDisclosedQuantity()));
      payload.put(APIParam.AMOTime + legIndex, String.valueOf(leg.getAmoTime()));
      payload.put(APIParam.AfterMarketOrder + legIndex, String.valueOf(leg.isAfterMarketOrder()));

      // Add optional fields if present
      if (leg.getBoProfitValue() != null) {
        payload.put(APIParam.BOProfitValue + legIndex, String.valueOf(leg.getBoProfitValue()));
      }
      if (leg.getBoStopLossValue() != null) {
        payload.put(APIParam.BOStopLossValue + legIndex, String.valueOf(leg.getBoStopLossValue()));
      }
      if (leg.getDrvExpiryDate() != null && !leg.getDrvExpiryDate().isEmpty()) {
        payload.put(APIParam.DrvExpiryDate + legIndex, leg.getDrvExpiryDate());
      }
      if (leg.getDrvOptionType() != null) {
        payload.put(APIParam.DrvOptionType + legIndex, leg.getDrvOptionType().toString());
      }
      if (leg.getDrvStrikePrice() != null) {
        payload.put(APIParam.DrvStrikePrice + legIndex, String.valueOf(leg.getDrvStrikePrice()));
      }
      if (leg.getAlgoId() != null && !leg.getAlgoId().isEmpty()) {
        payload.put(APIParam.AlgoId + legIndex, leg.getAlgoId());
      }
      if (leg.getOmsErrorCode() != null && !leg.getOmsErrorCode().isEmpty()) {
        payload.put(APIParam.OmsErrorCode + legIndex, leg.getOmsErrorCode());
      }
      if (leg.getOmsErrorDescription() != null && !leg.getOmsErrorDescription().isEmpty()) {
        payload.put(APIParam.OmsErrorDescription + legIndex, leg.getOmsErrorDescription());
      }
      if (leg.getFilledQty() != 0) {
        payload.put(APIParam.FilledQty + legIndex, String.valueOf(leg.getFilledQty()));
      }
      if (leg.getRemainingQuantity() != 0) {
        payload.put(
            APIParam.RemainingQuantity + legIndex, String.valueOf(leg.getRemainingQuantity()));
      }
      if (leg.getAverageTradedPrice() != null) {
        payload.put(
            APIParam.AverageTradedPrice + legIndex, String.valueOf(leg.getAverageTradedPrice()));
      }
    }

    // Add super order level parameters
    if (request.getOrderType() != null) {
      payload.put(APIParam.OrderType, request.getOrderType().toString());
    }
    if (request.getValidity() != null) {
      payload.put(APIParam.Validity, request.getValidity().toString());
    }
    if (request.getProductType() != null) {
      payload.put(APIParam.ProductType, request.getProductType().toString());
    }
    if (request.getCorrelationId() != null && !request.getCorrelationId().isEmpty()) {
      payload.put(APIParam.CorrelationID, request.getCorrelationId());
    }

    // Process trigger conditions
    if (request.getConditions() != null && !request.getConditions().isEmpty()) {
      for (int i = 0; i < request.getConditions().size(); i++) {
        SuperOrderRequest.TriggerCondition condition = request.getConditions().get(i);
        int conditionIndex = i + 1; // 1-based indexing

        payload.put(APIParam.FromLeg + conditionIndex, condition.getFromLeg());
        payload.put(APIParam.ToLeg + conditionIndex, condition.getToLeg());
        payload.put(APIParam.TriggerType + conditionIndex, condition.getType().toString());
        payload.put(
            APIParam.TriggerValue + conditionIndex, String.valueOf(condition.getTriggerValue()));
        payload.put(APIParam.TriggerOperator + conditionIndex, condition.getOperator().toString());
        payload.put(APIParam.IsMet + conditionIndex, String.valueOf(condition.isMet()));
      }
    }

    return dhanConnection
        .getDhanHTTP()
        .doHttpPostRequest(APIEndpoint.CreateSuperOrder, payload)
        .convertToType(SuperOrderResponse.class);
  }

  /**
   * Modifies a pending super order.
   *
   * <p>Endpoint: PUT https://api.dhan.co/v2/super/orders/{order-id}
   *
   * <p>Request contains: Super order modification details including order type, leg name, validity,
   * quantity, price, and trigger price.
   *
   * <p>Response contains: Super order confirmation with order ID and status.
   *
   * <p>Throws: - DhanAPIException: For API authentication/authorization failure, invalid request
   * format, or non-200 status code from the API
   *
   * @param request Super order modification request containing order ID and updated parameters
   * @return Super order confirmation with order ID and status
   */
  public SuperOrderResponse modifySuperOrder(SuperOrderModificationRequest request)
      throws DhanAPIException {
    // Input validation
    if (request == null
        || request.getOrderId() == null
        || request.getOrderId().isEmpty()
        || request.getOrderType() == null
        || request.getLegName() == null
        || request.getValidity() == null
        || request.getQuantity() == 0) {
      throw new DhanAPIException(
          "Input Error",
          "One of the values are invalid -> OrderID, OrderType, OrderLegName, OrderValidity, OrderQuantity.");
    }
    Map<String, String> payload = new HashMap<>();

    payload.put(APIParam.OrderID, request.getOrderId());
    payload.put(APIParam.OrderType, request.getOrderType().toString());
    payload.put(APIParam.LegName, request.getLegName().toString());
    payload.put(APIParam.Validity, request.getValidity().toString());
    payload.put(APIParam.Quantity, String.valueOf(request.getQuantity()));
    payload.put(APIParam.DisclosedQuantity, String.valueOf(request.getDisclosedQuantity()));
    payload.put(APIParam.Price, String.valueOf(request.getPrice()));
    payload.put(APIParam.TriggerPrice, String.valueOf(request.getTriggerPrice()));

    String endpoint = String.format(APIEndpoint.ModifySuperOrder, request.getOrderId());
    return dhanConnection
        .getDhanHTTP()
        .doHttpPutRequest(endpoint, payload)
        .convertToType(SuperOrderResponse.class);
  }

  /**
   * Cancels a specific leg of a pending super order.
   *
   * <p>Endpoint: DELETE https://api.dhan.co/v2/super/orders/{order-id}/{order-leg}
   *
   * @param orderId   the super order ID (must not be null or blank)
   * @param orderLeg  the order leg to cancel (must not be null or blank)
   * @return Super order confirmation with order ID and status
   * @throws DhanAPIException if orderId or orderLeg is null or blank, or if the API returns an error
   */
  public SuperOrderResponse cancelSuperOrderLeg(String orderId, String orderLeg)
      throws DhanAPIException {
    // Input validation
    if (orderId == null || orderId.isBlank()) {
      throw new DhanAPIException("Input Error", "Order ID must not be null or blank");
    }
    if (orderLeg == null || orderLeg.isBlank()) {
      throw new DhanAPIException("Input Error", "Order leg must not be null or blank");
    }
    String endpoint = String.format(APIEndpoint.CancelSuperOrderLeg, orderId, orderLeg);
    return dhanConnection
        .getDhanHTTP()
        .doHttpDeleteRequest(endpoint)
        .convertToType(SuperOrderResponse.class);
  }
}
