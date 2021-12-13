import aoc.numeric.AoCPart1Test
import aoc.string.AoCPart2StringTest

object Day13 extends App with AoCPart1Test with AoCPart2StringTest {

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

  override def part2(strings: Seq[String]): String = {
    val points: Seq[(Int, Int)] = strings.takeWhile(s => !s.isEmpty).map(s => s.strip().split(",").map(_.toInt)).map(l => (l.head, l.last))
    val folds = strings.filter(s => s.startsWith("fold along")).map(s => s.split(" ").last).map(s => (s.split("=").head, s.split("=").last.toInt))
    var output = points.toSet
    for ((axis, value) <- folds) {
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
    val sb = new StringBuilder()
    sb.append("\n")
    for (y <- 0 until 6) {
      sb.append((0 until (40)).map(x => if (output.contains(x, y)) "#" else ".").reduce(_ + _).stripMargin('.'))
      sb.append("\n")
    }

    sb.toString()
  }
}
