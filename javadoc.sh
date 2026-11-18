#!/usr/bin/env bash
# Generate warning-free Javadoc HTML into docs/javadoc.
set -euo pipefail
cd "$(dirname "$0")"
JAVADOC=javadoc
if ! command -v javadoc >/dev/null 2>&1; then
  if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/javadoc" ]; then
    JAVADOC="$JAVA_HOME/bin/javadoc"
  else
    echo "javadoc not found: put it on PATH or set JAVA_HOME to your JDK folder."
    exit 1
  fi
fi
"$JAVADOC" -Xdoclint:all -d docs/javadoc -author -sourcepath src \
  -subpackages app:model:service:ui:test src/Main.java
