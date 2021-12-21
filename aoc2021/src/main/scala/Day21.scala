import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import java.util.Objects
import scala.collection.mutable

object Day21 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val p1 = new DiracBoard(strings.head.split("starting position:").last.trim.toLong, 10, 1000)
    val p2 = new DiracBoard(strings.last.split("starting position:").last.trim.toLong, 10, 1000)
    var dice = 1
    while (!(p1.hasWon || p2.hasWon)) {
      for (i <- 1 to 3) {
        p1.move(dice)
        dice = nextDice(dice)
      }
      p1.calcScore
      if (!p1.hasWon) {
        for (i <- 1 to 3) {
          p2.move(dice)
          dice = nextDice(dice)
        }
        p2.calcScore
      }
    }
    if (p1.hasWon) {
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
    val p1Init = new DiracBoard(strings.head.split("starting position:").last.trim.toLong, 10, 21)
    val p2Init = new DiracBoard(strings.last.split("starting position:").last.trim.toLong, 10, 21)
    val done = mutable.HashMap[((DiracBoard, DiracBoard), Boolean), Long]()
    val pq = mutable.PriorityQueue[((DiracBoard, DiracBoard), Long)]()(Ordering.Long.on(t => t._2))
    pq.addOne(((p1Init, p2Init), 1L))
    while (pq.nonEmpty) {
      val pop = pq.dequeue()
      val ((p1, p2), countDimensions) = pop
      rollQuantumRound(countDimensions)
        .map(t => {
          val inP1 = p1.copy()
          val inP2 = p2.copy()
          inP1.move(t._1)
          inP1.calcScore
          ((inP1, inP2), t._2)
        })
        .foreach(p1Res => {
          val (pt1@(inP1, inP2), countDimensionsP2) = p1Res

          if (!p1Res._1._1.hasWon) {
            rollQuantumRound(countDimensionsP2)
              .map(t => {
                val outP1 = inP1.copy()
                val outP2 = inP2.copy()
                outP2.move(t._1)
                outP2.calcScore
                ((outP1, outP2), t._2)
              })
              .foreach(p2Res => {
                val (pt2@(_, outP2), countDimensionsDone) = p2Res
                if (outP2.hasWon) {
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

  class DiracBoard(start: Long, size: Long, winsAt: Long, scoreInit: Long = 0L, rollsInit: Long = 0L) {

    private val inner = mutable.ListBuffer[Long]()
    inner.addAll((1L to size).toList)
    (0L until start).foreach(_ => rotateOne)
    var score: Long = scoreInit
    var rolls: Long = rollsInit

    def move(steps: Int): Unit = {
      rolls += 1
      (0 until steps % 10 + 1 / 10).foreach(_ => rotateOne)
    }

    def calcScore: Unit = score += inner.last

    def hasWon: Boolean = score >= winsAt

    def rotateOne: Unit = {
      val head = inner.head
      inner.dropInPlace(1)
      inner.addOne(head)
    }

    def copy(): DiracBoard = {
      new DiracBoard(this.inner.last, size, winsAt, score, rolls)
    }

    override def equals(obj: Any): Boolean = Option(obj)
      .filter(_.isInstanceOf[DiracBoard])
      .map(_.asInstanceOf[DiracBoard])
      .exists(other => other.score == this.score && other.inner == this.inner && other.rolls == this.rolls)

    override def hashCode(): Int = Objects.hash(score, rolls, inner)

  }

}
