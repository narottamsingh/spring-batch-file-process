import os

def read_and_trim(file_path):
    """
    Reads a file line by line, trims each line to remove extra spaces,
    and returns a list of non-empty lines.
    """
    trimmed_lines = []
    with open(file_path, 'r') as file:
        for line in file:
            # Remove leading/trailing whitespace.
            trimmed_line = line.strip()
            if trimmed_line:
                trimmed_lines.append(trimmed_line)
    return trimmed_lines

def main():
    # Define input file names.
    input_files = [f"DEVOUT.{i}" for i in range(0, 2)]
    #input_files = [f"DEVOUT.{i}" for i in range(0, 1)]
    
    # Define the search file name.
    search_file = "DEVOUT.BIG"
    
    # Check that the search file exists.
    if not os.path.exists(search_file):
        print(f"Error: {search_file} not found.")
        return
    
    # Read and trim the search file records.
    search_records = read_and_trim(search_file)
    # Using a set for fast lookup.
    search_set = set(search_records)
    
    report_lines = []
    
    # Process each of the 10 input files.
    for file_name in input_files:
        if os.path.exists(file_name):
            # Read and trim the input file records.
            input_records = read_and_trim(file_name)
            for record in input_records:
                # Check if the record exists in the search set.
                result = "Yes" if record in search_set else "No"
                # Include the record and the result in the report.
                report_lines.append(f"{record}: {result}")
        else:
            # If an input file is missing, add a warning to the report.
            report_lines.append(f"Warning: {file_name} not found.")
    
    # Write the generated report to 'report.txt'.
    with open("report1.txt", "w") as report_file:
        for line in report_lines:
            report_file.write(line + "\n")
    
    print("Report generated in report.txt.")

if __name__ == "__main__":
    main()

