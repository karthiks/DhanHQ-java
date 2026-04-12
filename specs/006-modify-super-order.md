# Implementation Spec for Super Order: PUT /super/orders/{order-id} - Modify a Pending Super Order

**Task**
Implement the Dhan API v2 endpoint to modify a pending super order.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method:

    ```java
    public SuperOrderResponse modifySuperOrder(SuperOrderModificationRequest request)
    ```

2. **Javadoc Documentation**
   Write standard Javadoc with endpoint referencing `PUT https://api.dhan.co/v2/super/orders/{order-id}`.

3. **Implement Using TDD**
    - Test path parameter substitution and request body.

**Models**
Reuse or extend `SuperOrderResponse`. Create `SuperOrderModificationRequest` POJO following existing modification request patterns (e.g., `ModifyOrderRequest`).

**API Details**

- HTTP Method: PUT
- URL: `https://api.dhan.co/v2/super/orders/{order-id}`
- Path param: `orderId` (required)
- Request body: modification details
- Use existing HTTP client and auth.

**Constraints**

- Validate `orderId` is not blank.
- Implement only this method and supporting models/tests.
