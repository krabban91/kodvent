import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.{LogUtils, Loggable}

import java.awt.Point
import java.util.Date
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val s = getAmphipods(strings)
    shortestPath(s)
  }

  private def logPath(pods: Set[Amphipod], prevMap: Map[Set[Amphipod], (Set[Amphipod], Long)], level: Int, cost: Long): Unit = {
    val prev = prevMap.get(pods)
    if (prev.isDefined) {
      logPath(prev.get._1, prevMap, level - 1, prev.get._2)
    }
    val mm = pods.map(p => (new Point(p.location._1, p.location._2), p)).toMap
    val logger = new LogUtils[Amphipod]()
    println(logger.mapToText(mm.asJava, p => if (p == null) "." else p.name))
    println(s"Path: level=$level. Cost=${cost}")
  }

  private def logLocs(pods: Set[Amphipod], cost: Long, heuristic: Long) = {
    println(new Date().toString)
    println(s"cost: $cost, heuristic: $heuristic, pods: ")
    val mm = pods.map(p => (new Point(p.location._1, p.location._2), p)).toMap
    val logger = new LogUtils[Amphipod]()
    println(logger.mapToText(mm.asJava, p => if (p == null) "." else p.name))
  }

  private def getAmphipods(strings: Seq[String], part2: Boolean = false): Set[Amphipod] = {
    (if (part2) (strings.take(3) ++ Seq("  #D#C#B#A#  ", "  #D#B#A#C#  ") ++ strings.takeRight(2)) else strings)
      .zipWithIndex.flatMap(ty => ty._1.zipWithIndex.filterNot(tx => Seq('#', '.', ' ').contains(tx._1)).map(tx => Amphipod(tx._1.toString, (tx._2, ty._2))))
      .toSet
  }

  override def part2(strings: Seq[String]): Long = {
    val s = getAmphipods(strings, part2 = true)
    shortestPath(s, part2 = true)
  }


  def shortestPath(s: Set[Amphipod], part2: Boolean = false): Long = {
    val pq = mutable.PriorityQueue[(Set[Amphipod], Long, Long, Set[Amphipod])]()(Ordering.Long.reverse.on(_._3))
    val prevMap = mutable.HashMap[Set[Amphipod], (Set[Amphipod], Long)]()
    val checked = mutable.HashSet[Set[Amphipod]]()
    pq.addOne((s, 0, s.map(_.heuristic(s, part2 = part2)).sum, Set()))
    while (pq.nonEmpty) {
      val (pods, cost, _, prev) = pq.dequeue()
      if (!checked.contains(pods)) {
        prevMap.put(pods, (prev, cost))
        checked.add(pods)
        if (isGoal(pods, part2 = part2)) {
          logPath(pods, prevMap.toMap, 0, cost)
          return cost
        } else {
          val next = pods
            .flatMap(_.possibleMoves(pods, part2 = part2))
            .filterNot(t => checked.contains(t._1))
            .map(t => (t._1, cost + t._2, cost + t._2 + t._1.map(_.heuristic(t._1, part2=part2)).sum, pods))
          pq.addAll(next)
        }
      }
    }
    -1L
  }


  def wantedLocations(name: String, part2: Boolean = false): Seq[(Int, Int)] = {
    name match {
      case "A" => Seq((3, 2), (3, 3)) ++ (if (part2) Seq((3, 4), (3, 5)) else Seq())
      case "B" => Seq((5, 2), (5, 3)) ++ (if (part2) Seq((5, 4), (5, 5)) else Seq())
      case "C" => Seq((7, 2), (7, 3)) ++ (if (part2) Seq((7, 4), (7, 5)) else Seq())
      case "D" => Seq((9, 2), (9, 3)) ++ (if (part2) Seq((9, 4), (9, 5)) else Seq())
    }
  }

  def isGoal(locations: Set[Amphipod], part2: Boolean = false): Boolean = {
    val as = locations.filter(_.name == "A").map(_.location).toSeq.sortBy(_._2)
    val bs = locations.filter(_.name == "B").map(_.location).toSeq.sortBy(_._2)
    val cs = locations.filter(_.name == "C").map(_.location).toSeq.sortBy(_._2)
    val ds = locations.filter(_.name == "D").map(_.location).toSeq.sortBy(_._2)

    as == wantedLocations("A", part2) &&
      bs == wantedLocations("B", part2) &&
      cs == wantedLocations("C", part2) &&
      ds == wantedLocations("D", part2)
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

    def heuristic(currentLocations: Set[Amphipod], part2: Boolean = false): Long = {
      val wantedX = Map("A" -> 3, "B" -> 5, "C" -> 7, "D" -> 9)
      val x = wantedX(name)
      //val missing = wantedLocations(name, part2).count(p => currentLocations.find(a => a.location == p).forall(_.name!=name))
      val steps = if (location._1 != x) {
        if (location._2 == 1) {
          1 + math.abs(x - location._1)
        } else {
          location._2 - 1 + 1 + math.abs(x - location._1)
        }
      } else {
        val under = (location._2 + 1 to (if (part2) 5 else 3))
          .map(y => currentLocations.find(p => p.location == (location._1, y)))
        if (under.exists(o => o.exists(ot => ot.name != name))) {
          ((location._2 - 1) + 1) + 2
        } else {
          0
        }
      }
      energyToMove * steps
    }

    def possibleMoves(currentLocations: Set[Amphipod], part2: Boolean): Seq[(Set[Amphipod], Long)] = {
      val currY = this.location._2
      val currX = this.location._1
      val without = currentLocations.filterNot(_ == this)
      val potential = mutable.ListBuffer[(Amphipod, Long)]()
      val hallwayLocs = Seq(1, 2, 4, 6, 8, 10, 11)
      if (currY == 1) {
        val w = wantedLocations(name, part2 = part2)
        w.foreach(t => {
          val one = without.find(p => p.location == (t._1, 2))
          val two = without.find(p => p.location == (t._1, 3))
          val three = without.find(p => p.location == (t._1, 4))
          val four = without.find(p => p.location == (t._1, 5))
          val (over, goal, under) = if (part2) {
            if (t._2 == 2) {
              (Seq(), one, Seq(two, three, four))
            } else if (t._2 == 3) {
              (Seq(one), two, Seq(three, four))
            }
            else if (t._2 == 4) {
              (Seq(one, two), three, Seq(four))
            }
            else {
              (Seq(one, two, three), four, Seq())
            }
          } else {
            if (t._2 == 2) {
              (Seq(), one, Seq(two))
            } else {
              (Seq(one), two, Seq())
            }
          }

          if (goal.isEmpty && over.flatten.isEmpty && under.forall(_.exists(p => p.name == name))) {
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
        })
      } else {
        val one = without.find(p => p.location == (location._1, 2))
        val two = without.find(p => p.location == (location._1, 3))
        val three = without.find(p => p.location == (location._1, 4))
        val four = without.find(p => p.location == (location._1, 5))
        val (over, under) = if (part2) {
          if (currY == 2) {
            (Seq(), Seq(two, three, four))
          } else if (currY == 3) {
            (Seq(one), Seq(three, four))
          }
          else if (currY == 4) {
            (Seq(one, two), Seq(four))
          }
          else {
            (Seq(one, three, four), Seq())
          }
        } else {
          if (currY == 2) {
            (Seq(), Seq(two))
          } else {
            (Seq(one), Seq())
          }
        }
        if (over.forall(_.isEmpty)) {
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

    override def showTile(): String = this.name
  }
}
