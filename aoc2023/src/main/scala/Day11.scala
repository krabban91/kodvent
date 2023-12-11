import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day11 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val parsed = strings.zipWithIndex.flatMap {case (s, y) => s.zipWithIndex.map{case (c, x) => ((x, y), c)}}.filter(_._2 == '#').map(_._1)
    distances(parsed, 2)
  }

  private def distances(parsed: Seq[(Int, Int)], unit: Int) = {
    val galaxies = mutable.HashMap[(Int, Int), (Int, Int)]()
    parsed.foreach(p => galaxies.put(p, (0, 0)))

    val maxY = galaxies.keySet.map(_._2).max
    val maxX = galaxies.keySet.map(_._1).max
    //expand

    (0 to maxY).foreach(y => if (!galaxies.exists(t => t._1._2 == y)) {
      val toUpdate = galaxies.filter(t => t._1._2 > y)
      toUpdate.foreach { case (k, (dx, dy)) => galaxies.put(k, (dx, dy + 1 * unit - 1)) }
    } else {
      ()
    })
    (0 to maxX).foreach(x => if (!galaxies.exists(t => t._1._1 == x)) {
      val toUpdate = galaxies.filter(t => t._1._1 > x)
      toUpdate.foreach { case (k, (dx, dy)) => galaxies.put(k, (dx + unit-1, dy)) }
    } else {
      ()
    })
    val calculated: Set[(Int, Int)] = galaxies.toSeq
      .map { case ((x, y), (dx, dy)) => (x + dx, y + dy) }
      .toSet


    //distances
    val take2 = calculated
      .flatMap(l => calculated.filter(_ != l).map(r => Set(l, r)))
      .map(s => s.toSeq)
      .map(s => (s.head, s.last))
      .toSeq

    //shortest sum
    val value = take2.map { case kv@((lx, ly), (rx, ry)) => (kv, (math.abs(lx - rx) + math.abs(ly - ry)).toLong) }
    value.map(_._2).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val parsed = strings.zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x, y), c) } }.filter(_._2 == '#').map(_._1)
    distances(parsed, 1000000)
  }
}
