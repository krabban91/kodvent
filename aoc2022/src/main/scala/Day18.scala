import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val points = strings.map(_.split(",")).map(l => (l.head.toInt, l.tail.head.toInt, l.last.toInt))
    val directions = Seq(
      (-1, 0, 0),
      (1, 0, 0),
      (0, -1, 0),
      (0, 1, 0),
      (0, 0, -1),
      (0, 0, 1),

    )
    points.map { case current@(x, y, z) =>
      val others = points.filterNot(_ == current)
      val neighbors = directions
        .map { case (dx, dy, dz) => (x + dx, y + dy, z + dz) }
      val freeSurfaces = neighbors.filterNot(others.contains)
      freeSurfaces.size
    }.sum
  }

  def manhattan(p: (Int, Int, Int), o: (Int, Int, Int)): Int = {
    math.abs(p._1 - o._1) +
      math.abs(p._2 - o._2) +
      math.abs(p._3 - o._3)
  }

  override def part2(strings: Seq[String]): Long = {
    val points = strings.map(_.split(",")).map(l => (l.head.toInt, l.tail.head.toInt, l.last.toInt))
    val directions = Seq(
      (-1, 0, 0),
      (1, 0, 0),
      (0, -1, 0),
      (0, 1, 0),
      (0, 0, -1),
      (0, 0, 1),
    )
    val (xmin, xmax) = (points.map(_._1).min, points.map(_._1).max)
    val (ymin, ymax) = (points.map(_._2).min, points.map(_._2).max)
    val (zmin, zmax) = (points.map(_._3).min, points.map(_._3).max)
    val sides = points.map { case current@(x, y, z) =>
      val others = points.filterNot(_ == current)
      val neighbors = directions
        .map { case (dx, dy, dz) => ((x + dx, y + dy, z + dz)) }
      val freeSurfaces = neighbors.filterNot(others.contains)
      val nonTrapped = freeSurfaces
      nonTrapped
    }

    val flatten = sides.flatten
    val escapesFrom = mutable.HashMap[(Int, Int, Int), Boolean]()
    val sideSet = flatten.distinct
      .filter(p => {
        val frontier = mutable.PriorityQueue[(Int, Int, Int)]()
        frontier.enqueue(p)
        var escaped = false
        val visited = mutable.HashSet[(Int, Int, Int)]()
        while (frontier.nonEmpty) {
          val pop@(x,y,z) = frontier.dequeue()
          if (x < xmin || y < ymin || z < zmin || x>xmax || y > ymax || z > zmax) {
            escaped = true
            frontier.clear()
          } else if(escapesFrom.contains(pop)) {
            escaped = escapesFrom(pop)
            frontier.clear()
          } else if (visited.add(pop)) {
            val moves = directions
              .map { case (dx, dy, dz) => (x + dx, y + dy, z + dz) }
              .filterNot(visited.contains)
              .filterNot(points.contains)
            frontier.addAll(moves)
          }
        }
        visited.foreach(v => escapesFrom.put(v, escaped))
        escaped
      })

    flatten.filter(sideSet.contains).size
    // 4132 is wrong -> six sides
    // 3682 is wrong -> 5 sides (was hoping for a tip)
    // 4143 is too high
    // 4138 is wrong
    // 4108 is wrong
  }
}
