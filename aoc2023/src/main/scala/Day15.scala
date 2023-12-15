import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class HASH(str: String) {
    def value : Long = {
      calculate(0L)
    }
    def calculate(initialValue: Long): Long = {
      val out = str.foldLeft(0L){case (o, h) =>
        val inc = o + h.toLong
        val mul = inc * 17
        val byte = mul % 256
        byte
      }

      out
    }
  }

  override def part1(strings: Seq[String]): Long = {
    strings.head.split(",").map(HASH).toSeq.map(_.value).map(_.toLong).sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
