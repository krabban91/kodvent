import scala.io.Source

trait AoCTest {
  def part1Test: Long
  def part2Test: Long


  def getInputTest: Seq[String] = {
    val source = Source.fromResource(s"test/${this.getClass.getName.toLowerCase}.txt")
    try {
      source.getLines().toSeq
    } finally {
      source.close()
    }
  }

  def printResultTest: Unit = {
    println(s"::${this.getClass.getName} - Test data::")
    println(s"Part 1 -> ${this.part1Test}")
    println(s"Part 2 -> ${this.part2Test}")
  }
}
