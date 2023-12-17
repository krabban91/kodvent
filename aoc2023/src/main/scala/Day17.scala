import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

object Day17 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = solve(strings, 0, 3)

  override def part2(strings: Seq[String]): Long = solve(strings, 4, 10)

  private def solve(strings: Seq[String], min: Long, max: Long): Long = {
    val (city, starts, end) = parse(strings)
    val (shortest, path) = Graph.shortestPath[CityLocation](starts, p => p.pos == end, p => p.pos manhattan end, p => p.neighbors(city, min, max))
    //printPath(city, path)
    shortest
  }

  case class CityLocation(pos: (Long, Long), direction: (Long, Long), sameDirection: Long) {

    def neighbors(city: Map[(Long, Long), Long], min: Long, max: Long): Seq[(CityLocation, Long)] = {
      val directions = DIRECTIONS
        .filterNot(sameDirection >= max && _ == direction)
        .filterNot(_ == direction * (-1L, -1L))
        .filter(sameDirection >= min || _ == direction)
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
