import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day06 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val tuples: Array[(Long, Long)] = deriveTimes(strings)
    tuples.map{ case (t, d) =>
      (1L until t).map { h => (t - h) * h }.count(_ > d).toLong
    }.product
  }

  private def deriveTimes(strings: Seq[String]) = {
    val times = strings.head.split(" ").tail.filter(_.nonEmpty).map(_.strip().toLong)
    val distances = strings.tail.head.split(" ").tail.filter(_.nonEmpty).map(_.strip().toLong)

    val tuples = times zip distances
    tuples
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
