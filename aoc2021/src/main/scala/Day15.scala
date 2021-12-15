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
    val vv: Seq[Seq[Long]] = strings.map(s => s.chars().mapToObj(_.toChar.asDigit.toLong).collect(Collectors.toList[Long]).asScala.toSeq)
    val maps = vv.zipWithIndex.flatMap(t0 => t0._1.zipWithIndex.map(t => ((t0._2, t._2), t._1))).toMap
    val frontier = mutable.PriorityQueue[Seq[((Int, Int), Long)]]()(Ordering.Long.reverse.on(_.map(_._2).sum))
    val checked = mutable.HashSet[((Int, Int), Long)]()
    val start = (maps.keys.map(_._1).min, maps.keys.map(_._2).min)
    val end = (maps.keys.map(_._1).max, maps.keys.map(_._2).max)
    var path: Seq[((Int, Int), Long)] = null
    frontier.addOne(Seq(((0, 0), 0)))
    while (frontier.nonEmpty && path == null) {
      val curr = frontier.dequeue()
      if (curr.last._1 == end) {
        path = curr
      }
      if (!checked.contains(curr.last)) {
        checked.add(curr.last)
        val around = Seq((-1, 0), (0, -1), (1, 0), (0, 1))
          .map(d => (curr.last._1._1 + d._1, curr.last._1._2 + d._2))
          .flatMap(p => maps.get(p).map(r => (p, r)))
          .map(n => curr ++ Seq(n))
          .filterNot(p => checked.contains(p.last))
        frontier.addAll(around)


      }

    }
    path.map(_._2).sum
  }

  override def part2(strings: Seq[String]): Long = -1
}
