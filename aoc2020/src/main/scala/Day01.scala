import aoc.numeric.{AoCPart1Test, AoCPart2Test}


object Day01 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val input = strings.map(_.toLong)
    for (i <- input; j <- input) {
      if (i + j == 2020) return i * j
    }
    -1
  }

  override def part2(strings: Seq[String]): Long = {
    val input = strings.map(_.toLong)
    for (i <- input; j <- input; k <- input) {
      if (i + j + k == 2020) return i * j * k
    }
    -1
  }
}
