import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day10 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  def connectsTo(c: Char, pos: (Int, Int)): Set[(Int, Int)] = {
    val map = keys
    map(c).map(dp => (pos._1 + dp._1, pos._2 + dp._2))
  }

  private def keys = {
    Map(
      '|' -> Set((0, -1), (0, 1)),
      '-' -> Set((-1, 0), (1, 0)),
      'L' -> Set((0, -1), (1, 0)),
      'J' -> Set((0, -1), (-1, 0)),
      '7' -> Set((0, 1), (-1, 0)),
      'F' -> Set((1, 0), (0, 1)),
      '.' -> Set(),
      'S' -> Set((0, -1), (0, 1), (-1, 0), (1, 0))
    )
  }

  def loopStartingWith(strings: Seq[String], str: Char): (Map[(Int, Int), Char], Long) = {
    val l = parse(strings)
    val (start, s) = l.find{ case (_, c) => c == str}.get
    val nextTo = connectsTo(s, start)
    val connected = nextTo.map(p => (p, connectsTo(l(p), p)))
      .filter(_._2.contains(start))
    val dx = connected.map(_._1).map(p => (p._1-start._1, p._2 - start._2))
    val k = keys
    val trueS = k.find(_._2 == dx).get
    val table = mutable.HashMap[(Int, Int), Char]()
    val curr = (trueS._1, start)
    table.put(curr._2, curr._1)
    var maxD = 0L
    val frontier = mutable.PriorityQueue[((Int, Int), Char, Int)]()(Ordering.by(-_._3))
    frontier.addOne((curr._2, curr._1, 0))
    while (frontier.nonEmpty) {
      val pop@(p, c, d) = frontier.dequeue()
      if (d > maxD) { maxD = d }
      val con = connectsTo(c, p)
        .filter(p => !table.contains(p))
        .map(cp => (cp, l(cp), d + 1))
      con.foreach{v =>
        table.put(v._1, v._2)
      }
      frontier.addAll(con)
    }
    (table.toMap, maxD)
  }

  private def parse(strings: Seq[String]): Map[(Int, Int), Char] = {
    strings.zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x, y), c) } }.toMap
  }

  override def part1(strings: Seq[String]): Long = {
    val (loop, maxD) = loopStartingWith(strings, 'S')
    maxD

  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
