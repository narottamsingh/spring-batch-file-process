import pandas as pd

# Function to process Excel file
def process_excel(input_file, output_file, chunk_size=100):
    try:
        # Read the Excel file into a DataFrame
        df = pd.read_excel(input_file, header=None)  # Read without headers
        all_values = df.iloc[:, 0].tolist()  # Assuming data is in the first column

        # Open the output file for writing
        with open(output_file, "w") as file:
            for i in range(0, len(all_values), chunk_size):
                # Get a chunk of data
                chunk = all_values[i:i + chunk_size]
                # Convert chunk to a comma-separated string and write to file
                file.write(",".join(map(str, chunk)) + "\n")

        print(f"File '{output_file}' has been created successfully!")

    except Exception as e:
        print(f"An error occurred: {e}")

# Input and output files
input_file = "/home/narottam/Documents/testdata.xlsx"  # Replace with your Excel file name
output_file = "/home/narottam/Documents/output.txt"

# Process the file
process_excel(input_file, output_file)
