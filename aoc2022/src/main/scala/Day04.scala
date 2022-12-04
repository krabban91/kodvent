import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day04 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = Assignment.from(strings).count(_.fullyContained)

  override def part2(strings: Seq[String]): Long = Assignment.from(strings).count(_.overlap)

  case class Assignment(l: Work, r: Work) {
    def fullyContained: Boolean = l.fullyContained(r) || r.fullyContained(l)

    def overlap: Boolean = l.overlaps(r)
  }

  case class Work(from: Int, to: Int) {
    def fullyContained(other: Work): Boolean = from <= other.from && other.to <= to

    def overlaps(o: Work): Boolean = (o.from <= to && from <= o.from) || (from <= o.to && o.from <= from)
  }

  object Assignment {
    def from(strings: Seq[String]): Seq[Assignment] = strings
      .map(_.split(",")
        .map(_.split("-"))
        .map(l => Work(l.head.toInt, l.last.toInt)).toSeq)
      .map(l => Assignment(l.head, l.last))
  }
}
