# Implementation Spec: GET /trades - Retrieve List of All Trades for the Day

**Task**
Implement the Dhan API v2 endpoint to retrieve the list of all trades executed for the current day.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method to the appropriate endpoint class (e.g., the class that already contains `getCurrentOrders()` and similar order-related methods):

    ```java
    public List<Trade> getCurrentTrades()
    ```

2. **Javadoc Documentation**
   Write standard professional Javadoc including:
    - Clear description of what the method does
    - Reference to the API endpoint: `GET https://api.dhan.co/v2/trades`
    - Details about the return value
    - Any thrown exceptions (e.g., authentication or network errors)

3. **Implement Using TDD**

4. **Integration Test (Happy Path)**
   Add one integration test (happy path only) that calls the real Dhan API (using existing test credentials or sandbox setup in the project) and asserts:
    - The call succeeds
    - The returned list is not null
    - The list contains valid `Trade` objects with expected fields populated

**Model Requirements**
Create a new POJO model class named `Trade` if it does not exist. It must include all the following fields with proper Lombok annotations for getters, setters, and a no-arg constructor (follow the exact style of existing models like `Order` or `Position` in the project):

```java
- String dhanClientId
- String orderId
- String exchangeOrderId
- String exchangeTradeId
- String transactionType          // e.g. "BUY", "SELL"
- String exchangeSegment          // e.g. "NSE_EQ"
- String productType
- String orderType
- String tradingSymbol
- String securityId
- Integer tradedQuantity
- Double tradedPrice
- String createTime
- String updateTime
- String exchangeTime
- String drvExpiryDate            // optional for derivatives
- String drvOptionType            // optional
- Double drvStrikePrice           // optional
```

**API Details**

- HTTP Method: GET
- URL: `https://api.dhan.co/v2/trades`
- No request body
- No query parameters
- Response: JSON array of trade objects
- Use the existing HTTP client, authentication mechanism, and error handling used by other GET methods in the SDK.

**Constraints**

- Do not modify any other endpoints or existing methods.
- Reuse existing utility classes for HTTP calls and JSON parsing.
- Handle standard Dhan API error responses gracefully.
