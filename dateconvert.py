from datetime import datetime
import pytz

def convert_timezone(input_date_string):
    try:
        # Define the UTC timezone
        utc_timezone = pytz.utc
        
        # Convert the input string to a datetime object
        utc_date = datetime.strptime(input_date_string[:-6], "%Y-%m-%dT%H:%M:%S.%f")
        
        # Localize the datetime object to UTC timezone
        utc_date = utc_timezone.localize(utc_date)
        
        # Convert to the target timezone
        target_timezone = pytz.timezone("America/New_York")
        target_datetime = utc_date.astimezone(target_timezone)
        formatted_datetime = target_datetime.strftime("%Y-%m-%d")
        return formatted_datetime
    except Exception as e:
        print("Error occurred:", e)
        return None


input_date_string = "2024-03-28T12:12:17.064791+00:00"  
converted_datetime = convert_timezone(input_date_string)
print("Converted Date in America/New_York timezone (YYYY-MM-DD):", converted_datetime)
