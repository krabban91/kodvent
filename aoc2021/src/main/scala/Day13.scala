import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day13 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val points: Seq[(Int, Int)] = strings.takeWhile(s => !s.isEmpty).map(s => s.strip().split(",").map(_.toInt)).map(l => (l.head, l.last))
    val folds = strings.filter(s => s.startsWith("fold along")).map(s => s.split(" ").last).map(s => (s.split("=").head, s.split("=").last.toInt))
    var output = points.toSet
    for ((axis, value) <- Seq(folds.head)) {
      output = output.map(t => {
        if (axis == "x") {
          if (t._1 < value) {
            t
          } else {
            (t._1 - (t._1 - value) * 2, t._2)
          }
        } else {
          if (t._2 < value) {
            t
          } else {
            (t._1, t._2 - (t._2 - value) * 2)
          }
        }
      })
    }

    output.size
  }

  override def part2(strings: Seq[String]): Long = -1
}
