package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.dto.SuperOrder;
import co.dhan.http.DhanAPIException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

public class SuperOrderEndpoint {

  interface APIParam {
    // No specific parameters needed for GET /super/orders endpoint
    // Adding placeholder for future extensions if needed
  }

  interface APIEndpoint {
    String GetAllSuperOrders = "/super/orders";
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
}
