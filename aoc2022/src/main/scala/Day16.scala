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
            if (!op.exists(opened.contains)) {
              front.enqueue(((me, opened ++ Set(op).flatten, min), accum + op.map(in(_).flowRate * (minutes - min)).getOrElse(0)))
            }
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
      //meMut.addOne((me, minute + 1, Some(me.name)))
    } else {
      minutesBetween(me.name)
        .map { case ((_, to), cost) => (to, cost + minute) }
        .filter { case (_, newMin) => newMin <= minutes }
        .foreach { case (to, nextMin) => meMut.addOne((in(to), nextMin, Some(to))) }

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
    val startS = ((((in("AA"), 0), (in("AA"), 0)), Set[String](), 0), 0)
    // goal -> (_ , keys)
    val front = mutable.PriorityQueue[((((Valve, Int), (Valve, Int)), Set[String], Int), Int)]()(Ordering.by(v => (-v._1._3, v._2)))
    val vis = mutable.HashSet[(Set[Valve], Set[String], Int)]()
    front.enqueue((startS))
    var res: ((((Valve, Int), (Valve, Int)), Set[String], Int), Int) = null

    while (front.nonEmpty) {
      val (pop, accum) = front.dequeue()
      val ((me, elefant), opened, minute) = pop

      if (minute > minutes) {
        println("strange")
        // failed path
      } else if (minute == minutes) {
        if (accum != 0) {
          val lastAdd = 0 * opened.map(in(_).flowRate).sum
          res = (pop, accum + lastAdd)
          front.clear()
        }
      } else {
        val state = ((Set(me._1, elefant._1)), opened, minute)
        if (vis.add(state)) {
          val combined = duoStates(in, accum, minutes, valves, minutesBetween, me, elefant, opened, minute)
          front.addAll(combined)
        }
      }
    }
    res._2
    // 2542 is too high
    // 2344 is too low
    // wait 1 minute
  }


  private def duoStates(in: Map[String, Valve], accum: Int, minutes: Int, valves: Set[String], minutesBetween: Map[String, Iterable[((String, String), Int)]], me: (Valve, Int), elefant: (Valve, Int), opened: Set[String], minute: Int): Seq[((((Day16.Valve, Int), (Day16.Valve, Int)), Set[String], Int), Int)]  = {
    //new state
    val meStates: Seq[(Valve, Int, Option[String])] = if (me._2 == minute)
      states(minutes, valves, minutesBetween, me._1, opened, minute, in) else Seq((me._1, me._2, None))
    val elStates: Seq[(Valve, Int, Option[String])] = if (elefant._2 == minute)
      states(minutes, valves, minutesBetween, elefant._1, opened, minute, in)
    else Seq((elefant._1, elefant._2, None))

    val combined = meStates
      .filterNot(v => v._3.exists(opened.contains))
      .filterNot(v => v._1 != me._1 && opened(v._1.name))
      .flatMap { case (newMe, meMin, meOp) =>
        elStates
          .filterNot(v => v._3.exists(opened.contains))
          .filterNot(v => v._1 != elefant._1 && opened(v._1.name))
          .filterNot(v => v._3.isDefined && v._3 == meOp)
          .map { case (el, elMin, elOp) =>
            val acc = accum +
              meOp.map(in(_).flowRate * (minutes - meMin)).getOrElse(0) +
              elOp.map(in(_).flowRate * (minutes - elMin)).getOrElse(0)
            val nextOpen = opened ++ Set(meOp, elOp).flatten
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
