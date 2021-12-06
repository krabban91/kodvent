import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day06 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val input = strings.head.split(",").map(_.toInt)
    var fishes = input.map(new LanternFish(_))
    println(s"Initial state: ${fishes.foldLeft("")((a, b) => (s"$a,${b.curr}"))}")
    val days = 80
    for (day <- 1 to days) {
      val newFishes = fishes.flatMap(_.incDay()).toSeq
      fishes = fishes ++ newFishes
      //println(s"After \t$day days: ${fishes.foldLeft("")((a,b)=> (s"$a,${b.curr}"))}")
    }
    fishes.length
  }

  class LanternFish(initVal: Int) {

    var curr: Int = initVal

    def incDay(): Option[LanternFish] = {
      curr -= 1
      if (curr < 0) {
        curr = 6
        Option(new LanternFish(8))
      } else {
        None
      }
    }
  }

  override def part2(strings: Seq[String]): Long = {
    val input = strings.head.split(",").map(_.toInt)
    var fishes = mutable.HashMap[Int, Long]()
    input.foreach(i => fishes.put(i, fishes.getOrElse(i, 0L) + 1L))
    val days = 256
    for (day <- 1 to days) {
      val nextDay: mutable.HashMap[Int, Long] = iterateDay(fishes)
      fishes = nextDay
    }
    fishes.values.sum
  }

  private def iterateDay(fishes: mutable.HashMap[Int, Long]): mutable.HashMap[Int, Long] = {
    val nextDay = mutable.HashMap[Int, Long]()
    fishes.foreach(t => nextDay.put(t._1 - 1, t._2))
    if (nextDay.contains(-1)) {
      nextDay.put(6, nextDay(-1) + nextDay.getOrElse(6, 0L))
      nextDay.put(8, nextDay(-1) + nextDay.getOrElse(8, 0L))
      nextDay.remove(-1)
    }
    nextDay
  }
}
