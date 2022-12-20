import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day20 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(v => v.toLong)
    val l = mixNumbers(v, 1)
    l(1000 % v.size) + l(2000 % v.size) + l(3000 % v.size)
  }

  override def part2(strings: Seq[String]): Long = {
    val v = strings.map(v => v.toLong).map(_ * 811589153L)
    val l = mixNumbers(v, 10)
    l(1000 % v.size) + l(2000 % v.size) + l(3000 % v.size)
  }

  private def mixNumbers(v: Seq[Long], times: Int): Seq[Long] = {
    val initial = v.zipWithIndex
    (1 to times).foldLeft(initial) { case (l, _) => mixNumbers(l, initial) }.map(_._1)
  }

  private def mixNumbers(v: Seq[(Long, Int)], initial: Seq[(Long, Int)]): Seq[(Long, Int)] = {
    val l = mutable.ArrayDeque[(Long, Int)]()
    val zero = initial.find(_._1 == 0L).get
    l.addAll(v)
    var pointer = 0
    initial.foreach(x => {
      // move to item
      while (l.head != x) {
        l.append(l.removeFirst(_ => true).get)
        pointer += 1
      }
      pointer = pointer % v.size

      //move item
      val o = l.removeFirst(_ => true).get
      if (x != o) {
        println(s"moved wrong item x=${x}, o=$o")
      }
      val times = x._1 % (v.size - 1)

      if (times > 0) {
        (1L to times).foreach(t => l.append(l.removeFirst(_ => true).get))
        l.prepend(x)
        //move back
        (1L to (times)).foreach(t => l.prepend(l.removeLast()))
      } else {
        (1L to math.abs(times)).foreach(t => l.prepend(l.removeLast()))
        l.prepend(x)
        //move back
        (1L to math.abs(times)).foreach(t => l.append(l.removeFirst(_ => true).get))
      }
    })
    while (l.head != zero) {
      l.append(l.removeFirst(_ => true).get)
    }
    l.toSeq
  }

}
