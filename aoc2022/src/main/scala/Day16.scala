import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val in = strings.map(Valve(_)).map(i => (i.name, i)).toMap
    val keys = in.filterNot(_._2.flowRate == 0).keySet
    val maxPot = in.values.map(_.flowRate).sum * 30

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


    val startS = (in("AA"), Set[String](), 0)
    // goal -> (_ , keys)
    val front = mutable.PriorityQueue[(Seq[((Valve, Set[String], Int), Int)], Int, Int)]()(Ordering.by(v => (-v._1.last._1._3, v._2)))
    val vis = mutable.HashMap[(Valve, Set[String]), Int]()
    front.enqueue((Seq((startS, 0)), 0, 0))
    var res: (Seq[((Valve, Set[String], Int), Int)], Int) = null
    val potent = mutable.HashMap[Seq[((Valve, Set[String], Int), Int)], Int]()
    while (front.nonEmpty) {
      val (pop, accum, heur) = front.dequeue()
      val minute = pop.last._1._3
      if (minute > 30) {
        println("strange")
        // failed path
      } else if (minute == 30) {
        if (accum != 0) {
          val lastAdd = 0 * pop.last._1._2.map(in(_).flowRate).sum
          potent.put(pop, accum + lastAdd)
          //front.clear()
        }
      } else {
        val state = (pop.last._1._1, pop.last._1._2)
        val bool = !vis.contains(state) || vis(state) < accum
        if (bool) {
          vis.put(state, accum)
          //new state
          val moves = mutable.ListBuffer[(Seq[((Valve, Set[String], Int), Int)], Int, Int)]()
          // open
          if (!pop.last._1._2.contains(pop.last._1._1.name) && pop.last._1._1.flowRate != 0) {
            val addedFlow = pop.last._1._2.map(in(_).flowRate).sum * 1
            val nextMin = pop.last._1._3 + 1
            val tuple: (Valve, Set[String], Int) = (pop.last._1._1, pop.last._1._2 ++ Set(pop.last._1._1.name), nextMin)
            val tuple1: ((Valve, Set[String], Int), Int) = (tuple, addedFlow)
            val newHeur = (30 - nextMin) * addedFlow
            val nh = maxPot - (accum + newHeur)
            moves.addOne((pop ++ Seq(tuple1), accum + addedFlow, nh))
          } else {
            // move
            //if (true) {
            val steps = minutesBetween(pop.last._1._1.name).map { case ((from, to), cost) =>
              val addedFlow = pop.last._1._2.map(in(_).flowRate).sum * cost
              val nextMin = pop.last._1._3 + cost
              val newHeur = (30 - nextMin) * addedFlow
              val nh = maxPot - (accum + newHeur)
              if (nextMin > 30) {
                val addedFlow = pop.last._1._2.map(in(_).flowRate).sum * 1
                val nextMin = pop.last._1._3 + 1
                val nh = maxPot - (accum + newHeur)
                (pop ++ Seq(((pop.last._1._1, pop.last._1._2, nextMin), addedFlow)), accum + addedFlow, nh)
              } else {
                (pop ++ Seq(((in(to), pop.last._1._2, nextMin), addedFlow)), accum + addedFlow, nh)
              }
            }
            moves.addAll(steps)
          }


          moves.foreach(m => {
            front.enqueue(m)
          })
        } else {
          // already been
        }
      }
    }
    val sorted = potent.toSeq.sortBy(_._2)
    res = sorted.maxBy(_._2)
    res._2
    // 1768 is wrong
  }

  override def part2(strings: Seq[String]): Long = {
    -1
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
