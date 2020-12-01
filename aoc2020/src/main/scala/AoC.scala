import scala.io.Source

trait AoC {

  def part1(strings: Seq[String]): Long

  def part2(strings: Seq[String]): Long

  def getInput: Seq[String] = {
    val source = Source.fromResource(s"${this.getClass.getName.toLowerCase}.txt")
    try {
      source.getLines().toSeq
    } finally {
      source.close()
    }
  }

  def part1Result: Long = part1(getInput)

  def part2Result: Long = part2(getInput)

  def printResult: Unit = {
    println(s"::${this.getClass.getName}::")
    println(s"Part 1 -> ${this.part1Result}")
    println(s"Part 2 -> ${this.part2Result}")
  }
}
