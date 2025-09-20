#!/bin/bash

# Script to fix CocoaPods compatibility with Xcode 16.4
# Temporarily changes project format from 70 to 56 for pod install

PROJECT_FILE="iosApp.xcodeproj/project.pbxproj"

# Backup original project file
cp "$PROJECT_FILE" "$PROJECT_FILE.backup"

# Change object version to 56 for CocoaPods compatibility
sed -i '' 's/objectVersion = 70;/objectVersion = 56;/g' "$PROJECT_FILE"

# Run pod install
LANG=en_US.UTF-8 LC_ALL=en_US.UTF-8 pod install

# Restore original project file
mv "$PROJECT_FILE.backup" "$PROJECT_FILE"

echo "Pod install completed successfully!"
