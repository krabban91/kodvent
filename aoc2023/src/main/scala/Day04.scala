import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day04 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = strings.map(ScratchCard(_)).map(_.points).sum

  override def part2(strings: Seq[String]): Long = {
    val cards = strings.map(ScratchCard(_)).sortBy(_.id)
    val held = mutable.HashMap[Int, Long]()
    cards.foreach(card => held.put(card.id, 1))
    cards.foreach { card => (0 until card.matching).foreach(v => held(card.id + v + 1) = held(card.id + v + 1) + held(card.id)) }
    held.values.sum
  }


  case class ScratchCard(id: Int, winning: Seq[Int], drawn: Seq[Int]) {
    def matching = drawn.count(winning.contains)

    def points: Long = {
      val i = matching
      if (i > 0) {
        math.pow(2, i - 1).toLong
      } else 0
    }
  }

  object ScratchCard {
    def apply(str: String): ScratchCard = {
      val s = str.split(":")
      val id = s.head.drop(4).strip().toInt
      val s2 = s.last.split("""\|""")
      val winning = s2.head.split(" ").filter(_.nonEmpty).map(_.strip().toInt).toSeq
      val drawn = s2.last.split(" ").filter(_.nonEmpty).map(_.strip().toInt).toSeq
      ScratchCard(id, winning, drawn)
    }
  }

}
