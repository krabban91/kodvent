import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day13 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Pattern(map: Set[(Long, Long)]) {
    def vertical: Option[Long] = {
      val rows = map.groupBy(_._1)
      (0L to rows.keySet.max - 1).find { x =>
        val l = rows.filter(_._1 <= x).toSeq.sortBy(_._1).reverse
        val r = rows.filter(_._1 > x).toSeq.sortBy(_._1)
        val z = l.zip(r).map(tt => (tt._1._2.map(_._2), tt._2._2.map(_._2)))
        val bool = z.forall { case (ls, rs) => ls == rs }
        bool
      }
    }

    def horisontal: Option[Long] = {
      val columns = map.groupBy(_._2)
      (0L to columns.keySet.max - 1).find { y =>
        val l = columns.filter(_._1 <= y).toSeq.sortBy(_._1).reverse
        val r = columns.filter(_._1 > y).toSeq.sortBy(_._1)
        val z = l.zip(r).map(tt => (tt._1._2.map(_._1), tt._2._2.map(_._1)))
        val bool = z.forall { case (ls, rs) => ls == rs }
        bool
      }
    }
  }

  object Pattern {
    def apply(str: String): Pattern = {
      val lines = str.split("\n").map(_.strip()).filter(_.nonEmpty)
      Pattern(lines.zipWithIndex.flatMap{ case (str, y) => str.zipWithIndex.filter{ case (c, _) =>c == '#'}.map{ case (_, x) =>(x.toLong,y.toLong)}}.toSet)
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val patterns = groupsSeparatedByTwoNewlines(strings).map(Pattern(_))
    val value = patterns.map(v => v.vertical.map(_ + 1).orElse(v.horisontal.map(y => (y + 1) * 100)).get)
    value.sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}

