

object Day01 extends App with AoC with AoCTest {

  override def part1(strings: scala.Seq[String]): Long = {
    val input = strings.map(_.toLong)
    for (i <- input) {
      for (j <- input) {
        if (i + j == 2020) return i * j
      }
    }
    -1
  }

  override def part2(strings: scala.Seq[String]): Long = {
    val input = strings.map(_.toLong)
    for (i <- input) {
      for (j <- input) {
        for (k <- input) {
          if (i + j + k == 2020) return i * j * k
        }
      }
    }
    -1
  }
}
