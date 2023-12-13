import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day13 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Pattern(map: Set[(Long, Long)]) {

    def reflectionRow(part2: Boolean = false): Option[Long] = {
      val powers = (0L to map.map(_._1).max).map(math.pow(2, _).toLong)
      val columns = map.groupBy(_._2).map{ case (k, s) => (k, s.map(_._1))}
      (1L to columns.keySet.max).findLast { y =>
        val u = columns.filter(_._1 < y).toSeq.sortBy(_._1).reverse.map(_._2)
        val d = columns.filter(_._1 >= y).toSeq.sortBy(_._1).map(_._2)
        val z = u.zip(d)
          .map { case (l, r) => (l.map(math.pow(2, _).toLong).sum, r.map(math.pow(2, _).toLong).sum) }

        val diff = z.map { case (ls, rs) =>
          val xor = ls ^ rs
          val l1 = math.abs(ls - rs)

          if (powers.contains(xor)) 1 else xor
        }.sum
        diff == (if (part2) 1 else 0)
      }
    }
    def transpose: Pattern = Pattern(map.map(p => (p._2, p._1)))
  }


  object Pattern {
    def apply(str: String): Pattern = {
      val lines = str.split("\n").map(_.strip()).filter(_.nonEmpty)
      Pattern(lines.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.filter { case (c, _) => c == '#' }.map { case (_, x) => (x.toLong, y.toLong) } }.toSet)
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val patterns = groupsSeparatedByTwoNewlines(strings).map(Pattern(_))
    val value = patterns.map{v =>
      val vertical = v.reflectionRow().map(y => (y) * 100).getOrElse(0L)
      val horisontal = v.transpose.reflectionRow().getOrElse(0L)
      math.max(vertical, horisontal)
    }
    value.sum
  }

  override def part2(strings: Seq[String]): Long = {
    val patterns = groupsSeparatedByTwoNewlines(strings).map(Pattern(_))
    val value = patterns.map{v =>
      val vertical = v.reflectionRow(part2 = true).map(y => (y) * 100).getOrElse(0L)
      val horisontal = v.transpose.reflectionRow(part2 = true).getOrElse(0L)
      math.max(vertical, horisontal)
    }
    value.sum
  }
}

