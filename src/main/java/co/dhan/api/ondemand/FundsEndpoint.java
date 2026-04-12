package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.dto.FundSummary;
import co.dhan.dto.Margin;
import co.dhan.dto.MarginRequest;
import co.dhan.http.DhanAPIException;
import java.util.HashMap;
import java.util.Map;

public class FundsEndpoint {
  interface APIParam {
    String SecurityID = "securityId";
    String ExchangeSegment = "exchangeSegment";
    String TransactionType = "transactionType";
    String Quantity = "quantity";
    String ProductType = "productType";
    String Price = "price";
    String TriggerPrice = "triggerPrice";
  }

  interface APIEndpoint {
    String GetFundLimitDetails = "/fundlimit";
    String ComputeMargin = "/margincalculator";
    String ComputeMarginMulti = "/margincalculator/multi";
  }

  private final DhanConnection dhanConnection;

  public FundsEndpoint(DhanConnection dhanConnection) {
    this.dhanConnection = dhanConnection;
  }

  public FundSummary getFundLimitDetails() throws DhanAPIException {
    return dhanConnection
        .getDhanHTTP()
        .doHttpGetRequest(APIEndpoint.GetFundLimitDetails)
        .convertToType(FundSummary.class);
  }

  public Margin computeSingleOrderMargin(MarginRequest request) throws DhanAPIException {
    if (request == null) {
      throw new DhanAPIException("Input Error", "MarginRequest cannot be null");
    }
    Map<String, String> payload = new HashMap<>();
    payload.put(APIParam.SecurityID, request.getSecurityID());
    payload.put(APIParam.ExchangeSegment, String.valueOf(request.getExchangeSegment()));
    payload.put(APIParam.TransactionType, String.valueOf(request.getTransactionType()));
    payload.put(APIParam.Quantity, String.valueOf(request.getQuantity()));
    payload.put(APIParam.ProductType, String.valueOf(request.getProductType()));
    payload.put(APIParam.Price, request.getPrice().toString());
    payload.put(APIParam.TriggerPrice, request.getTriggerPrice().toString());

    return dhanConnection
        .getDhanHTTP()
        .doHttpPostRequest(APIEndpoint.ComputeMargin, payload)
        .convertToType(Margin.class);
  }
}
