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

  override def part2(strings: Seq[String]): Long = -1

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
  }

}
