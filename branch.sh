#!/bin/bash

# Get the current branch name
branch_name=$(git rev-parse --abbrev-ref HEAD)
branch_name=dev

# Determine the version based on the branch name
case "$branch_name" in
  dev)
    version="dev-$branch_name-release"
    ;;
  qa)
    version="qa-$branch_name-release"
    ;;
  prod)
    version="prod-$branch_name-release"
    ;;
  *)
    version="unknown-$branch_name-release"
    ;;
esac

# Output the version
echo "Branch: $branch_name"
echo "Version: $version"

# Optionally, update a file or configuration with the version
# For example, updating application.yml
# sed -i "s/version-placeholder/$version/" application.yml

