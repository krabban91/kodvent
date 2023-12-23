import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (map, start, end) = parse(strings)
    val tuple = Graph.longestPath[(Long, Long)](Seq(start), p => p == end, _ manhattan end, p => {
      val neighbors = map(p) match {
        case "." => DIRECTIONS.map(_ + p)
        case ">" => Seq(EAST + p)
        case "<" => Seq(WEST + p)
        case "v" => Seq(SOUTH + p)
        case "^" => Seq(NORTH + p)
        case _ => Seq()
      }
      val value = neighbors.filter(n => map.get(n).exists(s => s != "#"))
      value.map((_, 1L))
    })
    tuple._1

  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  def parse(strings: Seq[String]): (Map[(Long, Long), String], (Long, Long), (Long, Long)) = {
    val map = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), s"$c") } }.toMap
    val start = (strings.head.indexOf(".").toLong, 0L)
    val end = (strings.last.indexOf(".").toLong, strings.size.toLong - 1)

    (map, start, end)
  }
}
