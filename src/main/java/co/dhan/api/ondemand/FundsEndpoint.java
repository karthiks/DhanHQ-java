package co.dhan.api.ondemand;

import co.dhan.api.DhanContext;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.ProductType;
import co.dhan.constant.TransactionType;
import co.dhan.helper.BigDecimalUtils;
import co.dhan.http.DhanAPIException;
import co.dhan.dto.FundSummary;
import co.dhan.dto.Margin;

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

    private final DhanContext dhanContext;

    public FundsEndpoint(DhanContext dhanContext) {
        this.dhanContext = dhanContext;
    }

    public FundSummary getFundLimitDetails() throws DhanAPIException {
        return dhanContext.getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetFundLimitDetails)
                .convertToType(FundSummary.class);
    }

    public Margin computeMargin(String securityID, ExchangeSegment exchangeSegment, TransactionType transactionType,
                                int quantity, ProductType productType, String price, String triggerPrice) throws DhanAPIException {
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.SecurityID, securityID);
        payload.put(APIParam.ExchangeSegment, String.valueOf(exchangeSegment));
        payload.put(APIParam.TransactionType, String.valueOf(transactionType));
        payload.put(APIParam.Quantity, String.valueOf(quantity));
        payload.put(APIParam.ProductType, String.valueOf(productType));
        payload.put(APIParam.Price, BigDecimalUtils.toBigDecimal(price).toString());
        payload.put(APIParam.TriggerPrice, BigDecimalUtils.toBigDecimal(triggerPrice).toString());

        return dhanContext.getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.ComputeMargin,payload)
                .convertToType(Margin.class);
    }

    public Margin computeMargin(String securityID, ExchangeSegment exchangeSegment, TransactionType transactionType,
                                int quantity, ProductType productType, String price) throws DhanAPIException {
        return computeMargin(securityID,exchangeSegment,transactionType,quantity,productType,price,"0.0");
    }
}
