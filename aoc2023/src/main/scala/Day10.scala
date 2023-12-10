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

  def loopStartingWith(l: Map[(Int, Int), Char], str: Char): (Map[(Int, Int), Char], Long) = {
    val (start, s) = l.find { case (_, c) => c == str }.get
    val nextTo = connectsTo(s, start)
    val connected = nextTo.map(p => (p, connectsTo(l(p), p)))
      .filter(_._2.contains(start))
    val dx = connected.map(_._1).map(p => (p._1 - start._1, p._2 - start._2))
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
      if (d > maxD) {
        maxD = d
      }
      val con = connectsTo(c, p)
        .filter(p => !table.contains(p))
        .map(cp => (cp, l(cp), d + 1))
      con.foreach { v =>
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
    val map = parse(strings)
    val (_, maxD) = loopStartingWith(map, 'S')
    maxD

  }

  override def part2(strings: Seq[String]): Long = {
    val map = parse(strings)
    val (loop, _) = loopStartingWith(map, 'S')
    exploreInterior(loop, map).size
  }

  def around(p: (Int, Int)) = Seq((-1, 0), (1, 0), (0, -1), (0, 1))
    .map { case p2@(dx, dy) => (p2, (p._1 + dx, p._2 + dy)) }

  def exploreInterior(loop: Map[(Int, Int), Char], map: Map[(Int, Int), Char]): Seq[((Int, Int), Char)] = {
    val frontier = mutable.PriorityQueue[((Int, Int), Char, (Int, Int))]()(Ordering.by(_._3))
    val visited = mutable.HashMap[(Int, Int), Char]()
    val topLeft = loop.toSeq.minBy(v => (v._1._2, v._1._1))
    frontier.addOne(((topLeft._1._1 - 1, topLeft._1._2+1), topLeft._2, (1, 0)))
    while (frontier.nonEmpty) {
      val pop@(p@(x, y), c, direction) = frontier.dequeue()
      if (!visited.contains(p)) {
        visited.put(p, c)
        val surr = around(p)
          .filter(a => map.contains(a._2))
          .filterNot(a => visited.contains(a._2))
        direction match {
          case (1, 0) => c match {
            case '|' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case 'J' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case '7' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case _ => around(p)
              .filter(a => map.contains(a._2))
              .filterNot(a => visited.contains(a._2))
              .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
          }
          case (-1, 0) => c match {
            case '|' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case 'L' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case 'F' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case _ =>
              around(p)
                .filter(a => map.contains(a._2))
                .filterNot(a => visited.contains(a._2))
                .foreach(a => frontier.addOne(a._2, map(a._2), a._1))

          }
          case (0, 1) => c match {
            case '-' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case 'L' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case 'J' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case _ =>
              around(p)
                .filter(a => map.contains(a._2))
                .filterNot(a => visited.contains(a._2))
                .foreach(a => frontier.addOne(a._2, map(a._2), a._1))

          }
          case (0, -1) => c match {
            case '-' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case 'F' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case '7' =>
              if (!loop.contains(p)) {
                surr
                  .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
              }
            case _ =>
              surr
                .foreach(a => frontier.addOne(a._2, map(a._2), a._1))
          }
        }

      }
    }
    (visited.toSet -- loop.toSet).toSeq
  }
}
