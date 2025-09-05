#!/bin/bash

# -----------------------------
# Configuration
# -----------------------------
FLUME_AGENT_NAME="agent1"
CHECK_INTERVAL=5       # seconds between checks
KILL_HOUR=18           # 24h format
KILL_MINUTE=22
# -----------------------------

while true; do
  HOUR=$(date +%H)
  MINUTE=$(date +%M)

  if [ "$HOUR" -eq "$KILL_HOUR" ] && [ "$MINUTE" -eq "$KILL_MINUTE" ]; then
    echo "[$(date)] Scheduled time reached. Stopping Flume agent $FLUME_AGENT_NAME gracefully..."

    # Find Flume PID by searching for "--name agent1" anywhere in command
    FLUME_PID=$(ps -ef | grep -- "org.apache.flume.node.Application" | grep -- "--name $FLUME_AGENT_NAME" | grep -v grep | awk '{print $2}')

    if [ -n "$FLUME_PID" ]; then
      echo "[$(date)] Sending SIGTERM to PID $FLUME_PID"
      kill -15 $FLUME_PID
      echo "[$(date)] Flume agent $FLUME_AGENT_NAME stopped gracefully."
    else
      echo "[$(date)] Flume agent $FLUME_AGENT_NAME is not running."
    fi

    # Sleep 60s to avoid repeating in the same minute
    sleep 60
  fi

  sleep $CHECK_INTERVAL
done
