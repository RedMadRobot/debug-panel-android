#!/usr/bin/env bash
#Bash script for debug panel builds and publishing.

# Navigate up one directory to be outside of the bin directory and into the app's home directory
cd ..

# Clean all gradle build folders
./gradlew clean

# Build Debug Panel artifacts
./gradlew debug-panel-core:build
./gradlew debug-panel-no-op:build
./gradlew debug-panel-common:build

# Build plugin artifacts
./gradlew server-plugin:build
./gradlew flipper-plugin:build
./gradlew accounts-plugin:build
./gradlew app-settings-plugin:build

# Publish all artifacts
./gradlew publishToMavenLocal
