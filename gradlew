#!/bin/sh
GRADLE_HOME=$(find "$HOME/.gradle/wrapper/dists/gradle-9.4.1-bin" -name "gradle-9.4.1" -type d 2>/dev/null | head -1)
exec "$GRADLE_HOME/bin/gradle" "$@"
