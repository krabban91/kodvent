#!/usr/bin/env zsh
set -euxo pipefail

d=$( echo $1 | sed "s/^0//")
filename="rustvent/input/real/day$1.txt"
curl https://adventofcode.com/2024/day/$d/input -H "cookie: session=$AOC_ID" > "$filename"

open https://adventofcode.com/2024/day/$d
