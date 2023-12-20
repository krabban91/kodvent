import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.MathUtils.LCM

import scala.collection.mutable

object Day20 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  //printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val debug = false
    val invs = strings.filter(_.startsWith("%")).map(FlipFlop(_))
    val broadcast = Broadcast(strings.find(_.startsWith("broadcaster")).get)
    val tmpC = strings.filter(_.startsWith("&")).map(s => Conjunction(s, (invs :+ broadcast).filter(v => v.targets.contains(s.split("->").head.strip().stripPrefix("&")))))
    val allNodes = (invs.map(n => (n.id, n)).toMap ++ tmpC.map(n => (n.id, n)).toMap ++ Map((broadcast.id -> broadcast)))
    val conjs = tmpC.map(c => Conjunction(c.id, allNodes.values.filter(_.targets.contains(c.id)).map(v => (v.id, false)).toMap, c.targets))
    var highPulses = 0L
    var lowPulses = 0L
    (1 to 1000).foldLeft((broadcast, invs, conjs)) { case (computer@(bc, in, c), time) =>
      val tmp = (in.map(n => (n.id, n)).toMap ++ c.map(n => (n.id, n)).toMap ++ Map((bc.id -> bc)))
      val nodes = mutable.HashMap[String, Node]()
      nodes.addAll(tmp)
      //simulate button
      val toHandle = mutable.Queue[(Node, Boolean, String)]()
      toHandle.append((bc, false, "button"))
      if (debug){println(s"button --low-> ${bc.name} ($time)")}
      lowPulses += 1L
      while (toHandle.nonEmpty) {
        val pop@(n, high, from) = toHandle.dequeue()
        val (repeat, next, targets) = n.pulse(high, from)
        if (repeat.isDefined) {
          val value = targets.map(v => (v, nodes.get(v))).map((_, repeat.get, n.id))
          if (debug) {value.foreach {case ((id, n), b, s) => println(s"$s --${if(b)"high" else "low"}--> ${id}")}}
          if (repeat.get) {
            highPulses += value.size
          } else {
            lowPulses += value.size
          }
          toHandle.addAll(value.collect{ case ((_, Some(n)), bool, str) => (n, bool, str) })
          nodes.put(n.id, next)
        }
        //println(nodes)
      }
      val nextComputer = (nodes(bc.id).asInstanceOf[Broadcast], in.map(i => nodes(i.id).asInstanceOf[FlipFlop]), c.map(i => nodes(i.id).asInstanceOf[Conjunction]))
      nextComputer
    }
    highPulses * lowPulses
  }

  override def part2(strings: Seq[String]): Long = {
    val debug = false
    val invs = strings.filter(_.startsWith("%")).map(FlipFlop(_))
    val broadcast = Broadcast(strings.find(_.startsWith("broadcaster")).get)
    val tmpC = strings.filter(_.startsWith("&")).map(s => Conjunction(s, Seq()))
    val allNodes = (invs.map(n => (n.id, n)).toMap ++ tmpC.map(n => (n.id, n)).toMap ++ Map((broadcast.id -> broadcast)))
    val conjs = tmpC.map(c => Conjunction(c.id, allNodes.values.filter(_.targets.contains(c.id)).map(v => (v.id, false)).toMap, c.targets))
    val trigger = conjs.find(_.targets.contains("rx")).get
    val parentsSecond = mutable.HashMap[String, Option[Long]]()
    val parents = mutable.HashMap[String, Option[Long]]()
    trigger.inputPulses.keySet.foreach(s => parents.put(s, None))
    trigger.inputPulses.keySet.foreach(s => parentsSecond.put(s, None))
    var highPulses = 0L
    var lowPulses = 0L
    val maxStates = mutable.HashMap[Map[String, Boolean], Long]()
    (0 to 100000).foldLeft((broadcast, invs, conjs)) { case (computer@(bc, in, c), time) =>
      if (parentsSecond.exists(_._2.isEmpty)) {

      var hp = 0L
      var lp = 0L
      var adjusted = false
      val tmp = (in.map(n => (n.id, n)).toMap ++ c.map(n => (n.id, n)).toMap ++ Map((bc.id -> bc)))
      val nodes = mutable.HashMap[String, Node]()
      nodes.addAll(tmp)
      //simulate button
      val toHandle = mutable.Queue[(Node, Boolean, String)]()
      toHandle.append((bc, false, "button"))
      if (debug) {
        println(s"button --low-> ${bc.name} ($time)")
      }
      lowPulses += 1L
      while (toHandle.nonEmpty) {
        val pop@(n, high, from) = toHandle.dequeue()
        val v@(repeat, next, targets) = n.pulse(high, from)

        if (n.id == trigger.id && high) {
          if (parentsSecond(from).isEmpty) {
            parentsSecond.put(from, Some(time))
          }
        }

        if (repeat.isDefined) {
          if (parents.contains(n.id) && parents(n.id).isEmpty) {
            if (repeat.get) {
              parents.put(n.id, Some(time))
            }
          }
          if (parents.contains(n.id) && parents(n.id).isDefined && parents(n.id).get != time && parentsSecond.contains(n.id) && parentsSecond(n.id).isEmpty) {
            if (repeat.get) {
              parentsSecond.put(n.id, Some(time - parents(n.id).get))
            }
          }
          val value = targets.map(v => (v, nodes.get(v))).map((_, repeat.get, n.id))
          if (debug) {
            value.foreach { case ((id, n), b, s) => println(s"$s --${if (b) "high" else "low"}--> ${id}") }
          }
          if (repeat.get) {
            hp += value.size
            if (targets.contains("rx")) {
              val before = n.asInstanceOf[Conjunction].inputPulses
              val after = next.asInstanceOf[Conjunction].inputPulses
              if (after != before) {
                adjusted = true
                if (!maxStates.contains(after)) {
                  maxStates.put(after, time)
                  println(s"$time: $before \t becomes: $after")
                }
              }
            }
          } else {
            if (targets.contains("rx")) {
              return time
            }
            lp += value.size
          }
          toHandle.addAll(value.collect { case ((_, Some(n)), bool, str) => (n, bool, str) })
          nodes.put(n.id, next)
        }
        //println(nodes)
      }
      val nextComputer = (nodes(bc.id).asInstanceOf[Broadcast], in.map(i => nodes(i.id).asInstanceOf[FlipFlop]), c.map(i => nodes(i.id).asInstanceOf[Conjunction]))
      highPulses +=hp
      lowPulses +=lp
      //if (adjusted)
      //println(s"$hp\t$lp")
      nextComputer
      } else {computer}
    }
    val cycle = parents.values.map(_.get + 1).reduce(lcm)
    val cycle2 = parentsSecond.values.map(_.get + 1).reduce(lcm)
    val mul = parents.values.map(_.get + 1).product
    //val start = highs.values.max
    cycle

  }

  def gcd(l: Long, r: Long): Long = {
    var a = l
    var b = r
    while (b != 0) {
      val tmp = b
      b = a % b
      a = tmp
    }
    a
  }

  def lcm(l: Long, r: Long): Long = {
    l * r / gcd(l, r)
  }

  trait Node {

    def id: String

    def targets: Seq[String]

    def pulse(high: Boolean, from: String): (Option[Boolean], Node, Seq[String])
    def name: String
  }

  case class Button() extends Node {
    override def id: String = "button"

    override def targets: Seq[String] = Seq("broadcaster")

    override def pulse(high: Boolean, from: String): (Option[Boolean], Node, Seq[String]) = (Some(false), this, targets)

    override def name: String = id
  }

  case class FlipFlop(id: String, on: Boolean, targets: Seq[String]) extends Node {
    // ignores high pulses

    override def pulse(high: Boolean, from: String): (Option[Boolean], Node, Seq[String]) = {
      if (high) {
        (None, this, Seq())
      } else {
        val next = !on
        (Some(next), FlipFlop(id, next, targets), targets)
      }
    }

    override def name: String = s"%$id"
  }

  case class Conjunction(id: String, inputPulses: Map[String, Boolean], targets: Seq[String]) extends Node {
    // remembers all inputs

    override def pulse(high: Boolean, from: String): (Option[Boolean], Node, Seq[String]) = {
      // c sends high when should be low
      val inputs = inputPulses ++ Map(from -> high)
      val someBoolean = Some(!inputs.values.forall(v => v))
      (someBoolean, Conjunction(id, inputs, targets), targets)

    }
    override def name: String = s"&$id"

  }

  case class Broadcast(targets: Seq[String]) extends Node {
    override def pulse(high: Boolean, from: String): (Option[Boolean], Node, Seq[String]) = (Some(high), this, targets)

    override def id: String = "broadcaster"
    override def name: String = s"$id"

  }


  object FlipFlop {
    def apply(s: String): FlipFlop = {
      val split = s.split(" -> ")
      FlipFlop(split.head.strip().stripPrefix("%"), on = false, split.last.split(",").map(_.strip()))
    }
  }

  object Conjunction {
    def apply(s: String, inputs: Seq[Node]): Conjunction = {
      val split = s.split(" -> ")
      val targets = split.last.split(",").map(_.strip())
      val in = inputs.map(v => (v.id, false)).toMap
      Conjunction(split.head.strip().stripPrefix("&"), in, targets)
    }
  }

  object Broadcast {
    def apply(s: String): Broadcast = {
      val split = s.split(" -> ")
      Broadcast(split.last.split(",").map(_.strip()))
    }
  }
}
