import os

def read_and_trim(file_path):
    """
    Reads a file line by line, trims each line to remove leading and trailing spaces,
    and returns a list of non-empty lines.
    """
    trimmed_lines = []
    with open(file_path, 'r') as file:
        for line in file:
            trimmed_line = line.strip()
            if trimmed_line:
                trimmed_lines.append(trimmed_line)
    return trimmed_lines

def main():
    # Define the search file name.
    search_file = "DEVOUT.BIG"
    
    # Define input file names.
    input_files = [f"DEVOUT.{i}" for i in range(0, 2)]
    
    # Check that the search file exists.
    if not os.path.exists(search_file):
        print(f"Error: {search_file} not found.")
        return
    
    # Read and trim the search file records.
    search_records = read_and_trim(search_file)
    
    # Initialize a set to hold all unique records from input files.
    input_records_set = set()
    
    # Process each of the 10 input files.
    for file_name in input_files:
        if os.path.exists(file_name):
            # Read and trim the input file records.
            input_records = read_and_trim(file_name)
            # Add records to the set.
            input_records_set.update(input_records)
        else:
            print(f"Warning: {file_name} not found.")
    
    # Prepare the report lines.
    report_lines = []
    for record in search_records:
        # Check if the record exists in the combined input records set.
        result = "Yes" if record in input_records_set else "No"
        # Append the result to the report.
        report_lines.append(f"{record}: {result}")
    
    # Write the generated report to 'report.txt'.
    with open("report2.txt", "w") as report_file:
        for line in report_lines:
            report_file.write(line + "\n")
    
    print("Report generated in report.txt.")

if __name__ == "__main__":
    main()

