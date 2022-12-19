import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day19 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  //printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(Blueprint(_))
    val maxMinutes = 24
    val out: Seq[(Blueprint, State)] = calculateStates(v, maxMinutes)
    out.map{ case (bp, best) =>best.material("geode") * bp.id}.sum
  }

  override def part2(strings: Seq[String]): Long = {
    val v = strings.map(Blueprint(_)).take(3)
    val maxMinutes = 32
    val out: Seq[(Blueprint, State)] = calculateStates(v, maxMinutes)
    out.map(_._2.material("geode").toLong).product
  }

  private def calculateStates(v: Seq[Blueprint], maxMinutes: Int): Seq[(Blueprint, State)] = {
    val out = v.map(blueprint => {
      val start = State(
        Map("ore" -> 0, "clay" -> 0, "obsidian" -> 0, "geode" -> 0),
        Map("ore" -> 1, "clay" -> 0, "obsidian" -> 0, "geode" -> 0))
      // (ore,clay obsidian, geode), (ore, clayRobots, obsidian, geode)
      // State

      val frontier = mutable.PriorityQueue[(State, Int)]()(Ordering.by(v => (-v._2, v._1.material("geode"))))
      val visited = mutable.HashSet[State]()
      frontier.enqueue((start, 0))
      var best: State = null
      while (frontier.nonEmpty) {
        val pop@(state, minute) = frontier.dequeue()
        if (minute == maxMinutes) {
          best = state
          frontier.clear()
        } else {
          if (visited.add(state)) {
            val affords = blueprint.nextStates(state)
            val nextStates = affords
              .map(next => State(next.material.map { case (k, v) => (k, v + state.robots(k)) }, next.robots))
              .map(s => (s, minute + 1))
            frontier.addAll(nextStates)
          }
        }
      }
      val score = best.material("geode") * blueprint.id
      println(s"Blueprint ${blueprint.id} score=$score, state=$best")
      score
      (blueprint, best)
    })
    out
  }


  case class State(material: Map[String, Int], robots: Map[String,Int])

  case class Blueprint(id: Int, oreRobotCost: Seq[(Int, String)], clayRobotCost: Seq[(Int, String)], obsidianRobotCost: Seq[(Int, String)], geodeRobotCost: Seq[(Int, String)]) {
    def nextStates(state: State): Seq[State] = {
      val start = state

      val frontier = mutable.PriorityQueue[State]()(Ordering.by(v =>v.material.head._2))
      val visited = mutable.HashSet[State]()
      frontier.enqueue(start)
      while (frontier.nonEmpty) {
        val pop = frontier.dequeue()

        if (visited.add(pop)) {
          val affordable = buildStates(pop)
          //println(affordable)
          // todo add more states. done
          val next = affordable.reverse.take(2)
          frontier.addAll(next)
          visited.addAll(next)
        }

      }
      visited.toSeq
    }

    def buildStates(state: State): Seq[State] = {
      Seq(
        Some(oreRobotCost.map{ case (count, material) => state.material(material) - count})
          .filter(_.forall(_ >=0))
          .map(_ => ("ore", oreRobotCost)),
        Some(clayRobotCost.map{ case (count, material) => state.material(material) - count})
          .filter(_.forall(_ >=0))
          .map(_ => ("clay", clayRobotCost)),
        Some(obsidianRobotCost.map{ case (count, material) => state.material(material) - count})
          .filter(_.forall(_ >=0))
          .map(_ => ("obsidian", obsidianRobotCost)),
        Some(geodeRobotCost.map{ case (count, material) => state.material(material) - count})
          .filter(_.forall(_ >=0))
          .map(_ => ("geode", geodeRobotCost))
      ).flatten
        .map{case (mat, costs) =>
          val nextMaterials = state.material.map{case (k,v) => (k, v - costs.find(_._2 == k).map(_._1).getOrElse(0))}
          val nextRobots = state.robots.map{ case (k,v) => (k, v + (if(k == mat) 1 else 0))}
          State(nextMaterials, nextRobots)
        }
    }
  }

  object Blueprint {
    val pattern = """Blueprint (.+): Each ore robot costs (.+). Each clay robot costs (.+). Each obsidian robot costs (.+). Each geode robot costs (.+).""".r

    def apply(string: String): Blueprint = {
      string match {
        case pattern(id, ore, clay, obsidian, geode) =>
          val ores = extractCosts(ore)
          val clays = extractCosts(clay)
          val obsidians = extractCosts(obsidian)
          val geodes = extractCosts(geode)
          Blueprint(id.toInt, ores, clays, obsidians, geodes)
      }
    }

    private def extractCosts(ore: String) = {
      ore.split("and").map(_.trim).map(_.split(" ")).map(l => (l.head.toInt, l.last)).toSeq
    }
  }

}
