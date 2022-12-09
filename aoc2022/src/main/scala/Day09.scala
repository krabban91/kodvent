import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.Distances
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  // Distances


  override def part1(strings: Seq[String]): Long = {
    val directions = Map(
      "L" -> new Point(-1, 0),
      "R" -> new Point(1, 0),
      "U" -> new Point(0, -1),
      "D" -> new Point(0, 1),
    )

    val tail = new Point(0, 0)
    val head = new Point(0, 0)
    val tailPositions = mutable.HashMap[Point, String]()
    tailPositions.put(new Point(tail.x, tail.y), "#")
    val inputPattern = """(.+) (.+)""".r
    strings.foreach { case inputPattern(d, times) =>
      val dir = directions(d)
      (1 to times.toInt).foreach(i => {
        head.move(head.x + dir.x, head.y + dir.y)
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
            tailPositions.put(new Point(tail.x, tail.y), "#")
          }
          else if (tail.x != head.x) {
            tail.move(tail.x + (head.x - tail.x) / 2, tail.y + (head.y - tail.y) / 2)
            tailPositions.put(new Point(tail.x, tail.y), "#")

          } else if (tail.y != head.y) {
            tail.move(tail.x + (head.x - tail.x) / 2, tail.y + (head.y - tail.y) / 2)
            tailPositions.put(new Point(tail.x, tail.y), "#")
          }
        }
        // println(new LogUtils[String].mapToText(tailPositions.asJava, v => if(v == null) "." else v))
      })
    }
    tailPositions.keys.size
  }

  override def part2(strings: Seq[String]): Long = {
    val directions = Map(
      "L" -> new Point(-1, 0),
      "R" -> new Point(1, 0),
      "U" -> new Point(0, -1),
      "D" -> new Point(0, 1),
    )

    val snake = (1 to 10).map(_ => new Point(0, 0))
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
    tailPositions.keys.size
  }
}
