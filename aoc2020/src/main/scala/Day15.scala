import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val in = strings.head.split(",").map(_.toInt)
    val numbers: mutable.Map[Int, Int] = mutable.Map[Int, Int]() ++ in.map(v => v -> (in.indexOf(v)+1))
    var next = 0
    Range(in.length + 1, 2020).foreach(turn => {
        val curr = next
        next = numbers.get(curr).map(v => turn - v).getOrElse(0)
        numbers(curr) = turn
      })
    next
  }

  override def part2(strings: Seq[String]): Long = -1
}
