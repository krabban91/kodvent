import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.{LogUtils, Loggable}

import java.awt.Point
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val s = getAmphipods(strings)
    val pq = mutable.PriorityQueue[(Seq[Amphipod], Long, Long, Seq[Amphipod])]()(Ordering.Long.reverse.on(_._3))
    val prevMap = mutable.HashMap[Seq[Amphipod], (Seq[Amphipod], Long)]()
    val checked = mutable.HashSet[Seq[Amphipod]]()
    pq.addOne((s, 0, s.map(_.heuristic(s)).sum, Seq()))
    var reachedGoal: Option[(Seq[Amphipod], Long)] = None
    while (pq.nonEmpty && reachedGoal.isEmpty) {
      val (pods, cost, heur, prev) = pq.dequeue()
      //logLocs(pods, cost, heur)
      if (!checked.contains(pods)) {
        prevMap.put(pods, (prev, cost))
        checked.add(pods)
        if (isGoal(pods)) {
          reachedGoal = Option((pods, cost))
        } else {
          val next = pods
            .flatMap(_.possibleMoves(pods))
            .filterNot(t => checked.contains(t._1))
            .map(t => (t._1, cost + t._2, cost + t._2 + t._1.map(_.heuristic(t._1)).sum, pods))
          pq.addAll(next)
        }
      }
    }
    logPath(reachedGoal.get._1, prevMap.toMap, 0, reachedGoal.get._2)

    reachedGoal.get._2
  }

  private def logPath(pods: Seq[Amphipod], prevMap: Map[Seq[Amphipod], (Seq[Amphipod], Long)], level: Int, cost: Long): Unit = {
    val prev = prevMap.get(pods)
    if (prev.isDefined) {
      logPath(prev.get._1, prevMap, level - 1, prev.get._2)
    }
    val mm = pods.map(p => (new Point(p.location._1, p.location._2), p)).toMap
    val logger = new LogUtils[Amphipod]()
    println(logger.mapToText(mm.asJava, p => if (p == null) "." else p.name))
    println(s"Path: level=$level. Cost=${cost}")
  }

  private def logLocs(pods: Seq[Amphipod], cost: Long, heuristic: Long) = {
    println(s"cost: $cost, heuristic: $heuristic, pods: ")
    val mm = pods.map(p => (new Point(p.location._1, p.location._2), p)).toMap
    val logger = new LogUtils[Amphipod]()
    println(logger.mapToText(mm.asJava, p => if (p == null) "." else p.name))
  }

  private def getAmphipods(strings: Seq[String]): Seq[Amphipod] = {
    strings.zipWithIndex.flatMap(ty => ty._1.zipWithIndex.filterNot(tx => Seq('#', '.', ' ').contains(tx._1)).map(tx => Amphipod(tx._1.toString, (tx._2, ty._2))))
  }

  override def part2(strings: Seq[String]): Long = -1

  def isGoal(locations: Seq[Amphipod]): Boolean = {
    val as = locations.filter(_.name == "A").map(_.location).sortBy(_._2)
    val bs = locations.filter(_.name == "B").map(_.location).sortBy(_._2)
    val cs = locations.filter(_.name == "C").map(_.location).sortBy(_._2)
    val ds = locations.filter(_.name == "D").map(_.location).sortBy(_._2)
    as == Seq((3, 2), (3, 3)) &&
      bs == Seq((5, 2), (5, 3)) &&
      cs == Seq((7, 2), (7, 3)) &&
      ds == Seq((9, 2), (9, 3))
  }

  case class Amphipod(name: String, location: (Int, Int)) extends Loggable {

    def energyToMove: Long = {
      name match {
        case "A" => 1
        case "B" => 10
        case "C" => 100
        case "D" => 1000
      }
    }

    def heuristic(currentLocations: Seq[Amphipod]): Long = {
      val wantedX = Map("A" -> 3, "B" -> 5, "C" -> 7, "D" -> 9)
      val x = wantedX(name)
      val steps = if (location._1 != x) {
        if (location._2 == 1) {
          1 + math.abs(x - location._1)
        } else {
          location._2 - 1 + 1 + math.abs(x - location._1)
        }
      } else {
        if (location._2 == 2 && currentLocations.find(p => p.location == (location._1, location._2 + 1)).forall(_.name != name)) {
          2 + 2
        } else {
          0
        }
      }
      energyToMove * steps
    }

    def possibleMoves(currentLocations: Seq[Amphipod]): Seq[(Seq[Amphipod], Long)] = {
      val currY = this.location._2
      val currX = this.location._1
      val without = currentLocations.filterNot(_ == this)
      val potential = mutable.ListBuffer[(Amphipod, Long)]()
      val hallwayLocs = Seq(1, 2, 4, 6, 8, 10, 11)
      if (currY == 1) {
        val w = wantedLocations(name)
        w.foreach(t => {
          val up = without.find(p => p.location == (t._1, 2))
          val down = without.find(p => p.location == (t._1, 3))
          if (t._2 == 2) {
            if (down.exists(p => p.name == name)) {
              if (t._1 < currX) {
                val inWay = hallwayLocs.filter(v => v >= t._1 && v <= currX)
                if (inWay.forall(ox => !without.exists(_.location == (ox, 1)))) {
                  potential.addOne((Amphipod(name, t), (math.abs(currX - t._1) + math.abs(currY - t._2)) * energyToMove))
                }
              } else {
                val inWay = hallwayLocs.filter(v => v >= currX && v <= t._1)
                if (inWay.forall(ox => !without.exists(_.location == (ox, 1)))) {
                  potential.addOne((Amphipod(name, t), (math.abs(currX - t._1) + math.abs(currY - t._2)) * energyToMove))
                }
              }
            }
          } else {
            if (up.isEmpty) {
              if (t._1 < currX) {
                val inWay = hallwayLocs.filter(v => v >= t._1 && v <= currX)
                if (inWay.forall(ox => !without.exists(_.location == (ox, 1)))) {
                  potential.addOne((Amphipod(name, t), (math.abs(currX - t._1) + math.abs(currY - t._2)) * energyToMove))
                }
              } else {
                val inWay = hallwayLocs.filter(v => v >= currX && v <= t._1)
                if (inWay.forall(ox => !without.exists(_.location == (ox, 1)))) {
                  potential.addOne((Amphipod(name, t), (math.abs(currX - t._1) + math.abs(currY - t._2)) * energyToMove))
                }
              }
            }
          }
        })
      } else if (currY == 2) {
        for (x <- hallwayLocs) {
          if (x < currX) {
            val inWay = hallwayLocs.filter(v => v >= x && v <= currX)
            if (inWay.forall(ox => !without.exists(_.location == (ox, 1)))) {
              potential.addOne((Amphipod(name, (x, 1)), (math.abs(currX - x) + math.abs(currY - 1)) * energyToMove))
            }
          } else {
            val inWay = hallwayLocs.filter(v => v >= currX && v <= x)
            if (inWay.forall(ox => !without.exists(_.location == (ox, 1)))) {
              potential.addOne((Amphipod(name, (x, 1)), (math.abs(currX - x) + math.abs(currY - 1)) * energyToMove))
            }
          }
        }
      } else {
        if (!wantedLocations(name).contains(location) && !without.exists(_.location == (currX, currY - 1))) {
          for (x <- hallwayLocs) {
            if (x < currX) {
              val inWay = hallwayLocs.filter(v => v >= x && v <= currX)
              if (inWay.forall(ox => !without.exists(_.location == (ox, 1)))) {
                potential.addOne((Amphipod(name, (x, 1)), (math.abs(currX - x) + math.abs(currY - 1)) * energyToMove))
              }
            } else {
              val inWay = hallwayLocs.filter(v => v >= currX && v <= x)
              if (inWay.forall(ox => !without.exists(_.location == (ox, 1)))) {
                potential.addOne((Amphipod(name, (x, 1)), (math.abs(currX - x) + math.abs(currY - 1)) * energyToMove))
              }
            }
          }
        }
      }
      val value = potential.toSeq.filterNot(p => currentLocations.exists(o => p._1.location == o.location))
      value.map(t => (without ++ Seq(t._1), t._2))
    }

    def wantedLocations(name: String): Seq[(Int, Int)] = {
      name match {
        case "A" => Seq((3, 2), (3, 3))
        case "B" => Seq((5, 2), (5, 3))
        case "C" => Seq((7, 2), (7, 3))
        case "D" => Seq((9, 2), (9, 3))
      }
    }

    override def showTile(): String = this.name
  }
}
