import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import java.awt.Point

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val res: Point = strings.map(s => s.split(" "))
      .map(a => {
        val in = a.last.toInt
        a.head match {
          case "forward" => new Point(in, 0)
          case "down" => new Point(0, in)
          case "backward" => new Point(-in, 0)
          case _ => new Point(0, -in)
        }
      })
      .reduce((a, b) => new Point(a.x + b.x, a.y + b.y))
    res.x * res.y
  }

  override def part2(strings: Seq[String]): Long = {
    val (end, _) = strings.map(s => s.split(" "))
      .map(a => (a.head, a.last.toInt))
      .foldLeft((new Point(), 0))((t, in) => {
        val (loc, aim) = t
        in._1 match {
          case "forward" => (new Point(loc.x + in._2, loc.y + in._2 * aim), aim)
          case "down" => (loc, aim + in._2)
          case "backward" => (new Point(loc.x - in._2, loc.y - in._2 * aim), aim)
          case _ => (loc, aim - in._2)
        }
      })
    end.x * end.y
  }
}
