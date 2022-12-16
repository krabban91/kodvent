import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2


  override def part1(strings: Seq[String]): Long = {
    val in = strings.map(Valve(_)).map(i => (i.name, i)).toMap
    val minutes = 30
    val valves = in.filterNot(_._2.flowRate == 0).keySet

    val minutesBetween: Map[String, Iterable[((String, String), Int)]] = calculateDistances(in)

    val startS = (in("AA"), Set[String](), 0)
    // goal -> (_ , keys)
    val front = mutable.PriorityQueue[((Valve, Set[String], Int), Int)]()(Ordering.by(v => (-v._1._3, v._2)))
    val vis = mutable.HashSet[(Valve, Set[String], Int)]()
    front.enqueue((startS, 0))
    var res: ((Valve, Set[String], Int), Int) = null
    val potent = mutable.HashMap[(Valve, Set[String], Int), Int]()
    while (front.nonEmpty) {
      val (pop@(me, opened, minute), accum) = front.dequeue()
      if (minute > minutes) {
        println("strange")
        // failed path
      } else if (minute == minutes) {
        if (accum != 0) {
          val lastAdd = 0 * opened.map(in(_).flowRate).sum
          potent.put(pop, accum + lastAdd)
          front.clear()
        }
      } else {

        val state = (me, opened, minute)
        if (vis.add(state)) {
          //new state
          val minutesLeft = minutes - minute - 1
          val meStates: Seq[(Valve, Int, Option[String])] = states(minutes, valves, minutesBetween, me, opened, minute, in)
          meStates.foreach { case (me, min, op) =>
            front.enqueue(((me, opened ++ Set(op).flatten, min), accum + op.map(in(_).flowRate * minutesLeft).getOrElse(0)))
          }
        }
      }
    }
    val sorted = potent.toSeq.sortBy(_._2)
    res = sorted.maxBy(_._2)
    res._2
    // 1768 is wrong, 1775 is right.
  }

  private def states(minutes: Int, valves: Set[String], minutesBetween: Map[String, Iterable[((String, String), Int)]], me: Valve, opened: Set[String], minute: Int, in: Map[String, Valve]) = {
    // meState : (valve, minute, Option[key])
    val meMut = mutable.ListBuffer[(Valve, Int, Option[String])]()

    meMut.addOne((me, minutes, None))
    if (!opened.contains(me.name) && me.flowRate != 0) {
      meMut.addOne((me, minute + 1, Some(me.name)))
    } else {
      minutesBetween(me.name)
        .map{ case ((_, to), cost) => (to, cost + minute)}
        .filter{ case (_, newMin) => newMin <= minutes}
        .foreach { case (to, nextMin) => meMut.addOne((in(to), nextMin, None))}

    }
    val states = meMut.toSeq
    states
  }

  override def part2(strings: Seq[String]): Long = {
    val in = strings.map(Valve(_)).map(i => (i.name, i)).toMap
    val minutes = 26
    val valves = in.filterNot(_._2.flowRate == 0).keySet
    val minutesBetween: Map[String, Iterable[((String, String), Int)]] = calculateDistances(in)
    // state (((myLoc, minute), (elefantLoc, minute)), openValves, minute)
    val startS = Seq(((((in("AA"), 0), (in("AA"), 0)), Set[String](), 0), 0))
    // goal -> (_ , keys)
    val front = mutable.PriorityQueue[Seq[((((Valve, Int), (Valve, Int)), Set[String], Int), Int)]]()(Ordering.by(v => (-v.last._1._3, v.last._2)))
    val vis = mutable.HashSet[((Valve,Valve), Set[String], Int)]()
    front.enqueue((startS))
    var res: Seq[((((Valve, Int), (Valve, Int)), Set[String], Int), Int)] = null
    val potent = mutable.HashMap[Seq[((((Valve, Int), (Valve, Int)), Set[String], Int), Int)], Int]()
    while (front.nonEmpty) {
      val l = front.dequeue()
      val (pop, accum) = l.last
      val ((me, elefant), opened, minute) = pop

      if (minute > minutes) {
        println("strange")
        // failed path
      } else if (minute == minutes) {
        if (accum != 0) {
          val lastAdd = 0 * opened.map(in(_).flowRate).sum
          potent.put(l, accum + lastAdd)
          front.clear()
        }
      } else {
        val state = (((me._1), (elefant._1)), opened, minute)
        if (vis.add(state)) {
          //new state
          val minutesLeft = minutes - minute - 1
          val meStates: Seq[(Valve, Int, Option[String])] = if (me._2 == minute)
            states(minutes, valves, minutesBetween, me._1, opened, minute, in) else Seq((me._1, me._2, None))
          val elStates: Seq[(Valve, Int, Option[String])] = if (elefant._2 == minute)
            states(minutes, valves, minutesBetween, elefant._1, opened, minute, in)
          else Seq((elefant._1, elefant._2, None))

          val combined = meStates.flatMap { case (newMe, meMin, meOp) =>
            elStates.map { case (el, elMin, elOp) =>
              val acc = accum + Set(meOp, elOp).flatten.diff(opened).map(in(_).flowRate * minutesLeft).sum
              val nextOpen = opened ++ Set(meOp, elOp).flatten
              ((((newMe, meMin), (el, elMin)), nextOpen, math.min(meMin, elMin)), acc)
            }
          }.distinct.map(v => l ++ Seq(v))
          front.addAll(combined)
        }
      }
    }
    val sorted = potent.toSeq.sortBy(_._2).reverse
    res = sorted.maxBy(_._2)._1
    res.last._2
    // 2542 is too high
    // 2344 is too low
    // wait 1 minute
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
      ((from, to), out)
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
