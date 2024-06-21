#!/bin/bash

# Delete the output directory and its contents
rm -r output/

# Compile all three codebases
mvn clean install

# Execute the app host to generate the aspire-manifest.json and bicep files
mvn -f storage-explorer-apphost/pom.xml exec:java

# Feed these files into azd
