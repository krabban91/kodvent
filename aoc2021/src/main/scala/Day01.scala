import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day01 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = strings.map(_.toInt)
    .sliding(2)
    .count(t => t.head < t.last)

  override def part2(strings: Seq[String]): Long = strings.map(_.toInt)
    .sliding(4)
    .count(t => {
      val prev = t.head + t(1) + t(2)
      val curr = t(1) + t(2) + t.last
      prev < curr
    })
}
