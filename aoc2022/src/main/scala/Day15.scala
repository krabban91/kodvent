import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(Sensor(_))
    val test = v.map(_.location._1).max < 100
    val row = if (test) 10 else 2000000
    val yLimits = Some((row, row))
    val (_, _, lines, _) = chartBeacons(v, yLimits, None)
    val minX = lines(row).map(_._1).min
    val maxX = lines(row).map(_._2).max
    (minX to maxX).count(x => lines(row).exists(p => p._1 <= x && x <= p._2))
  }

  override def part2(strings: Seq[String]): Long = {
    val v = strings.map(Sensor(_))
    val test = v.map(_.location._1).max < 100
    val range = if (test) (0, 20) else (0, 4000000)
    val (_, _, _, potential) = chartBeacons(v, Some(range), Some(range))
    val points = potential.flatMap(kv => kv._2.map(_._1).map(x => (x,kv._1)))
    points.map(p => p._1.toLong * 4000000L + p._2.toLong).head
  }

  private def chartBeacons(v: Seq[Sensor], yLimits: Option[(Int, Int)], xLimits: Option[(Int, Int)]): (Set[(Int, Int)], Set[(Int, Int)], Map[Int, Seq[(Int, Int)]], Map[Int, Seq[(Int, Int)]]) = {
    val sensors = mutable.HashSet[(Int, Int)]()
    val beacons = mutable.HashSet[(Int, Int)]()
    v.foreach(s => sensors.add(s.location))
    v.foreach(s => beacons.add(s.closest))
    val lines = mutable.HashMap[Int, Seq[(Int, Int)]]()
    val potential = mutable.HashMap[Int, Seq[(Int, Int)]]()
    (yLimits.get._1 to yLimits.get._2).foreach(y => {
      val currentPot = Seq((xLimits.map(_._1).getOrElse(Int.MinValue), xLimits.map(_._2).getOrElse(Int.MaxValue)))
      val noBeacons = newPotential(beacons.filter(_._2 == y).map(v => (v._1, v._1)).toSeq, currentPot)
      val noSensors = newPotential(sensors.filter(_._2 == y).map(v => (v._1, v._1)).toSeq, noBeacons)
      potential.put(y, noSensors)

    })
    v.zipWithIndex.foreach{ case (sensor, i) =>
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
        val sOnSame = sensors.filter(_._2 == y).map(_._1).filter(s => xTuple._1 <= s && s <= xTuple._2)
        val bOnSame = beacons.filter(_._2 == y).map(_._1).filter(s => xTuple._1 <= s && s <= xTuple._2)
        val ranges = (sOnSame ++ bOnSame).toSeq.sorted.foldLeft(Seq(xTuple)) {
          case (l, x) => l.dropRight(1) ++ Seq((l.last._1, x - 1), (x + 1, l.last._2)).filterNot(v => v._2 - v._1 < 0)
        }
        val current = lines.getOrElse(y, Seq())
        val joint = current ++ ranges
        lines.put(y, joint)
        val currentPot = potential(y)
        val newPot = newPotential(joint, currentPot)
        potential.put(y, newPot)

      })
    }
    (sensors.toSet, beacons.toSet, lines.toMap, potential.toMap)
  }

  private def newPotential(joint: Seq[(Int, Int)], currentPot: Seq[(Int, Int)]) = {
    joint.foldLeft(currentPot) { case (l, notIn) =>
      val notinside = l.filterNot(v => notIn._1 <= v._1 && v._2 <= notIn._2)
      notinside.flatMap(v => {
        if (notIn._1 <= v._1 && v._2 <= notIn._2) {
          //      ()
          // (________)
          //
          Seq() //already covered above
        } else if (v._1 <= notIn._1 && notIn._2 <= v._2) {
          // (________)
          //      ()
          // (___)  (_)
          Seq((v._1, notIn._1 - 1), (notIn._2 + 1, v._2))
        } else if (v._1 <= notIn._2 && notIn._2 <= v._2) {
          //    (____) pot
          // (____)    notIn
          //       (_)
          Seq((notIn._2 + 1, v._2))
        } else if (notIn._1 <= v._2 && v._2 <= notIn._2) {
          // (____)     pot
          //    (____)  notIn
          // (_)
          Seq((v._1, notIn._1 - 1))
        } else {
          // (____)         pot
          //        (____)  notIn
          // (____)
          Seq(v)
        }
      })
    }.filterNot(v => v._2 - v._1 < 0)
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

}
