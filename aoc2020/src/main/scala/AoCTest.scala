import scala.io.Source

trait AoCTest extends AoC {

  def getInputTest: Seq[String] = {
    val source = Source.fromResource(s"test/${this.getClass.getName.toLowerCase}.txt")
    try {
      source.getLines().toSeq
    } finally {
      source.close()
    }
  }

  def part1TestResult: Long = part1(getInputTest)

  def part2TestResult: Long = part2(getInputTest)

  def printResultTest: Unit = {
    println(s"::${this.getClass.getName} - Test data::")
    println(s"Part 1 -> ${this.part1TestResult}")
    println(s"Part 2 -> ${this.part2TestResult}")
  }
}
