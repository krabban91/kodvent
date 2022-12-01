import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day01 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {

    strings.foldLeft((0, 0)) { case ((sum, max), row) =>
      if (row == "") {
        if (sum > max) (0, sum) else (0, max)
      } else {
        (sum + row.toInt, max)
      }
    }._2
  }

  override def part2(strings: Seq[String]): Long = {

    strings.foldLeft((0, Seq[Int]())) { case ((sum, max), row) =>
      if (row == "") {
        if (max.size < 3) {
          (0, max ++ Seq(sum))
        } else if (max.exists(v => v < sum)) {
          (0, (max diff Seq(max.min)) ++ Seq(sum))
        } else (0, max)
      } else {
        (sum + row.toInt, max)
      }
    }._2.sum
  }
}
