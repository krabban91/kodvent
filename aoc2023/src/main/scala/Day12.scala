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
          value1.filter(legal)
        }
        value.count(fits)
      } else 1L
    }

    private def fits(s: String): Boolean = {
      val seq = s.split("""\.""").filter(_.nonEmpty).map(_.length).toSeq
      seq == combinations
    }

    private def legal(s: String): Boolean = {
      val seq = s.split("""\.""").filter(_.nonEmpty).map(_.length).toSeq
      if (seq.isEmpty) {
        true
      } else {
        val value = combinations.take(seq.size)
        (value.dropRight(1) zip seq.dropRight(1)).forall{case (l, r) =>l == r} && value.last >= seq.last
      }
    }


    def unfold: Row = {

      Row(Seq.fill(5)(pattern).mkString("?"), Seq.fill(5)(combinations).flatten)
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
    val rows = strings.map(Row(_)).map(_.unfold)
    rows.map(_.countArrangements).sum
  }
}
