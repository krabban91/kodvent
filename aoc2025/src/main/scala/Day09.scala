import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  //printResultPart1Test
  printResultPart2Test
  //printResultPart1
  printResultPart2

  def parseInput(strings: Seq[String]): Seq[(Long, Long)] = {
    strings.map{s => val t = s.split(","); (t.head.toLong, t.last.toLong)}
  }

  def areaOfPair(p: Set[(Long, Long)]): Long = {
    val t = p.toSeq
    val (a, b) = (t.head , t.last)
    (Math.abs(a._1 - b._1) + 1) * (Math.abs(a._2 - b._2) + 1)
  }

  override def part1(strings: Seq[String]): Long = {
    val points: Set[(Long, Long)] = parseInput(strings).toSet
    val allPairs = points.subsets(2).toSeq
    val areas = allPairs.map(p => (p, areaOfPair(p)))
    val best = areas.maxBy(_._2)
    best._2
  }

  def isWithinBorders(t: (Set[(Long, Long)], Long), allBorders: Set[(Long, Long)], orderedRed: Seq[(Long, Long)]) = {
    val redSet = orderedRed.toSet
    val points = t._1.toSeq
    val (a, b) = (points.head, points.last)
    val xs = (Math.min(a._1, b._1) to Math.max(a._1, b._1)).toSeq
    val vertCheck = xs.forall{ x =>
      val sameX = allBorders.filter(_._1 == x)
      if (sameX.isEmpty) {
        false
      } else {
        val topMost = sameX.minBy(_._2)
        val bottomMost = sameX.maxBy(_._2)
        if (a._2 < topMost._2 || b._2 < topMost._2 || a._2 > bottomMost._2 || b._2 > bottomMost._2) {
          false
        } else {
          // TODO
          true
        }
      }
    }
    val ys = (Math.min(a._2, b._2) to Math.max(a._2, b._2)).toSeq
    val horiCheck = ys.forall { y =>
      val sameY = allBorders.filter(_._2 == y)
      if (sameY.isEmpty) {
        false
      } else {
        val leftMost = sameY.minBy(_._1)
        val rightMost = sameY.maxBy(_._1)
        if (a._1 < leftMost._1 || b._1 < leftMost._1 || a._1 > rightMost._1 || b._1 > rightMost._1) {
          false
        } else {
          // TODO
          true
        }
      }
    }
    println(s"area: ${t._2}, xs.size=${xs.size}, ys.size=${ys.size}")

    vertCheck && horiCheck
  }

  override def part2(strings: Seq[String]): Long = {
    val redPoints: Seq[(Long, Long)] = parseInput(strings)
    val greenPointBorders = redPoints.sliding(2).flatMap { window =>
      val (from, to) = (window.head, window.last)
      if (from._1 == to._1) {
        val min = Math.min(from._2, to._2)
        val max = Math.max(from._2, to._2)
        (min + 1 until max).map((from._1, _)).toSet
      } else {
        val min = Math.min(from._1, to._1)
        val max = Math.max(from._1, to._1)
        (min + 1 until max).map((_, from._2)).toSet
      }
    }.toSet
    val allBorders = redPoints.toSet ++ greenPointBorders
    val allPairs = redPoints.toSet.subsets(2).toSeq
    val areas = allPairs.map(p => (p, areaOfPair(p))).sortBy(_._2).reverse
    val filtered = areas.find { t => isWithinBorders(t, allBorders, redPoints)}
    val best = filtered.maxBy(_._2)
    best._2

  }
}
