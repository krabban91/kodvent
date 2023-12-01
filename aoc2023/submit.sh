#!/usr/bin/env zsh
set -euxo pipefail

d=$( echo $1 | sed "s/^0//")
l=$2
r=$3
y=${4:-2023}
echo "{\"level\": $l, \"answer\": \"$r\"}" | curl https://adventofcode.com/$y/day/$d/answer -H "cookie: session=$AOC_ID"
res=$?
if test "$res" != "0"; then
  echo "failed with $res"
  exit $res
fi
git add -u
git commit -m ":sparkles: Year $y Day $1 - Completed part $l"

open https://adventofcode.com/$y/day/$d
