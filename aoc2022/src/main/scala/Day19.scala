import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day19 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = geodeCount(strings.map(Blueprint(_)), 24)
    .map { case (bp, best) => best * bp.id }.sum

  override def part2(strings: Seq[String]): Long = geodeCount(strings.map(Blueprint(_)).take(3), 32)
    .map(_._2.toLong).product

  private def geodeCount(v: Seq[Blueprint], maxMinutes: Int): Seq[(Blueprint, Int)] = {
    v.map(bp => (bp, bp.bestScenario(maxMinutes).material("geode")))
  }

  case class State(material: Map[String, Int], robots: Map[String, Int])

  case class Blueprint(id: Int, oreRobotCost: Seq[(Int, String)], clayRobotCost: Seq[(Int, String)], obsidianRobotCost: Seq[(Int, String)], geodeRobotCost: Seq[(Int, String)]) {

    def bestScenario(minutes: Int): State = {
      val start = State(Map("ore" -> 0, "clay" -> 0, "obsidian" -> 0, "geode" -> 0), Map("ore" -> 1, "clay" -> 0, "obsidian" -> 0, "geode" -> 0))

      val frontier = mutable.PriorityQueue[(State, Int)]()(Ordering.by(v => (minutes - v._2, v._1.material("geode"))))
      val visited = mutable.HashSet[State]()
      frontier.enqueue((start, 0))
      var best: State = null
      while (frontier.nonEmpty) {
        val (state, minute) = frontier.dequeue()
        if (minute == minutes) {
          best = state
          frontier.clear()
        } else {
          if (visited.add(state)) {
            frontier.addAll(this.nextStates(state)
              .map(next => State(next.material.map { case (k, v) => (k, v + state.robots(k)) }, next.robots))
              .filterNot(visited)
              .map(s => (s, minute + 1)))
          }
        }
      }
      val geodeCount = best.material("geode")
      println(s"Blueprint ${id}\t for $minutes:\tgeodes=$geodeCount,\tstate=$best")
      best
    }

    private def nextStates(state: State): Seq[State] = {
      val prepared = (buildStates(state))

      if (prepared.exists(_._1 == "geode")) prepared.filter(_._1 == "geode").map(_._2) else (Seq(state) ++ prepared.map(_._2)).takeRight(2)
    }

    private def buildStates(state: State): Seq[(String, State)] = {
      Seq(("ore", oreRobotCost), ("clay", clayRobotCost), ("obsidian", obsidianRobotCost), ("geode", geodeRobotCost))
        .filterNot(v => state.material("ore") + state.robots("ore") > 2 * maxRequirement(v._1))
        .filterNot(v => state.robots("ore") > maxRequirement(v._1))
        .filter(_._2.forall { case (count, material) => state.material(material) - count >= 0 })
        .map { case (mat, costs) =>
          val nextMaterials = state.material.map { case (k, v) => (k, v - costs.find(_._2 == k).map(_._1).getOrElse(0)) }
          val nextRobots = state.robots.map { case (k, v) => (k, v + (if (k == mat) 1 else 0)) }
          (mat, State(nextMaterials, nextRobots))
        }
    }

    private def maxRequirement(name: String): Int = {
      if (name == "geode") 1000 else Seq(oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost)
        .flatMap(_.find(_._2 == name).map(_._1)).max
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

    private def extractCosts(ore: String): Seq[(Int, String)] = {
      ore.split("and").map(_.trim).map(_.split(" ")).map(l => (l.head.toInt, l.last)).toSeq
    }
  }

}
