#!/usr/bin/env bash
# Compile all sources into out/ and run the 8-case plain-Java test runner.
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p out
javac -d out \
  src/app/AppInfo.java \
  src/Main.java \
  src/model/BookStatus.java \
  src/model/Book.java \
  src/service/LibraryException.java \
  src/service/LibraryService.java \
  src/ui/LibraryFrame.java \
  src/test/LibraryServiceTest.java
java -cp out test.LibraryServiceTest
