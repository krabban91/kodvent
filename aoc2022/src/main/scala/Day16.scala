import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val in = strings.map(Valve(_)).map(i => (i.name, i)).toMap
    val minutes = 30
    val valves = in.filterNot(_._2.flowRate == 0).keySet

    val minutesBetween: Map[String, Iterable[((String, String), Int)]] = calculateDistances(in)

    val start = ("AA", valves, 0)
    val frontier = mutable.PriorityQueue[((String, Set[String], Int), Int)]()(Ordering.by(v => (-v._1._3, v._2)))
    val visited = mutable.HashSet[(String, Set[String], Int)]()
    frontier.enqueue((start, 0))
    var res: Int = -1
    while (frontier.nonEmpty) {
      val ((me, toOpen, minute), accum) = frontier.dequeue()
      if (minute == minutes) {
        res = accum
        frontier.clear()
      } else if (visited.add((me, toOpen, minute))) {
        frontier.addAll(soloEffort(in, accum, minutes, minutesBetween, me, minute, toOpen))
      }
    }
    res
  }

  override def part2(strings: Seq[String]): Long = {
    val in = strings.map(Valve(_)).map(i => (i.name, i)).toMap
    val minutes = 26
    val valves = in.filterNot(_._2.flowRate == 0).keySet
    val minutesBetween: Map[String, Iterable[((String, String), Int)]] = calculateDistances(in)
    val start = (((("AA", 0), ("AA", 0)), valves, 0), 0)
    val frontier = mutable.PriorityQueue[((((String, Int), (String, Int)), Set[String], Int), Int)]()(Ordering.by(v => (-v._1._3, v._2)))
    val visited = mutable.HashSet[(Set[String], Set[String], Int)]()
    frontier.enqueue((start))
    var res: Int = -1
    while (frontier.nonEmpty) {
      val (((me, elephant), toOpen, minute), accum) = frontier.dequeue()
      if (minute == minutes) {
        res = accum
        frontier.clear()
      } else if (visited.add((Set(me._1, elephant._1), toOpen, minute))) {
        frontier.addAll(jointEffort(in, accum, minutes, minutesBetween, me, elephant, toOpen, minute))
      }
    }
    res
  }

  private def states(minutes: Int, minutesBetween: Map[String, Iterable[((String, String), Int)]], me: String, toOpen: Set[String], minute: Int) = {
    minutesBetween(me)
      .map { case ((_, to), cost) => (to, cost + minute) }
      .filter { case (to, _) => toOpen.contains(to) }
      .filter { case (_, newMin) => newMin <= minutes }
      .map { case (to, nextMin) => (to, nextMin, Some(to)) }
      .toSeq ++ Seq((me, minutes, None))
  }

  private def soloEffort(in: Map[String, Valve], accum: Int, minutes: Int, minutesBetween: Map[String, Iterable[((String, String), Int)]], me: String, minute: Int, toOpen: Set[String]): Seq[((String, Set[String], Int), Int)] = {
    states(minutes, minutesBetween, me, toOpen, minute)
      .map { case (me, min, op) => ((me, toOpen -- Set(op).flatten, min), accum + op.map(in(_).flowRate * (minutes - min)).getOrElse(0)) }
  }

  private def jointEffort(in: Map[String, Valve], accum: Int, minutes: Int, minutesBetween: Map[String, Iterable[((String, String), Int)]], me: (String, Int), elephant: (String, Int), toOpen: Set[String], minute: Int): Seq[((((String, Int), (String, Int)), Set[String], Int), Int)] = {
    //new state
    val meStates: Seq[(String, Int, Option[String])] = if (me._2 == minute)
      states(minutes, minutesBetween, me._1, toOpen, minute) else Seq((me._1, me._2, None))
    val elStates: Seq[(String, Int, Option[String])] = if (elephant._2 == minute)
      states(minutes, minutesBetween, elephant._1, toOpen, minute)
    else Seq((elephant._1, elephant._2, None))

    val combined = meStates
      .flatMap { case (newMe, meMin, meOp) =>
        elStates
          .filterNot(v => v._3.isDefined && v._3 == meOp)
          .map { case (el, elMin, elOp) =>
            val acc = accum +
              meOp.map(in(_).flowRate * (minutes - meMin)).getOrElse(0) +
              elOp.map(in(_).flowRate * (minutes - elMin)).getOrElse(0)
            val nextOpen = toOpen -- Set(meOp, elOp).flatten
            ((((newMe, meMin), (el, elMin)), nextOpen, math.min(meMin, elMin)), acc)
          }
      }.distinct
    combined
  }

  private def calculateDistances(in: Map[String, Valve]) = {
    val minutesBetween = in.keys.map(from => (from, in.filter(_._2.flowRate != 0).keys.filter(_ != from).map(to => {
      // find shortest path
      val frontier = mutable.PriorityQueue[(String, Int)]()(Ordering.by(-_._2))
      val visited = mutable.HashSet[String]()
      frontier.enqueue((from, 0))
      var out: Int = Int.MaxValue
      while (frontier.nonEmpty) {
        val (curr, cost) = frontier.dequeue()
        if (curr == to) {
          out = cost
          frontier.clear()
        } else {
          if (visited.add(curr)) {
            in(curr).tunnels.foreach(v => frontier.enqueue((v, cost + 1)))
          }
        }
      }
      ((from, to), out + 1)
    }))).toMap
    minutesBetween
  }


  case class Valve(name: String, flowRate: Int, tunnels: Seq[String]) {
    override def toString: String = name
  }

  object Valve {
    def apply(string: String): Valve = {
      val pattern = """Valve (.*) has flow rate=(-?\d+); tunnel[s]? lead[s]? to valve[s]? (.*)""".r
      string match {
        case pattern(name, rate, tunnels) => Valve(name, rate.toInt, tunnels.split(",").map(_.trim))
      }
    }
  }
}
