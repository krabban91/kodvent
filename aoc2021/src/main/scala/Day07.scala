import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day07 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val input = strings.head.split(",").map(_.toInt)
    val min = input.min
    val max = input.max
    var cheapest = Long.MaxValue
    for (i <- min to max) {
      val sum = input.map(v => Math.abs(v - i)).sum
      if (sum < cheapest) {
        cheapest = sum
      }
    }
    cheapest
  }

  override def part2(strings: Seq[String]): Long = -1
}
