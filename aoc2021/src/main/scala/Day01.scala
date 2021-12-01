import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day01 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val in = strings.map(_.toInt)
    in.indices
      .filter(_ >= 1)
      .filter(i => in(i) > in(i - 1))
      .count(_ => true)
  }

  override def part2(strings: Seq[String]): Long = {
    val in = strings.map(_.toInt)
    in.indices
      .filter(_ >= 3)
      .filter(i => {
        val prev = in(i - 1) + in(i - 2) + in(i - 3)
        val curr = in(i) + in(i - 1) + in(i - 2)
        prev < curr
      }).count(_ => true)
  }
}
