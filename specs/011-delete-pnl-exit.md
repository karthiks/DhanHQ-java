# Implementation Spec: DELETE /pnlExit - Stop P&L Based Exit

**Task**
Implement the Dhan API v2 endpoint to disable/stop the configured P&L-based exit.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method:

    ```java
    public PnlExitResponse disablePnlExit()
    ```

2. **Javadoc Documentation**
   Write standard Javadoc referencing `DELETE https://api.dhan.co/v2/pnlExit`.

3. **Implement Using TDD**

**Model**
Reuse or create `PnlExitResponse` having **Lombok** annotations.

**API Details**

- HTTP Method: DELETE
- URL: `https://api.dhan.co/v2/pnlExit`
- No request body
- Use existing DELETE pattern from the SDK.

**Constraints**

- Implement only this method and tests.
