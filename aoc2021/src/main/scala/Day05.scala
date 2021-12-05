import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import java.awt.Point
import scala.collection.mutable

object Day05 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val seaFloor = new mutable.HashMap[Point, Int]()
    val walls: Seq[(Point, Point)] = strings
      .map(s => {
        val points = s.split("->").map(s1 => {
          val split = s1.strip().split(",").map(_.toInt)
          new Point(split.head, split.last)
        }).toSeq
        (points.head, points.last)
      }).filter(l => l._1.x == l._2.x || l._1.y == l._2.y)

    walls.foreach(l => {
      if (l._1.x == l._2.x) {
        (math.min(l._1.y, l._2.y) to math.max(l._1.y, l._2.y)).foreach(y => {
          val point = new Point(l._1.x, y)
          if (!seaFloor.contains(point)) {
            seaFloor.put(point, 0)
          }
          seaFloor.put(point, seaFloor(point) + 1)
        })
      } else if (l._1.y == l._2.y) {
        (math.min(l._1.x, l._2.x) to math.max(l._1.x, l._2.x)).foreach(x => {
          val point = new Point(x, l._1.y)
          if (!seaFloor.contains(point)) {
            seaFloor.put(point, 0)
          }
          seaFloor.put(point, seaFloor(point) + 1)
        })
      } else {
        println("curved")
      }
    })
    seaFloor.count(t => t._2 > 1)
  }

  override def part2(strings: Seq[String]): Long = -1
}
