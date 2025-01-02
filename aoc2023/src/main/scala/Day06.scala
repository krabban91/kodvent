import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day06 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val tuples: Seq[(Long, Long)] = deriveTimes(strings)
    tuples.map { case (t, d) =>
      (1L until t).map { h => (t - h) * h }.count(_ > d).toLong
    }.product
  }

  override def part2(strings: Seq[String]): Long = {
    val tuples: Seq[(Long, Long)] = deriveTimes(strings, part2 = true)
    tuples.map { case (t, d) =>
      (1L until t).map { h => (t - h) * h }.count(_ > d).toLong
    }.product
  }

  private def deriveTimes(strings: Seq[String], part2: Boolean = false) = {
    val times = strings.head.split(" ").tail.filter(_.nonEmpty).map(_.strip())
    val distances = strings.tail.head.split(" ").tail.filter(_.nonEmpty).map(_.strip())

    if(!part2) {
      (times.map(_.toLong).toSeq zip distances.map(_.toLong))

    } else {
      (Seq(times.foldLeft("")(_+_).toLong) zip Seq(distances.foldLeft("")(_+_).toLong))
    }
  }
}
