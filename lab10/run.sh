#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

mvn -q compile
java -cp target/classes "lab10.simple.${1:-Main}"
