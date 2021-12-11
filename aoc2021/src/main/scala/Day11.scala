import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.grid.Grid

import java.awt.Point
import java.util
import java.util.stream.Collectors
import scala.collection.mutable
import scala.jdk.CollectionConverters.{CollectionHasAsScala, IterableHasAsJava}

object Day11 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    var curr: Grid[Int] = parseDay(strings)
    var flashes = 0L
    for (step <- 1 to 100) {
      val (next, newFlashes) = stepDay(curr)
      flashes += newFlashes
      curr = next
    }
    flashes
  }

  def parseDay(strings: Seq[String]): Grid[Int] = {
    val input = strings.map(_.chars().mapToObj(_.toChar.toString.toInt).collect(Collectors.toList[Int]))
    new Grid[Int](new util.ArrayList(input.asJavaCollection))
  }

  override def part2(strings: Seq[String]): Long = -1

  def stepDay(curr: Grid[Int]): (Grid[Int], Long) = {
    var next = curr.map((v, p) => v + 1)
    val toFlash = mutable.Stack[Point]()
    next.forEach((v, p) => if (v > 9) toFlash.push(p))
    val hasFlashed = mutable.HashSet[Point]()

    while (toFlash.nonEmpty) {
      val c = toFlash.pop()
      if (!hasFlashed.contains(c)) {
        hasFlashed.add(c)
        next = next.map((v, p) => {
          if (next.getSurroundingTilesWithPoints(p, false).asScala.exists(kv => kv.getKey.equals(c))) {
            if (v == 9) {
              toFlash.push(p)
            }
            v + 1
          } else v
        })
      }
    }
    if (hasFlashed.nonEmpty) {
      next = next.map((v, p) => if (v > 9) 0 else v)
    }
    (next, hasFlashed.size.toLong)
  }
}
