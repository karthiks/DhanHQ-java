# This is only used for developing the zsh-in-docker script, but can be used as an example.
# Build:
# docker build -f Dockerfile.zsh -t node-custom-user .
# Run n Verify:
# docker run -it --rm node-custom-user whoami

FROM node:20-bookworm-slim

# Build arguments for user configuration
# On Windows with Docker Desktop (especially using WSL2 backend), file permissions are mapped based on UID/GIDs.
# The default first user in WSL2 is typically UID 1000. If you use UID 2000 inside the container, files created by the container will have owner UID 2000, which on the WSL2 host will appear as an unknown user (or a different user), leading to permission issues when you try to edit those files from the host.
# Better approach: Reuse UID 1000 cleanly by renaming the existing node user (which already has UID 1000) to your desired username, instead of deleting it or creating a new user with a different UID.
# ARG USER_UID=1000
# ARG USER_GID=1000
ARG USERNAME=ccagent

# ============================================
# Step 1: Install System Dependencies
# ============================================
RUN apt-get update && apt-get install -y --no-install-recommends \
    zsh \
    wget curl git vim jq ca-certificates \
    python3 python3-pip python3-venv python3-dev build-essential \
    sudo \
    ripgrep \
    && rm -rf /var/lib/apt/lists/*

# ============================================
# Step 2: Create User with Sudo Access
# Reuse existing 'node' user (UID 1000) by renaming it to $USERNAME and setting zsh
# ============================================

# 2.1. Rename the group and the user
RUN groupmod -n $USERNAME node \
    && usermod -l $USERNAME -m -d /home/ccagent node \
    # Add user to sudoers with NOPASSWD
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

# 2.2. Update environment variables
ENV NODE_USER=$USERNAME

# 3.3 Set zsh as the system default for the non-root user (adjust user name as needed)
RUN chsh -s $(which zsh) $USERNAME

# ============================================
# Step 3: Setup Workspace
# ============================================
USER $USERNAME
WORKDIR /workspace
RUN chown $USERNAME:$USERNAME /workspace

# Create a small bashrc that hands interactive bash sessions over to zsh
RUN echo 'if [ -t 1 ]; then\n  exec /bin/zsh\nfi' > /home/$USERNAME/.bashrc

ENTRYPOINT [ "/bin/zsh" ]
CMD ["-l"]