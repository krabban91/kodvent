import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day04 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (wall, width, height) = parseInput(strings)
    getAccessible(wall, width, height).size.toLong
  }

  override def part2(strings: Seq[String]): Long = {
    val (wall, width, height) = parseInput(strings)
    val start = wall.count(_._2).toLong
    var accessible: Seq[(Int, Int)] = getAccessible(wall, width, height)
    while (accessible.nonEmpty) {
      accessible.foreach(p => wall(p) = false)
      accessible = getAccessible(wall, width, height)
    }
    start - wall.count(_._2).toLong
  }

  private def parseInput(strings: Seq[String]): (mutable.HashMap[(Int, Int), Boolean], Int, Int) = {
    val wall = mutable.HashMap[(Int, Int), Boolean]()
    for {
      (line, y) <- strings.zipWithIndex
      (char, x) <- line.zipWithIndex
    } {
      wall.update((x, y), char == '@')
    }
    (wall, strings.head.length, strings.length)
  }

  private def getAccessible(wall: mutable.Map[(Int, Int), Boolean], width: Int, height: Int): Seq[(Int, Int)] = {
    (0 until width).flatMap(x => (0 until height).map(y => (x, y)))
      .filter { case (x, y) => canAccess(wall, (x, y), width, height) }.toSeq
  }

  private def canAccess(wall: mutable.Map[(Int, Int), Boolean], loc: (Int, Int), width: Int, height: Int): Boolean = {
    val (x, y) = loc
    if (wall((x, y))) {
      adjacent(loc, width, height).count(wall) < 4
    } else false
  }

  private def adjacent(loc: (Int, Int), width: Int, height: Int): Seq[(Int, Int)] = {
    val (x, y) = loc
    borders(x, y).filter { case (xi, yi) => xi < width && xi >= 0 && yi < height && yi >= 0 }
  }

  private def borders(x: Int, y: Int) = Seq(
    (x - 1, y),
    (x - 1, y - 1),
    (x - 1, y + 1),
    (x + 1, y),

    (x + 1, y - 1),
    (x + 1, y + 1),
    (x, y - 1),
    (x, y + 1)
  )
}
