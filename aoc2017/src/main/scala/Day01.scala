import aoc.numeric.{AoCPart1, AoCPart2}

object Day01 extends App with AoCPart1 with AoCPart2 {

  override def part1(strings: Seq[String]): Long = {
    val inp = strings.head.map(_.toString).map(_.toInt)
    inp.indices.map(i => if (inp(i) == inp((i + 1) % inp.length)) inp(i) else 0).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val inp = strings.head.map(_.toString).map(_.toInt)
    inp.indices.map(i => if (inp(i) == inp((i + inp.length / 2) % inp.length)) inp(i) else 0).sum
  }
}
