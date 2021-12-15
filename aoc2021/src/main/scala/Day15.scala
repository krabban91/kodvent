import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import java.util.stream.Collectors
import scala.collection.mutable
import scala.jdk.CollectionConverters.CollectionHasAsScala

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val maps: Map[(Int, Int), Long] = extractMap(strings, 1)
    val start = (maps.keys.map(_._1).min, maps.keys.map(_._2).min)
    val end = (maps.keys.map(_._1).max, maps.keys.map(_._2).max)

    val path: (Seq[(Int, Int)], Long) = findPath(start, end, maps)
    path._2
  }

  override def part2(strings: Seq[String]): Long = {
    val maps: Map[(Int, Int), Long] = extractMap(strings, 5)
    val start = (maps.keys.map(_._1).min, maps.keys.map(_._2).min)
    val end = (maps.keys.map(_._1).max, maps.keys.map(_._2).max)

    val path: (Seq[(Int, Int)], Long) = findPath(start, end, maps)
    path._2
  }

  private def extractMap(strings: Seq[String], tiles: Int): Map[(Int, Int), Long] = {
    val vv: Seq[Seq[Long]] = strings
      .map(s => s.chars().mapToObj(_.toChar.asDigit.toLong)
        .collect(Collectors.toList[Long]).asScala.toSeq)
    val tMaps = vv.zipWithIndex
      .flatMap(t0 => t0._1.zipWithIndex.map(t => ((t0._2, t._2), t._1)))
      .toMap
    (0 until tiles)
      .flatMap(x => (0 until tiles)
        .flatMap(y => tMaps
          .map(t => ((t._1._1 + vv.head.size * x, t._1._2 + vv.size * y), (t._2 + x + y) % 10 + (t._2 + x + y) / 10)))).toMap
  }

  def findPath(start: (Int, Int), end: (Int, Int), maps: Map[(Int, Int), Long]): (Seq[(Int, Int)], Long) = {
    val directions = Seq((-1, 0), (0, -1), (1, 0), (0, 1))
    val frontier = mutable.PriorityQueue[(Seq[(Int, Int)], Long, Long)]()(Ordering.Long.reverse.on(_._3))
    val checked = mutable.HashSet[(Int, Int)]()
    var path: (Seq[(Int, Int)], Long) = null
    frontier.addOne((Seq(start), 0, 0 + manhattan(start, end)))
    while (frontier.nonEmpty && path == null) {
      val curr = frontier.dequeue()
      if (curr._1.last == end) {
        path = (curr._1, curr._2)
      }
      if (!checked.contains(curr._1.last)) {
        checked.add(curr._1.last)
        val around = directions
          .map(d => (curr._1.last._1 + d._1, curr._1.last._2 + d._2))
          .filterNot(p => checked.contains(p))
          .map(p => curr._1 ++ Seq(p))
          .flatMap(p => maps.get(p.last).map(_ + curr._2).map(r => (p, r, r + manhattan(p.last, end))))
        frontier.addAll(around)
      }
    }
    path
  }

  private def manhattan(a: (Int, Int), b: (Int, Int)): Long = math.abs(a._1 - b._1) + math.abs(a._2 - b._2)

}
