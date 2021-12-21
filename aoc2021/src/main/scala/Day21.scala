import aoc.numeric.{AoCPart1Test, AoCPart2Test}

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
    while (!(p1.hasWon || p2.hasWon)){
      for(i <- 1 to 3){
          p1.move(dice)
          dice = nextDice(dice)
      }
      p1.calcScore
      if(!p1.hasWon){
        for(i <- 1 to 3) {
          p2.move(dice)
          dice = nextDice(dice)
        }
        p2.calcScore
      }
    }
    if(p1.hasWon){
      p2.score * (p1.rolls + p2.rolls)
    } else {
      p1.score * (p1.rolls + p2.rolls)
    }
  }

  def nextDice(curr: Int): Int = if(curr == 100) 1 else (curr + 1)

  override def part2(strings: Seq[String]): Long = -1

  class DiracBoard(start: Long, size: Long, winsAt: Long) {

    private val inner = mutable.ListBuffer[Long]()
    inner.addAll((1L to size).toList)
    (0L until start).foreach(_=>rotateOne)
    var score: Long = 0L
    var rolls: Long = 0L

    def move(steps: Int): Unit = {
      rolls += 1
      (0 until steps%10+1/10).foreach(_ => rotateOne)
    }

    def calcScore: Unit = score += inner.last

    def hasWon: Boolean = score >= winsAt

    def rotateOne: Unit = {
      val head = inner.head
      inner.dropInPlace(1)
      inner.addOne(head)
    }
  }
}
