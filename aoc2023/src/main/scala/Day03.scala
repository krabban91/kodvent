import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day03 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Gear(number: Long, adjacent: String) {
    def hasSymbol: Boolean = adjacent.filterNot(_.isDigit).exists(_ != '.')
  }

  override def part1(strings: Seq[String]): Long = {
    val res = deriveGears(strings)
    val value = res.filter(_.hasSymbol)
    println(value)
    value.map(_.number).sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  private def deriveGears(strings: Seq[String]) = {
    strings.indices.flatMap { y =>
      val line = strings(y)
      val mask = line.indices.map(x => (x, line(x).isDigit))
      mask.foldLeft(Seq[Gear]()) { case (l, (x, isDig)) =>
        if (isDig) {
          if (x == 0 || !line(x - 1).isDigit) {
            val n = mask.drop(x).takeWhile { case (x, d) => d }.map(_._1)
            val v = n.foldLeft("") { case (s, v) => s"$s${line(v)}" }.toLong
            val value = (math.max(0, y - 1) to math.min(strings.length - 1, y + 1))
              .flatMap(dy => (math.max(0, x - 1) to math.min(x + n.size + 1, line.length - 1)).map((_, dy)))
            val adj = value
              .filterNot { case (dx, dy) => y == dy && dx >= x && dx <= x + n.size - 1 }
              .map { case (dx, dy) => strings(dy)(dx) }
            l ++ Seq(Gear(v, adj.foldLeft("") { case (s, v) => s"$s$v" }))
          } else {
            l
          }
        } else {
          l
        }
      }
    }
  }

}
