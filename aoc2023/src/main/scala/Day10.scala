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
      .filter(l.contains)
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

  def exploreInterior(loop: Map[(Int, Int), Char], map: Map[(Int, Int), Char]): Set[(Int, Int)] = {
    val toCheck = map.keySet -- loop.keySet
    val tl = toCheck.minBy(v => (v._2, v._1))
    val regions = mutable.HashSet[Set[(Int, Int)]]()
    val f = mutable.PriorityQueue[(Int, Int)]()(Ordering.by(_._2))
    f.addAll(toCheck)
    while (f.nonEmpty) {
      val pop = f.dequeue()
      val ns = around(pop)
        .filter(a => map.contains(a._2))
        .filterNot(a => loop.contains(a._2))
      val rs = ns.flatMap(a => regions.find(_.contains(a._2))).toSet
      if (rs.isEmpty) {
        //new reg
        val newReg = Set[(Int, Int)](pop)
        regions.add(newReg)
      } else if (rs.size > 1) {
        // join regs
        rs.foreach(r => regions.remove(r))
        val joined = rs.flatten ++ Set(pop)
        regions.add(joined)
      } else {
        // one reg
        regions.remove(rs.head)
        regions.add(rs.head ++ Set(pop))
      }
    }

    val skipOuter = regions.toSet.filterNot(s => s.contains((0, 0)))


    val inside = mutable.HashSet[Set[(Int, Int)]]()

    val topLeft = loop.toSeq.minBy(v => (v._1._2, v._1._1))
    // F, -> (medsols)

    val frontier = mutable.PriorityQueue[((Int, Int), Char, (Int, Int))]()(Ordering.by(_._3))
    val visited = mutable.HashMap[(Int, Int), Char]()
    frontier.addOne((topLeft._1, topLeft._2, (1, 0)))
    while (frontier.nonEmpty) {
      val pop@(p@(x, y), c, direction) = frontier.dequeue()
      if (!visited.contains(p)) {
        visited.put(p, c)
        direction match {
          case (1, 0) =>
            val below = (x, y + 1)
            if (!loop.contains(below)) {
              regions.find(s => s.contains(below))
                .foreach(s => inside.add(s))
            }
            c match {
              case '-' =>
                val nextDirection = direction
                val next = (x + 1, y)
                frontier.addOne(next, loop(next), nextDirection)
              case 'J' =>
                val nextDirection = (0, -1)
                val next = (x, y - 1)
                frontier.addOne(next, loop(next), nextDirection)
                val rightOf = (x + 1, y)
                if (!loop.contains(rightOf)) {
                  regions.find(s => s.contains(rightOf))
                    .foreach(s => inside.add(s))
                }
              case '7' =>
                val nextDirection = (0, 1)
                val next = (x, y + 1)
                frontier.addOne(next, loop(next), nextDirection)

              case _ =>
                if (pop._1 == topLeft._1) {
                  val nextDirection = direction
                  val next = (x + 1, y)
                  frontier.addOne(next, loop(next), nextDirection)
                } else {
                  println(s"shouldn't happen: $pop")
                }
            }
          case (-1, 0) =>
            val above = (x, y - 1)
            if (!loop.contains(above)) {
              regions.find(s => s.contains(above))
                .foreach(s => inside.add(s))
            }
            c match {

              case '-' =>
                val nextDirection = direction
                val next = (x - 1, y)
                frontier.addOne(next, loop(next), nextDirection)
              case 'L' =>
                val nextDirection = (0, -1)
                val next = (x, y - 1)
                frontier.addOne(next, loop(next), nextDirection)

              case 'F' =>

                val nextDirection = (0, 1)
                val next = (x, y + 1)
                frontier.addOne(next, loop(next), nextDirection)
                val leftOf = (x - 1, y)
                if (!loop.contains(leftOf)) {
                  regions.find(s => s.contains(leftOf))
                    .foreach(s => inside.add(s))
                }
              case _ => println(s"shouldn't happen: $pop")


            }
          case (0, 1) =>

            val leftOf = (x - 1, y)
            if (!loop.contains(leftOf)) {
              regions.find(s => s.contains(leftOf))
                .foreach(s => inside.add(s))
            }
            c match {
              case '|' =>
                val nextDirection = direction
                val next = (x, y + 1)
                frontier.addOne(next, loop(next), nextDirection)

              case 'L' =>
                val nextDirection = (1, 0)
                val next = (x + 1, y)
                frontier.addOne(next, loop(next), nextDirection)
                val below = (x, y+1)
                if (!loop.contains(below)) {
                  regions.find(s => s.contains(below))
                    .foreach(s => inside.add(s))
                }
              case 'J' =>
                val nextDirection = (-1, 0)
                val next = (x - 1, y)
                frontier.addOne(next, loop(next), nextDirection)
              case _ => println(s"shouldn't happen: $pop")


            }
          case (0, -1) =>
            val rightOf = (x + 1, y)
            if (!loop.contains(rightOf)) {
              regions.find(s => s.contains(rightOf))
                .foreach(s => inside.add(s))
            }
            c match {

              case '|' =>
                val nextDirection = direction
                val next = (x, y - 1)
                frontier.addOne(next, loop(next), nextDirection)

              case 'F' =>
                val nextDirection = (1, 0)
                val next = (x + 1, y)
                frontier.addOne(next, loop(next), nextDirection)

              case '7' =>
                val nextDirection = (-1, 0)
                val next = (x - 1, y)
                frontier.addOne(next, loop(next), nextDirection)
                val above = (x, y - 1)
                if (!loop.contains(above)) {
                  regions.find(s => s.contains(above)).foreach(s => inside.add(s))
                }
              case _ => println(s"shouldn't happen: $pop")
            }
        }

      }
    }
    val flatten = inside.toSet.flatten
    val outside = regions.flatten -- flatten
    val outsideG = regions -- inside.toSet
    flatten
  }
}
