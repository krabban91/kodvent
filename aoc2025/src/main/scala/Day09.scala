import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  //printResultPart1Test
  printResultPart2Test
  //printResultPart1
  printResultPart2

  def parseInput(strings: Seq[String]): Seq[(Long, Long)] = {
    strings.map { s => val t = s.split(","); (t.head.toLong, t.last.toLong) }
  }

  def areaOfPair(p: Set[(Long, Long)]): Long = {
    val t = p.toSeq
    val (a, b) = (t.head, t.last)
    (Math.abs(a._1 - b._1) + 1) * (Math.abs(a._2 - b._2) + 1)
  }

  override def part1(strings: Seq[String]): Long = {
    val points: Set[(Long, Long)] = parseInput(strings).toSet
    val allPairs = points.subsets(2).toSeq
    val areas = allPairs.map(p => (p, areaOfPair(p)))
    val best = areas.maxBy(_._2)
    best._2
  }

  def isWithinBorders(t: (Set[(Long, Long)], Long), allBorders: Set[(Long, Long)], orderedRed: Seq[(Long, Long)], knownInside : mutable.HashSet[(Long, Long)]) = {
    if (t._2 >= 2964756515L) {
      false
    } else {
      println(s"Checking ${t}")
      val redSet = orderedRed.toSet
      val points = t._1.toSeq
      val (a, b) = (points.head, points.last)
      val xs = (Math.min(a._1, b._1) to Math.max(a._1, b._1))
      val ys = (Math.min(a._2, b._2) to Math.max(a._2, b._2))

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
            var enteredOnRed = false
            var inside = false
            var isValid = true
            var currX = leftMost._1 - 1
            val (minX, maxX) = (Math.min(a._1, b._1), Math.max(a._1, b._1))
            while (isValid && currX <= maxX) {
              val currP = (currX, y)
              if (redSet.contains(currP)) {
                val nextP = (currX + 1, y - 1)
                enteredOnRed = !enteredOnRed
                if (inside && knownInside(nextP)) {
                  // naive inside check worked
                  print(s"Stayed inside: $currP")
                } else {
                  floodFromKnown(allBorders, knownInside)
                  if (inside && knownInside(nextP)) {
                    // inside check worked
                    print(s"Stayed inside: $currP")
                  } else {
                  inside = !inside
                  }
                }
              }
              else if (!enteredOnRed && allBorders.contains(currP)) {
                inside = !inside
              }
              else {
                if (inside && !allBorders(currP)) {
                  knownInside.add(currP)
                }
                if (currX >= minX && currX <= maxX && !inside) {
                  isValid = false
                }
              }
              currX += 1
            }

            isValid
          }
        }
      }
      val out = if (horiCheck) {

        val vertCheck = xs.forall { x =>
          val sameX = allBorders.filter(_._1 == x)
          if (sameX.isEmpty) {
            false
          } else {
            val topMost = sameX.minBy(_._2)
            val bottomMost = sameX.maxBy(_._2)
            if (a._2 < topMost._2 || b._2 < topMost._2 || a._2 > bottomMost._2 || b._2 > bottomMost._2) {
              false
            } else {
              // TODO ensure that all Ys are inside polygon
              var enteredOnRed = false
              var inside = false
              var isValid = true
              var currY = topMost._2 - 1
              val (minY, maxY) = (Math.min(a._2, b._2), Math.max(a._2, b._2))
              while (isValid && currY <= maxY) {
                val currP = (x, currY)
                if (redSet.contains(currP)) {
                  val nextP = (x - 1, currY + 1)
                  enteredOnRed = !enteredOnRed
                  if (inside && knownInside(nextP)) {
                    // naive inside check worked
                    print(s"Stayed inside: $currP")
                  } else {
                    floodFromKnown(allBorders, knownInside)
                    if (inside && knownInside(nextP)) {
                      // inside check worked
                      print(s"Stayed inside: $currP")
                    } else {
                      inside = !inside
                    }
                  }
                }
                else if (!enteredOnRed && allBorders.contains(currP)) {
                  inside = !inside
                }
                else {
                  if (inside && !allBorders(currP)) {
                    knownInside.add(currP)
                  }
                  if (currY >= minY && currY <= maxY && !inside) {
                    isValid = false
                  }
                }
                currY += 1
              }

              isValid
            }
          }
        }
        vertCheck
      } else {
        false
      }
      println(s"area: ${t._2}, [$a,$b] xs.size=${xs.size}, ys.size=${ys.size}")
      out
    }
  }

  private def floodFromKnown(allBorders: Set[(Long, Long)], knownInside: mutable.HashSet[(Long, Long)]): Unit = {
    // need to flood from known.
    val candidates = mutable.PriorityQueue[(Long, Long)]()
    candidates.addAll(knownInside)
    val checked = mutable.HashSet[(Long, Long)]()
    while (candidates.nonEmpty) {
      val pop = candidates.dequeue()
      if (!checked(pop)) {
        knownInside.add(pop)
        checked.add(pop)
        val directions = Seq((0, 1), (1, 0), (0, -1), (-1, 0)).map(p => (p._1 + pop._1, p._2 + pop._2)).toSet
          .diff(knownInside).diff(checked).diff(allBorders)
        candidates.addAll(directions)
      }
    }
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

    val knownInside = mutable.HashSet[(Long, Long)]()

    val redSet = redPoints.toSet
    val allBorders = redSet ++ greenPointBorders

    // from left
    greenPointBorders.map(_._2).foreach{ y =>
      val xs = allBorders.map(_._1)
      val (minX, maxX) = (xs.min, xs.max)
      var inside = false
      var redCornered = false
      var upCorner = false
      (minX - 1 to maxX).foreach{ x =>
        val p = (x,y)
        if (inside) {
          if (allBorders(p)) {
            inside = false
            if (redSet(p)) {
              redCornered = true
              if (greenPointBorders((p._1, p._2 - 1))) {
                upCorner = true
              }
            }
          } else {
            knownInside.add(p)
          }
        } else {
          if (!redCornered) {
            if (redSet(p)) {
              redCornered = true
              if (greenPointBorders((p._1, p._2 - 1))) {
                upCorner = true
              }
            } else {
              if (greenPointBorders(p)) {
                inside = true
              }
            }
          } else {
            if (redSet(p)) {

              redCornered = false
              if (upCorner) {
                if (greenPointBorders((p._1, p._2 - 1))) {
                  inside = false
                } else {
                  inside = true
                }
              } else {
                if (greenPointBorders((p._1, p._2 - 1))) {
                  inside = true
                } else {
                  inside = false
                }
              }
            } else {
              if (greenPointBorders(p)) {
                inside = true
              }
            }
          }
        }
      }
    }


    val allPairs = redSet.subsets(2).toSeq
    val areas = allPairs.map(p => (p, areaOfPair(p))).sortBy(_._2).reverse
    val filtered = areas.find { t => isWithinBorders(t, allBorders, redPoints, knownInside) }
    val best = filtered.maxBy(_._2)
    best._2

  }


}
