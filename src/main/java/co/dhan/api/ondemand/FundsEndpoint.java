package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.ProductType;
import co.dhan.constant.TransactionType;
import co.dhan.dto.FundSummary;
import co.dhan.dto.Margin;
import co.dhan.http.DhanAPIException;

import java.math.BigDecimal;
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
    }

    private final DhanConnection dhanConnection;

    public FundsEndpoint(DhanConnection dhanConnection) {
        this.dhanConnection = dhanConnection;
    }

    public FundSummary getFundLimitDetails() throws DhanAPIException {
        return dhanConnection.getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetFundLimitDetails)
                .convertToType(FundSummary.class);
    }

    public Margin computeMargin(String securityID, ExchangeSegment exchangeSegment, TransactionType transactionType,
                                int quantity, ProductType productType, BigDecimal price, BigDecimal triggerPrice)
            throws DhanAPIException {
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.SecurityID, securityID);
        payload.put(APIParam.ExchangeSegment, String.valueOf(exchangeSegment));
        payload.put(APIParam.TransactionType, String.valueOf(transactionType));
        payload.put(APIParam.Quantity, String.valueOf(quantity));
        payload.put(APIParam.ProductType, String.valueOf(productType));
        payload.put(APIParam.Price, price.toString());
        payload.put(APIParam.TriggerPrice, triggerPrice.toString());

        return dhanConnection.getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.ComputeMargin,payload)
                .convertToType(Margin.class);
    }

    public Margin computeMargin(String securityID, ExchangeSegment exchangeSegment, TransactionType transactionType,
                                int quantity, ProductType productType, BigDecimal price)
            throws DhanAPIException {
        return computeMargin(securityID,exchangeSegment,transactionType,quantity,productType,price,new BigDecimal(0));
    }
}
