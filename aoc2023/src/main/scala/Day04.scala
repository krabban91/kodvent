import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day04 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = strings.map(ScratchCard(_)).map(_.points).sum

  override def part2(strings: Seq[String]): Long = {
    val cards = strings.map(ScratchCard(_)).groupBy(_.id).map(t => (t._1, t._2.head))
    val held = mutable.HashMap[Int, Long]()
    cards.foreach{ case (k, v) => held.put(k, 1)}
    cards.keys.toSeq.sorted.foreach { i =>
      val card = cards(i)
      val m = card.matching
      (0 until m).foreach(v => held(i+v+1) = held(i+v+1) + held(i))
    }
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
