import java.awt.Point
import java.util.stream.Collectors

import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.grid.Grid
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

    def nextState1(grid: Grid[Seat], point: Point): Seat = nextState(grid.getSurroundingTiles(point).asScala.toSeq, 4)

    def nextState2(grid: Grid[Seat], point: Point): Seat = nextState(grid.getNearestTilesOfSurroundingDirections(point,Seat.isSeat).asScala, 5)

    def nextState(seats: Iterable[Seat], threshold: Int): Seat = if (state == 'L') {
      if (seats.count(s => s.state == '#') == 0) {
        Seat('#')
      } else this
    } else if (state == '#') {
      if (seats.count(s => s.state == '#') >= threshold) {
        Seat('L')
      } else this
    } else this

    override def showTile(): String = {
      if (state == '#') "#" else if (state == 'L') "L" else "."
    }
  }
  object Seat {
    def isSeat(seat: Seat): Boolean = seat.isSeat
  }

}
