#!/bin/bash

# Define the base and destination directories
BASE_DIR="/home/narottam/testscript/o2/in"
DEST_DIR="/home/narottam/testscript/o2/out"

# Get the current date in YYYY-MM-DD format
CURRENT_DATE=$(date +"%Y-%m-%d")

# Define temporary directory in /tmp for .csv and .json files
TMP_DIR="/home/narottam/testscript/o2/tmp_$CURRENT_DATE"

zip_file_count=0
json_file_count=0

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
        
        # Ensure destination and temporary folder structures exist
        mkdir -p "$DEST_DIR/$parentfolder"
        mkdir -p "$TMP_DIR/$parentfolder/$subfolder"
        
        # Initialize an array to hold json files for zipping
        json_files=()
        
        # Loop through all files in the subfolder
        for FILE in "$SUB_DIR"/*; do
          if [[ -f "$FILE" ]]; then
            # Move non-JSON files
            if [[ "$FILE" != *.json ]]; then
              echo "    Non-JSON file: $FILE"
              cp "$FILE" "$TMP_DIR/$parentfolder/$subfolder"
              ((zip_file_count++))
            else
              # Move JSON files and add them to the list for zipping
              echo "    JSON file: $FILE"
              cp "$FILE" "$TMP_DIR/$parentfolder/$subfolder"
              ((json_file_count++))
            fi
          fi
        done
        
        # Zip all files in the temporary directory if any files were moved
        if [[ -n "$(ls -A "$TMP_DIR/$parentfolder/$subfolder")" ]]; then
          cd "$TMP_DIR/$parentfolder/$subfolder" || exit
          zip -r "$DEST_DIR/$parentfolder/$zipfolder" .
          echo "Zipped files from $subfolder into $zipfolder"
          cd - || exit
        else
          echo "No files found in $subfolder to zip"
        fi

        # Navigate back to the base directory
        cd "$BASE_DIR" || exit
      fi
    done
  fi
  
  echo "Zip file count: $zip_file_count"
  echo "JSON file count: $json_file_count"
done

# Clean up: delete the temporary directory
#rm -rf "$TMP_DIR"
echo "Temporary directory deleted: $TMP_DIR"
