import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.grid.CubeGrid

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val points = strings.map(_.split(",")).map(l => (l.head.toInt, l.tail.head.toInt, l.last.toInt))
    val directions = Seq(
      (-1,  0,  0),
      ( 1,   0,  0),
      ( 0,  -1, 0),
      ( 0,   1,  0),
      ( 0,   0, -1),
      ( 0,   0,  1),

    )
    points.map{ case current@(x, y, z) =>
      val others = points.filterNot(_==current)
      val neighbors = directions
        .map{ case (dx, dy, dz) => (x+dx, y+dy, z+dz)}
      val freeSurfaces = neighbors.filterNot(others.contains)
      freeSurfaces.size
    }.sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
