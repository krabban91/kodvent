import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val N = strings.head.length
    val moves = 100
    val cups = new Cups(strings, N)
    cups.moveNTimes(moves)
    cups.next(strings.head.length - 1, 1)
      .map(_.toString)
      .reduce((l, r) => l + r).toLong
  }

  override def part2(strings: Seq[String]): Long = {
    val N = 1_000_000
    val moves = 10_000_000
    val cups = new Cups(strings, N)
    cups.moveNTimes(moves)
    cups.next(2, 1).product
  }

  class Cups(strings: Seq[String], size: Int) {
    private val inp: Seq[Long] = strings.head.map(c => Integer.parseInt(c.toString).toLong) ++ Range(strings.head.length + 1, size + 1).map(_.toLong)
    private val minV: Long = inp.min
    private val maxV: Long = inp.max
    private val map: mutable.Map[Long, Long] = mutable.HashMap() ++ inp.indices.map(i => inp(i) -> inp((i + 1) % inp.size)).toMap
    private var head: Long = inp.head

    def apply(i: Long): Long = map(i)

    def moveNTimes(N: Int): Unit = Range(0, N)
      .foreach(_ => move)

    def next(n: Long): Seq[Long] = next(n, head)

    def next(n: Long, after: Long): Seq[Long] = Range(0, n.toInt)
      .foldLeft(Seq(after))((l, _) => l ++ Seq(this (l.last))).tail

    def move(): Unit = {
      val pickedup = next(3)
      map.update(head, map(pickedup.last))
      var destination = head - 1
      while (pickedup.contains(destination) || destination < minV) {
        destination -= 1
        if (destination < minV) {
          destination = maxV
        }
      }
      map.update(pickedup.last, map(destination))
      map.update(destination, pickedup.head)
      head = map(head)
    }
  }

}
