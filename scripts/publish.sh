#!/usr/bin/env bash
#Bash script for debug panel builds and publishing.

# Navigate up one directory to be outside of the bin directory and into the app's home directory
DIR="${0%/*}"
cd "$DIR/.."

# Clean and then build and publish Debug Panel artifacts
./gradlew clean publishToSonatype closeAndReleaseSonatypeStagingRepository
