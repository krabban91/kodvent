import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = followLight(((0,0), (1, 0)), parse(strings)).size

  override def part2(strings: Seq[String]): Long = {
    val m = parse(strings)
    val minX = m.map(_._1._1).min
    val maxX = m.map(_._1._1).max
    val maxY = m.map(_._1._2).max
    val minY = m.map(_._1._2).min
    val borders = (minX to maxX)
      .flatMap(x => (minY to maxY).map(y => (x, y)))
      .filter{ case (x, y) => x == minX || x == maxX || y == minY || y == maxY }
    val (n, s, e, w) = ((0L, -1L), (0L, 1L), (1L, 0L), (-1L, 0L))
    val starts = borders.flatMap(p => Set((p, n)).filter(_ => p._2 == maxY) ++
      Set((p, s)).filter(_ => p._2 == minY) ++
      Set((p, e)).filter(_ => p._1 == minX) ++
      Set((p, w)).filter(_ => p._1 == maxX))
    starts.map(followLight(_, m)).map(_.size).max
  }

  private def followLight(start: ((Long, Long), (Long, Long)), m: Map[(Long, Long), Char]) = {
    val energized = mutable.HashMap[(Long, Long), Set[(Long, Long)]]()
    val (n, s, e, w) = ((0L, -1L), (0L, 1L), (1L, 0L), (-1L, 0L))
    val frontier: mutable.PriorityQueue[((Long, Long), (Long, Long))] = mutable.PriorityQueue[((Long, Long), (Long, Long))]()(Ordering.by(_._1))
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
