#!/usr/bin/env zsh
set -euxo pipefail

l=${2:-1}
r=$1
dd=${3:-$(gdate +%d)}
d=$( echo $dd | sed "s/^0//")
y=${4:-2025}
python ./submit.py $d $l $r $y
res=$?
if test "$res" != "0"; then
  echo "failed with $res"
  exit $res
fi

git add -u
git commit -m ":sparkles: Year $y Day $dd - Completed part $l"

open https://adventofcode.com/$y/day/$d
