import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  case class Row(pattern: String, combinations: Seq[Int]) {

    def notAccountedFor: Seq[Int] = {
      val groups = pattern.split("""\.|\?""").filter(_.nonEmpty).map(_.size).toSeq
      combinations diff groups
    }

    def countArrangements: Long = {
      if (pattern.contains('?')) {

        var s = pattern
        val sets = mutable.ListBuffer[(String, String)]()
        while (s.nonEmpty) {
          val pre = s.takeWhile(_ != '?')
          val t = s.dropWhile(_ != '?')
          val curr = t.takeWhile(_ == '?')
          s = t.dropWhile(_ == '?')
          sets.addOne((pre, curr))
        }
        val groups = sets
          .flatMap { t =>
            val mSize = combinations.max
            val gs = t._2.grouped(mSize).toSeq
            if (gs.isEmpty) {
              Seq(t)
            } else {
            val h = (t._1, gs.head)
            val tail = gs.tail.map(("", _))
            Seq(h) ++ tail
            }
          }
          .foldLeft(Seq[((String, String), Int)]()) { case (o, t) =>
          o ++ Seq((t, o.lastOption.map(_._2).getOrElse(0) + t._1.size + t._2.size))
        }
        val out = groups.foldLeft(Seq[(String, Long, Seq[Int])](("", 1, Seq()))) { case (out, ((pre, curr), endsAt)) =>
          val combo = combine(pre, curr)
          val joined = out.flatMap { case (s, l, _) => combo.map(n => (s + n, l, usedSoFar(s + n))) }
          val filtered = joined
            .filter(t => legal(t._1, endsAt))

          val grouped = filtered.groupBy(t => (t._1.endsWith("#"), t._3))
          val added = grouped.values.flatMap(l => Seq((l.head._1, l.map(_._2).sum, l.head._3))).toSeq
          added
        }
        out.filter(t => fits(t._1)).map(_._2).sum
      } else 1L
    }

    def combine(pre: String, wild: String): Seq[String] = {
      val atMost = Some(notAccountedFor).filter(_.nonEmpty).map(_.max).getOrElse(1)
      val out = wild.foldLeft(Seq[String]("")) { case (s, _) => Seq('.', '#').flatMap { cc => s.filter(ss => cc != '#' || ss.reverse.takeWhile(_ == '#').length < atMost).map(v => s"$v$cc") } }.map(pre ++ _)
      out
    }


    private def fits(s: String): Boolean = {
      val seq = usedSoFar(s)
      seq == combinations
    }

    private def legal(s: String, endsAt: Int): Boolean = {
      val seq = s.split("""\.""").filter(_.nonEmpty).map(_.length).toSeq

      if (seq.isEmpty) {
        true
      } else {
        if (seq.size > combinations.size) {
          false
        } else {
          val groupsLeft = combinations.drop(seq.size)
          val maxLeft = Some(groupsLeft).filter(_.nonEmpty).map(_.max).getOrElse(0)
          val spaceRequired = groupsLeft.sum + groupsLeft.size - 1
          val stringLeft = pattern.size - endsAt
          if (spaceRequired > stringLeft) {
            false
          } else {
            val value = combinations.take(seq.size)
            if (s.endsWith(".")) {
              (value zip seq).forall { case (l, r) => l == r }
            } else {
              (value.dropRight(1) zip seq.dropRight(1)).forall { case (l, r) => l == r } && value.last >= seq.last

            }
          }
        }
      }
    }


    def unfold: Row = {

      Row(Seq.fill(5)(pattern).mkString("?"), Seq.fill(5)(combinations).flatten)
    }
  }

  private def usedSoFar(s: String) = {
    s.split("""\.""").filter(_.nonEmpty).map(_.length).toSeq
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
    rows.zipWithIndex.map { t => val res = t._1.countArrangements; println(s"${t._2} completed:\t $res"); res }.sum
  }
}
