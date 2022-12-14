import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.annotation.tailrec
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day14 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val sandMap = buildMap(strings)
    pourSand(sandMap, bottomless = true).values.count(_ == "o")
  }

  override def part2(strings: Seq[String]): Long = {
    val sandMap: mutable.HashMap[(Int, Int), String] = buildMap(strings)
    pourSand(sandMap, bottomless = false).values.count(_ == "o")
  }

  private def pourSand(sandMap: mutable.HashMap[(Int, Int), String], bottomless: Boolean): mutable.HashMap[(Int, Int), String] = {
    val sandSource = (500, 0)
    val lowestY = sandMap.keys.map(_._2).max + 2
    var sand = nextSand(sandSource, sandMap, lowestY, bottomless)
    while (sand.isDefined && !sandMap.contains(sandSource)) {
      sandMap.put(sand.get, "o")
      sand = nextSand(sandSource, sandMap, lowestY, bottomless)
    }
    //logMap(sandMap)
    sandMap
  }

  @tailrec
  private def nextSand(sand: (Int, Int), lookup: mutable.HashMap[(Int, Int), String], bottomY: Int, bottomless: Boolean): Option[(Int, Int)] = {
    val below = (sand._1, sand._2 + 1)
    if (below._2 == bottomY) {
      val maybeTuple = Some(sand).filterNot(_ => bottomless)
      maybeTuple
    } else if (!lookup.contains(below)) {
      nextSand(below, lookup, bottomY, bottomless)
    } else {
      val belowLeft = (below._1 - 1, below._2)
      val belowRight = (below._1 + 1, below._2)
      if (!lookup.keySet.contains(belowLeft)) {
        nextSand(belowLeft, lookup, bottomY, bottomless)
      } else if (!lookup.keySet.contains(belowRight)) {
        nextSand(belowRight, lookup, bottomY, bottomless)
      } else {
        Some(sand)
      }
    }
  }

  private def buildMap(strings: Seq[String]): mutable.HashMap[(Int, Int), String] = {
    val paths = strings.map(string => string.split(" -> ").map(s => s.split(",")).map(v => (v.head.toInt, v.last.toInt)).toSeq)
    val sandMap = mutable.HashMap[(Int, Int), String]()

    paths.foreach(_.sliding(2).map(l => (l.head, l.last))
      .foreach { case ((fx, fy), (tx, ty)) =>
        if (fx == tx) {
          val (minY, maxY) = if (fy < ty) (fy, ty) else (ty, fy)
          (minY to maxY).foreach(y => sandMap.put((fx, y), "#"))
        } else {
          val (minX, maxX) = if (fx < tx) (fx, tx) else (tx, fx)
          (minX to maxX).foreach(x => sandMap.put((x, fy), "#"))
        }
      })
    sandMap
  }

  private def logMap(sandMap: mutable.HashMap[(Int, Int), String]) = {
    val java = sandMap.map(kv => (new Point(kv._1._1, kv._1._2), kv._2)).asJava
    println(new LogUtils[String].mapToText(java, v => if (v == null) "." else v))
  }

}
