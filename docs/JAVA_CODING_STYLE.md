# Coding Style And Practices

- Java 17+ (preferably 21), modular if possible (JPMS support later)
- Use modern Java patterns: records, sealed interfaces/classes, text blocks, switch expressions
- Immutability preferred — use records for DTOs where possible
- Always run `mvn spotless:apply` before commit (if Spotless is configured)
- Javadoc: complete for every public class/method, include usage examples
- Semantic Versioning — bump major on breaking changes only
- No new external dependencies without discussion
- Security: never log credentials, use secure random where needed
- Check pom.xml for dependencies to know which library to use for coding.
- **Logging**: SLF4J + sensible backend (logback-classic usually)
- **Dependencies**: Use Maven for dependency management
- **Environment**: Use `dev` profile for local development
- **Build**: Maven build (pom.xml in root)
- **Testing**: Always run unit tests before committing. Use Use JUnit5 + AssertJ + Mockito libs.
- Use **Lombok** annotations where possible for code brevity, especially in DTOs and Constants.

## Code Design

### Core Components

- **co.dhan.api.DhanCore**: Central hub connecting to all endpoints
- **co.dhan.api.DhanConnection**: to connect to Dhan REST API server
- **co.dhan.api.ondemand**: is java package containing classes to connect to Dhan via REST endpoints
    - All endpoint classes have 2 key interfaces defined within it:
        - `APIEndpoint` containing constants referencing REST Endpoint URIs.
        - `APIParam` containing constants referencing Input parameters to be used in JSON translation of POJO. This JSON becomes the payload for REST API.
- **co.dhan.api.stream**: is java package containing classes to connect to Dhan via its streaming endpoints leveraging Websockets
- **co.dhan.constant**: is java package containing various constants grouped by relevant Java classes.
- **co.dhan.dto**: is java package containing various DTOs (Data Transfer Objects aka Models) to translate InputParams or JSON Response to Java Object encapsulating OO principles. We use DTOs instead of Models because we are building an SDK library.
- **co.dhan.helper**: is java package containing various functional utility functions grouped by relevant Java classes.
- **co.dhan.http**: is java package containing all HTTP request or response related classes including all Exceptions, and HTTP specific utilities like Deserializers and Transformers.
- **OrderEndpoint**: Handles order placement/modification/cancellation
- **PortfolioEndpoint**: Manages holdings and positions
- **FundsEndpoint**: Provides financial limits and margin calculations
- **MarketQuotesEndpoint**: Fetches real-time market data

### Key Patterns

- Fluent API design for intuitive usage
- Domain-specific naming (e.g., `getCurrentHoldings()`)
- Separation of concerns across endpoints
- Comprehensive error handling with DhanAPIException

## Code Style

- **Package structure**: `co.dhan.*` (api, constant, dto, http, etc.)
- **API Hierarchy Pattern**: `DhanCore.get[Endpoint]Endpoint().action()`
- **Constants**: Use enums in `co.dhan.constant` package
- **Error Handling**: Wrap API errors in `DhanAPIException`

## Testing Standards

| Test Type            | Test File Naming Convention | Source Directory            | Resource Directory   |
| -------------------- | --------------------------- | --------------------------- | -------------------- |
| **Unit test**        | `*Test.java`                | `src/test/java`             | `src/test/resources` |
| **Integration test** | `ITest_*.java`              | `src/test-integration/java` | `src/test/resources` |
| **Manual test**      | `ManualTest_*.java`         | `src/test-manual/java`      | `src/test/resources` |

- Ensure Unit Test code coverage is greater than 80%
