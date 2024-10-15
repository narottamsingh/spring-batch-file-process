#!/bin/bash

# Define the base directory where the folders are located
BASE_DIR="/home/narottam/testscript/o2/in"

# Get the current date in YYYY-MM-DD format
CURRENT_DATE=$(date +"%Y-%m-%d")

# Loop through all directories in the base directory
for PARENT_DIR in "$BASE_DIR"/*; do
  # Check if the parent directory name contains the current date
  if [[ -d "$PARENT_DIR" && $(basename "$PARENT_DIR") == *"$CURRENT_DATE"* ]]; then
    echo "Found parent folder with today's date: $(basename "$PARENT_DIR")"
    
    # Loop through the subfolders of this parent folder
    for SUB_DIR in "$PARENT_DIR"/*; do
      if [[ -d "$SUB_DIR" ]]; then
        echo "  Subfolder: $(basename "$SUB_DIR")"
        
        # Loop through all files in this subfolder
        for FILE in "$SUB_DIR"/*; do
          if [[ -f "$FILE" ]]; then
            echo "    File: $(basename "$FILE")"
          fi
        done
      fi
    done
  fi
done
