import json
import csv

# Read JSON data from sample.json
with open("sample.json", "r") as json_file:
    data = json.load(json_file)

# Open CSV file for writing
with open("monitoring_groups.csv", mode="w", newline="") as file:
    writer = csv.writer(file)
    writer.writerow(["key", "MonitoringGroup"])

    # Loop through each JSON object
    for item in data:
        key = item.get("key", "")
        monitoring_groups = item.get("properties", {}).get("MonitoringGroup", [])
        for group in monitoring_groups:
            writer.writerow([key, group])
