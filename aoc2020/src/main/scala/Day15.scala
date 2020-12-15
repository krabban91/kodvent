import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val in = strings(0).split(",").map(_.toLong)
    val numbers: mutable.Map[Long, Seq[Long]] = mutable.Map[Long, Seq[Long]]() ++ (in.map(n => n -> Seq(in.indexOf(n) + 1L)))
    var prev = in.last
    var turn = in.size.toLong
    while (turn < 2020) {
      turn += 1
      val prvL = numbers.get(prev).get

      if (prvL.size == 1) {
        prev = 0L
      } else {
        prev = prvL(prvL.size - 1) - prvL(prvL.size - 2)
      }
      numbers(prev) = numbers.getOrElse(prev, Seq()) ++ Seq(turn)
    }
    prev
  }

  override def part2(strings: Seq[String]): Long = -1
}
