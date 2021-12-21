import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day21 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    var p1 = DiracBoard(strings.head.split("starting position:").last.trim.toLong)
    var p2 = DiracBoard(strings.last.split("starting position:").last.trim.toLong)
    val winsAt = 1000
    var dice = 1
    while (!(p1.hasWon(winsAt) || p2.hasWon(winsAt))) {
      for (i <- 1 to 3) {
        p1 = p1.move(dice)
        dice = nextDice(dice)
      }
      p1 = p1.calcScore
      if (!p1.hasWon(winsAt)) {
        for (i <- 1 to 3) {
          p2 = p2.move(dice)
          dice = nextDice(dice)
        }
        p2 = p2.calcScore
      }
    }
    if (p1.hasWon(winsAt)) {
      p2.score * (p1.rolls + p2.rolls)
    } else {
      p1.score * (p1.rolls + p2.rolls)
    }
  }

  def nextDice(curr: Int): Int = if (curr == 100) 1 else (curr + 1)

  /**
   * 1+1+1,1+1+2,1+1+3,1+2+1,1+2+2,1+2+3,1+3+1,1+3+2,1+3+3,
   * 2+1+1,2+1+2,2+1+3,2+2+1,2+2+2,2+2+3,2+3+1,2+3+2,2+3+3,
   * 3+1+1,3+1+2,3+1+3,3+2+1,3+2+2,3+2+3,3+3+1,3+3+2,3+3+3,
   * =>
   * 3,4,5, 4,5,6, 5,6,7
   * 4,5,6, 5,6,7, 6,7,8
   * 5,6,7, 6,7,8, 7,8,9
   * =>
   * 27 dimensions
   * only 7 unique
   * [1x3, 3x4, 6x5, 7x6, 6x7, 3x8, 1x9]
   */
  def rollQuantumRound(count: Long): Seq[(Int, Long)] = Seq((3, 1), (4, 3), (5, 6), (6, 7), (7, 6), (8, 3), (9, 1))
    .map(t => (t._1, t._2 * count))

  override def part2(strings: Seq[String]): Long = {
    val p1Init = DiracBoard(strings.head.split("starting position:").last.trim.toLong)
    val p2Init = DiracBoard(strings.last.split("starting position:").last.trim.toLong)
    val winsAt = 21
    val done = mutable.HashMap[((DiracBoard, DiracBoard), Boolean), Long]()
    val pq = mutable.PriorityQueue[((DiracBoard, DiracBoard), Long)]()(Ordering.Long.on(t => t._2))
    pq.addOne(((p1Init, p2Init), 1L))
    while (pq.nonEmpty) {
      val pop = pq.dequeue()
      val ((p1, p2), countDimensions) = pop
      rollQuantumRound(countDimensions)
        .map(t => ((p1.move(t._1).calcScore, p2), t._2))
        .foreach(p1Res => {
          val (pt1@(inP1, inP2), countDimensionsP2) = p1Res

          if (!inP1.hasWon(winsAt)) {
            rollQuantumRound(countDimensionsP2)
              .map(t => ((inP1, inP2.move(t._1).calcScore), t._2))
              .foreach(p2Res => {
                val (pt2@(_, outP2), countDimensionsDone) = p2Res
                if (outP2.hasWon(winsAt)) {
                  done.put((pt2, false), countDimensionsDone + done.getOrElse((pt2, false), 0L))
                } else {
                  pq.addOne(p2Res)
                }
              })
          } else {
            done.put((pt1, true), p1Res._2 + done.getOrElse((pt1, true), 0L))
          }
        })
    }
    val p1Wins = done.filter(t => t._1._2)
    val p2Wins = done.filterNot(t => t._1._2)
    if (p1Wins.size > p2Wins.size) {
      p1Wins.values.sum
    } else {
      p2Wins.values.sum
    }
  }

  case class DiracBoard(location: Long, score: Long = 0L, rolls: Long = 0L) {

    def move(steps: Int): DiracBoard = DiracBoard(((location + steps - 1) % 10) + 1, score, rolls + 1)

    def calcScore: DiracBoard = DiracBoard(location, score + location, rolls)

    def hasWon(winsAt: Int): Boolean = score >= winsAt

  }

}
