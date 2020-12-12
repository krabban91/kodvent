import java.awt.Point

import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.Distances

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val start = new Point(0, 0)
    var (current, waypoint) = (start, new Point(1, 0))
    strings.map(Movement(_)).foreach(m => {
      val out = m.execute(current, waypoint, moveWaypoint = false)
      current = out._1
      waypoint = out._2
    })
    Distances.manhattan(start, current)
  }

  override def part2(strings: Seq[String]): Long = {
    val start = new Point(0, 0)
    var (current, waypoint) = (start, new Point(10, -1))
    strings.map(Movement(_)).foreach(m => {
      val out = m.execute(current, waypoint, moveWaypoint = true)
      current = out._1
      waypoint = out._2
    })
    Distances.manhattan(start, current)
  }

  case class Movement(action: String, size: Int) {
    private val directions = Map[String, Point](
      "N" -> new Point(0, -1),
      "E" -> new Point(1, 0),
      "S" -> new Point(0, 1),
      "W" -> new Point(-1, 0)
    )

    def rotate(waypoint: Point): Point = {
      // N negative -> positive rotate is clockwise
      val angle = math.toRadians(this.size) * (if (action == "L") -1 else 1)
      new Point((waypoint.x * Math.cos(angle) - waypoint.y * Math.sin(angle)).round.toInt,
        (waypoint.x * Math.sin(angle) + waypoint.y * Math.cos(angle)).round.toInt)
    }

    def move(waypoint: Point): Point = directions.get(this.action)
      .map(d => new Point(waypoint.x + d.x * size, waypoint.y + d.y * size))
      .getOrElse(waypoint)

    def forward(current: Point, waypoint: Point): Point = new Point(current.x + (waypoint.x * size), current.y + (waypoint.y * size))

    def execute(current: Point, heading: Point, moveWaypoint: Boolean): (Point, Point) = {
      if (directions.contains(this.action)) {
        if (moveWaypoint) (current, move(heading)) else (move(current), heading)
      } else if (Seq("L", "R").contains(this.action)) {
        (current, rotate(heading))
      } else if (this.action == "F") {
        (forward(current, heading), heading)
      }
      else (current, heading)
    }
  }

  object Movement {
    def apply(string: String): Movement = Movement(string.substring(0, 1), string.substring(1).toInt)
  }

}
