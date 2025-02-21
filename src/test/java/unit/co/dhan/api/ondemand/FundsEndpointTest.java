package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.ProductType;
import co.dhan.constant.TransactionType;
import co.dhan.dto.FundSummary;
import co.dhan.dto.Margin;
import co.dhan.helper.BigDecimalUtils;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FundsEndpointTest extends UnitTestRoot {

    @Mock
    DhanConnection mockDhanConnection;

    @Mock
    DhanHTTP mockDhanHTTP;

    @Mock
    DhanResponse mockDhanResponse;

    @Spy
    @InjectMocks
    FundsEndpoint fundsEndpoint;

    @Test
    void getFundLimitDetails_ReturnsFundSummary() {
        FundSummary expectedFundSummary = new FundSummary();

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpGetRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(FundSummary.class)).thenReturn(expectedFundSummary);

        assertThat(fundsEndpoint.getFundLimitDetails()).isEqualTo(expectedFundSummary);
        verify(mockDhanHTTP).doHttpGetRequest("/fundlimit");
    }

    @Test
    void computeMargin_ReturnsMargin() {
        String securityid = "1";
        ExchangeSegment exseg = ExchangeSegment.NSE_EQ;
        TransactionType transTypeBuy = TransactionType.BUY;
        int qty = 100;
        ProductType prodTypeCnc = ProductType.CNC;
        BigDecimal price = BigDecimalUtils.toBigDecimal("99.99");
        BigDecimal triggerPrice = BigDecimalUtils.toBigDecimal("99.90");

        Margin expectedMargin = new Margin();
        Map<String, String> expectedPayload = new HashMap<>();
        expectedPayload.put(FundsEndpoint.APIParam.SecurityID, securityid);
        expectedPayload.put(FundsEndpoint.APIParam.ExchangeSegment, String.valueOf(exseg));
        expectedPayload.put(FundsEndpoint.APIParam.TransactionType, String.valueOf(transTypeBuy));
        expectedPayload.put(FundsEndpoint.APIParam.Quantity, String.valueOf(qty));
        expectedPayload.put(FundsEndpoint.APIParam.ProductType, String.valueOf(prodTypeCnc));
        expectedPayload.put(FundsEndpoint.APIParam.Price, price.toString());
        expectedPayload.put(FundsEndpoint.APIParam.TriggerPrice, triggerPrice.toString());

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(),anyMap())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(Margin.class)).thenReturn(expectedMargin);

        assertThat(fundsEndpoint.computeMargin(securityid, exseg, transTypeBuy, qty, prodTypeCnc, price, triggerPrice))
                .isEqualTo(expectedMargin);
        verify(mockDhanHTTP).doHttpPostRequest(eq("/margincalculator"), argThat(payload -> {
            assertThat(payload).isEqualTo(expectedPayload);
            return true;
        }));
    }
}