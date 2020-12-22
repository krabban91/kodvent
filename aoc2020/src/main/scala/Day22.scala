import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day22 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val result = Option(input(strings)).map(t => combat(t._1, t._2)).map(t => t._1 ++ t._2).get
    result.indices.map(i => (result.size - i) * result(i)).sum
  }

  override def part2(strings: Seq[String]): Long = -1

  def combat(initialP1: Seq[Long], initialP2: Seq[Long]): (Seq[Long], Seq[Long]) = {
    var (p1, p2) = (initialP1, initialP2)
    while (p1.nonEmpty && p2.nonEmpty) {
      if (p1.head > p2.head) {
        p1 = p1.tail ++ Seq(p1.head, p2.head)
        p2 = p2.tail
      } else {
        p2 = p2.tail ++ Seq(p2.head, p1.head)
        p1 = p1.tail
      }
    }
    (p1, p2)
  }

  def input(strings: Seq[String]): (Seq[Long], Seq[Long]) = {
    val v = groupsSeparatedByTwoNewlines(strings)
      .map(s => s.split("\n").filterNot(_.isBlank))
      .map(_.tail)
      .map(v => v.map(_.toLong))
      .map(_.toSeq)
    (v.head, v.tail.head)
  }
}
