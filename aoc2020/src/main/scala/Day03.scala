import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day03 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = countTrees(strings, 3, 1)

  override def part2(strings: Seq[String]): Long = Seq((1, 1), (3, 1), (5, 1), (7, 1), (1, 2))
    .map(e => countTrees(strings, e._1, e._2))
    .product

  private def countTrees(strings: Seq[String], right: Int, down: Int): Long = strings.indices
    .filter(_ % down == 0)
    .count(i => strings(i)((right * i / down) % strings(i).length).equals('#'))
}
