import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.Distances

import java.awt.Point
import scala.collection.mutable

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = followTail(strings, 2).size

  override def part2(strings: Seq[String]): Long = followTail(strings, 10).size

  private def followTail(strings: Seq[String], length: Int): Set[Point] = {
    val directions = Map(
      "L" -> new Point(-1, 0),
      "R" -> new Point(1, 0),
      "U" -> new Point(0, -1),
      "D" -> new Point(0, 1),
    )

    val snake = (1 to length).map(_ => new Point(0, 0))
    val tailPositions = mutable.HashMap[Point, String]()
    tailPositions.put(new Point(snake.last.x, snake.last.y), "#")
    val inputPattern = """(.+) (.+)""".r
    strings.foreach { case inputPattern(d, times) =>
      val dir = directions(d)
      (1 to times.toInt).foreach(i => {
        snake.head.move(snake.head.x + dir.x, snake.head.y + dir.y)
        snake.sliding(2).filter(_.size == 2).foreach(l => {
          val head = l.head
          val tail = l.last
          val dist = Distances.manhattan(tail, head)
          if (dist > 1) {
            if (tail.x != head.x && tail.y != head.y && dist > 2) {
              if (tail.x < head.x && tail.y < head.y) {
                tail.move(tail.x + 1, tail.y + 1)
              }
              else if (tail.x < head.x && tail.y > head.y) {
                tail.move(tail.x + 1, tail.y + -1)
              }
              else if (tail.x > head.x && tail.y < head.y) {
                tail.move(tail.x + -1, tail.y + 1)
              }
              else if (tail.x > head.x && tail.y > head.y) {
                tail.move(tail.x + -1, tail.y + -1)
              }
              else {
                println(tail + " and " + head)
              }

            }
            else if (tail.x != head.x) {
              tail.move(tail.x + (head.x - tail.x) / 2, tail.y + (head.y - tail.y) / 2)

            } else if (tail.y != head.y) {
              tail.move(tail.x + (head.x - tail.x) / 2, tail.y + (head.y - tail.y) / 2)
            }
          }
        })
        tailPositions.put(new Point(snake.last.x, snake.last.y), "#")
      })
    }
    //println(new LogUtils[String].mapToText(tailPositions.asJava, v => if(v == null) "." else v))
    tailPositions.keys.toSet
  }
}
