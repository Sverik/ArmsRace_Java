#!/bin/bash

source $1.devenv.properties

cd "$ARMSRACE_FRONTEND"
"$DART_SDK"/bin/pub build
cd -

rm -rf ./src/main/webapp/static/*
cp -r "$ARMSRACE_FRONTEND"/build/web/* ./src/main/webapp/static/

mvn clean install

if [ "update" == $2 ]
then
	mvn appengine:update
else
	mvn appengine:devserver
fi
