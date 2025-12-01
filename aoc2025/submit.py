from aocd import submit
import sys
import os

if not os.environ.get('AOC_SESSION'):
    os.environ['AOC_SESSION'] = os.environ['AOC_ID']
a = sys.argv[1:]
d = int(a[0])
part = "a" if int(a[1]) == 1 else "b"
res = a[2]
y = int(a[3]) if len(a) >= 4 else 2023

s = f'Year {y} Day {d} - part {part}: {res}'
ret = submit(year=y, part=part, day=d, answer=res)
if ret is None:
    exit(1)
else:
    exit(0)

