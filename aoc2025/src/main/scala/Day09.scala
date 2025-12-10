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

  def isWithinBorders(t: (Set[(Long, Long)], Long), allBorders: Set[(Long, Long)], orderedRed: Seq[(Long, Long)], knownOutside: mutable.HashSet[(Long, Long)], knownInside: mutable.HashSet[(Long, Long)]) = {
    val points = t._1.toSeq
    val (a, b) = (points.head, points.last)
    val (minY, maxY) = (Math.min(a._2, b._2), Math.max(a._2, b._2))
    val (minX, maxX) = (Math.min(a._1, b._1), Math.max(a._1, b._1))
    val corners = Seq((minX, minY), (maxX, minY), (minX, maxY), (maxX, maxY))
    val rejectFast = corners.find(p => knownOutside(p))
      .orElse(knownOutside.find { case (ox, oy) => ox >= minX && ox <= maxX && oy >= minY && oy <= maxY })
    if (rejectFast.isDefined) {
      //println(s"Reject fast: area: ${t._2}, [$a,$b] due to ${rejectFast.get} being inside box")
      false
    } else {
      println(s"Checking: area=${t._2}, [$a,$b]")
      val redSet = orderedRed.toSet
      val smallYs = orderedRed.map(_._2).flatMap(y => Seq((y - 1), y, (y + 1))).distinct
        .filter(y => y >= minY && y <= maxY).sorted

      val horiCheck = smallYs.forall { y =>
        val sameY = allBorders.filter(_._2 == y)
        if (sameY.isEmpty) {
          false
        } else {
          val leftMost = sameY.minBy(_._1)
          val rightMost = sameY.maxBy(_._1)
          if (a._1 < leftMost._1 || b._1 < leftMost._1 || a._1 > rightMost._1 || b._1 > rightMost._1) {
            if ((a._1 < leftMost._1 || a._1 > rightMost._1)) {
              val outside = (a._1, y)
              knownOutside.add(outside)
            }
            if ((b._1 < leftMost._1 || b._1 > rightMost._1)) {
              val outside = (b._1, y)
              knownOutside.add(outside)
            }
            false
          } else {
            val redSameY = redSet.filter(_._2 == y)
            val xToCheck = (Set(minX, maxX) ++ sameY.map(_._1).flatMap(x => Seq(x - 1, x, x + 1)))
              .filter(x => x <= maxX).toSeq.sorted
            val needsToBeInside = xToCheck.filter(x => x >= minX).map((_, y))
            if (needsToBeInside.forall(p => knownInside(p))) {
              // already checked. Ok
              true
            } else {
              // TODO
              // either enter on red, or cross green border.
              // - if enter on red, need to know if exits into same or different.
              // - if cross green border, need to know if we are crossing green multiple times
              var enteredOnRed = false
              var inside = false
              var isValid = true
              var upIsIn = false
              var ix = 0
              while (isValid && ix < xToCheck.size) {
                val currX = xToCheck(ix)
                val p = (currX, y)
                if (!inside) {
                  if (enteredOnRed) {
                    println("Unhandled scenario: outside, enterOnRed")
                    val z = 1 - 1
                    val v = 1 / z
                  } else {
                    if (redSet(p)) {
                      inside = true
                      // prev was empty
                      enteredOnRed = true
                      // instead of checking node directly above: check the ordered seq if item before or after is above
                      val redIx = orderedRed.indexOf(p)
                      val sameX = Seq(redIx - 1, redIx + 1).map(v => ((v + orderedRed.size) % orderedRed.size))
                        .map(orderedRed)
                        .filter(_._1 == p._1).head

                      val turnFromAbove = sameX._2 < p._2
                      if (turnFromAbove) {
                        upIsIn = true
                      } else {
                        upIsIn = false
                      }
                    } else if (allBorders(p)) {
                      inside = true
                    } else {
                      if (currX >= minX) {
                        // not inside, not on border. Fail if intersect with box
                        knownOutside.add(p)
                        println(s"Adding ${p} to known outside due to inside check failed")
                        isValid = false
                      }
                    }

                  }
                } else {
                  if (redSet(p)) {
                    if (!enteredOnRed) {
                      //println("Unhandled scenario: inside: Entering on red")
                      // now lets figure out when aboveIsIn
                      enteredOnRed = true
                      val redIx = orderedRed.indexOf(p)
                      val sameX = Seq(redIx - 1, redIx + 1).map(v => ((v + orderedRed.size) % orderedRed.size))
                        .map(orderedRed)
                        .filter(_._1 == p._1).head

                      val turnFromAbove = sameX._2 < p._2
                      if (turnFromAbove) {
                        upIsIn = false
                      } else {
                        upIsIn = true
                      }

                    } else {

                      // exiting hori-line
                      enteredOnRed = false
                      val redIx = orderedRed.indexOf(p)
                      val sameX = Seq(redIx - 1, redIx + 1).map(v => ((v + orderedRed.size) % orderedRed.size))
                        .map(orderedRed)
                        .filter(_._1 == p._1).head
                      val turnsUp = sameX._2 < p._2
                      inside = (turnsUp != upIsIn)
                      if (turnsUp) {
                        // regardless: inside = (turnsUp != upIsIn)
                        // from outside (upIsIn == true)
                        // ´-´ is out: false = (turnsUp != upIsIn)
                        // `-. is in : true  = (turnsUp != upIsIn)
                        // from inside (upIsIn == false)
                        // ´-´ is in : true  = (turnsUp != upIsIn)
                        // `-. is out: false = (turnsUp != upIsIn)
                        // inside = (turnsUp != upIsIn)
                      } else {
                        // regardless: inside = (turnsUp != upIsIn)
                        // from outside (upIsIn == false)
                        // .-. is out: false = (turnsUp != upIsIn)
                        // .-´ is in : true  = (turnsUp != upIsIn)
                        // from inside (upIsIn == true)
                        // .-. is in : true  = (turnsUp != upIsIn)
                        // .-´ is out: false = (turnsUp != upIsIn)
                        // inside = (turnsUp != upIsIn)
                      }

                    }
                  } else if (allBorders(p)) {
                    if (enteredOnRed) {
                      // follow line
                    } else {
                      // flip state
                      inside = false
                    }
                  } else {
                    // inside, nothing to check
                  }
                }
                ix += 1
              }
              if (isValid) {
                knownInside.addAll(needsToBeInside)
              }
              isValid
            }
          }
        }
      }

      //println(s"area: ${t._2}, [$a,$b] xs.size=${xs.size}, ys.size=${ys.size}: $horiCheck")
      horiCheck
    }
  }

  override def part2(strings: Seq[String]): Long = {
    val redPoints: Seq[(Long, Long)] = parseInput(strings)
    val greenPointBorders = (redPoints :+ redPoints.head).sliding(2).flatMap { window =>
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

    val redSet = redPoints.toSet
    val allBorders = redSet ++ greenPointBorders

    val knownOutside = mutable.HashSet[(Long, Long)]()
    val knownInside = mutable.HashSet[(Long, Long)]()
    val allPairs = redSet.subsets(2).toSeq
    val areas = allPairs.map(p => (p, areaOfPair(p))).sortBy(_._2).reverse
    val filtered = areas
      //.find { t => checkFloodBorders(t, allBorders, redPoints, knownInside) }
      .find { t => isWithinBorders(t, allBorders, redPoints, knownOutside, knownInside) }
    val best = filtered.maxBy(_._2)
    best._2
    // 2445367554 is too high => had to try
    // 1566346198
    // 222157805 is too low => rejecting too many
    // 113278013 is too low => rejecting too many

  }


}
