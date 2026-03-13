---
name: add-new-endpoint
invokable_by_user: true
invokable_by_claude: true
---

High-level workflow for adding a new DhanHQ API endpoint:

1. Ask user which endpoint (e.g. "Place Order", "Market Depth", "WebSocket OHLC")
2. Spawn api-endpoint subagent in worktree
3. Spawn test-coverage subagent in parallel worktree (for tests)
4. Spawn javadoc subagent if needed
5. After both finish → propose consolidated diff / merge plan
6. Remind to never commit credentials
