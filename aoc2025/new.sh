#!/usr/bin/env zsh
set -euxo pipefail

git stash
git checkout -b y2025d$1

cat src/main/scala/DayXX.scala | sed "s/XX/$1/" > src/main/scala/Day$1.scala
cat src/test/scala/DayXXSpec.scala | sed "s/XX/$1/" > src/test/scala/Day$1Spec.scala
cat src/main/resources/dayXX\$.txt | sed "s/XX/$1/" > src/main/resources/day$1\$.txt
cat src/main/resources/test/dayXX\$.txt | sed "s/XX/$1/" > src/main/resources/test/day$1\$.txt

git add src
git commit -m ":package: Year 2024 Day $1 preparations"
