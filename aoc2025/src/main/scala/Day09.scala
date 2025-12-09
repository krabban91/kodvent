import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  def parseInput(strings: Seq[String]): Set[(Long, Long)] = {
    strings.map{s => val t = s.split(","); (t.head.toLong, t.last.toLong)}.toSet
  }

  def areaOfPair(p: Set[(Long, Long)]): Long = {
    val t = p.toSeq
    val (a, b) = (t.head , t.last)
    (Math.abs(a._1 - b._1) + 1) * (Math.abs(a._2 - b._2) + 1)
  }

  override def part1(strings: Seq[String]): Long = {
    val points: Set[(Long, Long)] = parseInput(strings)
    val allPairs = points.subsets(2).toSeq
    val areas = allPairs.map(p => (p, areaOfPair(p)))
    val best = areas.maxBy(_._2)
    best._2
  }

  override def part2(strings: Seq[String]): Long = {
    val points: Set[(Long, Long)] = parseInput(strings)
    val allPairs = points.subsets(2).toSeq
    val areas = allPairs.map(p => (p, areaOfPair(p)))
    val filtered = areas
    val best = filtered.maxBy(_._2)
    best._2

  }
}
