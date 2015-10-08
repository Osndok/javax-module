#!/bin/bash
#
# build.sh
#
# Official build products should have UTC times, and need proper 'version'.
#
VERSION=$(cat .version)

if $(dirname $0)/release.sh --build-needed ; then
	echo "WARNING: not on an official release tag"
	mvn package
else
	TZ=UTC mvn -Drelease.version=${VERSION} clean package install
fi
