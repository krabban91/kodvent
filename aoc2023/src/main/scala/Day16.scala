import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = followLight(parse(strings)).size

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  private def followLight(m: Map[(Long, Long), Char]) = {
    val energized = mutable.HashMap[(Long, Long), Set[(Long, Long)]]()
    val (n, s, e, w) = ((0L, -1L), (0L, 1L), (1L, 0L), (-1L, 0L))
    val frontier: mutable.PriorityQueue[((Long, Long), (Long, Long))] = mutable.PriorityQueue[((Long, Long), (Long, Long))]()(Ordering.by(_._1))
    val start = ((0L, 0L), e)
    frontier.addOne(start)
    while (frontier.nonEmpty) {
      val pop@(p@(x, y), dir@(dx, dy)) = frontier.dequeue()
      if (!energized.contains(p)) {
        energized.put(p, Set())
      }
      val ls = energized(p)
      if (!ls.contains(dir)) {
        energized.put(p, Set(dir) ++ ls)
        // new state
        val next: Set[((Long, Long), (Long, Long))] = m(p) match {
          case '/' => dir match {
            case (1L, _) => Set(((x, y - 1), n))
            case (-1L, _) => Set(((x, y + 1), s))
            case (_, 1) => Set(((x - 1, y), w))
            case (_, -1) => Set(((x + 1, y), e))
          }
          case '\\' => dir match {
            case (1L, _) => Set(((x, y + 1), s))
            case (-1L, _) => Set(((x, y - 1), n))
            case (_, 1) => Set(((x + 1, y), e))
            case (_, -1) => Set(((x - 1, y), w))
          }
          case '-' => dir match {
            case (_, 1L) => Set(((x - 1, y), w), ((x + 1, y), e))
            case (_, -1L) => Set(((x - 1, y), w), ((x + 1, y), e))
            case _ => Set(((x + dx, y + dy), dir))
          }
          case '|' => dir match {
            case (1L, _) => Set(((x, y - 1), n), ((x, y + 1), s))
            case (-1L, _) => Set(((x, y - 1), n), ((x, y + 1), s))
            case _ => Set(((x + dx, y + dy), dir))
          }
          case '.' => Set(((x + dx, y + dy), dir))
        }
        val filtered = next.filter(t => m.contains(t._1))
        frontier.addAll(filtered)
      }
    }
    energized.toMap
  }


  private def parse(strings: Seq[String]) = {
    strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), c) } }.toMap

  }
}
