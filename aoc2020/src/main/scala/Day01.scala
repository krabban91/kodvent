

object Day01 extends App with AoC with AoCTest {

  printResult
  printResultTest

  override def part1: Long = p1(getInput.map(_.toLong))

  override def part2: Long = p2(getInput.map(_.toLong))

  override def part1Test: Long = p1(getInputTest.map(_.toLong))

  override def part2Test: Long = p2(getInputTest.map(_.toLong))

  private def p1(input: scala.Seq[Long]): Long = {
    for (i <- input) {
      for (j <- input) {
        if (i + j == 2020) return i * j
      }
    }
    -1
  }

  private def p2(input: scala.Seq[Long]): Long = {
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
