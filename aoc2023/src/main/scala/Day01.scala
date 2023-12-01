import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day01 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val lines = strings.map(_.split(",").map(_.trim.toLong).toSeq)
    lines.map(l => l(0)).sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
