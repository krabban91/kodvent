import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day22 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val (map, ops) = parseInput(strings)


    var loc = map.filter(kv => kv._1._2 == 0 && kv._2 == '.').minBy(_._1._1)._1
    var dir = (1, 0)

    ops._1.foreach { case op@(steps, turn) =>
      (1 to steps).foreach(i => {
        val nextPos = move(loc, dir)
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
    val (map, ops) = parseInput(strings)
    val mod = if (strings.head.length < 50) 4 else 50
    val maps: Map[(Int, Int), Map[(Int, Int), Char]] = slicedMap(mod, map)
    val edges = if (mod == 4) testEdges else realEdges

    var locMap = maps.keys.minBy(v => (v._2, v._1))
    var locPos = maps(locMap).filter(_._2 == '.').minBy(v => (v._1._2, v._1._1))._1
    var dir = (1, 0)

    ops._1.foreach { case op@(steps, turn) =>
      (1 to steps).foreach(_ => {
        val nextP = move(locPos, dir)
        maps(locMap).get(nextP).map(c => (locMap, nextP, dir, c)).orElse({
          val (nextMap, times) = edges.getOrElse((locMap, dir), (move(locMap, dir), (invCompass(dir), invCompass(dir))))
          val wrappedT = passOverEdge(nextP, mod, times)
          val wrappedDir = compass(times._2)
          Some((nextMap, wrappedT, wrappedDir, maps(nextMap)(wrappedT)))
        })
          .filterNot(_._4 == '#')
          .foreach { case (nextMap, nextLoc, nextDir, c) =>
            locPos = nextLoc
            locMap = nextMap
            dir = nextDir
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
    val (cx, cy) = convertCoords(mod, locMap, locPos)
    val password = 1000 * (cy + 1) + 4 * (cx + 1) + dirMap(dir)
    password
  }

  private def parseInput(strings: Seq[String]): (Map[(Int, Int), Char], (Seq[(Int, String)], String)) = {
    val grouped = groupsSeparatedByTwoNewlines(strings)
    val map = grouped.head.split("\n")
      .filter(_.nonEmpty).zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x, y), c) } }.filterNot(_._2 == ' ').toMap
    val ops = (grouped.last.trim + "S").foldLeft((Seq[(Int, String)](), "")) { case ((l, s), c) =>
      if (c.isLetter) {
        (l ++ Seq((s.toInt, s"$c")), "")
      } else {
        (l, s + c)
      }
    }
    (map, ops)
  }

  private def move(locPos: (Int, Int), dir: (Int, Int)) = {
    (locPos._1 + dir._1, locPos._2 + dir._2)
  }

  private def convertCoords(mod: Int, locMap: (Int, Int), locPos: (Int, Int)) = {
    (locMap._1 * mod + locPos._1, locMap._2 * mod + locPos._2)
  }

  private def testEdges: Map[((Int, Int), (Int, Int)), ((Int, Int), (Char, Char))] = {
    /*
    test input
         a
       bc1d
      e234f
       gh56i
         jk
    */

    Map[((Int, Int), (Int, Int)), ((Int, Int), (Char, Char))](
      ((2, 0), (0, -1)) -> ((0, 1), ('N', 'S')), // 1->2 (a,b)
      ((0, 1), (0, -1)) -> ((2, 0), ('N', 'S')),
      ((2, 0), (-1, 0)) -> ((1, 1), ('W', 'S')), // 1->3 (c,c)
      ((1, 1), (0, -1)) -> ((2, 0), ('N', 'E')),
      ((2, 0), (1, 0)) -> ((3, 2), ('E', 'W')), // 1->6 (d,i)
      ((3, 2), (1, 0)) -> ((2, 0), ('E', 'W')),
      ((0, 1), (-1, 0)) -> ((3, 2), ('W', 'N')), // 2->6 (e,k)
      ((3, 2), (0, 1)) -> ((0, 1), ('S', 'E')),
      ((2, 1), (1, 0)) -> ((3, 2), ('E', 'S')), // 4->6 (f,f)
      ((3, 2), (0, -1)) -> ((2, 1), ('N', 'W')),
      ((0, 1), (0, 1)) -> ((2, 2), ('S', 'N')), // 2->5 (g,j)
      ((2, 2), (0, 1)) -> ((0, 1), ('S', 'N')),
      ((1, 1), (-1, 0)) -> ((2, 1), ('W', 'N')), // 5->3 (h,h)
      ((2, 1), (0, 1)) -> ((1, 1), ('S', 'E')),
    )
  }

  private def realEdges: Map[((Int, Int), (Int, Int)), ((Int, Int), (Char, Char))] = {
    /*
    real input
        ab
       c12d
       e3f
      g45h
      i6j
       k
    */
    Map[((Int, Int), (Int, Int)), ((Int, Int), (Char, Char))](
      ((1, 0), (0, -1)) -> ((0, 3), ('N', 'E')), //1->6 (a,i)
      ((0, 3), (-1, 0)) -> ((1, 0), ('W', 'S')),
      ((1, 1), (1, 0)) -> ((2, 0), ('E', 'N')), //3->2 (f,f)
      ((2, 0), (0, 1)) -> ((1, 1), ('S', 'W')),
      ((0, 3), (1, 0)) -> ((1, 2), ('E', 'N')), //6->5 (j,j)
      ((1, 2), (0, 1)) -> ((0, 3), ('S', 'W')),
      ((2, 0), (0, -1)) -> ((0, 3), ('N', 'N')), //2->6 (b,k)
      ((0, 3), (0, 1)) -> ((2, 0), ('S', 'S')),
      ((1, 0), (-1, 0)) -> ((0, 2), ('W', 'E')), //1->4 (c,g)
      ((0, 2), (-1, 0)) -> ((1, 0), ('W', 'E')),
      ((1, 1), (-1, 0)) -> ((0, 2), ('W', 'S')), //3->4 (e,e)
      ((0, 2), (0, -1)) -> ((1, 1), ('N', 'E')),
      ((2, 0), (1, 0)) -> ((1, 2), ('E', 'W')), //2->5 (d,h)
      ((1, 2), (1, 0)) -> ((2, 0), ('E', 'W')),
    )
  }

  def passOverEdge(pos: (Int, Int), mod: Int, style: (Char, Char)): (Int, Int) = {
    val (x, y) = pos
    style match {
      case ('S', 'S') => (x, 0)
      case ('N', 'N') => (x, mod - 1)
      case ('W', 'W') => (mod - 1, y)
      case ('E', 'E') => (0, y)

      case ('N', 'S') => (mod - x - 1, 0)
      case ('S', 'N') => (mod - x - 1, mod - 1)
      case ('W', 'E') => (0, mod - y - 1)
      case ('E', 'W') => (mod - 1, mod - y - 1)

      case ('N', 'E') => (0, x)
      case ('S', 'W') => (mod - 1, x)
      case ('E', 'S') => (mod - y - 1, 0)
      case ('W', 'N') => (mod - y - 1, mod - 1)

      case ('W', 'S') => (y, 0)
      case ('E', 'N') => (y, mod - 1)
      case ('S', 'E') => (0, mod - x - 1)
      case ('N', 'W') => (mod - 1, mod - x - 1)
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

  private def compass = Map('E' -> (1, 0), 'S' -> (0, 1), 'W' -> (-1, 0), 'N' -> (0, -1))

  private def directions = Seq(compass('E'), compass('S'), compass('W'), compass('N'))

  private def invCompass = Map(compass('E') -> 'E', compass('S') -> 'S', compass('W') -> 'W', compass('N') -> 'N')

  private def left = Map(compass('E') -> compass('N'), compass('S') -> compass('E'), compass('W') -> compass('S'), compass('N') -> compass('W'))

  private def right = Map(compass('E') -> compass('S'), compass('S') -> compass('W'), compass('W') -> compass('N'), compass('N') -> compass('E'))
}
