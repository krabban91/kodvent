import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = getN(2020, strings.head.split(",").map(_.toInt))

  override def part2(strings: Seq[String]): Long = getN(30000000, strings.head.split(",").map(_.toInt))

  def getN(N: Int, input: Seq[Int]): Int = {
    val numbers: mutable.Map[Int, Int] = mutable.Map[Int, Int]() ++ input.map(v => v -> (input.indexOf(v) + 1))
    var next = 0
    Range(input.size + 1, N).foreach(turn => {
      val curr = next
      next = numbers.get(curr).map(v => turn - v).getOrElse(0)
      numbers(curr) = turn
    })
    next
  }

}
