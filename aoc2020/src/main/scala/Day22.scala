import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day22 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val result = Option(input(strings)).map(t => combat(t._1, t._2, recursive = false)).map(t => t._1 ++ t._2).get
    result.indices.map(i => (result.size - i) * result(i)).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val result = Option(input(strings)).map(t => combat(t._1, t._2, recursive = true)).map(t => t._1 ++ t._2).get
    result.indices.map(i => (result.size - i) * result(i)).sum
  }

  def combat(initialP1: Seq[Long], initialP2: Seq[Long], recursive: Boolean): (Seq[Long], Seq[Long]) = {
    var (p1, p2) = (initialP1, initialP2)
    val handHistory = mutable.Set[Seq[Long]]()
    while (p1.nonEmpty && p2.nonEmpty) {
      if(!handHistory.add(p1) || !handHistory.add(p2)){
        return (p1, Seq())
      }
      if (recursive && p1.head <= p1.tail.size && p2.head <= p2.tail.size) {
        val (rec1, rec2) = combat(p1.tail.take(p1.head.toInt), p2.tail.take(p2.head.toInt), recursive)
        if (rec1.size > rec2.size) {
          p1 = p1.tail ++ Seq(p1.head, p2.head)
          p2 = p2.tail
        } else {
          p2 = (p2.tail ++ Seq(p2.head, p1.head))
          p1 = p1.tail
        }
      }
      else if (p1.head > p2.head) {
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
