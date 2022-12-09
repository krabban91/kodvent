import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.Distances.manhattan

import java.awt.Point

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = followTail(strings, 2).size

  override def part2(strings: Seq[String]): Long = followTail(strings, 10).size

  private def followTail(strings: Seq[String], length: Int): Set[(Int, Int)] = {
    val directions = Map("L" -> new Point(-1, 0), "R" -> new Point(1, 0), "U" -> new Point(0, -1), "D" -> new Point(0, 1))

    val rope = (1 to length).map(_ => new Point(0, 0))

    val inputPattern = """(.+) (\d+)""".r
    strings.flatMap { case inputPattern(d, times) =>
      val dir = directions(d)
      (0 until times.toInt).map { _ =>
        rope.head.translate(dir.x, dir.y)
        rope.sliding(2).map(l => (l.head, l.last)).foreach {
          case (head, tail) => (tail.translate _).tupled(tailDelta(head, tail))
        }
        (rope.last.x, rope.last.y)
      }
    }.toSet
  }

  private def tailDelta(head: Point, tail: Point): (Int, Int) = {
    val (dx, dy) = (head.x - tail.x, head.y - tail.y)
    if (manhattan(head, tail) > 2) (math.signum(dx), math.signum(dy)) else (dx / 2, dy / 2)
  }

}
