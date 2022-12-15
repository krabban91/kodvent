import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(Sensor(_))
    val test = v.map(_.location._1).max < 100
    val row = if (test) 10 else 2000000
    val yLimits = Some((row, row))
    val beacons = chartBeacons(v, yLimits, None)

    val filtered = beacons.filter(kv => kv._1._2 == row).values

    val size = filtered.count(v => v == "#" || v == "S")
    size
  }

  override def part2(strings: Seq[String]): Long = {

    val v = strings.map(Sensor(_))
    val test = v.map(_.location._1).max < 100
    val range = if (test) (0, 20) else (0, 4000000)
    val beacons = chartBeacons(v, Some(range), Some(range))

    val points = (range._1 to range._2).flatMap(x => (range._1 to range._2).map(y => (x, y)))
    val maybeTuple = points.find(!beacons.contains(_))
    // test
    val maybeLong = maybeTuple.map(p => p._1.toLong * 4000000L + p._2.toLong)
    maybeLong.get
  }

  private def chartBeacons(v: Seq[Sensor], yLimits: Option[(Int, Int)], xLimits: Option[(Int, Int)]): Map[(Int, Int), String] = {
    val beacons = mutable.HashMap[(Int, Int), String]()
    v.foreach(sensor => {
      val distance = sensor.manhattan
      val minY = (sensor.location._2 - (distance - 1))
      val maxY = (sensor.location._2 + (distance - 1))
      val yTuple = (yLimits.map(_._1).map(math.max(_, minY)).getOrElse(minY), yLimits.map(_._2).map(math.min(_, maxY)).getOrElse(maxY))
      (yTuple._1 to yTuple._2).foreach(y => {
        val diff = math.abs(sensor.location._2 - y)
        val distanceLeft = distance - diff
        val minX = (sensor.location._1 - (distanceLeft))
        val maxX = (sensor.location._1 + (distanceLeft))
        val xTuple = (xLimits.map(_._1).map(math.max(_, minX)).getOrElse(minX), xLimits.map(_._2).map(math.min(_, maxX)).getOrElse(maxX))
        (xTuple._1 to xTuple._2).foreach(x => {
          val str = beacons.getOrElse((x, y), ".")
          if (!(str == "S" || str == "B")) {
            beacons.put((x, y), "#")
          }
        })
      })
      beacons.put(sensor.closest, "B")
      beacons.put(sensor.location, "S")
      //logMap(beacons)
    })
    beacons.toMap
  }

  case class Sensor(location: (Int, Int), closest: (Int, Int)) {
    def manhattan: Int = {
      math.abs(location._1 - closest._1) + math.abs(location._2 - closest._2)
    }
  }

  object Sensor {
    def apply(string: String): Sensor = {
      val pattern = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".r
      string match {
        case pattern(sx, sy, bx, by) => Sensor((sx.toInt, sy.toInt), (bx.toInt, by.toInt))
      }
    }
  }

  private def logMap(sandMap: mutable.HashMap[(Int, Int), String]) = {
    val java = sandMap.map(kv => (new Point(kv._1._1, kv._1._2), kv._2)).asJava
    println(new LogUtils[String].mapToText(java, v => if (v == null) "." else v))
  }

}
