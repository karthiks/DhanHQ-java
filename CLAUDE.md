# CLAUDE.md

## Project

Java SDK for [Dhan API](https://dhanhq.co/docs/v2/)

Refer [Project Structure](./docs/PROJECT_STRUCTURE.md) to know how project is modularized.

## Commands

- Build Project: `mvn clean install`
- Run all Tests: `mvn test`
- Run specific Test class like `OrderEndpointTest`: `mvn -Dtest=co.dhan.api.ondemand.OrderEndpointTest test`
- Check Test Coverage: `mvn jacoco:report`

## Agent Instructions (Applies to ALL agents)

- You are on WSL on Windows
- If the current branch is `main` or `master`, always create a new feature branch prefixed with `ai-` as in `ai-random-name` before starting work and checkout to that branch.Use Git tool for this. Never commit directly to main or master branch.
- When I report a bug, don't start by trying to fix it. Instead, start by writing a test that reproduces the bug. Then, let a subagent fix the bug and prove it by passing the test you wrote for this purpose.
- **Plan**: Always plan before execution. Keep it as concise, specific and explicit as possible. At the end of each plan, give me list of unresolved questions if any, for me to answer.
- **Style**: terse but complete and correct. Minimize tokens usage.
- **MANDATORY**: Before writing any new Java code, grok this project specific coding standards as detailed in [Coding Style](./docs/JAVA_CODING_STYLE.md).
- Follow the TDD loop: Test -> Implement -> Verify.
- When compacting, always preserve:
  - The full list of modified files
  - Any test commands and their results
  - The current implementation plan
  - Error messages that haven't been resolved

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
