#!/usr/bin/env bash
#Bash script for debug panel builds and publishing.

# Navigate up one directory to be outside of the bin directory and into the app's home directory
DIR="${0%/*}"
cd "$DIR/.."

# Clean all gradle build folders
./gradlew clean

# Build and publish Debug Panel artifacts
./gradlew core:publishReleasePublicationToOSSRHRepository
./gradlew no-op:publishReleasePublicationToOSSRHRepository
./gradlew common:publishReleasePublicationToOSSRHRepository

# Build and publish plugin artifacts
./gradlew plugins:servers:publishReleasePublicationToOSSRHRepository
./gradlew plugins:flipper:publishReleasePublicationToOSSRHRepository
./gradlew plugins:accounts:publishReleasePublicationToOSSRHRepository
./gradlew plugins:app-settings:publishReleasePublicationToOSSRHRepository
./gradlew plugins:variable:publishReleasePublicationToOSSRHRepository