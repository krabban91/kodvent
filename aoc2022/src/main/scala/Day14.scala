import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day14 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val paths = strings.map(string => string.split(" -> ").map(s => s.split(",")).map(v => (v.head.toInt, v.last.toInt)).toSeq)
    val sandMap = mutable.HashMap[(Int, Int), String]()

    paths.foreach(_.sliding(2).map(l => (l.head, l.last))
      .foreach{ case ((fx, fy), (tx, ty)) =>
        if (fx == tx) {
          //y
          val (minY, maxY) = if (fy < ty) (fy, ty) else (ty, fy)
          (minY to maxY).foreach(y => sandMap.put((fx, y), "#"))
        } else {
          //x
          val (minX, maxX) = if (fx < tx) (fx, tx) else (tx, fx)
          (minX to maxX).foreach(x => sandMap.put((x, fy), "#"))
        }
      })
    val lowestY = paths.map(_.map(_._2).max).max
    val sandSource = (500,0)
    logMap(sandMap)

    var lowestSand = sandSource._2
    while (lowestSand < lowestY){
      var sand = sandSource
      var moves = true
      while (moves && lowestSand < lowestY) {

        val below = (sand._1, sand._2 + 1)
        if(sandMap.keySet.contains(below)){
          val belowLeft = (below._1 -1, below._2)
          val belowRight = (below._1 +1, below._2)
          if(!sandMap.keySet.contains(belowLeft)){
            //belowLeft
            sand = belowLeft
          } else if(!sandMap.keySet.contains(belowRight)) {
            //belowRight
            sand = belowRight
          } else {
            //stay
            sandMap.put(sand, "o")
            println(sand)
            moves = false
            logMap(sandMap)
          }
        } else {
          //below
          sand = below
        }


        lowestSand = math.max(sand._2, lowestSand)
      }


    }
    logMap(sandMap)
    val i = sandMap.values.count(_ == "o")
    i
  }


  private def logMap(sandMap: mutable.HashMap[(Int, Int), String]) = {
    val java = sandMap.map(kv => (new Point(kv._1._1, kv._1._2), kv._2)).asJava
    println(new LogUtils[String].mapToText(java, v => if (v == null) "." else v))
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
