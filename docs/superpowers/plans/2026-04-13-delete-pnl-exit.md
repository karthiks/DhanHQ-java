# DELETE /pnlExit - Stop P&L Based Exit Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement the DELETE /pnlExit endpoint to disable/stop configured P&L-based exit in the Dhan Java SDK.

**Architecture:** Follow existing patterns in TraderControlEndpoint for HTTP DELETE requests, reusing PnlExitResponse model, and implementing comprehensive unit tests using Mockito.

**Tech Stack:** Java, Maven, JUnit 5, Mockito, AssertJ, Lombok

---

### Task 1: Add disablePnlExit method signature to TraderControlEndpoint

**Files:**
- Modify: `src/main/java/co/dhan/api/ondemand/TraderControlEndpoint.java`

- [ ] **Step 1: Add method signature**

```java
  /**
   * Disable/stop P&L based exit rules.
   *
   * <p>Endpoint: DELETE https://api.dhan.co/v2/pnlExit
   *
   * @return PnlExitResponse object containing the status and message
   * @throws DhanAPIException if the API request fails
   */
  public PnlExitResponse disablePnlExit() throws DhanAPIException {
    // TODO: Implement
  }
```

- [ ] **Step 2: Run tests to verify method exists**

Run: `mvn test -Dtest="TraderControlEndpointTest"`

Expected: Compilation should succeed (method exists but returns null/default)

### Task 2: Implement disablePnlExit method

**Files:**
- Modify: `src/main/java/co/dhan/api/ondemand/TraderControlEndpoint.java`

- [ ] **Step 1: Write failing test**

```java
@Test
void disablePnlExit_ReturnResultSuccessfully() {
  PnlExitResponse expectedResponse =
      new PnlExitResponse(PnLExitStatus.INACTIVE, "P&L exit disabled successfully");
  
  when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
  when(mockDhanHTTP.doHttpDeleteRequest(eq("/pnlExit"))).thenReturn(mockDhanResponse);
  when(mockDhanResponse.convertToType(PnlExitResponse.class)).thenReturn(expectedResponse);

  assertThat(traderControlEndpoint.disablePnlExit())
      .usingRecursiveComparison()
      .isEqualTo(expectedResponse);
  verify(mockDhanHTTP).doHttpDeleteRequest(eq("/pnlExit"));
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn test -Dtest="TraderControlEndpointTest#disablePnlExit_ReturnResultSuccessfully"`

Expected: TEST FAILS with "org.opentest4j.AssertionFailedError: expecting null to not be null"

- [ ] **Step 3: Write minimal implementation**

```java
  public PnlExitResponse disablePnlExit() throws DhanAPIException {
    DhanResponse dhanResponse =
        dhanConnection.getDhanHTTP().doHttpDeleteRequest(APIEndpoint.ConfigurePnlExit);
    return dhanResponse.convertToType(PnlExitResponse.class);
  }
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn test -Dtest="TraderControlEndpointTest#disablePnlExit_ReturnResultSuccessfully"`

Expected: TEST PASSES

- [ ] **Step 5: Commit**

```bash
git add src/main/java/co/dhan/api/ondemand/TraderControlEndpoint.java
git add src/test/java/co/dhan/api/ondemand/TraderControlEndpointTest.java
git commit -m "feat: add disablePnlExit method to TraderControlEndpoint"
```

### Task 3: Verify complete functionality

**Files:**
- Modify: `src/test/java/co/dhan/api/ondemand/TraderControlEndpointTest.java` (add test)

- [ ] **Step 1: Run all TraderControlEndpoint tests**

Run: `mvn test -Dtest="TraderControlEndpointTest"`

Expected: ALL TESTS PASS including new disablePnlExit test

- [ ] **Step 2: Commit final changes**

```bash
git commit -am "test: verify complete disablePnlExit functionality"
```