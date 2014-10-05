#!/bin/bash
source $1.devenv.properties
cd "$ARMSRACE_FRONTEND"
"$DART_SDK"/bin/pub build
cd -
cp -rf "$ARMSRACE_FRONTEND"/build/web ./src/main/webapp/static/
mvn clean install
mvn appengine:update
