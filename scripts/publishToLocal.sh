#!/usr/bin/env bash
#Bash script for debug panel builds and publishing.

# Navigate up one directory to be outside of the bin directory and into the app's home directory
DIR="${0%/*}"
cd "$DIR/.."

# Clean all gradle build folders
./gradlew clean

# Build and publish Debug Panel artifacts
./gradlew debug-panel-core:publishReleasePublicationToMavenLocalRepository
./gradlew debug-panel-no-op:publishReleasePublicationToMavenLocalRepository
./gradlew debug-panel-common:publishReleasePublicationToMavenLocalRepository

# Build and publish plugin artifacts
./gradlew plugins:servers-plugin:publishReleasePublicationToMavenLocalRepository
./gradlew plugins:flipper-plugin:publishReleasePublicationToMavenLocalRepository
./gradlew plugins:accounts-plugin:publishReleasePublicationToMavenLocalRepository
./gradlew plugins:app-settings-plugin:publishReleasePublicationToMavenLocalRepository
./gradlew plugins:variable-plugin:publishReleasePublicationToMavenLocalRepository