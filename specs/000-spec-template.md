# Spec: Implementation Spec: DELETE /positions - Exit All Positions

**Task**
Implement the Dhan API v2 endpoint to XXX.

**Requirements (strictly follow all of them):**

1. **Method Signature (Object-Oriented Style)**
   Add this method to the appropriate endpoint class:

    ```java
    public ReturnType yourMethodName(InputParam param)
    ```

2. **Javadoc Documentation**
   Write standard professional Javadoc including:
    - Clear description of the operation
    - Reference to the API endpoint: `DELETE https://api.dhan.co/v2/XXX`
    - Return value details
    - Exceptions

3. **Implement Using TDD**

4. **Integration Test (Happy Path)**
   Add one integration test (happy path only) that calls the real API and asserts the response status is "SUCCESS".

**Response Model**
Create a simple POJO named `CustomResponse` with the following fields (use Lombok annotations, getters, setters, and no-arg constructor, matching the style of other response models in the project):

```java
- String status        // e.g. "SUCCESS" or "FAILURE"
- String message       // descriptive message from the API
```

**API Details**

- HTTP Method: DELETE
- URL: `https://api.dhan.co/v2/xxx`
- No request body
- No parameters
- Example successful response:
    ```json
    {
        "status": "SUCCESS",
        "message": "All positions exited successfully"
    }
    ```
- Use the existing HTTP client and authentication pattern used by other DELETE methods (e.g. `cancelOrder`).

**Constraints**

- Implement only this method, the new response model, and the required tests.
- Do not change any other endpoints or classes.
- Follow the exact same error handling and coding style as the rest of the SDK.
