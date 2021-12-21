import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day21 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    var dirac = Dirac(strings)
    val winsAt = 1000
    while (dirac.winner(winsAt).isEmpty) {
      dirac = dirac.rollRound
    }
    dirac.result(winsAt)
  }

  override def part2(strings: Seq[String]): Long = {
    val dirac = QuantumDirac(strings)
    val winsAt = 21
    val done = mutable.HashMap[Int, Long]()
    val pq = mutable.PriorityQueue[(QuantumDirac, Long)]()(Ordering.Long.on(t => t._2))
    pq.addOne((dirac, 1L))
    while (pq.nonEmpty) {
      val (board, countDimensions) = pq.dequeue()
      board.rollQuantumRound(countDimensions)
        .foreach(boardRes => {
          val (b, dims) = boardRes
          val maybePlayer = b.winner(winsAt)
          if (maybePlayer.isDefined) {
            done.put(maybePlayer.get.id, dims + done.getOrElse(maybePlayer.get.id, 0L))
          } else {
            pq.addOne(boardRes)
          }
        })
    }
    done.values.max
  }

  case class Dirac(player1: DiracPlayer, player2: DiracPlayer, dice: Int, isP1: Boolean) {

    def nextDice(curr: Int): Int = if (curr == 100) 1 else (curr + 1)

    def rollRound: Dirac = {
      val (d, sum) = (1 to 3).foldLeft((dice, 0))((t, _) => (nextDice(t._1), t._2 + t._1))
      if (isP1) Dirac(player1.move(sum).calcScore, player2, d, !isP1) else Dirac(player1, player2.move(sum).calcScore, d, !isP1)
    }

    def winner(winsAt: Int): Option[DiracPlayer] = Option(player1).filter(_.hasWon(winsAt)).orElse(Option(player2).filter(_.hasWon(winsAt)))

    def result(winsAt: Int): Long = (player1.rolls + player2.rolls) * (if (player1.hasWon(winsAt)) player2 else player1).score
  }

  case class QuantumDirac(player1: DiracPlayer, player2: DiracPlayer, isP1: Boolean) {

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
    def rollQuantumRound(count: Long): Seq[(QuantumDirac, Long)] = Seq((3, 1), (4, 3), (5, 6), (6, 7), (7, 6), (8, 3), (9, 1))
      .map(t => (if (isP1) QuantumDirac(player1.move(t._1).calcScore, player2, !isP1) else QuantumDirac(player1, player2.move(t._1).calcScore, !isP1), t._2 * count))

    def winner(winsAt: Int): Option[DiracPlayer] = Option(player1).filter(_.hasWon(winsAt)).orElse(Option(player2).filter(_.hasWon(winsAt)))

  }

  case class DiracPlayer(id: Int, location: Long, score: Long = 0L, rolls: Long = 0L) {

    def move(steps: Int): DiracPlayer = DiracPlayer(id, ((location + steps - 1) % 10) + 1, score, rolls + 3)

    def calcScore: DiracPlayer = DiracPlayer(id, location, score + location, rolls)

    def hasWon(winsAt: Int): Boolean = score >= winsAt

  }

  object Dirac {
    def apply(strings: Seq[String]): Dirac = Dirac(
      DiracPlayer(1, strings.head.split("starting position:").last.trim.toLong),
      DiracPlayer(2, strings.last.split("starting position:").last.trim.toLong), 1, isP1 = true)

  }

  object QuantumDirac {
    def apply(strings: Seq[String]): QuantumDirac = QuantumDirac(
      DiracPlayer(1, strings.head.split("starting position:").last.trim.toLong),
      DiracPlayer(2, strings.last.split("starting position:").last.trim.toLong), isP1 = true)

  }
}