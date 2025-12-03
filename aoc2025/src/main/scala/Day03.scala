import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day03 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    strings.map(_.strip()).map(bestBank(_, 2)).map(_.toLong).sum
  }
  def bestBank(string: String, batteries: Int): String = {
    if (batteries == 0) {return ""}
    if (string == "") { print(s"bad input! $string and $batteries"); return ""}
    var first = 0;
    var firstVal: Char = 0;
    (0 to string.length-batteries).foreach(i => if (string(i) > firstVal){firstVal = string(i); first = i})
    val next = string.drop(first + 1)
    s"$firstVal${bestBank(next,batteries-1)}"
  }

  override def part2(strings: Seq[String]): Long = {
    strings.map(_.strip()).map(bestBank(_, 12)).map(_.toLong).sum
  }
}
