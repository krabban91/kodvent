import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

import scala.collection.mutable

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Instruction(dir: (Long, Long), dist: Long, color: String)

  object Instruction {
    def apply(string: String): Instruction = {
      val s = string.split(" ")
      val dir = s.head match {
        case "L" => WEST
        case "U" => NORTH
        case "R" => EAST
        case "D" => SOUTH
      }
      Instruction(dir, s(1).toLong, s.last.stripSuffix(")").stripPrefix("("))
    }
  }


  override def part1(strings: Seq[String]): Long = {
    val input = strings.map(Instruction(_))
    val boundaries = mutable.HashMap[(Long, Long), String]()
    var start = ZERO2
    input.foreach { case Instruction(dir@(x, y), dist, color) =>
      (0L until dist).foreach { i =>
        boundaries.put(start, color)
        start = start + dir
      }
    }
    val rims = boundaries.toMap
    //find point inside
    LogMap.printMap[String](rims, v => "#")
    val xMin = rims.keySet.map(_._1).min
    val yInside = rims.keySet.map(_._2).min + 2
    val bs = rims.keys.filter(_._2 == yInside).toSeq.sortBy(_._1).sliding(2).foldLeft(Seq[(Long, Long)]()){ case (l , vs) =>
      val (h, t) = (vs.head, vs.last)
      if (t._1 > h._1+1L) {
        l ++ Seq((h + (1L, 0L)))
      } else l
    }


    val filled = Graph.flood[(Long, Long)](Seq(bs.head), p => {
      DIRECTIONS.map(dp =>(dp + p)).filterNot(boundaries.contains).map((_, 1))
    })

    val i = rims.size + filled.size
    i
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
