import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable.ListBuffer
import scala.util.chaining.scalaUtilChainingOps

object Day04 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val numbers: Seq[Int] = strings.head.pipe(s => s.split(",")).map(_.toInt).toSeq
    val boards: Seq[BingoBoard] = extractBoards(strings)
    var hasWon = false
    var currentNumber = 0

    while (!hasWon){
      val curr = numbers(currentNumber)
      boards.foreach(_.callNumber(curr))
      if(boards.exists(_.bingo)){
        hasWon = true
      }else {
        currentNumber += 1
      }
    }
    val winner = boards.find(_.bingo)

    winner.get.missingNumbers.sum * numbers(currentNumber)
  }


  override def part2(strings: Seq[String]): Long = {
    val numbers: Seq[Int] = strings.head.pipe(s => s.split(",")).map(_.toInt).toSeq
    val boards: Seq[BingoBoard] = extractBoards(strings)
    var hasWon = false
    var currentNumber = 0
    var previousWinners = Seq[BingoBoard]()
    while (!hasWon){
      val curr = numbers(currentNumber)
      boards.foreach(_.callNumber(curr))
      if(boards.forall(_.bingo)){
        hasWon = true
      }else {
        currentNumber += 1
        previousWinners = boards.filter(_.bingo)
      }
    }
    val winner = boards.find(b => b.bingo && !previousWinners.contains(b))

    winner.get.missingNumbers.sum * numbers(currentNumber)
  }

  private def extractBoards(strings: Seq[String]): Seq[BingoBoard] = {
    strings.tail.tail.map(_.split(" ").filterNot(_.isBlank).map(_.toInt).toSeq).filterNot(_.isEmpty).grouped(5).toSeq.map(v => new BingoBoard(v))
  }


  class BingoBoard(board: Seq[Seq[Int]]){

    private val columns = board.transpose

    private val runNumbers = ListBuffer[Int]()

    def callNumber(number: Int): Unit = runNumbers.append(number)

    def bingo: Boolean = {
      this.board.exists(row => row.forall(i => runNumbers.contains(i))) ||
        this.columns.exists(row => row.forall(i => runNumbers.contains(i)))
    }

    def missingNumbers: Seq[Int] = {
      this.board.flatten.filterNot(runNumbers.contains(_))
    }
  }
}
