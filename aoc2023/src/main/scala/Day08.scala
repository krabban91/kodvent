import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.MathUtils.LCM

import scala.collection.mutable

object Day08 extends App with AoCPart1Test with AoCPart2Test {

  //printResultPart1Test
  //printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (instr, m) = extractInput(strings)
    val start = Set("AAA")
    val end = m.keySet.filter(_.last == 'Z')
    ghostTravel(start, end, instr, m)
  }

  override def part2(strings: Seq[String]): Long = {
    val (instr, m) = extractInput(strings)
    val start = m.keySet.filter(_.last == 'A')
    val end = m.keySet.filter(_.last == 'Z')
    ghostTravel(start, end, instr, m)
  }

  private def ghostTravel(start: Set[String], end: Set[String], instr: String, m: Map[String, (String, String)]): Long = {

    val cycles = start.toSeq.map(t => findCycle(t, instr, m))
    val toEnd = start.toSeq.map(s => travel(s, end, instr, m))
    val lcms = toEnd.toSet.foldLeft(1L) { case (o, v) => LCM(o, v) }
    val minOffs = cycles.map(_._1).max
    var curr = start
    var steps = lcms-1

    lcms
  }

  private def travel(start: String, end: Set[String], instr: String, m: Map[String, (String, String)]): Long = {
    var steps = 0L
    var curr = start

    while (!end.contains(curr)) {
      curr = instr((steps % instr.length).toInt) match {
        case 'L' => m(curr)._1
        case 'R' => m(curr)._2
      }
      steps = steps + 1
    }
    steps
  }

  private def findCycle(start: String, instr: String, m: Map[String, (String, String)]): (Long, (String, Long)) = {
    var steps = 0L
    var curr = start
    val visited = mutable.HashMap[(String, Int) , Long]()
    while (!visited.contains((curr, (steps % instr.length).toInt))) {
      visited.put((curr, (steps % instr.length).toInt), steps)
      curr = instr((steps % instr.length).toInt) match {
        case 'L' => m(curr)._1
        case 'R' => m(curr)._2
      }
      steps = steps + 1
    }
    (visited((curr, (steps % instr.length).toInt)), (curr, steps - visited((curr, (steps % instr.length).toInt))))
  }

  private def extractInput(strings: Seq[String]) = {
    val g = groupsSeparatedByTwoNewlines(strings)
    val instr = g.head.strip()
    val m = g.last.split("\n").map(_.strip()).filter(_.nonEmpty)
      .map(s => (s.split(" = ").head.strip(), (s.split(" = ").last.split(", ").head.stripPrefix("("), s.split(" = ").last.split(", ").last.stripSuffix(")"))))
      .toMap
    (instr, m)
  }
}
