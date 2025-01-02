import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day13 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = groupsSeparatedByTwoNewlines(strings)
    .map(Pattern(_)).map(_.reflectionScore()).sum

  override def part2(strings: Seq[String]): Long = groupsSeparatedByTwoNewlines(strings)
    .map(Pattern(_)).map(_.reflectionScore(part2 = true)).sum

  case class Pattern(map: Set[(Long, Long)]) {

    def reflectionScore(part2: Boolean = false): Long = reflectionRow(part2).map(_ * 100)
      .orElse(transpose.reflectionRow(part2)).get

    private def reflectionRow(part2: Boolean = false): Option[Long] = {
      val powers = (0L to map.map(_._1).max).map(math.pow(2, _).toLong)
      val columns = map.groupBy(_._2).map { case (k, s) => (k, s.map(_._1)) }
      (1L to columns.keySet.max).findLast { y =>
        val u = columns.filter(_._1 < y).toSeq.sortBy(_._1).reverse.map(_._2)
        val d = columns.filter(_._1 >= y).toSeq.sortBy(_._1).map(_._2)
        val z = u.zip(d)
          .map { case (l, r) => (l.map(math.pow(2, _).toLong).sum, r.map(math.pow(2, _).toLong).sum) }

        val diff = z.map { case (ls, rs) => ls ^ rs }.map(v => if (powers.contains(v)) 1 else v).sum
        diff == (if (part2) 1 else 0)
      }
    }

    private def transpose: Pattern = Pattern(map.map(p => (p._2, p._1)))
  }


  object Pattern {
    def apply(str: String): Pattern = {
      val lines = str.split("\n").map(_.strip()).filter(_.nonEmpty)
      Pattern(lines.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.filter { case (c, _) => c == '#' }.map { case (_, x) => (x.toLong, y.toLong) } }.toSet)
    }
  }
}

