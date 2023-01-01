import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = exposedSides(readInput(strings)).map(_.size).sum

  override def part2(strings: Seq[String]): Long = {
    val points = readInput(strings)
    val sides = exposedSides(points).flatten
    val wet = sides.toSet.intersect(emitSteam(points))
    sides.count(wet.contains)
  }

  private def exposedSides(points: Seq[(Int, Int, Int)]): Seq[Seq[(Int, Int, Int)]] = {
    points.map { case current@(x, y, z) =>
      val others = points.filterNot(_ == current)
      val neighbors = directions
        .map { case (dx, dy, dz) => (x + dx, y + dy, z + dz) }
      neighbors.filterNot(others.contains)
    }
  }

  private def readInput(strings: Seq[String]): Seq[(Int, Int, Int)] = {
    strings.map(_.split(",")).map(l => (l.head.toInt, l.tail.head.toInt, l.last.toInt))
  }

  def directions = Seq(
    (-1, 0, 0),
    (1, 0, 0),
    (0, -1, 0),
    (0, 1, 0),
    (0, 0, -1),
    (0, 0, 1),
  )

  def emitSteam(droplets: Seq[(Int, Int, Int)]): Set[(Int, Int, Int)] = {
    val visited = mutable.HashSet[(Int, Int, Int)]()
    val (xMin, xMax) = (droplets.map(_._1).min - 1, droplets.map(_._1).max + 1)
    val (yMin, yMax) = (droplets.map(_._2).min - 1, droplets.map(_._2).max + 1)
    val (zMin, zMax) = (droplets.map(_._3).min - 1, droplets.map(_._3).max + 1)
    val frontier = mutable.PriorityQueue[(Int, Int, Int)]()
    frontier.enqueue((xMin, yMin, zMin))
    while (frontier.nonEmpty) {
      val pop@(x, y, z) = frontier.dequeue()
      if (visited.add(pop)) {
        val moves = directions
          .map { case (dx, dy, dz) => (x + dx, y + dy, z + dz) }
          .filterNot { case (x1, y1, z1) => x1 < xMin || xMax < x1 || y1 < yMin || yMax < y1 || z1 < zMin || zMax < z1 }
          .filterNot(visited.contains)
          .filterNot(droplets.contains)
        frontier.addAll(moves)
      }
    }
    visited.toSet
  }
}
