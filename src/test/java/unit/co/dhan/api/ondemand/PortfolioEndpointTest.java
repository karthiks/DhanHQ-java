package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.PositionType;
import co.dhan.constant.ProductType;
import co.dhan.dto.Holding;
import co.dhan.dto.Position;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PortfolioEndpointTest extends UnitTestRoot {

    @Mock
    DhanConnection mockDhanConnection;

    @Mock
    DhanHTTP mockDhanHTTP;

    @Mock
    DhanResponse mockDhanResponse;

    @Spy
    @InjectMocks
    PortfolioEndpoint portfolioEndpoint;

    @Test
    void getCurrentHoldings_ReturnListOfHoldings() {
        List<Holding> expectedHoldings = new ArrayList<>();
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpGetRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType((TypeReference<List<Holding>>) any())).thenReturn(expectedHoldings);

        assertThat(portfolioEndpoint.getCurrentHoldings()).isEqualTo(expectedHoldings);
        verify(mockDhanHTTP).doHttpGetRequest(eq("/holdings"));
    }

    @Test
    void getCurrentPositions_ReturnListOfHoldings() {
        List<Position> expectedPositions = new ArrayList<>();
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpGetRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType((TypeReference<List<Position>>) any())).thenReturn(expectedPositions);

        assertThat(portfolioEndpoint.getCurrentPositions()).isEqualTo(expectedPositions);
        verify(mockDhanHTTP).doHttpGetRequest(eq("/positions"));
    }

    @Test
    void convertPosition() {
        int quantity = 100;
        Position currentPosition = new Position();
        currentPosition.setSecurityId("1");
        currentPosition.setExchangeSegment(ExchangeSegment.NSE_EQ);
        currentPosition.setPositionType(PositionType.LONG);
        currentPosition.setProductType(ProductType.CNC);
        currentPosition.setBuyQty(quantity);
        ProductType toProductType = ProductType.INTRADAY;
        Map<String, String> expectedPayload = new HashMap<>();
        expectedPayload.put(PortfolioEndpoint.APIParam.SecurityID,currentPosition.getSecurityId());
        expectedPayload.put(PortfolioEndpoint.APIParam.ExchangeSegment, currentPosition.getExchangeSegment().toString());
        expectedPayload.put(PortfolioEndpoint.APIParam.PositionType, currentPosition.getPositionType().toString());
        expectedPayload.put(PortfolioEndpoint.APIParam.FromProductType, currentPosition.getProductType().toString());
        expectedPayload.put(PortfolioEndpoint.APIParam.ToProductType, toProductType.toString());
        expectedPayload.put(PortfolioEndpoint.APIParam.ConvertQuantity, String.valueOf(quantity));

        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpPostRequest(anyString(),anyMap())).thenReturn(mockDhanResponse);

        portfolioEndpoint.convertPosition(currentPosition, toProductType, quantity);
        verify(mockDhanHTTP).doHttpPostRequest(eq("/positions/convert"), argThat(payload -> {
            assertThat(payload).isEqualTo(expectedPayload);
            return true;
        }));
    }
}