import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day03 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    strings.map(s => s.splitAt(s.length / 2))
      .map { case (l, r) => matched(l, r).head }
      .map(priority).sum
  }

  override def part2(strings: Seq[String]): Long = {
    strings.grouped(3).toSeq
      .map(l => matched(l.head, l.tail.head, l.last).head)
      .map(priority).sum
  }

  private def matched(l: String, r: String): String = l intersect r

  private def matched(a: String, b: String, c: String): String = matched(a, matched(b, c))

  private def priority(c: Char): Int = if (c.isUpper) (c - 'A') + 27 else c - 'a' + 1
}
