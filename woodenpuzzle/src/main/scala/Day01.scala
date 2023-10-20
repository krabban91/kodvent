import aoc.string.AoCPart1StringTest


object Day01 extends App with AoCPart1StringTest{

  override def part1(strings: Seq[String]): String = {
    val tiles = groupsSeparatedByTwoNewlines(strings)
    "-1"
  }
}
