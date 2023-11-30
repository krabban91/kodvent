#!/usr/bin/env zsh
set -euxo pipefail

d=$( echo $1 | sed "s/^0//")
l=$2
r=$3
echo "{\"level\": $l, \"answer\": \"$r\"}" | curl -f https://adventofcode.com/2023/day/$d/answer -H "cookie: session=$AOC_ID"

git add -u
git commit -m ":sparkles: Year 2023 Day $1 - Completed part $l"

open https://adventofcode.com/2023/day/$d
