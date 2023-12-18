import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

import scala.collection.mutable

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Instruction(dir: (Long, Long), dist: Long, color: String) {
    def fromHex: ((Long, Long), Long) = {
      val distance = color.last match {
        case '0' => EAST
        case '1' => SOUTH
        case '2' => WEST
        case '3' => NORTH
      }
      (distance, java.lang.Long.parseLong(color.tail.dropRight(1), 16).toLong)
    }
  }

  object Instruction {
    def apply(string: String): Instruction = {
      val s = string.split(" ")
      val dir = s.head match {
        case "L" => WEST
        case "U" => NORTH
        case "R" => EAST
        case "D" => SOUTH
      }
      Instruction(dir, s(1).toLong, s.last.stripSuffix(")").stripPrefix("("))
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val input = strings.map(Instruction(_))
    val ranges = mutable.ListBuffer[((Long, Long), (Long, Long))]()

    var start = ZERO2
    input.foreach { case Instruction(dir@(x, y), dist, color) =>
      val next = start + dir * (dist, dist)
      ranges.addOne((start, next))
      start = next
    }

    val a = area(ranges.toSeq)
    // A = i + b/2 -1
    // i = (A+1)- b/2
    val b = bCount(ranges.toSeq)
    val i = (a + 1) - b/2
    i + b
  }


  def bCount(ranges: Seq[((Long, Long), (Long, Long))]): Long = {
    ranges.map{ case (l, r) => l manhattan r }.sum
  }

  def area(ranges: Seq[((Long, Long),(Long, Long))]): Long = {
    val coords = ranges.map(_._1) ++ Seq(ranges.last._2)
    val points = (coords zip coords.tail) :+ (coords.last, coords.head)
    //val points = ranges
    val a = (points.map { case (a, b) => a._1 * b._2 - a._2 * b._1 }.sum / 2.0).toLong
    a
  }

  override def part2(strings: Seq[String]): Long = {
    val input = strings.map(Instruction(_))
    val ranges = mutable.ListBuffer[((Long, Long), (Long, Long))]()

    var start = ZERO2
    input.foreach { case inst@Instruction(_@(x, y), _, color) =>
      val (dir, dist) = inst.fromHex
      val next = start + dir * (dist, dist)
      ranges.addOne((start, next))
      start = next
    }

    val a = area(ranges.toSeq)
    // A = i + b/2 -1
    // i = (A+1)- b/2
    val b = bCount(ranges.toSeq)
    val i = (a + 1) - b / 2
    i + b
  }
}
