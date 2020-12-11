import java.awt.Point
import java.util.stream.Collectors

import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.Grid
import krabban91.kodvent.kodvent.utilities.logging.Loggable

import scala.jdk.CollectionConverters._

object Day11 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    var grid: Grid[Seat] = null
    var nextState: Grid[Seat] = initialState(strings)
    do {
      grid = nextState
      nextState = grid.map((s, p) => s.nextState1(grid, p))
    } while (!grid.equals(nextState))
    grid.sum(s => if (s.state == '#') 1 else 0)
  }

  override def part2(strings: Seq[String]): Long = {
    var grid: Grid[Seat] = null
    var nextState: Grid[Seat] = initialState(strings)
    do {
      grid = nextState
      nextState = grid.map((s, p) => s.nextState2(grid, p))
    } while (!grid.equals(nextState))
    grid.sum(s => if (s.state == '#') 1 else 0)
  }

  private def initialState(strings: Seq[String]): Grid[Seat] = {
    new Grid[Seat](strings.map(s => s.chars().mapToObj(Seat(_)).collect(Collectors.toList())).asJava)
  }

  case class Seat(state: Int) extends Loggable {

    def isSeat: Boolean = state == '#' || state == 'L'

    def nextState1(grid: Grid[Seat], point: Point): Seat = nextState(grid.getSurroundingTiles(point.y, point.x).asScala.toSeq, 4)

    def nextState2(grid: Grid[Seat], point: Point): Seat = {
      val directions = Seq((0, -1), (-1, -1), (-1, 0), (-1, 1), (0, 1), (1, 1), (1, 0), (1, -1))
      nextState(directions.flatMap(t => seatInDirection(grid, point, t)), 5)
    }

    def nextState(seats: Iterable[Seat], threshold: Int): Seat = if (state == 'L') {
      if (seats.count(s => s.state == '#') == 0) {
        Seat('#')
      } else this
    } else if (state == '#') {
      if (seats.count(s => s.state == '#') >= threshold) {
        Seat('L')
      } else this
    } else this

    def seatInDirection(grid: Grid[Seat], point: Point, delta: (Int, Int)): Option[Seat] = {
      val maxX = grid.getRaw.get(0).size() - 1
      val maxY = grid.getRaw.size() - 1
      var dx = point.x + delta._1
      var dy = point.y + delta._2
      var seat = Seat('.')
      while (dy >= 0 && dx >= 0 && dy <= maxY && dx <= maxX && !seat.isSeat) {
        seat = grid.get(dx, dy)
        dx += delta._1
        dy += delta._2
      }
      if (seat.isSeat) Some(seat) else None
    }

    override def showTile(): String = {
      if (state == '#') "#" else if (state == 'L') "L" else "."
    }
  }

}
