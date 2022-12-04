import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day04 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = strings.map(Assignment(_)).count(_.fullyContained)

  override def part2(strings: Seq[String]): Long = strings.map(Assignment(_)).count(_.overlap)

  case class Assignment(l: Work, r: Work) {
    def fullyContained: Boolean = l.fullyContained(r) || r.fullyContained(l)

    def overlap: Boolean = l.overlaps(r)
  }

  object Assignment {
    def apply(string: String): Assignment = {
      val (l, r) = string.splitAt(string.indexOf(","))
      Assignment(Work(l), Work(r.tail))
    }
  }

  case class Work(from: Int, to: Int) {
    def fullyContained(other: Work): Boolean = from <= other.from && other.to <= to

    def overlaps(o: Work): Boolean = (o.from <= to && from <= o.from) || (from <= o.to && o.from <= from)
  }

  object Work {
    def apply(string: String): Work = {
      val (l, r) = string.splitAt(string.indexOf("-"))
      Work(l.toInt, r.tail.toInt)
    }
  }
}
