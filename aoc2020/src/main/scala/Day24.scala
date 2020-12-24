import java.awt.Point

import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day24 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val floor: mutable.Map[Point, HexTile] = createFloor(strings)
    floor.values.count(v => v.black)
  }

  override def part2(strings: Seq[String]): Long = {
    var floor: mutable.Map[Point, HexTile] = createFloor(strings)
    for (day <- Range(0, 100)) {
      val next = floor.clone()
      val minX = next.filter(t => t._2.black).map(_._1.x).min
      val minY = next.filter(t => t._2.black).map(_._1.y).min
      val maxX = next.filter(t => t._2.black).map(_._1.x).max
      val maxY = next.filter(t => t._2.black).map(_._1.y).max
      Range(minX - 2, maxX + 2).foreach(x => Range(minY - 2, maxY + 2).foreach(y => {
        val p = new Point(x, y)
        val current = floor.getOrElse(p, HexTile(p, black = false))
        val neighbors = current.neighbors.flatMap(v => floor.get(v))
        next(p) = current.conway(neighbors)
      }))
      floor = next
    }
    floor.values.count(v => v.black)
  }

  private def createFloor(strings: Seq[String]): mutable.Map[Point, HexTile] = {
    val floor = mutable.Map[Point, HexTile]()
    strings.map(HexTile(_)).foreach(p => floor(p.point) = floor.getOrElse(p.point, HexTile(p.point, black = false)).toggle)
    floor
  }

  case class HexTile(point: Point, black: Boolean) {
    private val evens = Map("se" -> new Point(1, 1), "e" -> new Point(2, 0), "ne" -> new Point(1, 0), "nw" -> new Point(-1, 0), "w" -> new Point(-2, 0), "sw" -> new Point(-1, 1))
    private val odds = Map("se" -> new Point(1, 0), "e" -> new Point(2, 0), "ne" -> new Point(1, -1), "nw" -> new Point(-1, -1), "w" -> new Point(-2, 0), "sw" -> new Point(-1, 0))

    def move(direction: String): HexTile = {
      val dirs = if (point.x % 2 == 0) evens else odds
      val delta = dirs(direction)
      HexTile(new Point(point.x + delta.x, point.y + delta.y), black)
    }

    def toggle: HexTile = HexTile(point, !black)

    def conway(neighbors: Seq[HexTile]): HexTile = {
      val count = neighbors.map(v => if (v.black) 1 else 0).sum
      if (black) {
        HexTile(point, count == 1 || count == 2)
      } else {
        HexTile(point, count == 2)
      }
    }

    def neighbors: Seq[Point] = (if (point.x % 2 == 0) evens.values else odds.values)
      .map(d => new Point(point.x + d.x, point.y + d.y)).toSeq
  }


  object HexTile {

    object +: {
      def unapply(s: String): Option[(Char, String)] = s.headOption.map(c => (c, s.tail))
    }

    @scala.annotation.tailrec
    def rec(string: String, l: Seq[String]): Seq[String] = {
      string match {
        case 's' +: 'e' +: t => rec(t, l ++ Seq("se"))
        case 's' +: 'w' +: t => rec(t, l ++ Seq("sw"))
        case 'n' +: 'e' +: t => rec(t, l ++ Seq("ne"))
        case 'n' +: 'w' +: t => rec(t, l ++ Seq("nw"))
        case 'e' +: t => rec(t, l ++ Seq("e"))
        case 'w' +: t => rec(t, l ++ Seq("w"))
        case _ => l
      }
    }

    def apply(string: String): HexTile = {
      rec(string, Seq()).foldLeft(HexTile(new Point(0, 0), black = false))((v, s) => v.move(s))
    }
  }

}
