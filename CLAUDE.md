# CLAUDE.md

## Project

Java SDK for [Dhan API](https://dhanhq.co/docs/v2/)

## Commands

- Build Project: `mvn clean install`
- Run all Unit Tests: `mvn test`
- Run specific Unit Test class like `OrderEndpointTest`: `mvn -Dtest=co.dhan.api.ondemand.OrderEndpointTest test`
- Code formatting: `mvn spotless:apply`
- Check Test Coverage: `mvn jacoco:report`

## Agent Instructions (Applies to ALL agents)

- You are on WSL on Windows
- If the current branch is `main` or `master`, always create a new feature branch prefixed with `ai-` as in `ai-random-name` before starting work and checkout to that branch.Use Git tool for this. Never commit directly to main or master branch.
- When I report a bug, don't start by trying to fix it. Instead, start by writing a test that reproduces the bug. Then, let a subagent fix the bug and prove it by passing the test you wrote for this purpose.
- **Plan**: Always plan before execution. Keep it as concise, specific and explicit as possible. At the end of each plan, give me list of unresolved questions if any, for me to answer.
- **Style**: terse but complete and correct. Minimize tokens usage.
- **MANDATORY**: Before starting on any code related tasks:
    - refer [Project Structure](./docs/PROJECT_STRUCTURE.md) to know how project is modularized
    - refer project specific coding standards as detailed in [Coding Style](./docs/JAVA_CODING_STYLE.md).
- Follow the TDD loop: Test -> Implement -> Verify.
- When compacting, always preserve:
    - The full list of modified files
    - Any test commands and their results
    - The current implementation plan
    - Error messages that haven't been resolved
- **Discovery Trigger:** Whenever you encounter a non-obvious bug, a specific fix for a dependency issue, or a unique architectural pattern, you MUST suggest an update to the [Project Learnings](./CLAUDE.md#project-learnings) section of this file.
- **Session Handover:** At the end of a complex task, summarize what was learned and ask if you should update `CLAUDE.md` to ensure the next session (or a teammate) starts with this context.
- **Autonomous Documentation:** You are authorized to create or update files in the `.claude/rules/` directory to document sub-system specific logic (e.g., `.claude/rules/database.md`) when you find recurring patterns.
- **Tooling Constraints:** If a specific test command or build flag is found to be necessary for this environment, record it immediately in the "Build & Test" section.

### Typical tasks

- Adding new API endpoints (Orders, Positions, Market Data, WebSocket)
- Improving error mapping from Dhan responses
- Refactoring legacy code to records / modern patterns
- Writing integration tests with real credentials (never commit them!)
- Generating/updating README examples

### Review checklist before proposing merge/PR

- [ ] Table in `SDK API Cheatsheet` section of README.md file is updated for any public API changes in SDK. No other sections of the files are modified without explicit consent.
- [ ] mvn clean verify passes
- [ ] Spotless applied
- [ ] Javadoc warnings clean
- [ ] No credential leaks
- [ ] Public API remains backward compatible (unless major bump)

### Project Learnings
