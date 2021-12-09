import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.Distances
import krabban91.kodvent.kodvent.utilities.grid.Grid

import java.awt.Point
import java.util.stream.Collectors
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val grid: Grid[Int] = extractMap(strings)
    val points: Set[Point] = getLowPoints(grid)
    points.toSeq
      .map(grid.get(_).get().toLong + 1)
      .sum
  }

  override def part2(strings: Seq[String]): Long = {
    val grid: Grid[Int] = extractMap(strings)
    val points: Set[Point] = getLowPoints(grid)
    val basins: Map[Point, Set[Point]] = exploreBasins(grid, points)
    basins.values.map(_.size.toLong).toSeq
      .sortWith((a, b) => a > b)
      .take(3)
      .product
  }

  private def getLowPoints(grid: Grid[Int]) = {
    val points = ListBuffer[Point]()
    grid.forEach((i, p) => {
      val around = grid.getAdjacentTiles(p)
      if (around.stream().allMatch(v => v > i)) {
        points.append(p)
      }
    })
    points.toSet
  }

  private def extractMap(strings: Seq[String]): Grid[Int] = {
    val input = strings.map(s => s.chars().mapToObj(_.toChar.toString.toInt).collect(Collectors.toList[Int]))
    new Grid[Int](input.toList.asJava)
  }

  private def exploreBasins(g: Grid[Int], points: Set[Point]): Map[Point, Set[Point]] = {
    val basins = mutable.HashMap[Point, mutable.HashSet[Point]]()
    points.foreach(p => basins.put(p, mutable.HashSet(p)))
    basins.foreach(t => {
      val (p, l) = t

      val frontier = mutable.PriorityQueue[Point]()(Ordering.Int.reverse.on(v => g.get(v).get()))
      frontier.addAll(adjacentPoints(p, g))
      val checked = mutable.HashSet(p)
      while (frontier.nonEmpty) {
        val v = frontier.dequeue()
        if (g.get(v).get() != 9) {
          l.add(v)
          val as = adjacentPoints(v, g).filter(!checked.contains(_))
          frontier.addAll(as)
        }
        checked.add(v)
      }

    })
    basins.map(kv => (kv._1, kv._2.toSet)).toMap
  }

  def adjacentPoints(p: Point, g: Grid[Int]): Seq[Point] = {
    g.getSurroundingTilesWithPoints(p, false).asScala.map(_.getKey).filter(o => Distances.manhattan(o, p) == 1).toSeq
  }
}
