# Implementation Spec for Super Order: GET /super/orders - Retrieve List of All Super Orders

**Task**
Implement the Dhan API v2 endpoint to retrieve the list of all super orders.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method to the appropriate endpoint class (create `SuperOrderEndpoint` if it does not exist, otherwise use the class handling order-related operations):

    ```java
    public List<SuperOrder> getAllSuperOrders()
    ```

2. **Javadoc Documentation**
   Write standard professional Javadoc including:
    - Clear description of what the method does
    - Reference to the API endpoint: `GET https://api.dhan.co/v2/super/orders`
    - Details about the return value
    - Any thrown exceptions

3. **Implement Using TDD**

4. **Integration Test (Happy Path)**
   Add one integration test (happy path only) that calls the real Dhan API (using existing test credentials or sandbox setup) and asserts:
    - The call succeeds
    - The returned list is not null
    - The list contains valid `SuperOrder` objects

**Model Requirements**
Create a new POJO model class named `SuperOrder` (and any nested request/response objects it contains) with proper Lombok annotations - getters, setters, and a no-arg constructor. Follow the exact style of existing models like `Order` or `NewOrderRequest`. Include all fields returned by the API (order legs, conditions, status, etc.).

**API Details**

- HTTP Method: GET
- URL: `https://api.dhan.co/v2/super/orders`
- No request body
- No query parameters
- Response: JSON array of super order objects
- Use the existing HTTP client, authentication mechanism, and error handling used by other GET methods in the SDK.

**Constraints**

- Do not modify any other endpoints or existing methods.
- Reuse existing utility classes for HTTP calls and JSON parsing.
- Handle standard Dhan API error responses gracefully.
