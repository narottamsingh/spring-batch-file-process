

#!/bin/bash

# Read the values from the secret
secretkey=testsecret
#$(cat /path/to/secret/username.txt)
accesskey=testaccess
#$(cat /path/to/secret/password.txt)

# Generate the XML content
xml_content="<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<configuration>
<properties>
<name>secret.key</name>
<value>$secretkey</value>
</properties>
<properties>
<name>access.key</name>
<value>$accesskey</value>
</properties>
</configuration>"

# Write the XML content to a file
echo "$xml_content" > config.xml

echo "XML file generated successfully."
