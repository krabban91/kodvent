import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._
import scala.collection.mutable

object Day17 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class CityLocation(pos: (Long, Long), direction: (Long, Long), sameDirection: Long) {
    def neighbors(city: Map[(Long, Long), Long]): Seq[(CityLocation, Long)] = {
      val directions = Seq(NORTH, SOUTH, WEST, EAST).filterNot(d => (sameDirection >= 3 && d == direction) || d == direction * (-1L, -1L))
      val next = directions.map(d => (d + pos, d)).filter{ case (p, d) =>city.contains(p)}

      next.map{ case (p, d) => (CityLocation(p, d, if (d == direction) sameDirection + 1 else 1), city(p))}

    }

    def neighbors2(city: Map[(Long, Long), Long]): Seq[(CityLocation, Long)] = {
      val directions = Seq(NORTH, SOUTH, WEST, EAST)
        .filterNot(d => (sameDirection >= 10 && d == direction) || d == direction * (-1L, -1L))
        .filter(d => sameDirection >= 4 || d == direction)
      val next = directions.map(d => (d + pos, d)).filter { case (p, d) => city.contains(p) }
      next.map { case (p, d) => (CityLocation(p, d, if (d == direction) sameDirection + 1 else 1), city(p)) }
    }

  }

  override def part1(strings: Seq[String]): Long = {
    val city = parse(strings)
    val starts = Seq(CityLocation((0L, 0L), (0L, 1L), 0L)
      ,CityLocation((0L, 0L), (1L, 0L), 0L))
    val end = (city.keySet.map(_._1).max, city.keySet.map(_._2).max)
    Graph.shortestPath[CityLocation](starts, p => p.pos == end, p => p.pos manhattan end, p => p.neighbors(city))
  }

  override def part2(strings: Seq[String]): Long = {
    val city = parse(strings)
    val starts = Seq(CityLocation((0L, 0L), (0L, 1L), 0L)
      , CityLocation((0L, 0L), (1L, 0L), 0L))
    val end = (city.keySet.map(_._1).max, city.keySet.map(_._2).max)
    Graph.shortestPath[CityLocation](starts, p => p.pos == end, p => p.pos manhattan end, p => p.neighbors2(city))
  }

  private def parse(strings: Seq[String]) = {
    strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), s"$c".toLong) } }.toMap
  }
}
