import aoc.numeric.{AoCPart1Test, AoCPart2Test}


object Day01 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    groupsSeparatedByTwoNewlines(strings)
      .map(_.split("\n").filter(_.nonEmpty).map(_.toInt).sum)
      .sorted.takeRight(1).sum
  }

  override def part2(strings: Seq[String]): Long = {
    groupsSeparatedByTwoNewlines(strings)
      .map(_.split("\n").filter(_.nonEmpty).map(_.toInt).sum)
      .sorted.takeRight(3).sum
  }
}
