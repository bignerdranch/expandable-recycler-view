#!/bin/bash

set +x

#### BUILD
if [[ "$TRAVIS_PULL_REQUEST" != "false" ]]; then
  echo "PR build, running tests"
  ./gradlew clean test
  exit $?
fi
