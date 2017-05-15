#!/bin/sh

# Post new candidates
# Requires HTTPie
# Requires all services are running

set -e

HOST=${1:-localhost}
TEST_CYCLES=${2:-25}
URL="http://${HOST}:8095"

echo "POSTing new elections..."

TIME1=$(date +%s)

for i in $(seq ${TEST_CYCLES})
do
  # TIMEA=`expr $(date +%s%N) / 1000000`

  http POST ${URL}/election/elections \
    date='2008-11-04' \
    electionType='FEDERAL' \
    title='2008 Presidential Election'

  http POST ${URL}/election/elections \
    date='2014-11-04' \
    electionType='LOCAL' \
    title='2014 Washington, D.C. Mayoral Election'

  http POST ${URL}/election/elections \
    date='2014-11-04' \
    electionType='FEDERAL' \
    title='2014 US Senate Election, State of Alaska'

  # TIMEB=`expr $(date +%s%N) / 1000000`
  # TIMEC=`expr ${TIMEB} - ${TIMEA}`
  # echo "${TIMEC} ms for 3 HTTP POST requests"
done

TIME2=$(date +%s)

TIME3=`expr ${TIME2} - ${TIME1}`
TESTS=`expr ${TEST_CYCLES} \* 3`

echo ""
echo "${TIME3} seconds for ${TEST_CYCLES} test cycles, or ${TESTS} HTTP POST requests"
echo ""
echo "Script completed..."
