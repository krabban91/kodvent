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

    val path: Seq[((Int, Int), Long)] = findPath(start, end, maps)
    path.map(_._2).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val maps: Map[(Int, Int), Long] = extractMap(strings, 5)
    val start = (maps.keys.map(_._1).min, maps.keys.map(_._2).min)
    val end = (maps.keys.map(_._1).max, maps.keys.map(_._2).max)

    val path: Seq[((Int, Int), Long)] = findPath(start, end, maps)
    path.map(_._2).sum
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

  def findPath(start: (Int, Int), end: (Int, Int), maps: Map[(Int, Int), Long]): Seq[((Int, Int), Long)] = {
    val directions = Seq((-1, 0), (0, -1), (1, 0), (0, 1))
    val frontier = mutable.PriorityQueue[Seq[((Int, Int), Long)]]()(Ordering.Long.reverse.on(_.map(_._2).sum))
    val checked = mutable.HashSet[((Int, Int), Long)]()
    var path: Seq[((Int, Int), Long)] = null
    frontier.addOne(Seq((start, 0)))
    while (frontier.nonEmpty && path == null) {
      val curr = frontier.dequeue()
      if (curr.last._1 == end) {
        path = curr
      }
      if (!checked.contains(curr.last)) {
        checked.add(curr.last)
        val around = directions
          .map(d => (curr.last._1._1 + d._1, curr.last._1._2 + d._2))
          .flatMap(p => maps.get(p).map(r => (p, r)))
          .map(n => curr ++ Seq(n))
          .filterNot(p => checked.contains(p.last))
        frontier.addAll(around)
      }
    }
    path
  }

}
