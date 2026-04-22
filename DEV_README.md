# README for Developers of this project

## Cheat-sheet

### Syntax To export & verify env variable in Powershell
```
$env:MY_VAR="HelloWorld"
echo $env:MY_VAR
Get-ChildItem Env
```

### Syntax To export & verify env variable in Ubuntu Shell
```
# Print all environment variables
printenv
# Print the PATH variable
printenv PATH
# Print user-specific variable
printenv USER

export VARIABLE_NAME="value"
echo $VARIABLE_NAME
```

## Leveraging Claude Code with Docker using docker-compose

Run this in PowerShell from your project root:

```powershell
Get-ChildItem Env
$env:ANTHROPIC_BASE_URL="https://openrouter.ai/api";$env:ANTHROPIC_API_KEY="";$env:ANTHROPIC_AUTH_TOKEN="your-api-token"

docker compose build 

# To create non-persistable session-oriented container that is destroyed when you log out of the session.
docker compose run --rm cc4dhanjsdk

# To create a container that persists across sessions and is NOT destroyed when you log out of the container.
docker compose run cc4dhanjsdk
```

Within docker container run below commands:
```zsh
# Run CC with Worktree enabled
claude --workree 
cldw #alias 

# Run CC without worktree
claude
cld #alias
```

