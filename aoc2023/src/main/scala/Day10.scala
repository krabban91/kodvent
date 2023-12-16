import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples.{EAST, NORTH, RichTuples2Longs, SOUTH, WEST}

import scala.collection.mutable

object Day10 extends App with AoCPart1Test with AoCPart2Test {

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

  private def connectsTo(c: Char, pos: (Long, Long)): Set[(Long, Long)] = keys(c).map(pos + _)

  private def keys = {
    Map(
      '|' -> Set(NORTH, SOUTH),
      '-' -> Set(WEST, EAST),
      'L' -> Set(NORTH, EAST),
      'J' -> Set(NORTH, WEST),
      '7' -> Set(SOUTH, WEST),
      'F' -> Set(EAST, SOUTH),
      '.' -> Set(),
      'S' -> Set(NORTH, WEST, SOUTH, EAST)
    )
  }

  private def loopStartingWith(l: Map[(Long, Long), Char], str: Char): (Map[(Long, Long), Char], Long) = {
    val (start, s) = l.find { case (_, c) => c == str }.get
    val nextTo = connectsTo(s, start)
      .filter(l.contains)
    val connected = nextTo.map(p => (p, connectsTo(l(p), p)))
      .filter(_._2.contains(start))
    val dx = connected.map(_._1).map(p => (p._1 - start._1, p._2 - start._2))
    val k = keys
    val trueS = k.find(_._2 == dx).get
    val table = mutable.HashMap[(Long, Long), Char]()
    val curr = (trueS._1, start)
    table.put(curr._2, curr._1)
    var maxD = 0L
    val frontier = mutable.PriorityQueue[((Long, Long), Char, Long)]()(Ordering.by(-_._3))
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

  private def parse(strings: Seq[String]): Map[(Long, Long), Char] = {
    strings.zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), c) } }.toMap
  }


  private def around(p: (Long, Long)) = Seq((-1, 0), (1, 0), (0, -1), (0, 1))
    .map { case p2@(dx, dy) => (p2, (p._1 + dx, p._2 + dy)) }

  private def exploreInterior(loop: Map[(Long, Long), Char], map: Map[(Long, Long), Char]): Set[(Long, Long)] = {
    val toCheck = map.keySet -- loop.keySet
    val tl = toCheck.minBy(v => (v._2, v._1))
    val regions: Set[Set[(Long, Long)]] = allRegions(toCheck, loop, map)

    regionsInsideLoop(loop, regions).flatten
  }

  private def regionsInsideLoop(loop: Map[(Long, Long), Char], regions: Set[Set[(Long, Long)]]) = {
    val inside = mutable.HashSet[Set[(Long, Long)]]()

    val topLeft = loop.toSeq.minBy(v => (v._1._2, v._1._1))
    // F, -> (medsols)

    val frontier = mutable.PriorityQueue[((Long, Long), Char, (Long, Long))]()(Ordering.by(_._3))
    val visited = mutable.HashMap[(Long, Long), Char]()
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
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)
              case 'J' =>
                val nextDirection = NORTH
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)
                val rightOf = p + EAST
                if (!loop.contains(rightOf)) {
                  regions.find(s => s.contains(rightOf))
                    .foreach(s => inside.add(s))
                }
              case '7' =>
                val nextDirection = SOUTH
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)

              case _ =>
                if (pop._1 == topLeft._1) {
                  val nextDirection = direction
                  val next = p + nextDirection
                  frontier.addOne(next, loop(next), nextDirection)
                } else {
                  println(s"shouldn't happen: $pop")
                }
            }
          case (-1, 0) =>
            val above = p + NORTH
            if (!loop.contains(above)) {
              regions.find(s => s.contains(above))
                .foreach(s => inside.add(s))
            }
            c match {
              case '-' =>
                val nextDirection = direction
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)
              case 'L' =>
                val nextDirection = NORTH
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)
              case 'F' =>
                val nextDirection = SOUTH
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)
                val leftOf = p + WEST
                if (!loop.contains(leftOf)) {
                  regions.find(s => s.contains(leftOf))
                    .foreach(s => inside.add(s))
                }
              case _ => println(s"shouldn't happen: $pop")
            }
          case (0, 1) =>

            val leftOf = p + WEST
            if (!loop.contains(leftOf)) {
              regions.find(s => s.contains(leftOf))
                .foreach(s => inside.add(s))
            }
            c match {
              case '|' =>
                val nextDirection = direction
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)

              case 'L' =>
                val nextDirection = EAST
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)
                val below = p + SOUTH
                if (!loop.contains(below)) {
                  regions.find(s => s.contains(below))
                    .foreach(s => inside.add(s))
                }
              case 'J' =>
                val nextDirection = WEST
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)
              case _ => println(s"shouldn't happen: $pop")


            }
          case (0, -1) =>
            val rightOf = p + EAST
            if (!loop.contains(rightOf)) {
              regions.find(s => s.contains(rightOf))
                .foreach(s => inside.add(s))
            }
            c match {

              case '|' =>
                val nextDirection = direction
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)

              case 'F' =>
                val nextDirection = EAST
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)

              case '7' =>
                val nextDirection = WEST
                val next = p + nextDirection
                frontier.addOne(next, loop(next), nextDirection)
                val above = p + NORTH
                if (!loop.contains(above)) {
                  regions.find(s => s.contains(above)).foreach(s => inside.add(s))
                }
              case _ => println(s"shouldn't happen: $pop")
            }
        }

      }
    }
    inside.toSet
  }

  private def allRegions(toCheck: Set[(Long, Long)], loop: Map[(Long, Long), Char], map: Map[(Long, Long), Char]) = {
    val regions = mutable.HashSet[Set[(Long, Long)]]()
    val f = mutable.PriorityQueue[(Long, Long)]()(Ordering.by(_._2))
    f.addAll(toCheck)
    while (f.nonEmpty) {
      val pop = f.dequeue()
      val ns = around(pop)
        .filter(a => map.contains(a._2))
        .filterNot(a => loop.contains(a._2))
      val rs = ns.flatMap(a => regions.find(_.contains(a._2))).toSet
      if (rs.isEmpty) {
        //new reg
        val newReg = Set[(Long, Long)](pop)
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
    regions.toSet
  }
}
