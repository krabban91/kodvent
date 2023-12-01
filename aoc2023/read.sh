#!/usr/bin/env zsh
set -euxo pipefail

d=$( echo $1 | sed "s/^0//")
curl https://adventofcode.com/2023/day/$d/input -H "cookie: session=$AOC_ID" > src/main/resources/day$1\$.txt

git add -u
git commit -m ":card_file_box: Year 2023 Day $1 - Added input of day"

open https://adventofcode.com/2023/day/$d
