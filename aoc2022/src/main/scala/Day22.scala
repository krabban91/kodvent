import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day22 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val startDir = (1, 0)
    val grouped = groupsSeparatedByTwoNewlines(strings)

    val map = grouped.head.split("\n")
      .filter(_.nonEmpty).zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x, y), c) } }.toMap

    var loc = map.filter(kv => kv._1._2 == 0 && kv._2 == '.').minBy(_._1._1)._1
    var dir = startDir
    val directions = Seq((1, 0), (0, 1), (-1, 0), (0, -1))
    val left = Map(
      directions.head -> directions(3),
      directions(1) -> directions.head,
      directions(2) -> directions(1),
      directions(3) -> directions(2),
    )
    val right = Map(
      directions.head -> directions(1),
      directions(1) -> directions(2),
      directions(2) -> directions(3),
      directions(3) -> directions.head,
    )


    val ops = (grouped.last.trim + "S").foldLeft((Seq[(Int, String)](), "")) { case ((l, s), c) =>
      if (c.isLetter) {
        (l ++ Seq((s.toInt, s"$c")), "")
      } else {

        (l, s + c)
      }
    }
    ops._1.foreach { case op@(steps, turn) =>
      (1 to steps).foreach(i => {
        val nextPos = (loc._1 + dir._1, loc._2 + dir._2)
        val nextTile = map.getOrElse(nextPos, " ")
        if (nextTile == '.') {
          //move
          loc = nextPos
        } else if (nextTile == '#') {
          //halt
        } else {
          //wrap around
          val wrapped = dir match {
            case (1, 0) => map.filter(kv => kv._1._2 == nextPos._2 && kv._2 != ' ')
              .minBy(_._1._1)._1
            case (0, 1) => map.filter(kv => kv._1._1 == nextPos._1 && kv._2 != ' ')
              .minBy(_._1._2)._1
            case (-1, 0) => map.filter(kv => kv._1._2 == nextPos._2 && kv._2 != ' ')
              .maxBy(_._1._1)._1
            case (0, -1) => map.filter(kv => kv._1._1 == nextPos._1 && kv._2 != ' ')
              .maxBy(_._1._2)._1
          }
          //println(s"dir=$dir, loc=$loc, wrapped=$wrapped")
          val wrappedTile = map(wrapped)
          if (wrappedTile == '#') {
            //println(s"Halted wrapped=$wrapped")
            //halt
          } else {
            if (wrappedTile == ' ') {
              println(s"problem! loc=$loc, wrapped=$wrapped")
            }
            loc = wrapped
          }
        }
      })
      turn match {
        case "L" =>
          // println(s"L $dir becomes ${left(dir)}. loc=$loc")
          dir = left(dir)
        case "R" =>
          // println(s"R $dir becomes ${right(dir)}. loc=$loc")
          dir = right(dir)
        case "S" =>
          //last op
      }
    }
    val dirMap = directions.zipWithIndex.toMap
    val password = 1000 * (loc._2 + 1) + 4 * (loc._1 + 1) + dirMap(dir)
    // 30398 is wrong
    // 30358 is wrong
    password
  }

  override def part2(strings: Seq[String]): Long = {
    //part 2, the map is a cube
    val east =(1,0)
    val south=(0,1)
    val west =(-1,0)
    val north=(0,-1)
    val directions = Seq(east, south, west, north)
    val left = Map(
      directions.head -> directions(3),
      directions(1) -> directions.head,
      directions(2) -> directions(1),
      directions(3) -> directions(2),
    )
    val right = Map(
      directions.head -> directions(1),
      directions(1) -> directions(2),
      directions(2) -> directions(3),
      directions(3) -> directions.head,
    )
    val arrows = Map(
      directions.head -> '>',
      directions(1) -> 'v',
      directions(2) -> '<',
      directions(3) -> '^',
    )
    val startDir = (1, 0)
    val grouped = groupsSeparatedByTwoNewlines(strings)
    val mod = if (strings.head.length < 50) 4 else 50
    val map = grouped.head.split("\n")
      .filter(_.nonEmpty).zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x, y), c) } }.toMap

    val walked = mutable.HashMap[(Int, Int), Char]()

    val startLoc = map.filter(kv => kv._1._2 == 0 && kv._2 == '.').minBy(_._1._1)._1
    var loc = startLoc
    var dir = startDir

    val ops = (grouped.last.trim + "S").foldLeft((Seq[(Int, String)](), "")) { case ((l, s), c) =>
      if (c.isLetter) {
        (l ++ Seq((s.toInt, s"$c")), "")
      } else {

        (l, s + c)
      }
    }
    walked.addAll(map)
    walked.put(loc, arrows(dir))

    ops._1.foreach { case op@(steps, turn) =>
      (1 to steps).foreach(i => {
        val nextPos = (loc._1 + dir._1, loc._2 + dir._2)
        val nextTile = map.getOrElse(nextPos, " ")
        if (nextTile == '.') {
          //move
          loc = nextPos
          walked.put(loc, arrows(dir))
        } else if (nextTile == '#') {
          //halt
        } else {
          //wrap around
          val xDiv = loc._1 / mod
          val yDiv = loc._2 / mod
          val xMod = loc._1 % mod
          val yMod = loc._2 % mod

          val (wrapped, wDir) = if (mod == 4) {
            dir match {
              case (1, 0)  => (xDiv, yDiv) match {
                // east
                case (0,0) => (loc, dir)
                case (2,1) =>
                  // 4E rotate right into top of 6S
                  val x = (xDiv + 1) * mod + yMod + 1
                  val y = (yDiv + 1) * mod + 0
                  ((x,y), south)
              }
              case (0, 1)  => (xDiv, yDiv) match {
                // south
                case (0,0) => (loc, dir)
                case (2,2) =>
                  // 5 rotate twice into bottom of 2
                  val x = (xDiv - 2) * mod + (mod - xMod - 1)
                  val y = (yDiv - 1) * mod + (mod - 1)
                  ((x,y), north)
              }
              case (-1, 0) => (xDiv, yDiv) match {
                // west
                case (0,0) => (loc, dir)
              }
              case (0, -1) => (xDiv, yDiv) match {
                // north
                case (0,0) => (loc, dir)
                case (1,1) =>
                  // 3 turn left into 1
                  val x = (xDiv + 1) * mod + 0
                  val y = (yDiv - 1) * mod + (xMod)
                  ((x,y), east)
              }
            }
          } else {
            (xDiv, yDiv) match {

              case (1,0) => // 1
                dir match {
                  case (-1, 0) => // 1W -> 5W
                    val x = (0) * mod + 0
                    val y = (2) * mod + (mod - yMod - 1)
                    ((x, y), east)
                  case (0, -1) => // 1N -> 6W
                    val x = (0) * mod + 0
                    val y = (3) * mod + (xMod)
                    ((x,y), east)
                }
              case (2,0) => // 2
                dir match {
                  case (1, 0) => // 2E -> 4E
                    val x = (1) * mod + (mod - 1)
                    val y = (2) * mod + (mod - yMod - 1)
                    ((x,y), west)
                  case (0, 1)  => // 2S -> 3E
                    val x = (1) * mod + (mod - 1)
                    val y = (1) * mod + xMod
                    ((x,y), west)
                  case (0, -1) => // 2N -> 6S
                    val x = (0) * mod + xMod
                    val y = (3) * mod + (mod - 1)
                    ((x,y), dir)
                }
              case (1,1) => // 3
                dir match {
                  case (1,0) =>   // 3E -> 2S
                    val x = (2) * mod + (yMod)
                    val y = (0) * mod + (mod - 1)
                    ((x,y), north)
                  case (-1, 0) => // 3W -> 5N
                    val x = (xDiv - 1) * mod + yMod
                    val y = (yDiv + 1) * mod + 0
                    ((x,y), south)
                }
              case (1,2) => // 4
                dir match {
                  case (1, 0)  => // 4E -> 2E
                    val x = (2) * mod + (mod - 1)
                    val y = (0) * mod + (mod - yMod - 1)
                    ((x,y), west)
                  case (0, 1)  => // 4S -> 6E
                    val x = (0) * mod + (mod - 1)
                    val y = (3) * mod + (xMod)
                    ((x,y), west)
                }
              case (0,2) => // 5
                dir match {
                  case (-1, 0) => // 5W -> 1W
                    val x = (1) * mod + 0
                    val y = (0) * mod + (mod - yMod - 1)
                    ((x,y), east)
                  case (0, -1) => // 5N -> 3W
                    val x = (1) * mod + 0
                    val y = (1) * mod + (xMod)
                    ((x,y), east)

                }
              case (0,3) => // 6
                dir match {
                  case (1, 0)  => // 6E turn right into 4S
                    val x = (1) * mod + (yMod)
                    val y = (2) * mod + (mod - 1)
                    ((x,y), north)
                  case (0, 1)  => // 6S -> 2N
                    val x = (2) * mod + xMod
                    val y = (0) * mod + 0
                    ((x,y), dir)
                  case (-1, 0) => // 6W -> 1N
                    val x = (1) * mod + (yMod)
                    val y = (0) * mod + 0
                    ((x,y), south)
                }



            }

          }
          //println(s"dir=$dir, loc=$loc, wrapped=$wrapped")
          val wrappedTile = map(wrapped)
          if (wrappedTile == '#') {
            //println(s"Halted wrapped=$wrapped")
            //halt
          } else {
            if (wrappedTile == ' ') {
              println(s"problem! loc=$loc, wrapped=$wrapped")
            }
            loc = wrapped
            dir = wDir
            walked.put(loc, arrows(dir))
          }
        }
      })
      turn match {
        case "L" =>
          // println(s"L $dir becomes ${left(dir)}. loc=$loc")
          dir = left(dir)
        case "R" =>
          // println(s"R $dir becomes ${right(dir)}. loc=$loc")
          dir = right(dir)
        case "S" =>
          //last op
      }
      walked.put(loc, arrows(dir))
    }
    val dirMap = directions.zipWithIndex.toMap
    val password = 1000 * (loc._2 + 1) + 4 * (loc._1 + 1) + dirMap(dir)
    //logMap(walked)
    password
  }

  def m(x: Int, mod: Int): Int = (x + mod) % mod


  private def logMap(map: mutable.HashMap[(Int, Int), Char]) = {
    val java = map.map(kv => (new Point(kv._1._1, kv._1._2), kv._2)).asJava
    println(new LogUtils[Char].mapToText(java, v => if (v == 0) " " else s"$v"))
  }


}
