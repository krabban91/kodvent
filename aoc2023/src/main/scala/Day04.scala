import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day04 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = strings.map(ScratchCard(_)).map(_.points).sum

  override def part2(strings: Seq[String]): Long = {
    -1
  }


  case class ScratchCard(id: Int, winning: Seq[Int], drawn: Seq[Int]) {
    def points: Long = {
      val i = drawn.count(winning.contains)
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
