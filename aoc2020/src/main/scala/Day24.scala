import java.awt.Point

import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day24 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val inp = strings.map(TileMove(_))
    val map = mutable.Map[Point, Boolean]()
    inp.foreach(p => map(p.destination(new Point(0, 0))) = !map.getOrElse(p.destination(new Point(0, 0)), false))
    map.values.count(v => v)
  }

  override def part2(strings: Seq[String]): Long = {
    val inp = strings.map(TileMove(_))
    var map = mutable.Map[Point, Boolean]()
    inp.foreach(p => map(p.destination(new Point(0, 0))) = !map.getOrElse(p.destination(new Point(0, 0)), false))
    for (day <- Range(0, 100)) {
      val next = map.clone()
      val minX = next.filter(t => t._2).map(_._1.x).min
      val minY = next.filter(t => t._2).map(_._1.y).min
      val maxX = next.filter(t => t._2).map(_._1.x).max
      val maxY = next.filter(t => t._2).map(_._1.y).max
      for (x <- Range(minX - 2, maxX + 2); y <- Range(minY - 2, maxY + 2)) {
        val p = new Point(x, y)
        val curr = map.getOrElse(p, false)
        val neighbors = TileMove.adjacent(p)
        val count = neighbors.flatMap(v => map.get(v)).map(v => if (v) 1 else 0).sum
        if (curr) {
          next(p) = count == 1 || count == 2
        } else {
          next(p) = count == 2
        }
      }
      map = next
    }
    map.values.count(v => v)
  }

  case class TileMove(moves: Seq[String]) {

    def move(direction: String, from: Point): Point = {
      direction match {
        case "se" =>
          if (from.x % 2 == 0) {
            new Point(from.x + 1, from.y + 1)
          } else {
            new Point(from.x + 1, from.y)
          }
        case "e" =>
          new Point(from.x + 2, from.y)
        case "ne" =>
          if (from.x % 2 == 0) {
            new Point(from.x + 1, from.y)
          } else {
            new Point(from.x + 1, from.y - 1)
          }
        case "nw" =>
          if (from.x % 2 == 0) {
            new Point(from.x - 1, from.y)
          } else {
            new Point(from.x - 1, from.y - 1)
          }
        case "w" =>
          new Point(from.x - 2, from.y)

        case "sw" =>
          if (from.x % 2 == 0) {
            new Point(from.x - 1, from.y + 1)
          } else {
            new Point(from.x - 1, from.y)
          }
        case _ => println("bad direction")
          from
      }
    }

    def destination(from: Point): Point = {
      var p = from
      moves.foreach(v => p = move(v, p))
      p
    }
  }

  object TileMove {
    def apply(string: String): TileMove = {
      var iter = string
      val l = mutable.ListBuffer[String]()
      while (!iter.isBlank) {
        if (iter.startsWith("se")) {
          l.addOne("se")
          iter = iter.replaceFirst("se", "")
        } else if (iter.startsWith("e")) {
          l.addOne("e")
          iter = iter.replaceFirst("e", "")
        } else if (iter.startsWith("ne")) {
          l.addOne("ne")
          iter = iter.replaceFirst("ne", "")
        } else if (iter.startsWith("sw")) {
          l.addOne("sw")
          iter = iter.replaceFirst("sw", "")
        } else if (iter.startsWith("w")) {
          l.addOne("w")
          iter = iter.replaceFirst("w", "")
        } else if (iter.startsWith("nw")) {
          l.addOne("nw")
          iter = iter.replaceFirst("nw", "")
        }
      }
      TileMove(l.toSeq)
    }

    def adjacent(point: Point): Seq[Point] = {
      val delta = if (point.x % 2 == 0) {
        Seq(new Point(1, 1), new Point(2, 0), new Point(1, 0), new Point(-1, 0), new Point(-2, 0), new Point(-1, 1))
      } else {
        Seq(new Point(1, 0), new Point(2, 0), new Point(1, -1), new Point(-1, -1), new Point(-2, 0), new Point(-1, 0))
      }
      delta.map(d => new Point(point.x + d.x, point.y + d.y))
    }
  }

}
