#!/bin/sh

cd "$(dirname "$0")"
cd keycloak-9.0.2/bin
bash kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin
bash kcadm.sh create realms -f ../../keycloak_config.json
