import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = followLight(((0, 0), (1, 0)), parse(strings)).size

  override def part2(strings: Seq[String]): Long = {
    val m = parse(strings)
    startLocations(m).map(followLight(_, m)).map(_.size).max
  }

  private def directions = ((0L, -1L), (0L, 1L), (1L, 0L), (-1L, 0L))

  private def followLight(start: ((Long, Long), (Long, Long)), m: Map[(Long, Long), Char]) = {
    val energized = mutable.HashMap[(Long, Long), Set[(Long, Long)]]()
    val (n, s, e, w) = directions
    val frontier: mutable.PriorityQueue[((Long, Long), (Long, Long))] = mutable.PriorityQueue[((Long, Long), (Long, Long))]()(Ordering.by(_._1))
    frontier.addOne(start)
    while (frontier.nonEmpty) {
      val (p@(x, y), dir@(dx, dy)) = frontier.dequeue()
      if (!energized.contains(p)) {
        energized.put(p, Set())
      }
      val ls = energized(p)
      if (!ls.contains(dir)) {
        energized.put(p, Set(dir) ++ ls)
        // new state
        val next: Set[((Long, Long), (Long, Long))] = m(p) match {
          case '/' => dir match {
            case `e` => Set(((x, y - 1), n))
            case `w` => Set(((x, y + 1), s))
            case `s` => Set(((x - 1, y), w))
            case `n` => Set(((x + 1, y), e))
          }
          case '\\' => dir match {
            case `e` => Set(((x, y + 1), s))
            case `w` => Set(((x, y - 1), n))
            case `s` => Set(((x + 1, y), e))
            case `n` => Set(((x - 1, y), w))
          }
          case '-' => dir match {
            case `s` | `n` => Set(((x - 1, y), w), ((x + 1, y), e))
            case _ => Set(((x + dx, y + dy), dir))
          }
          case '|' => dir match {
            case `w` | `e` => Set(((x, y - 1), n), ((x, y + 1), s))
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

  private def startLocations(m: Map[(Long, Long), Char]) = {
    val maxX = m.map(_._1._1).max
    val maxY = m.map(_._1._2).max
    val borders = (0L to maxX)
      .flatMap(x => (0L to maxY).map(y => (x, y)))
      .filter { case (x, y) => x == 0L || x == maxX || y == 0L || y == maxY }
    val (n, s, e, w) = directions
    val starts = borders.flatMap(p => Set((p, n)).filter(_ => p._2 == maxY) ++
      Set((p, s)).filter(_ => p._2 == 0) ++
      Set((p, e)).filter(_ => p._1 == 0) ++
      Set((p, w)).filter(_ => p._1 == maxX))
    starts
  }

  private def parse(strings: Seq[String]) = {
    strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), c) } }.toMap

  }
}
