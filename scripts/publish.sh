#!/usr/bin/env bash
#Bash script for debug panel builds and publishing.

# Navigate up one directory to be outside of the bin directory and into the app's home directory
DIR="${0%/*}"
cd "$DIR/.."

# Clean all gradle build folders
./gradlew clean

# Build and publish Debug Panel artifacts
./gradlew debug-panel-core:publishReleasePublicationToOSSRHRepository
./gradlew debug-panel-no-op:publishReleasePublicationToOSSRHRepository
./gradlew debug-panel-common:publishReleasePublicationToOSSRHRepository

# Build and publish plugin artifacts
./gradlew plugins:servers-plugin:publishReleasePublicationToOSSRHRepository
./gradlew plugins:flipper-plugin:publishReleasePublicationToOSSRHRepository
./gradlew plugins:accounts-plugin:publishReleasePublicationToOSSRHRepository
./gradlew plugins:app-settings-plugin:publishReleasePublicationToOSSRHRepository
./gradlew plugins:variable-plugin:publishReleasePublicationToOSSRHRepository