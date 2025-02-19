package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.constant.ProductType;
import co.dhan.dto.Holding;
import co.dhan.dto.Position;
import co.dhan.http.DhanAPIException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioEndpoint {
    interface APIParam {
        String FromProductType = "fromProductType";
        String ExchangeSegment = "exchangeSegment";
        String PositionType = "positionType";
        String SecurityID = "securityId";
        String ConvertQuantity = "convertQty";
        String ToProductType = "toProductType";
    }

    interface APIEndpoint {
        String GetCurrentHoldings = "/holdings";
        String GetCurrentPositions = "/positions";
        String ConvertPosition = "/positions/convert";
    }

    private final DhanConnection dhanConnection;

    public PortfolioEndpoint(DhanConnection dhanConnection) {
        this.dhanConnection = dhanConnection;
    }

    public List<Holding> getCurrentHoldings() throws DhanAPIException {
        List<Holding> holdings = dhanConnection.getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetCurrentHoldings)
                .convertToType(new TypeReference<List<Holding>>() {});
        return holdings;
    }

    public List<Position> getCurrentPositions() throws DhanAPIException {
        List<Position> positions = dhanConnection.getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetCurrentPositions)
                .convertToType(new TypeReference<List<Position>>() {});
        return positions;
    }

    /**
     *
     * @param currentPosition values that are considered are securityID, exchangeSegment, positionType, productType
     * @param toProductType
     * @param quantityToConvert
     * @throws DhanAPIException
     */
    public void convertPosition(Position currentPosition, ProductType toProductType, int quantityToConvert)
            throws DhanAPIException {
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.SecurityID,currentPosition.getSecurityId());
        payload.put(APIParam.ExchangeSegment, currentPosition.getExchangeSegment().toString());
        payload.put(APIParam.PositionType, currentPosition.getPositionType().toString());
        payload.put(APIParam.FromProductType, currentPosition.getProductType().toString());
        payload.put(APIParam.ToProductType, toProductType.toString());
        payload.put(APIParam.ConvertQuantity, String.valueOf(quantityToConvert));

        dhanConnection.getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.ConvertPosition, payload);
    }
}
