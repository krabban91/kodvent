import java.awt.Point

import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.grid.HexGrid

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object Day24 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = createFloor(strings).sum(v => if (v.alive) 1 else 0)

  override def part2(strings: Seq[String]): Long = {
    var floor: HexGrid[Conway] = createFloor(strings)
    Range(0, 100).foreach(day => {
      floor = floor.shrink((t, _) => t.alive).expand(_ => Conway(alive = false), 1)
      floor = floor.map((ht, point) => ht.conway(floor.getSurroundingTiles(point).asScala.toSeq))
    })
    floor.sum(v => if (v.alive) 1 else 0)
  }

  private def createFloor(strings: Seq[String]): HexGrid[Conway] = {
    val floor = mutable.Map[Point, Conway]()
    strings.map(HexPoint(_)).foreach(p => floor(p) = floor.getOrElse(p, Conway(alive = false)).toggle)
    val minQ = floor.filter(t => t._2.alive).map(_._1.x).min
    val minR = floor.filter(t => t._2.alive).map(_._1.y).min
    val maxQ = floor.filter(t => t._2.alive).map(_._1.x).max
    val maxR = floor.filter(t => t._2.alive).map(_._1.y).max
    val value = new HexGrid[Conway](Range(minR, maxR + 1).map(y => Range(minQ, maxQ + 1).map(x => floor.getOrElse(new Point(x, y), Conway(alive = false))).asJava).asJava)
    value
  }

  case class Conway(alive: Boolean) {

    def toggle: Conway = Conway(!alive)

    def conway(neighbors: Seq[Conway]): Conway = {
      val count = neighbors.map(v => if (v.alive) 1 else 0).sum
      if (alive) {
        Conway(count == 1 || count == 2)
      } else {
        Conway(count == 2)
      }
    }
  }

  object HexPoint {

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

    def apply(string: String): Point = {
      rec(string, Seq()).foldLeft(new Point(0, 0))((v, s) => HexGrid.move(s, v))
    }
  }

}
