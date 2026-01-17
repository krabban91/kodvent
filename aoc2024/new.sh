#!/usr/bin/env zsh
set -euxo pipefail

git stash
git checkout -b y2024d$1

cat rustvent/src/dayXX.rs | sed "s/XX/$1/" > rustvent/src/day$1.rs
cat rustvent/src/test/dayXXTest.rs | sed "s/XX/$1/" > rustvent/src/test/Day$1Test.rs
cat rustvent/input/real/dayXX.txt | sed "s/XX/$1/" > rustvent/input/real/day$1.txt
cat rustvent/input/test/dayXX.txt | sed "s/XX/$1/" > rustvent/input/test/day$1.txt

git add rustvent
git commit -m ":package: Year 2024 Day $1 preparations"
