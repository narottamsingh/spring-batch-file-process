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
        
        # Initialize an array to hold json files
        json_files=()
        
        # Loop through all files in the subfolder
        for FILE in *; do
          if [[ -f "$FILE" ]]; then
            # Print the name of non-JSON files
            if [[ "$FILE" != *.json ]]; then
              echo "    Non-JSON file: $FILE"
            else
              # Add JSON files to the array
              json_files+=("$FILE")
            fi
          fi
        done

        # Zip only .json files if there are any
        if [[ ${#json_files[@]} -gt 0 ]]; then
          zip -r "$DEST_DIR/$parentfolder/$zipfolder" "${json_files[@]}"
          echo "Zipped .json files from $subfolder into $zipfolder"
          
          # Iterate over each .json file and delete it
          for json_file in "${json_files[@]}"; do
            if [[ "$json_file" == *.json ]]; then
              rm -f "$json_file"
              echo "Deleted .json file: $json_file"
            fi
          done
        else
          echo "No .json files found in $subfolder"
        fi

        # Navigate back to the base directory
        cd "$BASE_DIR" || exit
      fi
    done
  fi
done
