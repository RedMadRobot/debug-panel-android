#!/usr/bin/env bash
#Bash script for debug panel builds and publishing.

# Navigate up one directory to be outside of the bin directory and into the app's home directory
DIR="${0%/*}"
cd "$DIR/.."

# Clean all gradle build folders
./gradlew clean

# Build and publish Debug Panel artifacts
./gradlew core:publishReleasePublicationToMavenLocalRepository
./gradlew no-op:publishReleasePublicationToMavenLocalRepository
./gradlew common:publishReleasePublicationToMavenLocalRepository

# Build and publish plugin artifacts
./gradlew plugins:servers:publishReleasePublicationToMavenLocalRepository
./gradlew plugins:flipper:publishReleasePublicationToMavenLocalRepository
./gradlew plugins:accounts:publishReleasePublicationToMavenLocalRepository
./gradlew plugins:app-settings:publishReleasePublicationToMavenLocalRepository
./gradlew plugins:variable:publishReleasePublicationToMavenLocalRepository