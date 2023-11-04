#!/usr/bin/env zsh
set -euxo pipefail

d=$( echo $1 | sed "s/^0//")
curl https://adventofcode.com/2023/day/$d/input -H "cookie: session=$AOC_ID" > src/main/resources/day$1\$.txt
open https://adventofcode.com/2023/day/$d
