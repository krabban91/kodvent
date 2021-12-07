import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day07 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val input = strings.head.split(",").map(_.toInt)
    (input.min to input.max).foldLeft(Long.MaxValue)((cheapest, i) => {
      val sum = input.map(v => Math.abs(v - i)).sum
      if (sum < cheapest) sum else cheapest
    })
  }

  override def part2(strings: Seq[String]): Long = {
    val input = strings.head.split(",").map(_.toInt)
    (input.min to input.max).foldLeft(Long.MaxValue)((cheapest, i) => {
      val sum = input.map(v => (1 to Math.abs(v - i)).sum).sum
      if (sum < cheapest) sum else cheapest
    })
  }
}
