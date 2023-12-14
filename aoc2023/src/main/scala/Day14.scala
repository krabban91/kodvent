import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day14 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (walls: Set[(Long, Long)], rocks: Set[(Long, Long)]) = parse(strings)
    val still = rollRocks(walls, rocks, (0, -1), Ordering.by[(Long, Long), Long](-_._2))
    val maxY = strings.indices.max
    value(still, maxY)
  }

  override def part2(strings: Seq[String]): Long = {
    val (walls: Set[(Long, Long)], rocks: Set[(Long, Long)]) = parse(strings)
    var curr = rocks
    val maxY = strings.indices.max
    var cycle = 0L
    val states = mutable.HashMap[Set[(Long, Long)], Long]()
    val cycles = mutable.HashMap[Long, Long]()
    val l = 1000000000L
    while (cycle < l) {
      cycle = cycle + 1
      curr = spinRocks(walls, curr)
      if (states.contains(curr)) {
        val start = states(curr)
        val length = cycle - start
        val mod = (l - start) % length
        cycle = l
        cycles.put(cycle, cycles(start + mod))
      } else {
        cycles.put(cycle, value(curr, maxY))
        states.put(curr, cycle)
      }
    }
    cycles(l)
  }

  private def rollRocks(walls: Set[(Long, Long)], rocks: Set[(Long, Long)], direction: (Long, Long), sorting: Ordering[(Long, Long)]): Set[(Long, Long)] = {
    val maxX = math.max(walls.map(_._1).max, rocks.map(_._1).max)
    val maxY = math.max(walls.map(_._2).max, rocks.map(_._2).max)
    val rolling = mutable.PriorityQueue[(Long, Long)]()(sorting)
    val still = mutable.HashSet[(Long, Long)]()
    rolling.addAll(rocks)
    while (rolling.nonEmpty) {
      val pop@(x, y) = rolling.dequeue()
      val next = (x + direction._1, y + direction._2)
      if (isBorder(direction, maxX, maxY, x, y) || walls.contains(next) || still.contains(next)) {
        still.add(pop)
      } else {
        rolling.enqueue(next)
      }
    }
    still.toSet
  }

  private def isBorder(direction: (Long, Long), maxX: Long, maxY: Long, x: Long, y: Long) = {
    (if (direction._1 == 0) {
      if (direction._2 < 0) {
        y == 0
      } else {
        y == maxY
      }
    } else {
      if (direction._1 < 0) {
        x == 0
      } else {
        x == maxX
      }
    })
  }

  private def spinRocks(walls: Set[(Long, Long)], rocks: Set[(Long, Long)]): Set[(Long, Long)] = {
    val directions = Seq((0L, -1L), (-1L, 0L), (0L, 1L), (1L, 0L))
    val sorting = Seq(Ordering.by[(Long, Long), Long](-_._2), Ordering.by[(Long, Long), Long](-_._1), Ordering.by[(Long, Long), Long](_._2), Ordering.by[(Long, Long), Long](_._1))
    directions.indices.foldLeft(rocks) { case (curr, i) => rollRocks(walls, curr, directions(i), sorting(i)) }
  }

  private def parse(strings: Seq[String]) = {
    val walls = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.filter { case (c, _) => c == '#' }.map { case (_, x) => (x.toLong, y.toLong) } }.toSet
    val rocks = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.filter { case (c, _) => c == 'O' }.map { case (_, x) => (x.toLong, y.toLong) } }.toSet
    (walls, rocks)
  }

  private def value(rocks: Set[(Long, Long)], maxY: Int) = {
    val value = (0 to maxY).map(y => (y, rocks.count(p => p._2 == y).toLong))
    value.map(t => (maxY - t._1 + 1) * t._2).sum
  }
}
