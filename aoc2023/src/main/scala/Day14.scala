import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day14 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2


  override def part1(strings: Seq[String]): Long = {
    val walls = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.filter { case (c, _) => c == '#' }.map { case (_, x) => (x.toLong, y.toLong) } }.toSet
    val rocks = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.filter { case (c, _) => c == 'O' }.map { case (_, x) => (x.toLong, y.toLong) } }.toSet
    val rolling = mutable.PriorityQueue[(Long, Long)]()(Ordering.by(-_._2))
    val still = mutable.HashSet[(Long, Long)]()
    rolling.addAll(rocks)
    while(rolling.nonEmpty) {
      val pop@(x,y) = rolling.dequeue()
      val next = (x, y - 1)
      if(y == 0 || walls.contains(next) || still.contains(next)) {
        still.add(pop)
      } else {
        rolling.enqueue(next)
      }
    }
    val maxY = strings.indices.max
    val value = (0 to maxY).map(y => (y, still.count(p => p._2 == y).toLong))
    value.map(t => (maxY - t._1 + 1) * t._2).sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
