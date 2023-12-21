import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

object Day21 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (map, start) = parse(strings)



    val value = Graph.stepAround[(Long, Long)](Seq(start), p => neighbors (p, map), 64)
      value.size

  }

  def neighbors(p: (Long, Long), map: Map[(Long, Long), String]): Seq[((Long, Long), Long)] = {
    DIRECTIONS.map(_ + p).filter(map.get(_).exists(v => v == "." || v == "S")).map((_, 1))
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  private def parse(strings: Seq[String]) = {
    val map = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), s"$c") } }.toMap
    val start = map.find(_._2 == "S").map(_._1).get

    (map, start)
  }
}
