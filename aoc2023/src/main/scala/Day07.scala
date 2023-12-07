import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day07 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class CamelCard(hand: String, bid: Long) {
    def numberOfSameKind: Seq[(Char, Int)] = {
      val x0 = hand.distinct
      x0.map(v => (v, hand.count(c => c == v)))
    }

    def handCounts: Seq[(Char, Int)] = {
      numberOfSameKind.sortBy(v => (v._2, v._1)).reverse
    }

    def jokeredHandCounts: Seq[(Char, Int)] = {
      val hc = handCounts
      val xj = hc.find(_._1 == 'J').map(_._2).getOrElse(0)
      val filtered = hc.filterNot(_._1 == 'J')
      val head = filtered.headOption.getOrElse(('A', 0))
      Seq((head._1, head._2 + xj)) ++ Some(filtered).filter(_.nonEmpty).map(_.tail).getOrElse(Seq())
    }
  }

  object CamelCard {

    def apply(str: String): CamelCard = {
      val spl = str.split(" ")
      CamelCard(spl.head, spl.last.toLong)
    }

    object Ordering extends Ordering[CamelCard] {
      override def compare(x: CamelCard, y: CamelCard): Int = {
        CamelCard.comparing(x, y)
      }
    }

    object Ordering2 extends Ordering[CamelCard] {
      override def compare(x: CamelCard, y: CamelCard): Int = {
        CamelCard.comparing(x, y, part2 = true)
      }
    }

    private def comparing(x: CamelCard, y: CamelCard, part2: Boolean = false): Int = {
      val x0Adj = if (part2) x.jokeredHandCounts else x.handCounts
      val y0Adj = if (part2) y.jokeredHandCounts else y.handCounts
      val xcAdj = x0Adj.map(_._2).sorted
      val ycAdj = y0Adj.map(_._2).sorted

      if (xcAdj == ycAdj) {
        CamelCardHandOrdering.compare(x.hand, y.hand, part2)
      } else if (ycAdj == Seq(4, 1)) {
        //edge case 4 of a kind vs full house
        1
      } else if (xcAdj == Seq(4, 1)) {
        //edge case 4 of a kind vs full house
        -1
      } else if (x0Adj.map(_._2).max > y0Adj.map(_._2).max) {
        1
      } else if (x0Adj.size < y0Adj.size) {
        1
      } else {
        // y is better
        -1
      }
    }

    private object CamelCardHandOrdering {
      private val order = Seq('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
      private val order2 = Seq('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

      def compare(x: String, y: String, part2: Boolean = false): Int = (x zip y).foldLeft(0) { case (out, (l, r)) =>
        val o = if (part2) order2 else order
        if (out == 0) o.indexOf(r) - o.indexOf(l) else out
      }
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val value = strings.map(CamelCard(_)).sorted(CamelCard.Ordering)
    value.zipWithIndex.map { case (c, i) => c.bid * (i + 1L) }.sum
  }

  override def part2(strings: Seq[String]): Long = {
    val value = strings.map(CamelCard(_)).sorted(CamelCard.Ordering2)
    value.zipWithIndex.map { case (c, i) => c.bid * (i + 1L) }.sum
  }
}
