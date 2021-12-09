import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.Distances
import krabban91.kodvent.kodvent.utilities.grid.Grid

import java.awt.Point
import java.util.stream.Collectors
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val inp = strings.map(s => s.chars().mapToObj(_.toChar.toString.toInt).collect(Collectors.toList[Int]))
    val g = new Grid[Int](inp.toList.asJava)
    g.map((i, p) => {
      val around = g.getAdjacentTiles(p)
      if (around.stream().allMatch(v => v > i)) {
        i + 1
      } else 0
    }).sum(_.toLong)
  }

  override def part2(strings: Seq[String]): Long = {
    val inp = strings.map(s => s.chars().mapToObj(_.toChar.toString.toInt).collect(Collectors.toList[Int]))
    val g = new Grid[Int](inp.toList.asJava)
    val points = ListBuffer[Point]()

    g.forEach((i, p) => {
      val around = g.getAdjacentTiles(p)
      if (around.stream().allMatch(v => v > i)) {
        points.append(p)
      }
    })
    val basins = mutable.HashMap[Point, mutable.HashSet[Point]]()
    points.foreach(p => basins.put(p, mutable.HashSet(p)))
    basins.foreach(t => {
      val (p, l) = t

      val frontier = mutable.PriorityQueue[Point]()(Ordering.Int.reverse.on(v => g.get(v).get()))
      frontier.addAll(adjacents(p, g))
      val checked = mutable.HashSet(p)
      while (frontier.nonEmpty) {
        val v = frontier.dequeue()
        if (g.get(v).get() != 9) {
          l.add(v)
          val as = adjacents(v, g).filter(!checked.contains(_))
          frontier.addAll(as)
        }
        checked.add(v)
      }

    })
    basins.values.map(_.size).toSeq.sortWith((a, b) => a > b).take(3).map(_.toLong).product
  }

  def adjacents(p: Point, g: Grid[Int]): Seq[Point] = {
    g.getSurroundingTilesWithPoints(p, false).asScala.map(_.getKey).filter(o => Distances.manhattan(o, p) == 1).toSeq
  }
}
