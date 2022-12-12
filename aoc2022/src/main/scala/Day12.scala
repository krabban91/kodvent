import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings //.map(_.map(_.toInt).map(_ - 48))
      .zipWithIndex.flatMap { case (l, y) => l.zipWithIndex.map { case (height, x) => ((x, y), height) } }
      .toMap
    val start = v.find(t => t._2 == 'S').get
    val end = v.find(t => t._2 == 'E').get

    shortestPath(v, start, end)
  }

  private def shortestPath(v: Map[(Int, Int), Char], start: ((Int, Int), Char), end: ((Int, Int), Char)):Long = {
    val directions = Seq((-1, 0), (1, 0), (0, -1), (0, 1))

    val queue = mutable.PriorityQueue[((Int, Int), Long, Long)]((start._1, 0L, manhattan(start._1, end._1)))(Ordering.by(v => -(v._2 + v._3)))
    val visited = mutable.HashSet[(Int, Int)]()
    while (queue.nonEmpty) {
      val pop = queue.dequeue()
      if (pop._1 == end._1) {
        return pop._2
      }
      if (visited.add(pop._1)) {
        val value = directions.map(t => (pop._1._1 + t._1, pop._1._2 + t._2))
          .filterNot(visited.contains)
          .filter(v.contains)
        val value1 = value
          .filter(p => {
            val i = heightDiff(v, pop._1, p)
            i <= 1
          })
        val heuristic = 0 //manhattan(end._1, pop._1)
        value1
          .foreach(p => queue.enqueue((p, pop._2 + 1L, heuristic)))
      }
    }
    -1L
  }

  private def manhattan(end: (Int, Int), pop: (Int, Int)): Long = {
    math.abs(pop._1 - end._1) + math.abs(pop._2 - end._2)
  }

  private def heightDiff(v: Map[(Int, Int), Char], pop: (Int, Int), p: (Int, Int)) = {
    var to = v(p)
    var from = v(pop)
    if (from == 'S') {
      from = 'a'
    }
    if (from == 'E') {
      from = 'z'
    }
    if (to == 'S') {
      to = 'a'
    }
    if (to == 'E') {
      to = 'z'
    }
    val i = to - from
    i
  }

  override def part2(strings: Seq[String]): Long = {
     -1
  }
}
