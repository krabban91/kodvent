import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = openValves(30, 0, strings)

  override def part2(strings: Seq[String]): Long = openValves(26, 26, strings)

  def openValves(myTime: Int, elephantTime: Int, input: Seq[String]): Int = {
    val in = Valve.parse(input)
    val paths: Map[String, Seq[Path]] = Path.calculate(in)
    val start = State(("AA", myTime), ("AA", elephantTime), in.filterNot(_._2.flowRate == 0).keySet, math.max(myTime, elephantTime), 0)
    val frontier = mutable.PriorityQueue[State]()(Ordering.by(v => (-v.minute, v.releasedPressure)))
    val visited = mutable.HashSet[State]()
    frontier.enqueue(start)
    var max: Int = -1
    while (frontier.nonEmpty) {
      val state@State(me, elephant, toOpen, minute, accum) = frontier.dequeue()
      if (accum > max) {
        max = accum
      }
      if (minute > 0 && visited.add(state)) {
        val myNext = if (me._2 == minute) Move.neighbors(paths, me._1, toOpen, minute) else Seq(Move(me._1, me._2, None))
        val elephantNext = if (elephant._2 == minute) Move.neighbors(paths, elephant._1, toOpen, minute) else Seq(Move(elephant._1, elephant._2, None))
        if (Move.upperBound(accum, myNext, elephantNext) >= max) {
          frontier.addAll(state.next(myNext, elephantNext))
        }
      }
    }
    max
  }

  case class State(me: (String, Int), elephant: (String, Int), toOpen: Set[String], minute: Int, releasedPressure: Int) {
    def next(myNext: Seq[Move], elephantNext: Seq[Move]): Seq[State] = myNext
      .flatMap { case Move(newMe, meMin, meOp) =>
        elephantNext
          .filterNot(v => v.opened.isDefined && v.opened.map(_.name) == meOp.map(_.name))
          .map { case Move(el, elMin, elOp) =>
            val acc = releasedPressure + Seq(meOp, elOp).flatMap(_.map(_.pressure)).sum
            val nextOpen = toOpen -- Seq(meOp, elOp).flatMap(_.map(_.name))
            State((newMe, meMin), (el, elMin), nextOpen, math.max(meMin, elMin), acc)
          }
      }
  }

  case class Path(from: String, to: String, cost: Int, flowRate: Int)

  object Path {
    def calculate(in: Map[String, Valve]): Map[String, Seq[Path]] = in.keys.map(from => (from, in.filter(_._2.flowRate != 0).keys.filter(_ != from).map(to => {
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
      Path(from, to, out + 1, in(to).flowRate)
    }).toSeq)).toMap
  }


  case class Move(name: String, minute: Int, opened: Option[Open])

  object Move {

    def upperBound(curr: Int, myNext: Seq[Move], elephantNext: Seq[Move]): Int = {
      val elephantLookup = elephantNext.flatMap(_.opened).map(o => (o.name, o.pressure)).toMap
      val meLookup = myNext.flatMap(_.opened).map(o => (o.name, o.pressure)).toMap
      curr + (elephantLookup.keySet ++ meLookup.keySet)
        .map(k => math.max(elephantLookup.getOrElse(k, 0), meLookup.getOrElse(k, 0)))
        .sum
    }

    def neighbors(paths: Map[String, Seq[Path]], current: String, toOpen: Set[String], minute: Int): Seq[Move] = {
      paths(current)
        .map(v => (v.to, minute - v.cost, v.flowRate))
        .filter(v => v._2 >= 0)
        .filter(v => toOpen(v._1))
        .map(v => Move(v._1, v._2, Some(Open(v._1, v._2 * v._3))))
        .toSeq ++ Seq(Move(current, 0, None))
    }
  }

  case class Open(name: String, pressure: Int)


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

    def parse(strings: Seq[String]): Map[String, Valve] = strings.map(Valve(_)).map(i => (i.name, i)).toMap
  }
}
