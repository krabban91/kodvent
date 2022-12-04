import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day04 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    strings.map(_.split(",")
      .map(_.split("-"))
      .map(l => Work(l.head.toInt, l.last.toInt)).toSeq)
      .map(l => Assignment(l.head, l.last))
      .count(_.fullyContained)
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }


  case class Assignment(l: Work, r: Work) {

    def fullyContained: Boolean = {
      l.fullyContained(r) || r.fullyContained(l)
    }
  }

  case class Work(from: Int, to: Int) {
    def fullyContained(other: Work): Boolean = {
      from <= other.from && other.to <= to
    }
  }

}
