# Implementation Spec for Super Order: POST /super/orders - Create a New Super Order

**Task**
Implement the Dhan API v2 endpoint to create a new super order.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method to the `SuperOrderEndpoint`:

    ```java
    public SuperOrderResponse placeSuperOrder(SuperOrderRequest request)
    ```

2. **Javadoc Documentation**
   Write standard professional Javadoc referencing `POST https://api.dhan.co/v2/super/orders`.

3. **Implement Using TDD**
    - Test must verify correct HTTP method, URL, request body serialization, and response deserialization.

**Model Requirements**
Create `SuperOrderRequest` (request payload) and `SuperOrderResponse` (response) POJOs with Lombok annotations - getters, setters, and no-arg constructors. Follow the style of `NewOrderRequest` / `OrderResponse` in the project. Include fields for multiple order legs, trigger conditions, validity, etc.

**API Details**

- HTTP Method: POST
- URL: `https://api.dhan.co/v2/super/orders`
- Request body: JSON object (`SuperOrderRequest`)
- Response: Super order confirmation object
- Use the existing HTTP client, authentication, and error handling.

**Constraints**

- Implement only this method, the new models, and the required tests.
- Do not change any other endpoints.
