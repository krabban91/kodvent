import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day07 extends App with AoCPart1Test with AoCPart2Test {

  case class CamelCard(hand: String, bid: Long) {

    def value(rank: Int): Long = bid * rank

    private def numberOfSameKind: Seq[(Char, Int)] = {
      val x0 = hand.distinct
      x0.map(v => (v, hand.count(c => c == v)))
    }

    private def handCounts: Seq[(Char, Int)] = {
      numberOfSameKind.sortBy(v => (v._2, v._1)).reverse
    }

    private def jokeredHandCounts: Seq[(Char, Int)] = {
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
      val xCounts = if (part2) x.jokeredHandCounts else x.handCounts
      val yCounts = if (part2) y.jokeredHandCounts else y.handCounts

      if (xCounts.map(_._2).sorted == yCounts.map(_._2).sorted) {
        CamelCardHandOrdering.compare(x.hand, y.hand, part2)
      } else if (xCounts.map(_._2).max > yCounts.map(_._2).max) {
        1
      } else if (xCounts.size < yCounts.size) {
        1
      } else {
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

  override def part1(strings: Seq[String]): Long = strings.map(CamelCard(_)).sorted(CamelCard.Ordering)
    .zipWithIndex.map { case (c, i) => c.value(i + 1) }.sum

  override def part2(strings: Seq[String]): Long = strings.map(CamelCard(_)).sorted(CamelCard.Ordering2)
    .zipWithIndex.map { case (c, i) => c.value(i + 1) }.sum
}
