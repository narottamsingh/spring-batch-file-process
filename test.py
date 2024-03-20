import shutil

def copy_folder(source_folder, destination_folder):
    try:
        shutil.copytree(source_folder, destination_folder)
        print(f"Folder '{source_folder}' copied to '{destination_folder}' successfully.")
    except FileExistsError:
        print(f"Error: Destination folder '{destination_folder}' already exists.")

# Example usage
source_folder = 'source_folder'
destination_folder = 'destination_folder'

copy_folder(source_folder, destination_folder)







import requests

def download_jar_from_maven(repo_url, group_id, artifact_id, version, username, password, output_file):
    # Construct the Maven URL
    url = f"{repo_url}/{group_id.replace('.', '/')}/{artifact_id}/{version}/{artifact_id}-{version}.jar"

    # Send HTTP GET request with authentication
    response = requests.get(url, auth=(username, password))

    # Check if the request was successful (status code 200)
    if response.status_code == 200:
        # Save the JAR file
        with open(output_file, 'wb') as f:
            f.write(response.content)
        print(f"JAR file downloaded successfully: {output_file}")
    else:
        print(f"Failed to download JAR file. Status code: {response.status_code}")

# Example usage
repo_url = "https://repo.maven.apache.org/maven2"
group_id = "com.example"
artifact_id = "my-library"
version = "1.0.0"
username = "your_username"
password = "your_password"
output_file = "my-library.jar"

download_jar_from_maven(repo_url, group_id, artifact_id, version, username, password, output_file)
