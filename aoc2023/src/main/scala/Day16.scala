import implicits.Tuples._
import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = followLight((ZERO2, EAST), parse(strings)).size

  override def part2(strings: Seq[String]): Long = {
    val m = parse(strings)
    startLocations(m).map(followLight(_, m)).map(_.size).max
  }

  private def followLight(start: ((Long, Long), (Long, Long)), m: Map[(Long, Long), Char]) = {
    val energized = mutable.HashMap[(Long, Long), Set[(Long, Long)]]()
    val frontier: mutable.PriorityQueue[((Long, Long), (Long, Long))] = mutable.PriorityQueue[((Long, Long), (Long, Long))]()(Ordering.by(_._1))
    frontier.addOne(start)
    while (frontier.nonEmpty) {
      val (p, dir) = frontier.dequeue()
      if (!energized.contains(p)) {
        energized.put(p, Set())
      }
      val ls = energized(p)
      if (!ls.contains(dir)) {
        energized.put(p, Set(dir) ++ ls)
        // new state
        val nextDir = (m(p), dir) match {
          case ('/', `EAST`) | ('\\', `WEST`) => Set(NORTH)
          case ('/', `WEST`) | ('\\', `EAST`) => Set(SOUTH)
          case ('/', `SOUTH`) | ('\\', `NORTH`) => Set(WEST)
          case ('/', `NORTH`) | ('\\', `SOUTH`) => Set(EAST)
          case ('-', `NORTH` | `SOUTH`) => Set(WEST, EAST)
          case ('|', `WEST` | `EAST`) => Set(NORTH, SOUTH)
          case _ => Set(dir)
        }
        val next = nextDir
          .map(d => (p + d, d))
          .filter(t => m.contains(t._1))
        frontier.addAll(next)
      }
    }
    energized.toMap
  }

  private def startLocations(m: Map[(Long, Long), Char]) = {
    val maxX = m.map(_._1._1).max
    val maxY = m.map(_._1._2).max
    val borders = (ZERO2 to (maxX, maxY))
      .filter { case (x, y) => Seq(0L, maxX).contains(x) || Seq(0L, maxY).contains(y) }
    val starts = borders.flatMap(p => Set((p, NORTH)).filter(_ => p._2 == maxY) ++
      Set((p, SOUTH)).filter(_ => p._2 == 0) ++
      Set((p, EAST)).filter(_ => p._1 == 0) ++
      Set((p, WEST)).filter(_ => p._1 == maxX))
    starts
  }

  private def parse(strings: Seq[String]) = {
    strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), c) } }.toMap
  }
}
