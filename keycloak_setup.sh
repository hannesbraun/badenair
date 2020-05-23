#!/bin/sh

cd "$(dirname "$0")"
rm -r keycloak-9.0.2
tar -xzf keycloak-9.0.2.tar.gz
cd keycloak-9.0.2/bin
bash standalone.sh -b 0.0.0.0
