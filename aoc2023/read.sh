#!/usr/bin/env zsh
set -euxo pipefail

d=$( echo $1 | sed "s/^0//")
filename="src/main/resources/day$1\$.txt"
curl https://adventofcode.com/2023/day/$d/input -H "cookie: session=$AOC_ID" > "$filename"

git add -u
git commit -m ":card_file_box: Year 2023 Day $1 - Added input of day"

open https://adventofcode.com/2023/day/$d
