import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import java.awt.Point
import scala.collection.mutable

object Day05 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val seaFloor = new mutable.HashMap[Point, Int]()
    val walls: Seq[(Point, Point)] = getLines(strings)
      .filter(l => l._1.x == l._2.x || l._1.y == l._2.y)

    walls.flatMap(l => pointsBetween(l._1, l._2)).foreach(point => {
      if (!seaFloor.contains(point)) {
        seaFloor.put(point, 0)
      }
      seaFloor.put(point, seaFloor(point) + 1)
    })

    seaFloor.count(t => t._2 > 1)
  }

  override def part2(strings: Seq[String]): Long = {
    val seaFloor = new mutable.HashMap[Point, Int]()
    val walls: Seq[(Point, Point)] = getLines(strings)
    walls.flatMap(l => pointsBetween(l._1, l._2)).foreach(point => {
      if (!seaFloor.contains(point)) {
        seaFloor.put(point, 0)
      }
      seaFloor.put(point, seaFloor(point) + 1)
    })

    seaFloor.count(t => t._2 > 1)
  }


  private def getLines(strings: Seq[String]): Seq[(Point, Point)] = {
    strings
      .map(s => {
        val points = s.split("->").map(s1 => {
          val split = s1.strip().split(",").map(_.toInt)
          new Point(split.head, split.last)
        }).toSeq
        (points.head, points.last)
      })
  }

  def pointsBetween(a: Point, b: Point): Seq[Point] = {
    if (a.x == b.x) {
      pointsBetweenVertical(a, b)
    } else if (a.y == b.y) {
      pointsBetweenHorizontal(a, b)
    } else {
      pointsBetweenDiagonal(a, b)
    }
  }


  def pointsBetweenVertical(a: Point, b: Point): Seq[Point] = {
    (math.min(a.y, b.y) to math.max(a.y, b.y)).map(y => {
      new Point(a.x, y)
    })
  }

  def pointsBetweenHorizontal(a: Point, b: Point): Seq[Point] = {
    (math.min(a.x, b.x) to math.max(a.x, b.x)).map(x => {
      new Point(x, a.y)
    })
  }

  def pointsBetweenDiagonal(a: Point, b: Point): Seq[Point] = {
    val diff = a.x - b.x
    val ydiff = a.y - b.y
    (0 to math.abs(diff)).map(i => {
      if (diff > 0) {
        if (ydiff > 0) {
          new Point(a.x - i, a.y - i)
        } else {
          new Point(a.x - i, a.y + i)
        }
      } else {
        if (ydiff > 0) {
          new Point(a.x + i, a.y - i)
        } else {
          new Point(a.x + i, a.y + i)
        }
      }
    })
  }
}
