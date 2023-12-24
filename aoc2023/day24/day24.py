from itertools import combinations
from z3 import *
import os

FILENAME = os.path.join(os.path.dirname(__file__), "../src/main/resources/day24$.txt")


def parse_input(filename):
    with open(filename, "r") as input_file:
        return [
            tuple(
                [
                    tuple([int(num) for num in nums.split(",")])
                    for nums in row.split("@") if row
                ]
            )
            for row in input_file.read().split("\n")
        ]


def part_two(data):
    (x_1, y_1, z_1), (dx_1, dy_1, dz_1) = data[0]
    (x_2, y_2, z_2), (dx_2, dy_2, dz_2) = data[1]
    (x_3, y_3, z_3), (dx_3, dy_3, dz_3) = data[2]

    x, y, z = Reals("x y z")
    dx, dy, dz = Reals("dx dy dz")
    t1, t2, t3 = Reals("t1 t2 t3")
    solver = Solver()

    eq1 = x + t1 * dx == x_1 + t1 * dx_1
    eq2 = y + t1 * dy == y_1 + t1 * dy_1
    eq3 = z + t1 * dz == z_1 + t1 * dz_1
    eq4 = x + t2 * dx == x_2 + t2 * dx_2
    eq5 = y + t2 * dy == y_2 + t2 * dy_2
    eq6 = z + t2 * dz == z_2 + t2 * dz_2
    eq7 = x + t3 * dx == x_3 + t3 * dx_3
    eq8 = y + t3 * dy == y_3 + t3 * dy_3
    eq9 = z + t3 * dz == z_3 + t3 * dz_3
    tEq1 = t1 > 0
    tEq2 = t2 > 0
    tEq3 = t3 > 0

    solver.add(eq1, eq2, eq3, eq4, eq5, eq6, eq7, eq8, eq9, tEq1, tEq2, tEq3)

    if solver.check() == sat:
        model = solver.model()
        return sum(model[var].as_long() for var in [x, y, z])


def main():
    data = parse_input(FILENAME)
    print(part_two(data))


if __name__ == "__main__":
    main()
