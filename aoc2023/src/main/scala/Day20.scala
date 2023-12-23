import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.MathUtils.LCM

import scala.collection.mutable

object Day20 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (_, h, l) = (1 to 1000).foldLeft((parse(strings), 0L, 0L)) { case ((nodes, highPulses, lowPulses), _) =>
      val (next, lp, hp, _) = clickButton(nodes)
      (next, highPulses + hp, lowPulses + lp)
    }
    h * l
  }

  override def part2(strings: Seq[String]): Long = {
    var computer = parse(strings)
    val triggerId = computer.find(_._2.targets.contains("rx")).map(_._1)

    val parents = mutable.HashMap[String, Option[Long]]()
    triggerId.foreach(computer(_).asInstanceOf[Conjunction].inputPulses.keySet.foreach(s => parents.put(s, None)))

    var it = 1L
    while (parents.exists(_._2.isEmpty)) {
      val (nodes, _, _, pulsed) = clickButton(computer)
      computer = nodes
      pulsed.filter(s => parents(s).isEmpty).foreach(parents.put(_, Some(it)))
      it += 1
    }
    parents.values.map(_.get).foldLeft(1L)(LCM)
  }

  private def parse(strings: Seq[String]): Map[String, Node] = {
    val invs = strings.filter(_.startsWith("%")).map(FlipFlop(_))
    val broadcast = Broadcast(strings.find(_.startsWith("broadcaster")).get)
    val tmpC = strings.filter(_.startsWith("&")).map(s => Conjunction(s, Seq()))
    val allNodes = (invs.map(n => (n.id, n)).toMap ++ tmpC.map(n => (n.id, n)).toMap ++ Map((broadcast.id -> broadcast)))
    val conjs = tmpC.map(c => Conjunction(c.id, allNodes.values.filter(_.targets.contains(c.id)).map(v => (v.id, false)).toMap, c.targets))
    (invs.map(n => (n.id, n)).toMap ++ conjs.map(n => (n.id, n)).toMap ++ Map((broadcast.id -> broadcast)))
  }

  private def clickButton(computer: Map[String, Node]) = {
    val trigger = computer.find(_._2.targets.contains("rx")).map(_._1)
    val debug: Boolean = false
    val bid: String = "broadcaster"
    var hp: Long = 0L
    var lp: Long = 0L
    val t = mutable.HashMap[String, Boolean]()
    val nodes = mutable.HashMap[String, Node]()
    nodes.addAll(computer)
    //simulate button
    val toHandle = mutable.Queue[(String, Boolean, String)]()
    toHandle.append((bid, false, "button"))
    if (debug) {
      println(s"button --low-> ${nodes(bid).name}")
    }
    lp += 1L
    while (toHandle.nonEmpty) {
      val (nid, high, from) = toHandle.dequeue()
      val (repeat, next, targets) = nodes(nid).pulse(high, from)
      val n = nodes(nid)
      if (trigger.contains(nid) && high) {
        t.put(from, true)
      }

      if (repeat.isDefined) {
        val value = targets.map(v => (v, nodes.get(v))).map((_, repeat.get, n.id))
        if (repeat.get) {
          hp += value.size
        } else {
          lp += value.size
        }
        toHandle.addAll(value.collect { case ((_, Some(n)), bool, str) => (n.id, bool, str) })
        nodes.put(n.id, next)
      }
    }
    (nodes.toMap, lp, hp, t.filter(_._2).keys.toSeq)
  }

  def isPrime(n: Int): Boolean = !((2 until n - 1) exists (n % _ == 0))

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
