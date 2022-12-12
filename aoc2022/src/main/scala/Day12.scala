import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val mountain = parseMap(strings)
    val start = mountain.find(t => t._2 == 'S').get._1
    val end = mountain.find(t => t._2 == 'E').get._1
    shortestPath(mountain, start, end)
  }

  override def part2(strings: Seq[String]): Long = {
    val mountain = parseMap(strings)
    val edges = mountain.toSeq
      .filter(t => t._2 == 'a' || t._2 == 'S')
      .map(_._1)
      .filter(t => t._1 == 0 || t._1 == strings.size - 1 || t._2 == 0 || t._2 == strings.head.length - 1)
    val end = mountain.find(t => t._2 == 'E').get._1
    edges.map(shortestPath(mountain, _, end)).min
  }

  private def parseMap(strings: Seq[String]): Map[(Int, Int), Char] = strings
    .zipWithIndex.flatMap { case (l, y) => l.zipWithIndex.map { case (height, x) => ((x, y), height) } }
    .toMap

  private def shortestPath(v: Map[(Int, Int), Char], start: (Int, Int), end: (Int, Int)): Long = {
    val queue = mutable.PriorityQueue[((Int, Int), Long, Long)]((start, 0L, heuristic(start, end)))(Ordering.by(v => -(v._2 + v._3)))
    val visited = mutable.HashSet[(Int, Int)]()
    while (queue.nonEmpty) {
      val pop = queue.dequeue()
      if (pop._1 == end) {
        return pop._2
      }
      if (visited.add(pop._1)) {
        moves(v, pop._1)
          .filterNot(visited.contains)
          .foreach(p => queue.enqueue((p, pop._2 + 1L, heuristic(end, p))))
      }
    }
    Long.MaxValue
  }

  private def moves(v: Map[(Int, Int), Char], pop: (Int, Int)): Seq[(Int, Int)] = Seq((-1, 0), (1, 0), (0, -1), (0, 1))
    .map(t => (pop._1 + t._1, pop._2 + t._2))
    .filter(v.contains)
    .filter(height(v, pop) + 1 >= height(v, _))

  private def height(v: Map[(Int, Int), Char], point: (Int, Int)): Int = {
    val mapping = Map[Char, Char]('S' -> 'a', 'E' -> 'z')
    val c = v(point)
    mapping.getOrElse(c, c).asInstanceOf[Int]
  }

  private def heuristic(end: (Int, Int), pop: (Int, Int)): Long = math.abs(pop._1 - end._1) + math.abs(pop._2 - end._2)

}
