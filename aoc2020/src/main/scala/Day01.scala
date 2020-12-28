import aoc.numeric.{AoCPart1Test, AoCPart2Test}


object Day01 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = strings.map(_.toLong).combinations(2).find(_.sum == 2020).map(_.product).getOrElse(-1)

  override def part2(strings: Seq[String]): Long = strings.map(_.toLong).combinations(3).find(_.sum == 2020).map(_.product).getOrElse(-1)
}
