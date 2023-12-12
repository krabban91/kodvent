import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Row(pattern: String, combinations: Seq[Int]) {
    def countArrangements: Long = {
      if (pattern.contains('?')) {
        val value = pattern.foldLeft(Seq[String]("")) { case (s, c) =>
          val value1 = c match {
            case '?' => Seq('.', '#').flatMap(cc => s.map(v => s"$v$cc"))
            case _ => s.map(v => s"$v$c")
          }
          value1
        }
        value.count(fits)
      } else 1L
    }

    private def fits(s: String): Boolean = {
      val seq = s.split("""\.""").filter(_.nonEmpty).map(_.length).toSeq
      seq == combinations
    }
  }

  object Row {
    def apply(s: String): Row = {
      val a = s.split(" ")
      Row(a.head, a.last.split(",").map(_.toInt))
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val rows = strings.map(Row(_))
    rows.map(_.countArrangements).sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
