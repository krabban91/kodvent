import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.grid.Grid

import java.util.stream.Collectors
import scala.jdk.CollectionConverters._

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val inp = strings.map(s => s.chars().mapToObj(_.toChar.toString.toInt).collect(Collectors.toList[Int]))
    val g = new Grid[Int](inp.toList.asJava)
    g.map((i, p) => {
      val around = g.getAdjacentTiles(p)
      if (around.stream().allMatch(v => v > i)) {
        i + 1
      } else 0
    }).sum(_.toLong)
  }

  override def part2(strings: Seq[String]): Long = -1
}
