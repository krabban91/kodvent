import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day01 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val lookup = Map("0" -> 0, "1" -> 1, "2" -> 2, "3" -> 3, "4" -> 4, "5" -> 5, "6" -> 6, "7" -> 7, "8" -> 8, "9" -> 9)
    strings.map(v => parseDigit(v, lookup)).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val lookup = Map("zero" -> 0, "one" -> 1, "two" -> 2, "three" -> 3, "four" -> 4, "five" -> 5, "six" -> 6, "seven" -> 7, "eight" -> 8, "nine" -> 9, "0" -> 0, "1" -> 1, "2" -> 2, "3" -> 3, "4" -> 4, "5" -> 5, "6" -> 6, "7" -> 7, "8" -> 8, "9" -> 9)
    strings.map(v => parseDigit(v, lookup)).sum
  }

  def parseDigit(string: String, lookup: Map[String, Int]): Long = {
    val ll = lookup
      .toList
      .flatMap { case (k, vv) => (string.indices.map { i => val x = string.substring(i).indexOf(k); if (x != -1) x + i else -1 }.filter(_ != -1).map((_, vv))) }
      .sortBy(_._1)
    if (ll.isEmpty) {
      0
    } else {
      s"${ll.head._2}${ll.last._2}".toLong
    }
  }

}
