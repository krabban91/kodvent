import aoc.numeric.{AoCPart1Test, AoCPart2Test}
object Day08 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val easy = Set(2, 4, 3, 7)
    strings
      .map(s => s.split("\\|").last.split(" ").map(_.strip()))
      .map(l => l.count(s => easy.contains(s.length)))
      .sum
  }

  override def part2(strings: Seq[String]): Long = -1



}
