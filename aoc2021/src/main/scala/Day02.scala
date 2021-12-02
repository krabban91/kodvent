import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import java.awt.Point

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val res: Point = strings.map(s => s.split(" "))
      .map(a => {
        val in = a.last.toInt
        a.head match {
          case "forward" => new Point(in, 0)
          case "down" => new Point(0, in)
          case "backward" => new Point(-in, 0)
          case _ => new Point(0, -in)
        }
      })
      .reduce((a, b) => new Point(a.x + b.x, a.y + b.y))
    res.x * res.y
  }

  override def part2(strings: Seq[String]): Long = {
    var aim = 0
    var loc = new Point(0,0)
    strings.map(s => s.split(" ")).foreach(a => {
      a.head match {
        case "forward" => loc = new Point(loc.x + (a.last.toInt), loc.y + (a.last.toInt * aim))
        case "down" => aim +=(a.last.toInt)
        case "backward" => loc = new Point(loc.x - (a.last.toInt), loc.y - (a.last.toInt * aim))
        case _ => aim -=(a.last.toInt)
      }
    })
    loc.x * loc.y
  }
}
