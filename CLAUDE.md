# CLAUDE.md

## Agent Instructions

- You are on WSL on Windows
- Always create a new feature branch for any code changes. Before starting work, run `git checkout -b <branch-name>` using a descriptive name based on the task. Never commit directly to main or master.
- When I report a bug, don't start by trying to fix it. Instead, start by writing a test that reproduces the bug. Then, let a subagent fix the bug and prove it by passing the test you wrote for this purpose.
- **Plan**: Always plan before execution. Keep it as concise, specific and explicit as possible. At the end of each plan, give me list of unresolved questions if any, for me to answer.
- **Style**: terse but complete and correct. Minimize tokens usage.

### Coding Rules

- Java 17+ (preferably 21), modular if possible (JPMS support later)
- Use modern Java patterns: records, sealed interfaces/classes, text blocks, switch expressions
- Immutability preferred — use records for DTOs where possible
- Always run `mvn spotless:apply` before commit (if Spotless is configured)
- Javadoc: complete for every public class/method, include usage examples
- Semantic Versioning — bump major on breaking changes only
- No new external dependencies without discussion
- Security: never log credentials, use secure random where needed
- **Logging**: SLF4J + sensible backend (logback-classic usually)
- **Error handling**: custom checked exceptions + Result-like pattern where appropriate
- **Dependencies**: Use Maven for dependency management
- **Environment**: Use `dev` profile for local development
- **Build**: Maven build (pom.xml in root)
- **Testing**: Always run unit and integration tests before committing. Use Use JUnit5 + AssertJ + Mockito libs.
- **Test Coverage**: Aim for >80% test coverage

### Typical tasks

- Adding new API endpoints (Orders, Positions, Market Data, WebSocket)
- Improving error mapping from Dhan responses
- Refactoring legacy code to records / modern patterns
- Writing integration tests with real credentials (never commit them!)
- Generating/updating README examples

### Review checklist before proposing merge/PR

- [ ] mvn clean verify passes
- [ ] Spotless applied
- [ ] Javadoc warnings clean
- [ ] No credential leaks
- [ ] Public API remains backward compatible (unless major bump)

## Commands

### Build & Test

```bash
# Build the project
mvn clean install

# Run all unit tests
mvn test

# Run specific unit test
mvn -Dtest=co.dhan.api.ondemand.OrderEndpointTest test

# Check test coverage
mvn jacoco:report
```

## Code Design

### Core Components

- **DhanCore**: Central hub connecting to all endpoints
- **OrderEndpoint**: Handles order placement/modification/cancellation
- **PortfolioEndpoint**: Manages holdings and positions
- **FundsEndpoint**: Provides financial limits and margin calculations
- **MarketQuotesEndpoint**: Fetches real-time market data

### Key Patterns

- Fluent API design for intuitive usage
- Domain-specific naming (e.g., `getCurrentHoldings()`)
- Separation of concerns across endpoints
- Comprehensive error handling with DhanAPIException

## Project Structure

```
src/main/java/co/dhan/api/        # API endpoints
src/main/java/co/dhan/http/       # HTTP utilities
src/main/java/co/dhan/constant/   # Constants
src/main/java/co/dhan/dto/        # Data transfer objects
src/main/java/co/dhan/helper/    # Helper utilities
src/main/java/co/dhan/           # Core classes

src/test/java/co/dhan/api/        # Unit tests
src/test/java/integration/        # Integration tests
```
