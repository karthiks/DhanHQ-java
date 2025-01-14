package co.dhan.api.ondemand;

import co.dhan.api.DhanContext;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.PositionType;
import co.dhan.constant.ProductType;
import co.dhan.http.DhanAPIException;
import co.dhan.http.DhanResponse;
import co.dhan.dto.Holding;
import co.dhan.dto.Position;
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

    private final DhanContext dhanContext;

    public PortfolioEndpoint(DhanContext dhanContext) {
        this.dhanContext = dhanContext;
    }

    public List<Holding> getCurrentHoldings() throws DhanAPIException {
        List<Holding> holdings = dhanContext.getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetCurrentHoldings)
                .convertToType(new TypeReference<List<Holding>>() {});
        return holdings;
    }

    public List<Position> getCurrentPositions() throws DhanAPIException {
        List<Position> positions = dhanContext.getDhanHTTP()
                .doHttpGetRequest(APIEndpoint.GetCurrentPositions)
                .convertToType(new TypeReference<List<Position>>() {});
        return positions;
    }

    public DhanResponse convertPosition(String securityId, ExchangeSegment exchangeSegment, PositionType positionType,
                                        ProductType fromProductType, ProductType toProductType, int convertQuantity)
            throws DhanAPIException {
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.SecurityID,securityId);
        payload.put(APIParam.ExchangeSegment, exchangeSegment.toString());
        payload.put(APIParam.PositionType, positionType.toString());
        payload.put(APIParam.FromProductType, fromProductType.toString());
        payload.put(APIParam.ToProductType, toProductType.toString());
        payload.put(APIParam.ConvertQuantity, String.valueOf(convertQuantity));

        return dhanContext.getDhanHTTP()
                .doHttpPostRequest(APIEndpoint.ConvertPosition, payload);
    }
}
