from itertools import combinations
import pulp
import os

FILENAME = os.path.join(os.path.dirname(__file__), "../src/main/resources/day10$.txt")


def parse_input(filename):
    with open(filename, "r") as input_file:
        out = []
        lines = input_file.read().split("\n")
        for line in lines:
            if line:

                words = [w for w in line.split(" ") if w]
                buttons = words[1:-1]
                bs = []
                for word in buttons:
                    nums = word[1:-1].split(",")

                    bs.append(tuple(int(num) for num in nums))
                target_nums = [int(num) for num in words[-1][1:-1].split(",")]
                out.append({"buttons": bs, "targets": target_nums})
        return out

# 19860 too low

def part_two(data):
    results = []
    for d in data:
        bs = d["buttons"]
        targets = d["targets"]
        buttons = dict()
        t_b = dict()
        n = len(targets)
        m = len(bs)
        prob = pulp.LpProblem("Day10Part2", pulp.LpMinimize)

        xs = [pulp.LpVariable(f"x_{i}", lowBound=0, cat="Integer") for i in range(m)]

        prob += pulp.lpSum(xs), "total_presses"
        for i in range(n):
            c = pulp.lpSum(xs[j] for j in range(m) if i in bs[j])
            counter = c == targets[i]
            prob += counter, f"Counter_{i}"
        solver = pulp.PULP_CBC_CMD(msg=False)
        prob.solve()
        if pulp.LpStatus[prob.status] == "Optimal":
            v = pulp.value(prob.objective)
        #    print(f"Found solution with value {v}")
            results.append(v)
        else:
            print("!! No solution found")
            1/0
            results.append(0)
            continue
    s = sum(results)
    print("part two:", s)
    return s


def main():
    data = parse_input(FILENAME)
    print(part_two(data))


if __name__ == "__main__":
    main()
