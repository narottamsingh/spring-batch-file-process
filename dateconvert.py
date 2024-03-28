from datetime import datetime
import pytz

input_date_string = "2024-03-28T12:12:17.064791+00:00" 
utc_timezone = pytz.utc

# Convert the input string to a datetime object
utc_date = datetime.strptime(input_date_string[:-6], "%Y-%m-%dT%H:%M:%S.%f")

# Localize the datetime object to UTC timezone
utc_date = utc_timezone.localize(utc_date)

# Convert to the target timezone
target_timezone = pytz.timezone("America/New_York")
target_datetime = utc_date.astimezone(target_timezone)

# Format the converted date in desired format (YY-MM-DD)
formatted_datetime = target_datetime.strftime("%y-%m-%d %H:%M:%S")
formatted_datetime_hh_mm_ss = target_datetime.strftime("%y-%m-%d")

# Print the formatted date
print("Converted Date in America/New_York timezone (YY-MM-DD HH:MM:SS):", formatted_datetime)
# Print the formatted date
print("Converted Date in America/New_York timezone (YY-MM-DD):", formatted_datetime_hh_mm_ss)
