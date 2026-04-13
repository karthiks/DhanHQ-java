# Implementation Spec: POST /pnlExit - Configure P&L Based Exit

**Task**
Implement the Dhan API v2 endpoint to configure P&L-based exit rules.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method to the TraderControlEndpoint.class:

    ```java
    public PnlExitResponse configurePnlExit(PnlExitRequest request)
    ```

2. **Javadoc Documentation**
   Write standard professional Javadoc referencing `POST https://api.dhan.co/v2/pnlExit`.

3. **Implement Using TDD**

**Model Requirements**

Create two POJO classes leveraging **Lombok** annotations:

- PnlExitRequest - for API Request

    ```java
       package co.dhan.dto;

       import co.dhan.constant.ProductType;
       import java.math.BigDecimal;
       import lombok.Builder;
       import lombok.Data;

       /**
       * Request object for configuring P&L-based exit rules.
       * Warning: In case of profitValue set below the current Profit in P&L, then the P&L based exit will be triggered immediately. This applies to lossValue set above the current Loss in P&L as well.
       */
       @Data
       @Builder
       public class PnlExitRequest {
          /** User-defined target profit amount for the P&L exit */
          private BigDecimal profitValue;

          /** User-defined target loss amount for the P&L exit */
          private BigDecimal lossValue;

          /** Indicates if the kill switch is enabled for this P&L exit */
          private boolean enableKillSwitch;

          /** Product types applicable for the P&L exit are only one of [INTRADAY, DELIVERY] */
          private ProductType productType;
       }
    ```

- PnlExitResponse – for the API response (status, message, etc.).

    ```java
       package co.dhan.dto;

       import co.dhan.constant.PnLExitStatus;
       import lombok.AllArgsConstructor;
       import lombok.Data;
       import lombok.NoArgsConstructor;

       /**
       * Response object for P&L exit configuration.
       */
       @Data
       @AllArgsConstructor
       @NoArgsConstructor
       public class PnlExitResponse {

       /** P&L based exit configured successfully - ACTIVE or INACTIVE */
       private PnLExitStatus pnlExitStatus;

       /** Response message from API - Status of Conditional Trigger */
       private String message;
       }
    ```

**API Details**

- HTTP Method: POST
- URL: `https://api.dhan.co/v2/pnlExit`
- Request body: JSON (`PnlExitRequest`)
- Response: Confirmation object
- Use the existing HTTP client and authentication.

**Constraints**

- Implement only this method, the new models, and the required tests.
