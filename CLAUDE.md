# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Build & Test
```bash
# Build the project
mvn clean install

# Run all unit tests
mvn test

# Run integration tests
mvn -P test test

# Run specific unit test
mvn -Dtest=co.dhan.api.ondemand.OrderEndpointTest test

# Check test coverage
mvn jacoco:report
```

## Architecture

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
src/
  main/java/co/dhan/api/        # API endpoints
  main/java/co/dhan/http/       # HTTP utilities
  main/java/co/dhan/constant/   # Constants
  main/java/co/dhan/dto/        # Data transfer objects
  main/java/co/dhan/helper/    # Helper utilities
  main/java/co/dhan/           # Core classes

  test/java/co/dhan/api/        # Unit tests
  test/java/integration/        # Integration tests
```

## Important Notes

1. **Testing**: Always run tests before committing
2. **Coverage**: Aim for >80% test coverage
3. **Dependencies**: Use Maven for dependency management
4. **Environment**: Use `dev` profile for local development