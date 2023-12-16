import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples.{RichTuples2Longs, ZERO2}

import scala.collection.mutable

object Day11 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = distances(parse(strings), 2)

  override def part2(strings: Seq[String]): Long = distances(parse(strings), 1000000)

  private def distances(parsed: Seq[(Long, Long)], unit: Int) = {
    val galaxies = mutable.HashMap[(Long, Long), (Long, Long)]()
    parsed.foreach(p => galaxies.put(p, ZERO2))

    val maxY = galaxies.keySet.map(_._2).max
    val maxX = galaxies.keySet.map(_._1).max
    //expand

    val added = 1L * unit - 1
    val rowDelta = (0L, added)
    val columnDelta = (added, 0L)
    (0L to maxY).foreach(y => if (!galaxies.exists(t => t._1._2 == y)) {
      val toUpdate = galaxies.filter(t => t._1._2 > y)
      toUpdate.foreach { case (k, p) => galaxies.put(k, p + rowDelta) }
    } else {
      ()
    })
    (0L to maxX).foreach(x => if (!galaxies.exists(t => t._1._1 == x)) {
      val toUpdate = galaxies.filter(t => t._1._1 > x)
      toUpdate.foreach { case (k, p) => galaxies.put(k, p + columnDelta) }
    } else {
      ()
    })
    val calculated: Set[(Long, Long)] = galaxies.toSeq
      .map { case (p, dir) => p + dir }
      .toSet


    //distances
    val take2 = calculated
      .flatMap(l => calculated.filter(_ != l).map(r => Set(l, r)))
      .map(s => s.toSeq)
      .map(s => (s.head, s.last))
      .toSeq

    //shortest sum
    val value = take2.map { case kv@(l, r) => (kv, l manhattan r) }
    value.map(_._2).sum
  }


  private def parse(strings: Seq[String]) = {
    strings.zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), c) } }.filter(_._2 == '#').map(_._1)
  }
}
