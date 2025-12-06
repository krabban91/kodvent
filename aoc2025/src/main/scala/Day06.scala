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

  private def empty(size: Int) = (0 until size).map(_ => "")

  def parseInput(strings: Seq[String], part2: Boolean) = {
    val maxW = strings.map(_.length).max
    val (rem, tail) = (0 until maxW).foldRight((empty(strings.size), Seq[MathColumn]())) { case (ix, (curr, out)) =>
      if (curr.head.nonEmpty && strings.forall(_(ix) == ' ')) {
        (empty(strings.size), MathColumn.parse(curr, part2) +: out)
      } else {
        (curr.zipWithIndex.map { case (line, j) => (if (strings(j).length > ix) strings(j)(ix) else ' ') +: line }, out)
      }
    }
    val first = MathColumn.parse(rem, part2)
    first +: tail
  }

  object MathColumn {

    def parse(curr: Seq[String], part2: Boolean): MathColumn = {
      if (part2) {
        MathColumn.part2(curr)
      } else {
        MathColumn.part1(curr)
      }
    }

    def part1(curr: Seq[String]): MathColumn = {
      val op = curr.last.strip() == "*"
      val values = curr.dropRight(1).map(_.strip().toLong)
      MathColumn(values, op)
    }

    def part2(curr: Seq[String]): MathColumn = {
      val op = curr.last.strip() == "*"
      val numbers = curr.dropRight(1)
      val words = (0 until numbers.head.length)
        .map { ix => numbers.map(_(ix)).mkString("") }
      val values = words
        .map(_.strip()).filter(_.nonEmpty).map(_.toLong)
      MathColumn(values, op)
    }
  }
}
