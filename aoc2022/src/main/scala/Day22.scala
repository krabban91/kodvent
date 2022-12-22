import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day22 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val startDir = (1,0)
    val grouped = groupsSeparatedByTwoNewlines(strings)

    val map = grouped.head.split("\n")
      .filter(_.nonEmpty).zipWithIndex.flatMap{case (s, y) => s.zipWithIndex.map{case (c, x) => ((x,y), c)}}.toMap

    var loc = map.filter(kv => kv._1._2 == 0 && kv._2 =='.').minBy(_._1._1)._1
    var dir = startDir
    val directions = Seq((1,0), (0,1), (-1, 0), (0, -1))
    val left = Map(
      directions(0) -> directions(3),
      directions(1) -> directions(0),
      directions(2) -> directions(1),
      directions(3) -> directions(2),
    )
    val right = Map(
      directions(0) -> directions(1),
      directions(1) -> directions(2),
      directions(2) -> directions(3),
      directions(3) -> directions(0),
    )

    val ops = (grouped.last.trim + "S").foldLeft((Seq[(Int, String)](), "")){ case ((l ,s), c) =>
      if (c.isLetter) {
        (l ++ Seq((s.toInt, s"$c")), "")
      } else {

        (l, s + c)
      }
    }
    val finalOps = ()
    ops._1.foreach{ case op@(steps, turn) =>
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
            case (1,0) => map.filter(kv => kv._1._2 == nextPos._2 && kv._2 !=' ')
              .minBy(_._1._1)._1
            case (0,1) => map.filter(kv => kv._1._1 == nextPos._1 && kv._2 !=' ')
              .minBy(_._1._2)._1
            case (-1,0) => map.filter(kv => kv._1._2 == nextPos._2 && kv._2 !=' ')
              .maxBy(_._1._1)._1
            case (0,-1) => map.filter(kv => kv._1._1 == nextPos._1 && kv._2 !=' ')
              .maxBy(_._1._2)._1
          }
          //println(s"dir=$dir, loc=$loc, wrapped=$wrapped")
          val wrappedTile = map(wrapped)
          if (wrappedTile == '#'){
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
          println(s"last op: $op  loc=$loc , dir=$dir")

      }
    }
    val dirMap = directions.zipWithIndex.toMap
    val password = 1000 * (loc._2 + 1) + 4 * (loc._1 + 1) + dirMap(dir)
    // 30398 is wrong
    // 30358 is wrong
    password
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
