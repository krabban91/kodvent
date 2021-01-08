import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = strings
    .map(s => s.split("\\s+").filterNot(_.isBlank).map(_.toInt))
    .map(is => is.combinations(2)
      .map(_.sorted)
      .map(c => c.last - c.head)
      .max)
    .sum

  override def part2(strings: Seq[String]): Long = strings
    .map(s => s.split("\\s+").filterNot(_.isBlank).map(_.toInt))
    .map(is => is.combinations(2)
      .map(_.sorted)
      .find(c => c.last % c.head == 0)
      .map(c => c.last / c.head).get)
    .sum
}
