import java.awt.Point

import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.Distances

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val start = new Point(0,0)
    var current = start
    var heading = 1 // 0 == N, 1 == E ..

    strings.map(Movement(_)).foreach(m =>{
      if (m.action == "N"){
        current = new Point(current.x, current.y - m.size)
      } else if (m.action == "E"){
        current = new Point(current.x+ m.size, current.y)

      } else if (m.action == "S"){
        current = new Point(current.x, current.y+ m.size)

      } else if (m.action == "W"){
        current = new Point(current.x- m.size, current.y)

      } else if (m.action == "L"){
        heading = (heading - (m.size/90) + 4) % 4
      } else if (m.action == "R"){
        heading = (heading + (m.size/90)+ 4) % 4
      } else if (m.action == "F"){
        if (heading == 0){
          current = new Point(current.x, current.y - m.size)
        } else if (heading == 1){
          current = new Point(current.x+ m.size, current.y)

        } else if (heading == 2){
          current = new Point(current.x, current.y+ m.size)

        } else if (heading == 3 ){
          current = new Point(current.x- m.size, current.y)
        }
        else {
          //nop
          current
        }

      } else {
        //nop
      }
    })

    Distances.manhattan(start,current)
  }

  override def part2(strings: Seq[String]): Long = {
    val start = new Point(0,0)
    var current = start
    // N is flipped in this one
    var waypoint = new Point(10,1)
    strings.map(Movement(_)).foreach(m =>{
      if (m.action == "N"){
        waypoint = new Point(waypoint.x, waypoint.y + m.size)
      } else if (m.action == "E"){
        waypoint = new Point(waypoint.x+ m.size, waypoint.y)

      } else if (m.action == "S"){
        waypoint = new Point(waypoint.x, waypoint.y- m.size)

      } else if (m.action == "W"){
        waypoint = new Point(waypoint.x- m.size, waypoint.y)

      } else if (m.action == "L"){
        val angle = math.toRadians(m.size)
        waypoint = new Point((waypoint.x*Math.cos(angle) - waypoint.y*Math.sin(angle)).round.toInt,
          (waypoint.x*Math.sin(angle) + waypoint.y*Math.cos(angle)).round.toInt)
      } else if (m.action == "R"){
        val angle = math.toRadians(-m.size)
        waypoint = new Point((waypoint.x*Math.cos(angle) - waypoint.y*Math.sin(angle)).round.toInt,
          (waypoint.x*Math.sin(angle) + waypoint.y*Math.cos(angle)).round.toInt)
      } else if (m.action == "F"){
        current = new Point(current.x + ( waypoint.x*m.size), current.y + (waypoint.y*m.size))
      } else {
        //nop
      }
    })

    Distances.manhattan(start,current)
  }

  case class Movement(action: String, size: Int){

  }
  object Movement {
    def apply(string: String): Movement = {
      val i = string.substring(1)
      var s = string.substring(0,1)
      Movement(s,i.toInt)
    }
  }
}
