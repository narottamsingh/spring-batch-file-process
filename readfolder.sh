#!/bin/bash

# Define the base and destination directories
BASE_DIR="/home/narottam/testscript/o2/in"
DEST_DIR="/home/narottam/testscript/o2/out"

# Get the current date in YYYY-MM-DD format
CURRENT_DATE=$(date +"%Y-%m-%d")

# Loop through all directories in the base directory
for PARENT_DIR in "$BASE_DIR"/*; do
  # Check if the parent directory name contains the current date
  if [[ -d "$PARENT_DIR" && $(basename "$PARENT_DIR") == *"$CURRENT_DATE"* ]]; then
    echo "Found parent folder with today's date: $(basename "$PARENT_DIR")"
    
    # Set the parent folder name
    parentfolder=$(basename "$PARENT_DIR")
    
    # Loop through the subfolders of this parent folder
    for SUB_DIR in "$PARENT_DIR"/*; do
      if [[ -d "$SUB_DIR" ]]; then
        echo "  Subfolder: $(basename "$SUB_DIR")"
        
        # Set the subfolder name and zip folder name
        subfolder=$(basename "$SUB_DIR")
        zipfolder="$subfolder.zip"
        
        # Ensure destination folder structure exists
        mkdir -p "$DEST_DIR/$parentfolder"
        
        # Navigate to the subfolder
        cd "$SUB_DIR" || exit
        
        all_zip_files=()
        
        # Loop through all files in the subfolder
        for FILE in *; do
          if [[ -f "$FILE" ]]; then
            if [[ "$FILE" != *.json ]]; then
                all_zip_files+=("$FILE")
            else
             # add here conversion
             # remove origial .wav file
              all_zip_files+=("$FILE")
            fi
          fi
        done

        if [[ ${#all_zip_files[@]} -gt 0 ]]; then
          zip -r "$DEST_DIR/$parentfolder/$zipfolder" "${all_zip_files[@]}"
          echo "Zipped .json files from $subfolder into $zipfolder"
        else
          echo "No .json files found in $subfolder"
        fi

        cd "$BASE_DIR" || exit
      fi
    done
  fi
done
