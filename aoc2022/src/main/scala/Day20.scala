import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day20 extends App with AoCPart1Test with AoCPart2Test {

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
    initial.foreach(x => {
      while (l.head != x) rotate(l)

      l.removeFirst(_ => true).get
      val t1 = x._1 % l.size
      val t2 = l.size + t1
      val times = if (t1 > t2) t2 else t1
      if (times > 0) {
        (1L to times).foreach(_ => rotate(l))
        l.prepend(x)
        (1L to (times)).foreach(_ => rotateBack(l))
      } else {
        (1L to math.abs(times)).foreach(_ => rotateBack(l))
        l.prepend(x)
        (1L to math.abs(times)).foreach(_ => rotate(l))
      }
    })
    while (l.head != zero) {
      rotate(l)
    }
    l.toSeq
  }

  private def rotateBack(l: mutable.ArrayDeque[(Long, Int)]): Unit = {
    l.prepend(l.removeLast())
  }

  private def rotate(l: mutable.ArrayDeque[(Long, Int)]): Unit = {
    l.append(l.removeFirst(_ => true).get)
  }
}
