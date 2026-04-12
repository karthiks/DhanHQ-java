# Implementation Spec for Super Order: DELETE /super/orders/{order-id}/{order-leg} - Cancel a Pending Super Order Leg

**Task**
Implement the Dhan API v2 endpoint to cancel a specific leg of a pending super order.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method:

    ```java
    public SuperOrderResponse cancelSuperOrderLeg(String orderId, String orderLeg)
    ```

2. **Javadoc Documentation**
   Write standard Javadoc referencing `DELETE https://api.dhan.co/v2/super/orders/{order-id}/{order-leg}`.

3. **Implement Using TDD**

**Model**
Reuse `SuperOrderResponse` for the return type.

**API Details**

- HTTP Method: DELETE
- URL: `https://api.dhan.co/v2/super/orders/{order-id}/{order-leg}`
- Path parameters: `orderId` and `orderLeg` (both required)
- No request body
- Use existing DELETE pattern (e.g., `cancelOrder`).

**Constraints**

- Validate both parameters are not blank.
- Implement only this method and tests.
