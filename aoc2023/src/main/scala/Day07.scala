import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day07 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class CamelCard(hand: String, bid: Long) {
    def numberOfSameKind : Seq[(Char, Int)] = {
      val x0 = hand.distinct
      x0.map(v => (v, hand.count(c => c==v)))
    }
  }

  object CamelCard {

    def apply(str: String): CamelCard = {
      val spl = str.split(" ")
      CamelCard(spl.head, spl.last.toLong)
    }
  }



  object CamelCardOrdering extends Ordering[CamelCard] {
    override def compare(x: CamelCard, y: CamelCard): Int = {
      val x0 = x.numberOfSameKind
      val xc = x0.map(_._2).sorted
      val y0 = y.numberOfSameKind
      val yc = y0.map(_._2).sorted
      if (xc == yc) {
        CamelCardHandOrdering.compare(x.hand, y.hand)
      } else if (xc == Seq(3,2) && yc == Seq(4,1)) {
        //edge case 4 of a kind vs full house
        1
      } else if (yc == Seq(3, 2) && xc == Seq(4, 1)) {
        //edge case 4 of a kind vs full house
        -1
      } else if (x0.map(_._2).max > y0.map(_._2).max) {
        1
      } else if (x0.size < y0.size) {
        1
      } else {
        -1
        // y is better
      }
    }
  }

  object CamelCardHandOrdering extends Ordering[String] {
    val order = Seq('A', 'K', 'Q', 'J','T','9','8','7','6','5','4','3','2')
    override def compare(x: String, y: String): Int = (x zip y).foldLeft(0) { case (out, (l, r)) =>
      if (out == 0) {
        val li = order.indexOf(l)
        val ri = order.indexOf(r)
        ri - li
      } else out
    }
  }

    override def part1(strings: Seq[String]): Long = {
      val value = strings.map(CamelCard(_)).sorted(CamelCardOrdering)
      value.zipWithIndex.map{ case (c, i) => c.bid * (i + 1L)}.sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
