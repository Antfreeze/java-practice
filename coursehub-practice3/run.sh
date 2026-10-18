#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
rm -rf out
mkdir -p out
find src/main/java -name '*.java' -print0 | xargs -0 javac --release 21 -encoding UTF-8 -d out
LC_ALL=C.UTF-8 java -cp out coursehub.practice3.Main
