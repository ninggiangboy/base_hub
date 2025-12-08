#!/bin/bash
sleep 30

CONNECT_URL="http://localhost:8083/connectors"

for f in /kafka/connectors/*.json; do
  name=$(jq -r .name "$f")

  echo "Checking connector: $name"

  exists=$(curl -s -o /dev/null -w "%{http_code}" $CONNECT_URL/$name)

  if [ "$exists" = "200" ]; then
    echo "→ Connector '$name' existed, skip."
  else
    echo "→ Create connector '$name'"
    curl -X POST -H "Content-Type: application/json" --data @"$f" $CONNECT_URL
  fi
done

sleep infinity
