import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  def parseRanges(strings: Seq[String]) = {
    strings.head.split(",").map{s => s.split("-") match {
      case Array(start, end) => (start.toLong, end.toLong)
    }}.toSeq
  }

  def isInvalid(num: Long): Boolean = {
    val s = num.toString
    s.take(s.length/2) == s.drop(s.length/2)
  }

  override def part1(strings: Seq[String]): Long = {
    val ranges = parseRanges(strings)
    val invalid = ranges.map(r => (r._1 to r._2).filter(isInvalid))
    invalid.map(_.sum).sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
