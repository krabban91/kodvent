import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day06 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val columns = parseInput(strings, false)
    columns.map(_.calc).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val columns = parseInput(strings, true)
    columns.map(_.calc).sum
  }


  case class MathColumn(values: Seq[Long], multiply: Boolean) {
    def calc: Long = {
      if (multiply) {
        values.product
      } else {
        values.sum
      }
    }
  }

  def parseInput(strings: Seq[String], part2: Boolean) = {
    val maxW = strings.map(_.length).max

    def empty = strings.indices.map(_ => "")

    val res = (0 until maxW).foldRight((empty, Seq[MathColumn]())) { case (ix, (curr, out)) =>
      if (maxW - 1 > ix && strings.forall(_(ix) == ' ')) {
        // clear seqs
        val column = if (part2) {
          MathColumn.part2(curr)
        } else {
          MathColumn.part1(curr)
        }
        (empty, column +: out)
      } else {
        (curr.zipWithIndex.map { case (line, j) => (if (strings(j).length > ix) strings(j)(ix) else ' ') +: line }, out)
      }
    }
    val first = if (part2) {
      MathColumn.part2(res._1)
    } else {
      MathColumn.part1(res._1)
    }
    first +: res._2
  }

  object MathColumn {
    def part1(curr: Seq[String]): MathColumn = {
      val op = curr.last.strip() == "*"
      val values = curr.dropRight(1).map(_.strip().toLong)
      MathColumn(values, op)
    }

    def part2(curr: Seq[String]): MathColumn = {
      val op = curr.last.strip() == "*"
      val value = curr.dropRight(1)

      def empty = curr.indices.map(_ => "")

      val w = value.head.length
      val values = (0 until w).foldRight(empty) { case (ix, words) =>
        val next = value.map(_(ix)).foldLeft("") { case (w, c) => s"$w$c" }
        words :+ next
      }.map(_.strip()).filter(_.nonEmpty).map(_.toLong)
      MathColumn(values, op)
    }
  }


}
