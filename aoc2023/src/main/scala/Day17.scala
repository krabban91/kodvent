import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

object Day17 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (city, starts, end) = parse(strings)
    val (shortest, path) = Graph.shortestPath[CityLocation](starts, p => p.pos == end, p => p.pos manhattan end, p => p.neighbors(false)(city))
    printPath(city, path)
    shortest
  }


  override def part2(strings: Seq[String]): Long = {
    val (city, starts, end) = parse(strings)
    val (shortest, path) = Graph.shortestPath[CityLocation](starts, p => p.pos == end, p => p.pos manhattan end, p => p.neighbors(true)(city))
    printPath(city, path)
    shortest
  }

  case class CityLocation(pos: (Long, Long), direction: (Long, Long), sameDirection: Long) {

    def neighbors(part2: Boolean): Map[(Long, Long), Long] => Seq[(CityLocation, Long)] = if (part2) neighbors2 else neighbors1

    private def neighbors1(city: Map[(Long, Long), Long]): Seq[(CityLocation, Long)] = {
      val directions = Seq(NORTH, SOUTH, WEST, EAST).filterNot(d => (sameDirection >= 3 && d == direction) || d == direction * (-1L, -1L))
      val next = directions.map(d => (d + pos, d)).filter { case (p, d) => city.contains(p) }
      next.map { case (p, d) => (CityLocation(p, d, if (d == direction) sameDirection + 1 else 1), city(p)) }
    }

    private def neighbors2(city: Map[(Long, Long), Long]): Seq[(CityLocation, Long)] = {
      val directions = Seq(NORTH, SOUTH, WEST, EAST)
        .filterNot(d => (sameDirection >= 10 && d == direction) || d == direction * (-1L, -1L))
        .filter(d => sameDirection >= 4 || d == direction)
      val next = directions.map(d => (d + pos, d)).filter { case (p, d) => city.contains(p) }
      next.map { case (p, d) => (CityLocation(p, d, if (d == direction) sameDirection + 1 else 1), city(p)) }
    }
  }

  private def parse(strings: Seq[String]) = {
    val map = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), s"$c".toLong) } }.toMap
    val starts = Seq(CityLocation((0L, 0L), (0L, 1L), 0L), CityLocation((0L, 0L), (1L, 0L), 0L))
    val end = (strings.head.length.toLong, strings.length.toLong) - (1L, 1L)
    (map, starts, end)
  }

  private def printPath(city: Map[(Long, Long), Long], path: Seq[CityLocation]): Unit = {
    val ds = Map(NORTH -> "^", EAST -> ">", SOUTH -> "v", WEST -> "<")
    LogMap.printMapWithPath[Long, CityLocation](city, path, c => c.toString, v => (v.pos, ds(v.direction)))
  }
}
