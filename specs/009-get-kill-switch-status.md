# Implementation Spec for Trader Controls: GET /killswitch - Get Kill Switch Status

**Task**
Implement the Dhan API v2 endpoint to retrieve the current status of the Kill Switch (Trader's Control).

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method to the appropriate endpoint class (e.g., the class handling trader control or a new `TraderControlEndpoint`):

    ```java
    public KillSwitchStatus getKillSwitchStatus()
    ```

2. **Javadoc Documentation**
   Write standard professional Javadoc referencing `GET https://api.dhan.co/v2/killswitch`

3. **Implement Using TDD**
    - Ensure the test verifies correct HTTP method, URL, headers, and JSON deserialization.

**Model Requirements**

Create a new POJO model class named `KillSwitchStatus` and leverage Lombok annotations:

- Use `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, and `@Builder`.
- Include fields returned by the API (e.g., `status`, `enabled`, `reason`, timestamps, etc.).

**API Details**

- HTTP Method: GET
- URL: `https://api.dhan.co/v2/killswitch`
- No request body
- No query parameters
- Response: JSON object with kill switch status details
- Use the existing HTTP client, authentication mechanism, and error handling used by other GET methods in the SDK.

**Constraints**

- Do not modify any other endpoints or existing methods.
- Reuse existing utility classes for HTTP calls and JSON parsing.
- Handle standard Dhan API error responses gracefully.
