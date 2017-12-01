#!/bin/bash

# Drop all elections and POST new elections to CosmosDB, through API Gateway

url="http://api.voter-demo.coelection/elections"

elections=(
  '{"date":"2012-11-06","electionType":"FEDERAL","title":"2012 Presidential Election"}'
  '{"date":"2016-11-08","electionType":"FEDERAL","title":"2016 Presidential Election"}'
  '{"date":"2024-11-06","electionType":"STATE","title":"2018 New York Gubernatorial Election"}'
  '{"date":"2017-11-07","electionType":"LOCAL","title":"2017 New York City Mayoral Election"}'
)

echo "Dropping all existing election documents..."
curl --request POST \
 --url $url/drop \

echo ""

for election in "${elections[@]}"
do
  echo "POSTing $election"
  curl --request POST \
   --url $url \
   --header 'content-type: application/json' \
   --data "$election"
done
