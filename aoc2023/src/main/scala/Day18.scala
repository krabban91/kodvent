import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = solve(strings.map(Instruction(_)))


  override def part2(strings: Seq[String]): Long = solve(strings.map(Instruction(_)), part2 = true)


  def solve(instructions: Seq[Instruction], part2: Boolean = false): Long = {
    val points = mutable.ListBuffer[(Long, Long)]()

    var start = ZERO2
    instructions.foreach { case in =>
      val (dir, dist) = if (part2) in.fromHex else (in.dir, in.dist)
      points.addOne(start)
      val next = start + dir * (dist, dist)
      start = next
    }

    picksTheorem(points)
  }

  private def picksTheorem(points: ListBuffer[(Long, Long)]): Long = {
    // A = i + b/2 -1
    // i = (A+1) - b/2
    val a = area(points.toSeq)
    val b = bCount(points.toSeq)
    val i = (a + 1) - b / 2
    i + b
  }

  def bCount(cords: Seq[(Long, Long)]): Long = {
    (cords :+ cords.head).sliding(2).map { case l => l.head manhattan l.last }.sum
  }

  def area(coords: Seq[(Long, Long)]): Long = {
    val points = (coords zip coords.tail) :+ (coords.last, coords.head)
    (points.map { case (a, b) => a._1 * b._2 - a._2 * b._1 }.sum / 2.0).toLong
  }


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

}
