import scala.io.Source

trait AoC {
  def part1: Long
  def part2: Long


  def getInput: Seq[String] = {
    val source = Source.fromResource(s"${this.getClass.getName.toLowerCase}.txt")
    try {
      source.getLines().toSeq
    } finally {
      source.close()
    }
  }

  def printResult: Unit = {
    println(s"::${this.getClass.getName}::")
    println(s"Part 1 -> ${this.part1}")
    println(s"Part 2 -> ${this.part2}")
  }
}
