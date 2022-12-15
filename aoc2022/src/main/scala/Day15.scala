import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(Sensor(_))
    val beacons = mutable.HashMap[(Int, Int), String]()
    v.foreach(sensor => {
      val distance = sensor.manhattan
      val minY = (sensor.location._2 - (distance-1))
      val maxY = (sensor.location._2 + (distance-1))
      if((minY<= 10 && 10 <= maxY) || (minY<= 2000000 && 2000000 <= maxY)){
      Seq(10, 2000000).foreach(y => {
        val diff = math.abs(sensor.location._2 - y)
        val distanceLeft = distance - diff
        val minX = (sensor.location._1 - (distanceLeft))
        val maxX = (sensor.location._1 + (distanceLeft))
          (minX to maxX).foreach(x => {
            val str = beacons.getOrElse((x, y), ".")
            if(!(str == "S" || str == "B")) {
              beacons.put((x,y), "#")
            }
          })
        })
      }
      beacons.put(sensor.closest, "B")
      beacons.put(sensor.location, "S")
      //logMap(beacons)
    })
    val r10 = beacons.filter(kv => kv._1._2 == 10 && (kv._2=="#" ||kv._2 == "S"))

    val filtered = beacons.filter(kv => kv._1._2 == 2000000).values

    val size = filtered.count(v => v == "#" || v == "S")
    if (size!= 0) size else r10.size
  }

  override def part2(strings: Seq[String]): Long = {
    -1
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
