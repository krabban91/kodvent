import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day03 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    strings.map(_.strip()).map(bestBank(_, 2)).map(_.toLong).sum
  }

  override def part2(strings: Seq[String]): Long = {
    strings.map(_.strip()).map(bestBank(_, 12)).map(_.toLong).sum
  }

  private def bestBank(string: String, batteries: Int): String = {
    if (batteries == 0) {
      return ""
    }
    val test = string.dropRight(batteries - 1)
    val value = test.max
    val ix = test.indexOf(value)
    val next = string.drop(ix + 1)
    s"$value${bestBank(next, batteries - 1)}"
  }

}
