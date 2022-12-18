import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = exposedSides(readInput(strings)).map(_.size).sum

  override def part2(strings: Seq[String]): Long = {
    val points = readInput(strings)
    val (xmin, xmax) = (points.map(_._1).min, points.map(_._1).max)
    val (ymin, ymax) = (points.map(_._2).min, points.map(_._2).max)
    val (zmin, zmax) = (points.map(_._3).min, points.map(_._3).max)
    val limits = (xmin, xmax, ymin, ymax, zmin, zmax)
    val sides = exposedSides(points).flatten

    val escapesFrom = mutable.HashMap[(Int, Int, Int), Boolean]()
    val sideSet = sides.toSet.filter(p => reachesExterior(p, points, escapesFrom, limits))
    sides.count(sideSet.contains)
  }

  private def exposedSides(points: Seq[(Int, Int, Int)]): Seq[Seq[(Int, Int, Int)]] = {
    points.map { case current@(x, y, z) =>
      val others = points.filterNot(_ == current)
      val neighbors = directions
        .map { case (dx, dy, dz) => (x + dx, y + dy, z + dz) }
      neighbors.filterNot(others.contains)
    }
  }

  private def readInput(strings: Seq[String]) = {
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

  def reachesExterior(p: (Int, Int, Int), droplets: Seq[(Int, Int, Int)], escapesFrom: mutable.HashMap[(Int, Int, Int), Boolean], limits: (Int, Int, Int, Int, Int, Int)) = {
    val (xmin, xmax, ymin, ymax, zmin, zmax) = limits

    val frontier = mutable.PriorityQueue[(Int, Int, Int)]()
    frontier.enqueue(p)
    var escaped = false
    val visited = mutable.HashSet[(Int, Int, Int)]()
    while (frontier.nonEmpty) {
      val pop@(x, y, z) = frontier.dequeue()
      if (x < xmin || y < ymin || z < zmin || x > xmax || y > ymax || z > zmax) {
        escaped = true
        frontier.clear()
      } else if (escapesFrom.contains(pop)) {
        escaped = escapesFrom(pop)
        frontier.clear()
      } else if (visited.add(pop)) {
        val moves = directions
          .map { case (dx, dy, dz) => (x + dx, y + dy, z + dz) }
          .filterNot(visited.contains)
          .filterNot(droplets.contains)
        frontier.addAll(moves)
      }
    }
    visited.foreach(v => escapesFrom.put(v, escaped))
    escaped
  }
}
