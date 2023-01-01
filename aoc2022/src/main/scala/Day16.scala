import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val in = strings.map(Valve(_)).map(i => (i.name, i)).toMap
    val time = 30

    val minutesBetween: Map[String, Iterable[((String, String), Int)]] = calculateDistances(in)
    val start = SoloState("AA", in.filterNot(_._2.flowRate == 0).keySet, time, 0)
    val frontier = mutable.PriorityQueue[SoloState]()(Ordering.by(v => (-v.minute, v.accum)))
    val visited = mutable.HashSet[SoloState]()
    frontier.enqueue(start)
    var max: Int = -1
    while (frontier.nonEmpty) {
      val state@SoloState(me, toOpen, minute, accum) = frontier.dequeue()
      if (accum > max) {
        max = accum
      }
      if (minute > 0 && visited.add(state)) {
        val next = neighbors(minutesBetween, me, toOpen, minute, in)
        if (upperBound(accum, next) >= max) {
          val moves = state.next(next)
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
    val start = PairState(("AA", time), ("AA", time), in.filterNot(_._2.flowRate == 0).keySet, time, 0)
    val frontier = mutable.PriorityQueue[PairState]()(Ordering.by(v => (-v.minute, v.accum)))
    val visited = mutable.HashSet[PairState]()
    frontier.enqueue(start)
    var max: Int = -1
    while (frontier.nonEmpty) {
      val state@PairState(me, elephant, toOpen, minute, accum) = frontier.dequeue()
      if (accum > max) {
        max = accum
      }
      if (minute > 0 && visited.add(state)) {
        val myNext = if (me._2 == minute) neighbors(minutesBetween, me._1, toOpen, minute, in) else Seq(Move(me._1, me._2, None))
        val elephantNext = if (elephant._2 == minute) neighbors(minutesBetween, elephant._1, toOpen, minute, in) else Seq(Move(elephant._1, elephant._2, None))
        if (upperBound(accum, myNext, elephantNext) >= max) {
          frontier.addAll(state.next(myNext, elephantNext))
        }
      }
    }
    max
  }

  private def neighbors(minutesBetween: Map[String, Iterable[((String, String), Int)]], me: String, toOpen: Set[String], minute: Int, in: Map[String, Valve]): Seq[Move] = {
    minutesBetween(me)
      .map(v => (v._1._2, minute - v._2))
      .filter(v => v._2 >= 0)
      .filter(v => toOpen(v._1))
      .map(v => Move(v._1, v._2, Some(Open(v._1, v._2 * in(v._1).flowRate))))
      .toSeq ++ Seq(Move(me, 0, None))
  }

  def upperBound(curr: Int, next: Seq[Move]): Int = {
    curr + next.flatMap(_.opened.map(_.pressure)).sum
  }

  def upperBound(curr: Int, myNext: Seq[Move], elephantNext: Seq[Move]): Int = {
    val elephantLookup = elephantNext.flatMap(_.opened).map(o => (o.name, o.pressure)).toMap
    val meLookup = myNext.flatMap(_.opened).map(o => (o.name, o.pressure)).toMap
    curr + (elephantLookup.keySet ++ meLookup.keySet)
      .map(k => math.max(elephantLookup.getOrElse(k, 0), meLookup.getOrElse(k, 0)))
      .sum
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

  case class Move(name: String, minute: Int, opened: Option[Open])

  case class Open(name: String, pressure: Int)

  case class SoloState(name: String, toOpen: Set[String], minute: Int, accum: Int) {
    def next(next: Seq[Move]): Seq[SoloState] = next
      .map { case Move(me, min, op) => SoloState(me, toOpen -- Seq(op).flatMap(_.map(_.name)), min, accum + op.map(_.pressure).getOrElse(0)) }

  }
  case class PairState(me: (String, Int), elephant: (String, Int), toOpen: Set[String], minute: Int, accum: Int) {
    def next(myNext: Seq[Move], elephantNext: Seq[Move]): Seq[PairState] = myNext
      .flatMap { case Move(newMe, meMin, meOp) =>
        elephantNext
          .filterNot(v => v.opened.isDefined && v.opened.map(_.name) == meOp.map(_.name))
          .map { case Move(el, elMin, elOp) =>
            val acc = accum + Seq(meOp, elOp).flatMap(_.map(_.pressure)).sum
            val nextOpen = toOpen -- Seq(meOp, elOp).flatMap(_.map(_.name))
            PairState((newMe, meMin), (el, elMin), nextOpen, math.max(meMin, elMin), acc)
          }
      }
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
