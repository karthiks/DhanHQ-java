---
name: dhan-code-style
description: Learn and apply Dhan coding conventions when reading, reviewing, editing and writing code
invokable_by_user: true
invokable_by_claude: true
---

When Reading, Reviewing, Editing and Writing code, be aware of Dhan Coding Style and Conventions below to follow it strictly.

# Coding Style And Practices

- Java 17+ (preferably 21), modular if possible (JPMS support later)
- Use modern Java patterns: records, sealed interfaces/classes, text blocks, switch expressions
- Immutability preferred — use records for DTOs where possible
- Always run `mvn spotless:apply` before commit (if Spotless is configured)
- Javadoc: complete for every public class/method, include usage examples
- Semantic Versioning — bump major on breaking changes only
- No new external dependencies without discussion
- Fix all critical and high-severity CVE vulnerabilities in this project by invoking #validate_cves_for_java
- Security: never log credentials, use secure random where needed
- Check pom.xml for dependencies to know which library to use for coding.
- **Logging**: SLF4J + sensible backend (logback-classic usually)
- **Dependencies**: Use Maven for dependency management
- **Environment**: Use `dev` profile for local development
- **Build**: Maven build (pom.xml in root)
- **Testing**: Always run unit and integration tests before committing. Use Use JUnit5 + AssertJ + Mockito libs.

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

## Code Style

- **Package structure**: `co.dhan.*` (api, constant, dto, http, etc.)
- **API Hierarchy Pattern**: `DhanCore.get[Endpoint]Endpoint().action()`
- **Constants**: Use enums in `co.dhan.constant` package
- **Error Handling**: Wrap API errors in `DhanAPIException`

## Testing Standards

- **Unit test naming**: `*Test.java`
- **Integration test naming**: `*IT.java`
- **Coverage target**: >80% on core logic
