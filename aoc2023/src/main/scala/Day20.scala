import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day20 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
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
    -1
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
