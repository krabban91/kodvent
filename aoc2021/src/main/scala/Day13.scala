import aoc.numeric.AoCPart1Test
import aoc.string.AoCPart2StringTest

object Day13 extends App with AoCPart1Test with AoCPart2StringTest {

  override def part1(strings: Seq[String]): Long = {
    val (points: Seq[(Int, Int)], folds: Seq[(String, Int)]) = groupInput(strings)
    val (axis, value) = folds.head
    fold(points.toSet, axis, value).size
  }

  override def part2(strings: Seq[String]): String = {
    val (points: Seq[(Int, Int)], folds: Seq[(String, Int)]) = groupInput(strings)
    var output = points.toSet
    for ((axis, value) <- folds) {
      output = fold(output, axis, value)
    }
    asString(output)
  }

  private def groupInput(strings: Seq[String]): (Seq[(Int, Int)], Seq[(String, Int)]) = {
    val points: Seq[(Int, Int)] = strings.takeWhile(s => s.nonEmpty).map(s => s.strip().split(",").map(_.toInt)).map(l => (l.head, l.last))
    val folds: Seq[(String, Int)] = strings.filter(s => s.startsWith("fold along")).map(s => s.split(" ").last).map(s => (s.split("=").head, s.split("=").last.toInt))
    (points, folds)
  }

  private def fold(points: Set[(Int, Int)], axis: String, value: Int): Set[(Int, Int)] = {
    points.map(t => {
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

  private def asString(points: Set[(Int, Int)]) = {
    val sb = new StringBuilder()
    sb.append("\n")
    for (y <- 0 until 6) {
      sb.append((0 until (40)).map(x => if (points.contains(x, y)) "#" else ".").reduce(_ + _).stripMargin('.'))
      sb.append("\n")
    }

    sb.toString()
  }

}
