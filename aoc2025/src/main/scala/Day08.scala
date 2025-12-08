import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day08 extends App with AoCPart1Test with AoCPart2Test {

  private type Point = (Long, Long, Long)
  private type Junction = Set[Point]
  private type Circuit = Set[Point]

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val allPoints: Set[Point] = parsePoints(strings)
    val (circuits, _) = connectCircuits(allPoints, part1 = true)
    val threeLargest = circuits.toSeq.map(_.size.toLong).sorted.takeRight(3)
    threeLargest.product

  }

  override def part2(strings: Seq[String]): Long = {
    val allPoints = parsePoints(strings)
    val (_, last) = connectCircuits(allPoints, part1 = false)
    last.toSeq.map(_._1).product
  }

  private def parsePoints(strings: Seq[String]): Set[Point] = {
    strings.map { s =>
      val l = s.split(",")
      (l.head.toLong, l.tail.head.toLong, l.last.toLong)
    }.toSet
  }

  def distance(j: Junction): Double = {
    val seq: Seq[Point] = j.toSeq;
    val (a, b) = (seq.head, seq.last);
    Math.sqrt(Math.pow(a._1 - b._1, 2) + Math.pow(a._2 - b._2, 2) + Math.pow(a._3 - b._3, 2))
  }

  private def connectCircuits(allPoints: Set[Point], part1: Boolean): (Set[Circuit], Junction) = {
    val earlyStop = if (allPoints.size == 20) 10 else 1000
    val circuits = mutable.HashSet[Circuit]()
    circuits.addAll(allPoints.map(Set(_)))
    val connected = mutable.HashSet[Junction]()

    val allJunctions: Set[Junction] = allPoints.flatMap(a => allPoints.excl(a).map(b => Set(b, a)))
    val filteredDistances = mutable.PriorityQueue[(Junction, Double)]()(Ordering.by(-_._2))
    filteredDistances.addAll(allJunctions.toSeq.map { j => (j, distance(j)) })
    var last: Junction = null
    while (circuits.size > 1 && (!part1 || connected.size < earlyStop)) {
      val (junction, _) = filteredDistances.dequeue()

      last = junction
      val selected: Seq[Circuit] = junction.flatMap(s => circuits.find(_.contains(s))).toSeq
      val (from, to) = (selected.head, selected.last)

      circuits.remove(from)
      circuits.remove(to)
      circuits.add(from ++ to)
      connected.add(junction)
    }
    circuits.toSet -> last
  }
}
