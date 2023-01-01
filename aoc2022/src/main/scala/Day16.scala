import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val in = strings.map(Valve(_)).map(i => (i.name, i)).toMap
    val time = 30

    val minutesBetween: Map[String, Iterable[((String, String), Int)]] = calculateDistances(in)
    val start = ("AA", in.filterNot(_._2.flowRate == 0).keySet, time)
    val frontier = mutable.PriorityQueue[((String, Set[String], Int), Int)]()(Ordering.by(v => (v._1._3, v._2)))
    val visited = mutable.HashSet[(String, Set[String], Int)]()
    frontier.enqueue((start, 0))
    var max: Int = -1
    while (frontier.nonEmpty) {
      val ((me, toOpen, minute), accum) = frontier.dequeue()
      if (accum > max) {
        max = accum
      }
      if (minute > 0 && visited.add((me, toOpen, minute))) {
        val next = neighbors(minutesBetween, me, toOpen, minute, in)
        if (upperBound(accum, next) >= max) {
          val moves = soloEffort(next, toOpen, accum)
          frontier.addAll(moves)
        }
      }
    }
    max
  }

  override def part2(strings: Seq[String]): Long = {
    val in = strings.map(Valve(_)).map(i => (i.name, i)).toMap
    val time = 26
    val minutesBetween: Map[String, Iterable[((String, String), Int)]] = calculateDistances(in)
    val start = ((("AA", time), ("AA", time)), in.filterNot(_._2.flowRate == 0).keySet, time)
    val frontier = mutable.PriorityQueue[((((String, Int), (String, Int)), Set[String], Int), Int)]()(Ordering.by(v => v._1._3))
    val visited = mutable.HashSet[(Set[String], Set[String], Int)]()
    frontier.enqueue((start, 0))
    var max: Int = -1
    while (frontier.nonEmpty) {
      val (((me, elephant), toOpen, minute), accum) = frontier.dequeue()
      if (accum > max) {
        max = accum
      }
      if (minute > 0 && visited.add((Set(me._1, elephant._1), toOpen, minute))) {
        val myNext = if (me._2 == minute) neighbors(minutesBetween, me._1, toOpen, minute, in) else Seq((me._1, me._2, None))
        val elephantNext = if (elephant._2 == minute) neighbors(minutesBetween, elephant._1, toOpen, minute, in) else Seq((elephant._1, elephant._2, None))
        if (upperBound(accum, myNext, elephantNext) >= max) {
          frontier.addAll(jointEffort(myNext, elephantNext, toOpen, accum))
        }
      }
    }
    max
  }

  private def neighbors(minutesBetween: Map[String, Iterable[((String, String), Int)]], me: String, toOpen: Set[String], minute: Int, in: Map[String, Valve]) = {
    minutesBetween(me)
      .map(v => (v._1._2, minute - v._2))
      .filter(v => v._2 >= 0)
      .filter(v => toOpen(v._1))
      .map(v => (v._1, v._2, Some(v._1, v._2 * in(v._1).flowRate)))
      .toSeq ++ Seq((me, 0, None))
  }

  def upperBound(curr: Int, next: Seq[(String, Int, Option[(String, Int)])]): Int = {
    curr + next.flatMap(_._3.map(_._2)).sum
  }

  def upperBound(curr: Int, myNext: Seq[(String, Int, Option[(String, Int)])], elephantNext: Seq[(String, Int, Option[(String, Int)])]): Int = {
    val elephantLookup = elephantNext.flatMap(_._3).toMap
    val meLookup = myNext.flatMap(_._3).toMap
    curr + (elephantLookup.keySet ++ meLookup.keySet)
      .map(k => math.max(elephantLookup.getOrElse(k, 0), meLookup.getOrElse(k, 0)))
      .sum
  }

  private def soloEffort(next: Seq[(String, Int, Option[(String, Int)])], toOpen: Set[String], accum: Int): Seq[((String, Set[String], Int), Int)] = next
    .map { case (me, min, op) => ((me, toOpen -- Seq(op).flatMap(_.map(_._1)), min), accum + op.map(_._2).getOrElse(0)) }

  private def jointEffort(myNext: Seq[(String, Int, Option[(String, Int)])], elephantNext: Seq[(String, Int, Option[(String, Int)])], toOpen: Set[String], accum: Int): Seq[((((String, Int), (String, Int)), Set[String], Int), Int)] = myNext
    .flatMap { case (newMe, meMin, meOp) =>
      elephantNext
        .filterNot(v => v._3.isDefined && v._3.map(_._1) == meOp.map(_._1))
        .map { case (el, elMin, elOp) =>
          val acc = accum + Seq(meOp, elOp).flatMap(_.map(_._2)).sum
          val nextOpen = toOpen -- Seq(meOp, elOp).flatMap(_.map(_._1))
          ((((newMe, meMin), (el, elMin)), nextOpen, math.max(meMin, elMin)), acc)
        }
    }.distinct

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
