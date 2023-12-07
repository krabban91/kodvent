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
      comparing(x, y)
    }
  }

  object CamelCardOrdering2 extends Ordering[CamelCard] {
    override def compare(x: CamelCard, y: CamelCard): Int = {
      comparing(x, y, part2 = true)
    }
  }

  private def comparing(x: CamelCard, y: CamelCard, part2: Boolean = false): Int = {
    val x0 = x.numberOfSameKind.sortBy(v => (v._2, v._1)).reverse
    val xc = x0.map(_._2).sorted
    val y0 = y.numberOfSameKind.sortBy(v => (v._2, v._1)).reverse
    val yc = y0.map(_._2).sorted
    val xj = x0.find(_._1 == 'J').map(_._2).getOrElse(0)
    val yj = y0.find(_._1 == 'J').map(_._2).getOrElse(0)
    val x0Adj = if (part2) {
      if (x0 == Seq(('J', 5))) {
        Seq(('A', 5))
      } else (Seq((x0.filterNot(_._1 =='J').head._1, x0.filterNot(_._1 =='J').head._2 + xj)) ++ x0.filterNot(_._1 =='J').tail)
    } else x0
    val xcAdj = x0Adj.map(_._2).sorted
    val y0Adj = if (part2) {
      if (y0 == Seq(('J', 5))) {
        Seq(('A', 5))
      } else Seq((y0.filterNot(_._1 =='J').head._1, y0.filterNot(_._1 =='J').head._2 + yj)) ++ y0.filterNot(_._1 =='J').tail
    } else y0
    val ycAdj = y0Adj.map(_._2).sorted


    if (xcAdj == ycAdj) {
      CamelCardHandOrdering.compare(x.hand, y.hand, part2)
    } else if (xcAdj == Seq(3, 2) && ycAdj == Seq(4, 1)) {
      //edge case 4 of a kind vs full house
      1
    } else if (ycAdj == Seq(3, 2) && xcAdj == Seq(4, 1)) {
      //edge case 4 of a kind vs full house
      -1
    } else if (x0Adj.map(_._2).max > y0Adj.map(_._2).max) {
      1
    } else if (x0Adj.size < y0Adj.size) {
      1
    } else {
      -1
      // y is better
    }
  }

  object CamelCardHandOrdering {
    val order = Seq('A', 'K', 'Q', 'J','T','9','8','7','6','5','4','3','2')
    val order2 = Seq('A', 'K', 'Q','T','9','8','7','6','5','4','3','2', 'J')
    def compare(x: String, y: String, part2: Boolean = false): Int = (x zip y).foldLeft(0) { case (out, (l, r)) =>
      val o = if(part2) order2 else order
      if (out == 0) {
        val li = o.indexOf(l)
        val ri = o.indexOf(r)
        ri - li
      } else out
    }
  }

    override def part1(strings: Seq[String]): Long = {
      val value = strings.map(CamelCard(_)).sorted(CamelCardOrdering)
      value.zipWithIndex.map{ case (c, i) => c.bid * (i + 1L)}.sum
  }

  override def part2(strings: Seq[String]): Long = {
    val value = strings.map(CamelCard(_)).sorted(CamelCardOrdering2)
    value.zipWithIndex.map{ case (c, i) => c.bid * (i + 1L)}.sum
  }
}
