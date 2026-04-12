# Implementation Spec: GET /trades/{order-id} - Retrieve Trades by Order ID

**Task**
Implement the Dhan API v2 endpoint to retrieve all trades (including partial fills) associated with a specific order ID.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method to the same endpoint class used in the previous trades implementation:

    ```java
    public List<Trade> getTradesByOrderId(String orderId)
    ```

2. **Javadoc Documentation**
   Write standard professional Javadoc including:
    - Clear description
    - Reference to the API endpoint: `GET https://api.dhan.co/v2/trades/{order-id}`
    - Parameter description and validation note
    - Return value details
    - Exceptions that may be thrown

3. **Implement Using TDD**

4. **Integration Test (Happy Path)**
   Add one integration test (happy path only) that calls the real API with a valid `orderId` and asserts the response is a valid list of `Trade` objects.

**Model**
Reuse the exact `Trade` model created for the `/trades` endpoint (same fields and structure as defined in the previous spec).

**API Details**

- HTTP Method: GET
- URL: `https://api.dhan.co/v2/trades/{order-id}`
- Path parameter: `orderId` (String, required, non-blank)
- No request body
- Response: JSON array of `Trade` objects (can return multiple entries for partial fills)
- Use the same HTTP client, auth, and error handling as other parameterized GET calls in the SDK.

**Validation & Constraints**

- Throw an appropriate exception (consistent with SDK style) if `orderId` is null or blank.
- Do not modify any other code outside this method, the `Trade` model (if needed), and the new tests.
- Follow the exact same coding and testing conventions as existing methods like `getOrderByID(String orderID)`.
