import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val mountain = parseMap(strings)
    val start = mountain.find { case (_, v) => v == 'S' }.get._1
    val end = mountain.find { case (_, v) => v == 'E' }.get._1
    Graph.shortestPath[(Int, Int)](start,
      { case p@(_, _) => p == end },
      { case p@(_, _) => manhattan(p, end) },
      { case p@(_, _) => moves1(mountain, p) })
  }

  override def part2(strings: Seq[String]): Long = {
    val mountain = parseMap(strings)
    val xMax = mountain.keys.map(_._1).max
    val yMax = mountain.keys.map(_._2).max
    val end = mountain.find { case (_, v) => v == 'E' }.get._1
    Graph.shortestPath[(Int, Int)](end,
      { case (x, y) => (x == 0 || y == 0 || x == xMax || y == yMax) && Seq('S', 'a').contains(mountain(x, y)) },
      { case (x, y) => math.min(math.min(x, xMax - x), math.min(y, yMax - y)) },
      { case p@(_, _) => moves2(mountain, p) })

  }

  private def parseMap(strings: Seq[String]): Map[(Int, Int), Char] = strings
    .zipWithIndex.flatMap { case (l, y) => l.zipWithIndex.map { case (height, x) => ((x, y), height) } }
    .toMap

  private def moves1(v: Map[(Int, Int), Char], pop: (Int, Int)): Seq[((Int, Int), Long)] = Seq((-1, 0), (1, 0), (0, -1), (0, 1))
    .map(t => (pop._1 + t._1, pop._2 + t._2))
    .filter(v.contains)
    .filter(to => height(v, pop) + 1 >= height(v, to))
    .map((_, 1L))

  private def moves2(v: Map[(Int, Int), Char], pop: (Int, Int)): Seq[((Int, Int), Long)] = Seq((-1, 0), (1, 0), (0, -1), (0, 1))
    .map(t => (pop._1 + t._1, pop._2 + t._2))
    .filter(v.contains)
    .filter(to => height(v, to) + 1 >= height(v, pop))
    .map((_, 1L))

  private def height(v: Map[(Int, Int), Char], point: (Int, Int)): Int = {
    val mapping = Map[Char, Char]('S' -> 'a', 'E' -> 'z')
    val c = v(point)
    mapping.getOrElse(c, c).asInstanceOf[Int]
  }

  private def manhattan(end: (Int, Int), pop: (Int, Int)): Long = math.abs(pop._1 - end._1) + math.abs(pop._2 - end._2)

}
