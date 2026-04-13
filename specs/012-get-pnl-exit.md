# Implementation Spec: GET /pnlExit - Get Configured P&L Based Exit

**Task**
Implement the Dhan API v2 endpoint to retrieve the current P&L-based exit configuration.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method:

    ```java
    public PnlExitResponse getPnlExitConfig()
    ```

2. **Javadoc Documentation**
   Write standard Javadoc referencing `GET https://api.dhan.co/v2/pnlExit`.

3. **Implement Using TDD**

**Model**
ReUse `PnlExitResponse`.

**API Details**

- HTTP Method: GET
- URL: `https://api.dhan.co/v2/pnlExit`
- No parameters.

**Constraints**

- Implement only this method and tests.
