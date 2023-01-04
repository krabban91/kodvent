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
      .filter(_.nonEmpty).zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x, y), c) } }.filterNot(_._2 == ' ').toMap

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
            case (1, 0) => map.filter(kv => kv._1._2 == nextPos._2)
              .minBy(_._1._1)._1
            case (0, 1) => map.filter(kv => kv._1._1 == nextPos._1)
              .minBy(_._1._2)._1
            case (-1, 0) => map.filter(kv => kv._1._2 == nextPos._2)
              .maxBy(_._1._1)._1
            case (0, -1) => map.filter(kv => kv._1._1 == nextPos._1)
              .maxBy(_._1._2)._1
          }
          val wrappedTile = map(wrapped)
          if (wrappedTile == '#') {
            //halt
          } else {
            loc = wrapped
          }
        }
      })
      turn match {
        case "L" =>
          dir = left(dir)
        case "R" =>
          dir = right(dir)
        case "S" => //last op
      }
    }
    val dirMap = directions.zipWithIndex.toMap
    val password = 1000 * (loc._2 + 1) + 4 * (loc._1 + 1) + dirMap(dir)

    password
  }


  override def part2(strings: Seq[String]): Long = {
    //part 2, the map is a cube
    val east = (1, 0)
    val south = (0, 1)
    val west = (-1, 0)
    val north = (0, -1)
    val directions = Seq(east, south, west, north)
    val compass = Map('E' -> east, 'S' -> south, 'W' -> west, 'N' -> north)
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

    val maps: Map[(Int, Int), Map[(Int, Int), Char]] = slicedMap(mod, map)

    val edges = cubeEdges(mod == 4)


    val walked = mutable.HashMap[(Int, Int), Char]()

    var locMap = maps.keys.minBy(v => (v._2, v._1))
    var locPos = maps(locMap).filter(_._2 == '.').minBy(v => (v._1._2, v._1._1))._1

    var dir = startDir

    val ops = (grouped.last.trim + "S").foldLeft((Seq[(Int, String)](), "")) { case ((l, s), c) =>
      if (c.isLetter) {
        (l ++ Seq((s.toInt, s"$c")), "")
      } else {

        (l, s + c)
      }
    }

    walked.addAll(map)
    walked.put(convertCoords(mod, locMap, locPos), arrows(dir))

    ops._1.foreach { case op@(steps, turn) =>
      //logMap(walked)
      (1 to steps).foreach(_ => {
        val nextP = (locPos._1 + dir._1, locPos._2 + dir._2)
        maps(locMap).get(nextP).map(c => (locMap, nextP, dir, c)).orElse({
          val (nextMap, times) = edges(locMap, dir)
          val wrappedT = passOverEdge(locPos, mod, times)
          val wrappedDir = compass(times._2)
          Some((nextMap, wrappedT, wrappedDir, maps(nextMap)(wrappedT)))
        })
          .filterNot(_._4 == '#')
          .foreach { case (nextMap, nextLoc, nextDir, c) =>
            locPos = nextLoc
            locMap = nextMap
            dir = nextDir
            walked.put(convertCoords(mod, locMap, locPos), arrows(dir))
          }
      })
      turn match {
        case "L" =>
          dir = left(dir)
        case "R" =>
          dir = right(dir)
        case "S" => //last op
      }
      walked.put(convertCoords(mod, locMap, locPos), arrows(dir))
    }
    val dirMap = directions.zipWithIndex.toMap
    val password = 1000 * (locMap._2 * mod + locPos._2 + 1) + 4 * (locMap._1 * mod + locPos._1 + 1) + dirMap(dir)
    //logMap(walked)
    password
  }

  private def convertCoords(mod: Int, locMap: (Int, Int), locPos: (Int, Int)) = {
    (locMap._1 * mod + locPos._1, locMap._2 * mod + locPos._2)
  }

  private def cubeEdges(test: Boolean): Map[((Int, Int), (Int, Int)), ((Int, Int), (Char, Char))] = {
    /*
    test input
         a
       bc1d
      e234f
       gh56i
         jk
    */

    val testEdges = Map[((Int, Int), (Int, Int)), ((Int, Int), (Char, Char))](
      ((2, 0), (0, -1)) -> ((0, 1), ('N', 'S')), // a(N) -> b(S)
      ((0, 1), (0, -1)) -> ((2, 0), ('N', 'S')), // b(N) -> a(S)

      ((2, 0), (-1, 0)) -> ((1, 1), ('W', 'S')), // c(W) -> c(S)
      ((1, 1), (0, -1)) -> ((2, 0), ('N', 'E')), // c(N) -> c(E)

      ((2, 0), (1, 0)) -> ((3, 2), ('E', 'W')), // d(E) -> i(W)
      ((3, 2), (1, 0)) -> ((2, 0), ('E', 'W')), // i(E) -> d(W)

      ((0, 1), (-1, 0)) -> ((3, 2), ('W', 'N')), // e(W) -> k(N)
      ((3, 2), (0, 1)) -> ((0, 1), ('S', 'E')), // k(S) -> e(E)

      ((2, 1), (1, 0)) -> ((3, 2), ('E', 'S')), // f(E) -> f(S)
      ((3, 2), (0, -1)) -> ((2, 1), ('N', 'W')), // f(N) -> f(W)

      ((0, 1), (0, 1)) -> ((2, 2), ('S', 'N')), // g(S) -> j(N)
      ((2, 2), (0, 1)) -> ((0, 1), ('S', 'N')), // j(S) -> g(N)

      ((1, 1), (-1, 0)) -> ((2, 1), ('W', 'N')), // h(W) -> h(N)
      ((2, 1), (0, 1)) -> ((1, 1), ('S', 'E')), // h(S) -> h(E)

      ((2, 0), (0, 1)) -> ((2, 1), ('S', 'S')), // 1->4
      ((0, 1), (1, 0)) -> ((1, 1), ('E', 'E')), // 2->3
      ((1, 1), (1, 0)) -> ((2, 1), ('E', 'E')), // 3->4
      ((2, 1), (0, 1)) -> ((2, 2), ('S', 'S')), // 4->5
      ((2, 2), (1, 0)) -> ((3, 2), ('E', 'E')), // 5->6

      ((2, 1), (0, -1)) -> ((2, 0), ('N', 'N')), // 1<-4
      ((1, 1), (-1, 0)) -> ((0, 1), ('W', 'W')), // 2<-3
      ((2, 1), (-1, 0)) -> ((1, 1), ('W', 'W')), // 3<-4
      ((2, 2), (0, -1)) -> ((2, 1), ('N', 'N')), // 4<-5
      ((3, 2), (-1, 0)) -> ((2, 2), ('W', 'W')), // 5<-6
    )
    /*
    real input

        ab
       c12d
       e3f
      g45h
      i6j
       k
   */

    val edges = Map[((Int, Int), (Int, Int)), ((Int, Int), (Char, Char))](
      ((1, 0), (0, -1)) -> ((0, 3), ('N', 'E')),
      ((0, 3), (-1, 0)) -> ((1, 0), ('W', 'S')),

      ((1, 1), (1, 0)) -> ((2, 0), ('E', 'N')),
      ((2, 0), (0, 1)) -> ((1, 1), ('S', 'W')),

      ((0, 3), (1, 0)) -> ((1, 2), ('E', 'N')),
      ((1, 2), (0, 1)) -> ((0, 3), ('S', 'W')),

      ((2, 0), (0, -1)) -> ((0, 3), ('N', 'N')),
      ((0, 3), (0, 1)) -> ((2, 0), ('S', 'S')),

      ((1, 0), (-1, 0)) -> ((0, 2), ('W', 'E')),
      ((0, 2), (-1, 0)) -> ((1, 0), ('W', 'E')),

      ((1, 1), (-1, 0)) -> ((0, 2), ('W', 'S')),
      ((0, 2), (0, -1)) -> ((1, 1), ('N', 'E')),

      ((2, 0), (1, 0)) -> ((1, 2), ('E', 'W')),
      ((1, 2), (1, 0)) -> ((2, 0), ('E', 'W')),

      ((1, 0), (1, 0)) -> ((2, 0), ('E', 'E')), // 1->2
      ((1, 0), (0, 1)) -> ((1, 1), ('S', 'S')), // 1->3
      ((1, 1), (0, 1)) -> ((1, 2), ('S', 'S')), // 3->5
      ((0, 2), (1, 0)) -> ((1, 2), ('E', 'E')), // 4->5
      ((0, 2), (0, 1)) -> ((0, 3), ('S', 'S')), // 4->6

      ((2, 0), (-1, 0)) -> ((1, 0), ('W', 'W')), // 1<-2
      ((1, 1), (0, -1)) -> ((1, 0), ('N', 'N')), // 1<-3
      ((1, 2), (0, -1)) -> ((1, 1), ('N', 'N')), // 3<-5
      ((1, 2), (-1, 0)) -> ((0, 2), ('W', 'W')), // 4<-5
      ((0, 3), (0, -1)) -> ((0, 2), ('N', 'N')), // 4<-6
    )
    if (test) testEdges else edges
  }

  private def passOverEdge(pos: (Int, Int), mod: Int, style: (Char, Char)): (Int, Int) = {

    val (x, y) = pos
    style match {
      case ('N', 'N') => (x, mod - 1)
      case ('E', 'E') => (0, y)
      case ('S', 'S') => (x, 0)
      case ('W', 'W') => (mod - 1, y)

      case ('N', 'S') => (mod - x - 1, 0)
      case ('S', 'N') => (mod - x - 1, mod - 1)

      case ('E', 'W') => (mod - 1, mod - y - 1)
      case ('W', 'E') => (0, mod - y - 1)

      case ('N', 'E') => (0, x)
      case ('E', 'S') => (mod - y - 1, 0)
      case ('S', 'W') => (mod - 1, x)
      case ('W', 'N') => (mod - y - 1, 0)

      case ('N', 'W') => (mod - 1, mod - x - 1)
      case ('E', 'N') => (y, mod - 1)
      case ('S', 'E') => (0, mod - x - 1)
      case ('W', 'S') => (y, 0)
    }
  }

  private def slicedMap(mod: Int, input: Map[(Int, Int), Char]): Map[(Int, Int), Map[(Int, Int), Char]] = {
    val filtered = input.filterNot(_._2 == ' ')
    val maxX = filtered.keySet.map(_._1).max
    val maxY = filtered.keySet.map(_._2).max
    val maps = (0 to maxX / mod)
      .flatMap(x => (0 to maxY / mod).
        map(y => ((x, y), (0 until mod)
          .flatMap(dx => (0 until mod)
            .flatMap(dy => {
              filtered.get((x * mod + dx, y * mod + dy)).map(((dx, dy), _))
            })).toMap)))
      .filter(_._2.nonEmpty).toMap
    maps
  }

  private def logMap(map: mutable.HashMap[(Int, Int), Char]) = {
    val java = map.map(kv => (new Point(kv._1._1, kv._1._2), kv._2)).asJava
    println(new LogUtils[Char].mapToText(java, v => if (v == 0) " " else s"$v"))
  }


}
